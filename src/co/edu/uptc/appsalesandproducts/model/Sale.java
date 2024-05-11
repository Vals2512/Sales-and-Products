package co.edu.uptc.appsalesandproducts.model;

import java.time.LocalDate;

public class Sale {

	
	Product product;
	private double total ;
	private LocalDate saleDate;
	
	public Sale() {
	}
	
	public Sale(Product product, double total, LocalDate saleDate) {
		this.product = product;
		this.total = total;
		this.saleDate = saleDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public LocalDate getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}
	
	
	@Override
	public String toString() {
		return "Sale [Product=" + product + ", Total=" + total + ", Sale Date=" + saleDate + "]";
	}
	
}
