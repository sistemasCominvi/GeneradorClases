package com.rever.pom;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * Clase para explorar las propiedades del pom.xml
 * 
 * @author angelo.loza
 *
 */
public class POMExplorer {
	
	public static final String PACKAGE_PROPERTY = "entity-package";
	public static final String PACKAGE_PROPERTY_MODEL = "entity-package-model";
	/**
	 * Metodo para obtener la propiedad por su nombre
	 * 
	 * @param name
	 * @return
	 */
	public static String getPropertyValueByName(String name) {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model;
		try {
			model = reader.read(new FileReader("pom.xml"));

			return model.getProperties().get(name).toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}
}
