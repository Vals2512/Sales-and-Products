package co.edu.uptc.appsalesandproducts.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import co.edu.uptc.appsalesandproducts.commonconstants.Constants;
import co.edu.uptc.appsalesandproducts.config.Config;
import co.edu.uptc.appsalesandproducts.model.Product;
import co.edu.uptc.appsalesandproducts.model.Sale;

public class SaleAndProductManagement {

	private final String NAME_TAG_SALE = "sale";
	private final String NAME_TAG_PRODUCT = "product";
	
	private FilePlain file;

	private Config confValue;
	
	private Map<String, Sale> mapCodeSale;
	private Map<String, Product> mapCodeProduct;


	public SaleAndProductManagement() {
		this.mapCodeSale = new HashMap<>();
	}

	
	private void dumpFilePlain() {
		String filePath = confValue.getPath().concat(confValue.getNameFileTXT());
		List<Sale> sales = this.mapCodeSale.values().stream().collect(Collectors.toList());
		List<String> records = new ArrayList<>();
		for (Sale sale:sales) {
			 StringBuilder contentSale = new StringBuilder();
			 contentSale.append(sale.getCodeProduct()).append(Constants.SEMI_COLON);
			 contentSale.append(sale.getTotal()).append(Constants.SEMI_COLON);
			 contentSale.append(sale.getSaleDate()).append(Constants.SEMI_COLON);
			 records.add(contentSale.toString());
		 }
		//this.writer(rutaArchivo, records);
		file.writer(filePath, records);
	}
	
	private void loadFilePlain() {
		//List<String> contentInLine = this.reader(confValue.getPath().concat(confValue.getNameFileTXT()));
		List<String> contentInLine = file.reader(confValue.getPath().concat(confValue.getNameFileTXT()));
		contentInLine.forEach(row -> {
			StringTokenizer tokens = new StringTokenizer(row, Constants.SEMI_COLON);
			while(tokens.hasMoreElements()){
				String saleId = tokens.nextToken();
				String codeProduct = tokens.nextToken();
				String total = tokens.nextToken();
				String saleDate = tokens.nextToken();
				double dTotal = Double.parseDouble(total);
				mapCodeSale.put(saleId, new Sale(saleId, codeProduct, dTotal, saleDate));
			}
		});
	}
	
	public void dumpFileXML() {
		String filePath = confValue.getPath().concat(confValue.getNameFileXML());
		StringBuilder lines = new StringBuilder();
		List<Product> products = this.mapCodeProduct.values().stream().collect(Collectors.toList());
		lines.append("<XML version=\"1.0\" encoding=\"UTF-8\"> \n");
		for (Product product : products) {
			lines.append("<product>\n");
			lines.append("<code>"+product.getCode()+"</code>\n");
			lines.append("<description>"+product.getDescription()+"</description>\n");
			lines.append("<price>"+product.getPrice()+"</price>\n");
			lines.append("<quantity>"+product.getQuantity()+"</quantity>\n");
			lines.append("</product>\n");
			}
		lines.append("</XML>");
		//this.writeFile(filePath, lines.toString());
		file.writeFile(filePath, lines.toString());
	}
	
	public void loadFileXML() {
		try {
			File files = new File(confValue.getPath().concat(confValue.getNameFileXML()));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(files);
			NodeList list = document.getElementsByTagName(NAME_TAG_PRODUCT);
			for (int i = 0; i < list.getLength(); i++) {
				String code = document.getElementsByTagName("code").item(i).getTextContent();
				String description = document.getElementsByTagName("name").item(i).getTextContent();
				String price = document.getElementsByTagName("nacionality").item(i).getTextContent();
				String quantity = document.getElementsByTagName("quantity").item(i).getTextContent();
				double dPrice = Double.parseDouble(price);
				int iQuantity = Integer.parseInt(quantity);
				mapCodeProduct.put(code, new Product(code, description, dPrice, iQuantity));
			}
		}catch(Exception e) {
			System.out.println("Se presentó un error en el cargue del archivo XML");
		}
		
	}
	
	public void sale(String product, int quantity) {
		
	}
	
	public void devengado(String date) {
		
	}

	public Map<String, Sale> getMapCodeSale() {
		return mapCodeSale;
	}

	public void setMapCodeSale(Map<String, Sale> mapCodeSale) {
		this.mapCodeSale = mapCodeSale;
	}
	
	public Map<String, Product> getMapCodeProduct() {
		return mapCodeProduct;
	}
	
	public void setMapCodeProduct(Map<String, Product> mapCodeProduct) {
		this.mapCodeProduct = mapCodeProduct;
	}
}
