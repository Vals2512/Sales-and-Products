package co.edu.uptc.appsalesandproducts.model;

public class Product {
	
	private String code;
	private String description;
	private double price;
	private int quantity;
	
	public Product() {
	}

	public Product(String code, String description, float price, int quantity) {
		this.code = code;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Product [Code=" + code + ", Description=" + description + ", Price=" + price + ", Quantity=" + quantity
				+ "]";
	}
	

}
