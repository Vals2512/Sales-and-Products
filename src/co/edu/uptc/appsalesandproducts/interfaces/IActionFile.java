package co.edu.uptc.appsalesandproducts.interfaces;

import co.edu.uptc.appsalesandproducts.enums.ETypeFile;

public interface IActionFile {

    public void dumpFile(ETypeFile eTypeFile);

    public void loadProduct(ETypeFile eTypeFile);

}
