package co.edu.uptc.appsalesandproducts.runner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import co.edu.uptc.appsalesandproducts.model.Product;
import co.edu.uptc.appsalesandproducts.model.Sale;
import co.edu.uptc.appsalesandproducts.persistence.SaleAndProductManagement;

public class Runner {
	private static final Scanner scanner = new Scanner(System.in);
	private static final SaleAndProductManagement spm = new SaleAndProductManagement();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		System.out.println("*******************************************");
		System.out.println("Welcome to the Products and Sales System");
		System.out.println("*******************************************");

		int option;
		do {
			System.out.println("\nChoose an option:");
			System.out.println("1. Add product");
			System.out.println("2. Update product");
			System.out.println("3. Get product");
			System.out.println("4. Get all products");
			System.out.println("5. Delete product");
			System.out.println("6. Add a sale");
			System.out.println("7. Get sale");
			System.out.println("8. Get all sales");
			System.out.println("9. Update sale");
			System.out.println("10. Obtain total revenue");
			System.out.println("0. Exit");

			option = scanner.nextInt();
			scanner.nextLine(); // Consume la nueva línea después de la entrada del usuario

			switch (option) {
				case 1:
					addProduct();
					break;
				case 2:
					updateProduct();
					break;
				case 3:
					getProduct();
					break;
				case 4:
					getAllProducts();
					break;
				case 5:
					deleteProduct();
					break;
				case 6:
					addSale();
					break;
				case 7:
					getSale();
					break;
				case 8:
					getAllSales();
					break;
				case 9:
					updateSale();
					break;
				case 10:
					obtainTotalRevenue();
					break;
				case 0:
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Invalid option, please try again.");
					break;
			}
		} while (option != 0);
	}

	private static void addProduct() {
		try {
			System.out.println("Enter product code:");
			String code = scanner.nextLine();
			spm.loadFileXML();

			System.out.println("Enter product description:");
			String description = scanner.nextLine();

			System.out.println("Enter product price:");
			double price = scanner.nextDouble();

			System.out.println("Enter product quantity:");
			int quantity = scanner.nextInt();

			if (quantity < 0) {
				System.out.println("Invalid quantity. Quantity cannot be negative.");
				return;
			}

			int stockMinimoAlert = spm.getStockMinimo();

			if (quantity < stockMinimoAlert) {
				System.out.println("Warning: Product quantity is below minimum stock level.");
			}

			boolean added = spm.addProduct(code, description, price, quantity);
			if (added) {
				System.out.println("Product added successfully.");
			} else {
				System.out.println("Failed to add product. Product with the same code already exists.");
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}

	}

	private static void updateProduct() {
		try {
			System.out.println("Enter product code:");
			String code = scanner.nextLine();
			spm.loadFileXML();

			if (!spm.getMapCodeProduct().containsKey(code)) {
				System.out.println("Product with code " + code + " does not exist.");
				return;
			}

			System.out.println("Enter new description:");
			String newDescription = scanner.nextLine();

			System.out.println("Enter new price:");
			double newPrice = scanner.nextDouble();

			System.out.println("Enter new quantity:");
			int newQuantity = scanner.nextInt();

			if (newQuantity < 0) {
				System.out.println("Invalid quantity. Quantity cannot be negative.");
				return;
			}

			if (newQuantity < spm.getStockMinimo()) {
				System.out.println("Warning: New quantity is below minimum stock level.");
			}

			boolean updated = spm.updateProduct(code, newDescription, newPrice, newQuantity);
			if (updated) {
				System.out.println("Product updated successfully.");
			} else {
				System.out.println("Failed to update product. Product with code " + code + " does not exist.");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}

	}

	private static void getProduct() {
		System.out.println("Enter product code:");
		String code = scanner.nextLine();
		spm.loadFileXML();
		Product product = spm.getProduct(code);
		if (product != null) {
			System.out.println("Product found:");
			System.out.println(product);
		} else {
			System.out.println("Product with code " + code + " does not exist.");
		}
	}

	private static void getAllProducts() {
		spm.loadFileXML(); // Cargar los productos desde el archivo XML

		spm.getMapCodeProduct().values().forEach(System.out::println);
	}

	private static void deleteProduct() {

		System.out.println("Enter product code:");
		String code = scanner.nextLine();
		spm.loadFileXML();
		boolean deleted = spm.deleteProduct(code);
		if (deleted) {
			System.out.println("Product deleted successfully.");
		} else {
			System.out.println("Failed to delete product. Product with code " + code + " does not exist.");
		}
	}

	private static void addSale() {
		System.out.println("Enter sale ID:");
		String saleId = scanner.nextLine();
		spm.loadFilePlain();
		spm.loadFileXML();

		// Verificar si el ID de venta ya existe
		if (spm.getMapCodeSale().containsKey(saleId)) {
			System.out.println("Sale ID " + saleId + " already exists. Please enter a different sale ID.");
			return;
		}

		System.out.println("Enter product code:");
		String codeProduct = scanner.nextLine();

		if (!spm.getMapCodeProduct().containsKey(codeProduct)) {
			System.out.println("Product with code " + codeProduct + " does not exist.");
			return;
		}

		Product product = spm.getProduct(codeProduct);

		System.out.println("Enter quantity sold:");
		int quantitySold = scanner.nextInt();

		if (quantitySold <= 0) {
			System.out.println("Invalid quantity. Quantity must be greater than zero.");
			return;
		}

		// Verificar si la cantidad a comprar supera el stock mínimo establecido
		int minStock = spm.getStockMinimo();
		if (product.getQuantity() - quantitySold < minStock) {
			System.out.println("Warning: Product quantity is below minimum stock level.");
			System.out.println("Alert: " + spm.getStockMinimo());
			return;
		}

		System.out.println("Enter sale date (yyyy-MM-dd):");
		String saleDateStr = scanner.next();

		try {
			Date saleDate = dateFormat.parse(saleDateStr);
			Date currentDate = new Date();

			if (saleDate.after(currentDate)) {
				System.out.println("Invalid sale date. Sale date cannot be in the future.");
				return;
			}
		} catch (Exception e) {
			System.out.println("Invalid date format. Please enter date in yyyy-MM-dd format.");
			return;
		}

		double totalPrice = product.getPrice() * quantitySold;

		Sale sale = new Sale(saleId, codeProduct, totalPrice, saleDateStr, quantitySold);
		spm.addSale(saleId, codeProduct, quantitySold, saleDateStr);
		System.out.println("Sale added successfully.");
		System.out.println("Total Sale Amount: $" + totalPrice);

	}

	private static void getSale() {

		System.out.println("Enter sale ID:");
		String saleId = scanner.nextLine();
		spm.loadFilePlain();
		Sale sale = spm.getMapCodeSale().get(saleId);
		if (sale != null) {
			System.out.println("Sale found:");
			System.out.println(sale);
		} else {
			System.out.println("Sale with ID " + saleId + " does not exist.");
		}
	}

	private static void getAllSales() {
		spm.loadFilePlain();

		spm.getMapCodeSale().values().forEach(System.out::println);
	}

	private static void updateSale() {
		System.out.println("Enter sale ID:");
		String saleId = scanner.nextLine();
		spm.loadFilePlain();
		spm.loadFileXML();
		if (!spm.getMapCodeSale().containsKey(saleId)) {
			System.out.println("Sale with ID " + saleId + " does not exist.");
			return;
		}

		Sale sale = spm.getMapCodeSale().get(saleId);

		System.out.println("Enter new product code:");
		String newCodeProduct = scanner.nextLine();

		if (!spm.getMapCodeProduct().containsKey(newCodeProduct)) {
			System.out.println("Product with code " + newCodeProduct + " does not exist.");
			return;

		}

		Product product = spm.getProduct(newCodeProduct);

		System.out.println("Enter new sale date (yyyy-MM-dd):");
		String newSaleDateStr = scanner.next();

		try {
			Date newSaleDate = dateFormat.parse(newSaleDateStr);
			Date currentDate = new Date();

			if (newSaleDate.after(currentDate)) {
				System.out.println("Invalid sale date. Sale date cannot be in the future.");
				return;
			}
		} catch (Exception e) {
			System.out.println("Invalid date format. Please enter date in yyyy-MM-dd format.");
			return;
		}
		System.out.println("Enter the new quantity");
		int newQuantity = scanner.nextInt();

		if (newQuantity < 0) {
			System.out.println("Invalid quantity.Quatity cannot be negative.");
			return;
		}
		int stockMinimoAlert = spm.getStockMinimo();

		if (newQuantity < stockMinimoAlert) {
			System.out.println("Warning: The new quantity is below minimum stock level.");
		}

		spm.updateSale(saleId, product.getCode(), newQuantity, newSaleDateStr);
		System.out.println("Sale updated successfully.");
	}

	private static void obtainTotalRevenue() {
		System.out.println("Enter product code:");
		String code = scanner.nextLine();

		System.out.println("Enter sale date (yyyy-MM-dd):");
		String saleDateStr = scanner.nextLine();

		try {
			Date saleDate = dateFormat.parse(saleDateStr);
			Date currentDate = new Date();

			if (saleDate.after(currentDate)) {
				System.out.println("Invalid sale date. Sale date cannot be in the future.");
				return;
			}
		} catch (Exception e) {
			System.out.println("Invalid date format. Please enter date in yyyy-MM-dd format.");
			return;
		}

		double totalRevenue = spm.getTotalRevenueForProductOnDate(code, saleDateStr);
		if (totalRevenue >= 0) {
			System.out.println("Total revenue for product " + code + " on " + saleDateStr + ": " + totalRevenue);
		} else {
			System.out.println("No sales recorded for product " + code + " on " + saleDateStr);
		}
	}
}
