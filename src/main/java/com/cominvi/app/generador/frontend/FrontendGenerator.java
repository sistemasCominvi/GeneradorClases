package com.cominvi.app.generador.frontend;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import com.cominvi.app.generador.files.FileCreator;
import com.cominvi.app.generador.files.scriptbuilder.ScriptBuilder;
import com.cominvi.app.generador.folders.ProjectFolderConfiguration;
import com.cominvi.app.generador.xml.Column;
import com.cominvi.app.generador.xml.Entity;

/**
 * 
 * Clase para generar los modelos de TypeScript
 * @author angelo.loza
 *
 */
public class FrontendGenerator {

	private static final String numberFields[] = { "Integer", "Float", "Long", "Short", "Byte" };
	public static final String TS_PATH = System.getProperty(ProjectFolderConfiguration.PROJECT_DIR) + "/ts";

	/**
	 * Inicializa las carpetas
	 */
	public static void init() {
		File tsDirectory = new File(TS_PATH);
		if (tsDirectory.mkdir())
			System.out.println("Carpeta de archivpos TS creada.");
	}

	/**
	 * Crea el modelo
	 * @param entity la entidad a generar
	 */
	public static void createModel(Entity entity) {
		String buildModel = buildModel(entity);
		createFile(entity, buildModel);
	}

	/**
	 * @param entity la entidad
	 * @param buildModel el texto
	 */
	private static void createFile(Entity entity, String buildModel) {
		File sourceFile = new File(TS_PATH+"\\"+entity.getName().toLowerCase()+".ts");
		try {
			sourceFile.createNewFile();
			FileCreator.writeFile(buildModel, sourceFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param entity la entidad
	 * @return el modelo construido en String
	 */
	private static String buildModel(Entity entity) {
		String tsClass = "export class " + entity.getName() + " {\r\n";
		for (Column column : entity.getColumns()) {
			tsClass += "\t"+column.getName() + ":"
					+ getTypeScriptField(ScriptBuilder.getFieldByColummn(column, entity, true)) + ";\r\n";
		}
		tsClass += "}";
		return tsClass;
	}

	/**
	 * @param field el campo
	 * @return el tipo de dato en typescript
	 */
	private static String getTypeScriptField(Field field) {
		if(field == null) return "any";
		String fieldType = ScriptBuilder.getDebuggedField(field, false).toLowerCase();

		if (isTypeNumber(fieldType))
			return "number";
		if (fieldType.equals("string") || fieldType.equals("boolean"))
			return fieldType;
		return "any";
	}

	/**
	 * @param field el campo
	 * @return si es tipo number
	 */
	private static Boolean isTypeNumber(String field) {
		for (int i = 0; i < numberFields.length; i++)
			if (numberFields[i].toLowerCase().equals(field))
				return true;

		return false;
	}

}
