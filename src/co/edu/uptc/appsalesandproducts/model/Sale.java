package co.edu.uptc.appsalesandproducts.model;

public class Sale {

	private String saleId;
	private String codeProduct;
	private double total;
	private String saleDate;
	private int quantitySold;

	public Sale() {
	}

	public Sale(String saleId, String codeProduct, double total, String saleDate, int quantitySold) {
		this.saleId = saleId;
		this.codeProduct = codeProduct;
		this.total = total;
		this.saleDate = saleDate;
		this.quantitySold = quantitySold;
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

	public int getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	@Override
	public String toString() {
		return "Sale [Product=" + codeProduct + ", Total=" + total + ", Sale Date=" + saleDate + ", Quantity Sold="
				+ quantitySold + "]";
	}
}
