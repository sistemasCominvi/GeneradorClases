package com.cominvi.app.generador.folders;

import com.cominvi.app.generador.pom.POMExplorer;
import com.cominvi.app.generador.xml.XMLExtractor;

/**
 * @author angelo.loza Clase para obtener carpetas del proyecto
 */
public class ProjectFolderConfiguration {
	public static final String PACKAGE_DIVIDER = ".";
	public static final String SRC_LOCATION = "src\\main\\java";
	public static final String ORM_XML_LOCATION = SRC_LOCATION + "\\orm.xml";
	public static final String REVERSE_PERSISTENCE_XML_LOCATION = "src\\main\\resources\\META-INF\\reverse-persistence.xml";
	public static final String MODEL_DESIGNATION = "models";
	public static final String PROJECT_DIR = "user.dir";

	/**
	 * @param packageName el nombre del paquete
	 * @return los paquetes dividos en arreglo
	 */
	public static String[] getPackages(String packageName) {
		return packageName.split(PACKAGE_DIVIDER);
	}

	/**
	 * Obtiene la ruta de orm.xml generado por openjpa
	 * 
	 * @return path
	 */
	public static String getORMXMLAbsolutePath() {
		return System.getProperty(PROJECT_DIR) + "\\" + ORM_XML_LOCATION;
	}

	/**
	 * @return la ruta al reverse-persistence.xml
	 */
	public static String getReversePersistenceXMLAbsolutePath() {
		return System.getProperty(PROJECT_DIR) + "\\" + REVERSE_PERSISTENCE_XML_LOCATION;
	}

	/**
	 * Obtiene el paquete del proyecto establecida en el pom (paquete base + nombre
	 * del proyecto)
	 * 
	 * @return basepackage
	 */
	public static String getBasePackage() {
		return POMExplorer.getPropertyValueByName(POMExplorer.PACKAGE_PROPERTY) + PACKAGE_DIVIDER
				+ new XMLExtractor(REVERSE_PERSISTENCE_XML_LOCATION).getDatabaseName();
	}

	/**
	 * @return la ruta base del proyecto (disco c + workspace + src)
	 */
	public static String getBaseURI() {
		return System.getProperty(PROJECT_DIR) + "\\" + SRC_LOCATION + "\\" + getBasePackage().replace(".", "\\")
				+ "\\";
	}

	/**
	 * @return la ruta del modelo
	 */
	public static String getModelPath() {
		return System.getProperty(PROJECT_DIR) + "\\"+SRC_LOCATION+"\\"
				+ POMExplorer.getPropertyValueByName(POMExplorer.PACKAGE_PROPERTY_MODEL).replace(".", "\\") + "\\";
	}

	/**
	 * @return el paquete del modelo
	 */
	public static String getModelPackage() {
		return POMExplorer.getPropertyValueByName(POMExplorer.PACKAGE_PROPERTY_MODEL);
	}

	/**
	 * @return el paquete del generic dao
	 */
	public static String getGenericDaoPackage() {
		return POMExplorer.getPropertyValueByName(POMExplorer.PACKAGE_PROPERTY) + ".generic.dao";
	}

	/**
	 * @return el paquete del servicio generico
	 */
	public static String getGenericServicePackage() {
		return POMExplorer.getPropertyValueByName(POMExplorer.PACKAGE_PROPERTY) + ".generic.service";
	}
}
