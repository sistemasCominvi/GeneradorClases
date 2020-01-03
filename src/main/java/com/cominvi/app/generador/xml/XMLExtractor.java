package com.cominvi.app.generador.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cominvi.app.generador.xml.Column.ColumnType;

/**
 * 
 * Clase para obtener datos de los archivos XML, si es el orm.xml genera su
 * lista de entidades.
 * 
 * @author angelo.loza
 *
 */
public class XMLExtractor {

	private static final String ENTITY_TAG = "entity";
	private static final String CLASS_TYPE = "class";
	private static final String ID_TAG = "id";
	private static final String BASIC_TAG = "basic";
	private static final String TABLE_TAG = "table";
	private static final String ATTRIBUTE_NAME = "name";
	private static final String PROPERTY_TAG = "property";
	private static final Object CONNECTION_ATTRIBUTE = "openjpa.ConnectionURL";
	private static final String VALUE_ATTRIBUTE = "value";
	private static final String MANY_TO_ONE_TAG = "many-to-one";
	private String xmlPath;

	public static final String[] CAMPOS_AUDITORIA = { "fechahoraalta", "fechahoramod", "idempleadoalta",
			"idempleadomod", "ipalta", "ipmod", "latitudalta", "latitudmod", "longitudalta", "longitudmod",
			"tipodispositivoalta", "tipodispositivomod" };
	
	public static final int LIMIT_CAMPOS_AUDITORIA_PRIMARY = 4;

	public XMLExtractor(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	/**
	 * @return la lista de entidades con sus columnas
	 */
	public List<Entity> extractEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		try {
			File fXmlFile = new File(getXmlPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			/*
			 * Extrae primero las entidades
			 */
			NodeList xmlEntities = doc.getElementsByTagName(ENTITY_TAG);
			/*
			 * Recorrelas
			 */
			for (int j = 0; j < xmlEntities.getLength(); j++) {

				Node xmlEntity = xmlEntities.item(j);

				if (xmlEntity.getNodeType() == Node.ELEMENT_NODE) {

					Element xmlEntityElement = (Element) xmlEntity;

					Entity entity = new Entity();
					/*
					 * Extrae su nombre
					 */
					entity.setName(xmlEntityElement.getAttribute(CLASS_TYPE));
					/*
					 * Extrae sus atributos
					 */

					List<Column> columns = new ArrayList<Column>();

					NodeList tables = xmlEntityElement.getElementsByTagName(TABLE_TAG);
					Element table = (Element) tables.item(0);

					entity.setTableName(table.getAttribute(ATTRIBUTE_NAME));

					columns.addAll(getColumnsByTag(xmlEntityElement, ID_TAG, ColumnType.ID));
					columns.addAll(getColumnsByTag(xmlEntityElement, BASIC_TAG, ColumnType.BASIC));
					columns.addAll(getColumnsByTag(xmlEntityElement, MANY_TO_ONE_TAG, ColumnType.FOREIGN));

					entity.setColumns(columns);
					entities.add(entity);
				}
			}
		} catch (Exception e) {
			System.err.println("No se han generado entidades.");
		}
		return entities;
	}

	/**
	 * @param parent
	 * @param tag
	 * @param columnType
	 * @return
	 */
	private List<Column> getColumnsByTag(Element parent, String tag, ColumnType columnType) {
		NodeList basics = parent.getElementsByTagName(tag);

		List<Column> columnList = new ArrayList<>();

		for (int i = 0; i < basics.getLength(); i++) {
			Element basic = (Element) basics.item(i);
			Column column = getDefinition(basic).setColumnType(columnType);
			String fieldName = basic.getAttribute(ATTRIBUTE_NAME);
			column.setFieldName(fieldName);
			columnList.add(column);

		}

		return columnList;
	}

	/**
	 * @param element the xml element
	 * @return la columna formada
	 */
	private Column getDefinition(Element element) {
		NodeList definitions = element.getElementsByTagName("column");
		if (definitions == null || definitions.getLength() == 0)
			definitions = element.getElementsByTagName("join-column");
		Element definition = (Element) definitions.item(0);
		Column column = new Column();
		if (definition.getAttribute("column-definition") != null)
			column.setColumnDefinition(definition.getAttribute("column-definition"));
		column.setName(definition.getAttribute("name"));
		column.setAuditoria(isCampoAuditoria(column.getName()));
		column.setAuditoriaPrimary(isCampoAuditoriaPrimary(column.getName()));
		try {
			int length = Integer.parseInt(definition.getAttribute("length"));
			column.setLength(length);
		} catch (NumberFormatException ex) {
		}
		String nullable = definition.getAttribute("nullable");
		column.setNullable(false);
		if (nullable != null && !nullable.equals("")) {
			column.setNullable(!nullable.equals("false"));
		}

		return column;
	}

	/**
	 * @param column
	 * @return
	 */
	private boolean isCampoAuditoria(String column) {
		for (int i = 0; i < CAMPOS_AUDITORIA.length; i++)
			if (CAMPOS_AUDITORIA[i].equals(column))
				return true;
		return false;
	}
	
	/**
	 * @param column
	 * @return
	 */
	private boolean isCampoAuditoriaPrimary(String column) {
		for (int i = 0; i < LIMIT_CAMPOS_AUDITORIA_PRIMARY; i++)
			if (CAMPOS_AUDITORIA[i].equals(column))
				return true;
		return false;
	}

	/**
	 * @return el nombre de la base de datos
	 */
	public String getDatabaseName() {
		try {
			File fXmlFile = new File(getXmlPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList xmlEntities = doc.getElementsByTagName(PROPERTY_TAG);
			for (int i = 0; i < xmlEntities.getLength(); i++) {
				Element property = (Element) xmlEntities.item(i);
				String name = property.getAttribute(ATTRIBUTE_NAME);
				if (name.equals(CONNECTION_ATTRIBUTE)) {
					String connection = property.getAttribute(VALUE_ATTRIBUTE);
					String databaseName = connection.substring(connection.indexOf("//") + 2, connection.length());
					return databaseName.split("/")[databaseName.split("/").length - 1];
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getXmlPath() {
		return xmlPath;
	}

	private void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}
}
