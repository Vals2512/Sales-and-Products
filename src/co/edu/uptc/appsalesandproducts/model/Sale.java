package co.edu.uptc.appsalesandproducts.model;

import java.time.LocalDate;

public class Sale {

	/*
	 * Sea agrega una variable saleId ya que en un día es posible que haya más de una venta y de esta manera es más facil identificar la venta realizada
	 */
	private String saleId;
	private String codeProduct;
	private double total ;
	private String saleDate;
	
	public Sale() {
	}
	
	public Sale(String saleId, String codeProduct, double total, String saleDate) {
		this.saleId = saleId;
		this.codeProduct = codeProduct;
		this.total = total;
		this.saleDate = saleDate;	
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getCodeProduct() {
		return codeProduct;
	}

	public void setCodeProduct(String codeProduct) {
		this.codeProduct = codeProduct;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	
	
	@Override
	public String toString() {
		return "Sale [Product=" + codeProduct + ", Total=" + total + ", Sale Date=" + saleDate + "]";
	}
	
}