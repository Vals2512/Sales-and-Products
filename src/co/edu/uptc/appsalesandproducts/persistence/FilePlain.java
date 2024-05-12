package co.edu.uptc.appsalesandproducts.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import co.edu.uptc.appsalesandproducts.commonconstants.Constants;
import co.edu.uptc.appsalesandproducts.config.Config;

public class FilePlain {
	protected Config confValue;
	private static final Logger logger = Logger.getLogger(FilePlain.class.getName());

	public FilePlain() {
		confValue = Config.getInstance();
	}

	/**
	 * <b>Descripción: </b> Método encargado de leer el archivo agregando el
	 * carácter de salto de línea
	 * 
	 * @author jcharris
	 */
	private String readFile(String pathName) {
		StringBuilder contenido = new StringBuilder();
		try {
			FileReader fr = new FileReader(pathName);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				contenido.append(linea).append(Constants.NEXT_LINE);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			logger.info("Se presentó un error al leer el archivo específicado");
		}
		return contenido.toString();
	}

	/**
	 * <b>Descripción: </b> Método encargado de escribir en el archivo
	 * sobreescribiendo el contennido
	 * 
	 * @author jcharris
	 */
	public void writeFile(String pathName, String content) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>Descripción:</b> Método encargado de la lectura y organización de las
	 * líneas encontradas en el fichero<br>
	 * 
	 * @author jcharris
	 */
	protected List<String> reader(String pathName) {
		List<String> output = new ArrayList<>();
		StringTokenizer tokens = new StringTokenizer(this.readFile(pathName), Constants.NEXT_LINE);
		while (tokens.hasMoreElements()) {
			output.add(tokens.nextToken());
		}
		return output;
	}

	/**
	 * <b>Descripción:</b> Método encargado de la escritura en el fichero<br>
	 * 
	 * @author jcharris
	 */
	protected void writer(String pathName, List<String> file) {
		StringBuilder strContent = new StringBuilder();
		file.forEach(record -> strContent.append(record).append(System.getProperty("line.separator")));
		writeFile(pathName, strContent.toString());
	}

}