package co.edu.uptc.appsalesandproducts.persistence;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import co.edu.uptc.appsalesandproducts.commonconstants.Constants;
import co.edu.uptc.appsalesandproducts.config.Config;
import co.edu.uptc.appsalesandproducts.enums.ETypeFile;
import co.edu.uptc.appsalesandproducts.interfaces.IActionFile;
import co.edu.uptc.appsalesandproducts.model.Product;
import co.edu.uptc.appsalesandproducts.model.Sale;

public class SaleAndProductManagement extends FilePlain implements IActionFile {

    private final String NAME_TAG_SALE = "sale";
    private final String NAME_TAG_PRODUCT = "product";

    private FilePlain file;

    private Config confValue;

    private Map<String, Sale> mapCodeSale;
    private Map<String, Product> mapCodeProduct;

    public SaleAndProductManagement() {
        this.mapCodeSale = new HashMap<>();
        this.mapCodeProduct = new HashMap<>();
        this.file = new FilePlain();
        this.confValue = Config.getInstance();

    }

    private void dumpFilePlain() {
        String filePath = confValue.getPath().concat(confValue.getNameFileTXT());
        List<Sale> sales = this.mapCodeSale.values().stream().collect(Collectors.toList());
        List<String> records = new ArrayList<>();
        for (Sale sale : sales) {
            StringBuilder contentSale = new StringBuilder();
            contentSale.append(sale.getSaleId()).append(Constants.SEMI_COLON);
            contentSale.append(sale.getCodeProduct()).append(Constants.SEMI_COLON);
            contentSale.append(sale.getTotal()).append(Constants.SEMI_COLON);
            contentSale.append(sale.getSaleDate()).append(Constants.SEMI_COLON);
            contentSale.append(sale.getQuantitySold());
            records.add(contentSale.toString());
        }
        // this.writer(rutaArchivo, records);
        file.writer(filePath, records);
    }

    public void loadFilePlain() {
        List<String> contentInLine = file.reader(confValue.getPath().concat(confValue.getNameFileTXT()));
        contentInLine.forEach(row -> {
            StringTokenizer tokens = new StringTokenizer(row, Constants.SEMI_COLON);
            while (tokens.hasMoreElements()) {
                String saleId = tokens.nextToken();
                String codeProduct = tokens.nextToken();
                double total = Double.parseDouble(tokens.nextToken());
                String saleDate = tokens.nextToken();
                int quantitySold = Integer.parseInt(tokens.nextToken());
                mapCodeSale.put(saleId, new Sale(saleId, codeProduct, total, saleDate, quantitySold));
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
            lines.append("<code>" + product.getCode() + "</code>\n");
            lines.append("<description>" + product.getDescription() + "</description>\n");
            lines.append("<price>" + product.getPrice() + "</price>\n");
            lines.append("<quantity>" + product.getQuantity() + "</quantity>\n");
            lines.append("</product>\n");
        }
        lines.append("</XML>");
        // this.writeFile(filePath, lines.toString());
        file.writeFile(filePath, lines.toString());
    }

    public void loadFileXML() {
        try {
            File file = new File(confValue.getPath() + confValue.getNameFileXML());
            if (!file.exists()) {
                System.out.println("XML file does not exist.");
                return;
            }

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList list = document.getElementsByTagName(NAME_TAG_PRODUCT);
            for (int i = 0; i < list.getLength(); i++) {
                String code = document.getElementsByTagName("code").item(i).getTextContent();
                String description = document.getElementsByTagName("description").item(i).getTextContent();
                String price = document.getElementsByTagName("price").item(i).getTextContent();
                String quantity = document.getElementsByTagName("quantity").item(i).getTextContent();
                double dPrice = Double.parseDouble(price);
                int iQuantity = Integer.parseInt(quantity);
                mapCodeProduct.put(code, new Product(code, description, dPrice, iQuantity));
            }
        } catch (ParserConfigurationException e) {
            System.out.println("Parser configuration error: " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("SAX error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }

    public boolean addProduct(String code, String description, double price, int quantity) throws Exception {
        if (this.validateValueLessThanStockMin(quantity)) {
            throw new Exception("Warning quantity invalidad because is les than stock minimum ");
        }
        if (!mapCodeProduct.containsKey(code)) {
            mapCodeProduct.put(code, new Product(code, description, price, quantity));
            dumpFileXML(); // Guardar el nuevo producto en el archivo XML
            return true;
        }
        return false; // El producto ya existe con ese código
    }

    public Product getProduct(String code) {
        return mapCodeProduct.get(code);
    }

    public Collection<Product> getAllProducts() {
        return mapCodeProduct.values();
    }

    public boolean deleteProduct(String code) {
        if (mapCodeProduct.containsKey(code)) {
            mapCodeProduct.remove(code);
            dumpFileXML(); // Actualizar el archivo XML después de eliminar el producto
            return true;
        }
        return false; // El producto no existe con ese código
    }

    public boolean updateProduct(String code, String newDescription, double newPrice, int newQuantity)
            throws Exception {
        if (this.validateValueLessThanStockMin(newQuantity)) {
            throw new Exception("Warning quantity invalidad because is les than stock minimum ");
        }
        if (mapCodeProduct.containsKey(code)) {
            Product product = mapCodeProduct.get(code);
            product.setDescription(newDescription);
            product.setPrice(newPrice);
            product.setQuantity(newQuantity);
            dumpFileXML(); // Actualizar el archivo XML después de actualizar el producto
            return true;
        }
        return false; // El producto no existe con ese código
    }

    public boolean validateValueLessThanStockMin(int value) {
        return value < this.getStockMinimo();
    }

    public int getStockMinimo() {
        String stockMinimoString = confValue.getProperties().getProperty("app.stock.minimo");
        try {
            return Integer.parseInt(stockMinimoString);
        } catch (NumberFormatException e) {
            return -1; // Devolver un valor predeterminado si hay un error
        }
    }

    // Método para agregar una venta
    public void addSale(String saleId, String codeProduct, int quantity, String saleDate) {
        // Validar que la cantidad no sea negativa
        if (quantity <= 0) {
            return;
        }

        // Validar que la fecha no sea mayor a la actual ni negativa
        if (!isValidDate(saleDate)) {
            return;
        }

        // Obtener el producto
        Product product = getProduct(codeProduct);
        if (product == null) {
            return;
        }
        // Reducir el stock del producto
        if (this.validateValueLessThanStockMin(product.getQuantity() - quantity)) {
            throw new Error("Warning quantity invalidad because is les than stock minimum ");
        }

        // Reducir el stock del producto
        product.setQuantity(product.getQuantity() - quantity);

        // Calcular el total de la venta
        double totalPrice = product.getPrice() * quantity;

        // Crear la venta y agregarla al mapa de ventas
        Sale sale = new Sale(saleId, codeProduct, totalPrice, saleDate, quantity);
        mapCodeSale.put(saleId, sale);

        // Actualizar el archivo de ventas después de agregar la venta
        dumpFilePlain();
        dumpFileXML();
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Para que las fechas inválidas (como 2022-02-30) no sean aceptadas

        try {
            Date date = sdf.parse(dateStr);
            // Verificar si la fecha es menor o igual a la fecha actual
            return date.compareTo(new Date()) <= 0;
        } catch (ParseException e) {
            return false; // La fecha no tiene un formato válido
        }
    }

    // Método para actualizar la información de una venta
    public boolean updateSale(String saleId, String newCodeProduct, int newQuantity, String newSaleDate) {
        if (mapCodeSale.containsKey(saleId)) {
            // Validar que la cantidad no sea negativa
            if (newQuantity <= 0) {
                return false;
            }

            // Validar que la fecha no sea mayor a la actual ni negativa
            if (!isValidDate(newSaleDate)) {
                return false;
            }

            // Obtener la venta y el producto asociado
            Sale sale = mapCodeSale.get(saleId);
            Product product = getProduct(newCodeProduct);

            // Verificar si el producto existe
            if (product == null) {
                return false;
            }

            // Calcular el total de la venta
            double newTotalPrice = product.getPrice() * newQuantity;

            // Verificar si hay suficiente stock
            if (product.getQuantity() + sale.getQuantitySold() - newQuantity < getStockMinimo()) {
                return false;
            }

            // Actualizar la venta
            sale.setCodeProduct(newCodeProduct);
            sale.setQuantitySold(newQuantity);
            sale.setTotal(newTotalPrice);
            sale.setSaleDate(newSaleDate);

            // Actualizar el stock del producto
            product.setQuantity(product.getQuantity() - newQuantity);

            // Actualizar el archivo de ventas después de modificar la venta
            dumpFilePlain();
            dumpFileXML();
            return true;
        }
        return false; // La venta con el saleId especificado no existe
    }

    // Método para generar un reporte del total devengado por producto en un día
    // determinado
    public double getTotalRevenueForProductOnDate(String productCode, String date) {
        double totalRevenue = 0.0;

        // Iterar sobre todas las ventas
        for (Sale sale : mapCodeSale.values()) {
            // Verificar si la venta ocurrió en el día especificado y es del producto
            // deseado
            if (sale.getSaleDate().equals(date) && sale.getCodeProduct().equals(productCode)) {
                totalRevenue += sale.getTotal();
            }
        }

        return totalRevenue;
    }

    public Collection<Sale> getAllSales() {
        return mapCodeSale.values();
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

    @Override
    public void dumpFile(ETypeFile eTypeFile) {
        if (ETypeFile.PLAIN.equals(eTypeFile)) {
            this.dumpFilePlain();
        }
        if (ETypeFile.XML.equals(eTypeFile)) {
            this.dumpFileXML();
        }

    }

    @Override
    public void loadProduct(ETypeFile eTypeFile) {
        if (ETypeFile.PLAIN.equals(eTypeFile)) {
            this.loadFilePlain();
        }
        if (ETypeFile.XML.equals(eTypeFile)) {
            this.loadFileXML();
        }
    }
}
