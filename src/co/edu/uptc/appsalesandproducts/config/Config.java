package co.edu.uptc.appsalesandproducts.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	private static Config config;
	private Properties properties;
	private String path;
	private String path2;
	private String nameFileTXT;
	private String nameFileXML;

	private Config() {
		this.properties = new Properties();

		try (FileInputStream entrada = new FileInputStream("resources/conf/appsap.config.properties")) {
			properties.load(entrada);
			this.path = properties.getProperty("app.file.path.txt");
			this.nameFileTXT = properties.getProperty("app.file.name.txt");
			this.nameFileXML = properties.getProperty("app.file.name.xml");
			this.path2 = properties.getProperty("stock.minimo.alerta");
		} catch (IOException ex) {
			System.err.println("Error al cargar el archivo properties de configuración: " + ex.getMessage());
		}
	}

	public static Config getInstance() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNameFileTXT() {
		return nameFileTXT;
	}

	public void setNameFileTXT(String nameFileTXT) {
		this.nameFileTXT = nameFileTXT;
	}

	public String getNameFileXML() {
		return nameFileXML;
	}

	public void setNameFileXML(String nameFileXML) {
		this.nameFileXML = nameFileXML;
	}

	public static Config getConfig() {
		return config;
	}

	public static void setConfig(Config config) {
		Config.config = config;
	}

	public String getPath2() {
		return path2;
	}

	public void setPath2(String path2) {
		this.path2 = path2;
	}

}
