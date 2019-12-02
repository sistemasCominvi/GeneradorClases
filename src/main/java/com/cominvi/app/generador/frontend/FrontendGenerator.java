package com.cominvi.app.generador.frontend;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import com.cominvi.app.generador.files.FileCreator;
import com.cominvi.app.generador.files.scriptbuilder.ScriptBuilder;
import com.cominvi.app.generador.files.scriptbuilder.ScriptBuilder.PrimaryKeyScriptType;
import com.cominvi.app.generador.folders.ProjectFolderConfiguration;
import com.cominvi.app.generador.xml.Column;
import com.cominvi.app.generador.xml.Entity;

/**
 * 
 * Clase para generar los modelos de TypeScript
 * 
 * @author angelo.loza
 *
 */
public class FrontendGenerator {

	private static final String numberFields[] = { "Integer", "Float", "Long", "Short", "Byte" };

	public static final String TS_PATH = System.getProperty(ProjectFolderConfiguration.PROJECT_DIR) + "/ts";
	public static final String TS_MODELS_PATH = TS_PATH + "/models";
	public static final String TS_SERVICES_PATH = TS_PATH + "/services";

	/**
	 * Inicializa las carpetas
	 */
	public static void init() {
		mkdir(TS_PATH);
		mkdir(TS_MODELS_PATH);
		mkdir(TS_SERVICES_PATH);
	}

	/**
	 * @param path
	 * @return
	 */
	private static boolean mkdir(String path) {
		File tsDirectory = new File(path);
		if (tsDirectory.mkdir()) {
			System.out.println("Carpeta de archivos TS creada.");
			return true;
		}
		return false;
	}

	/**
	 * @param entity
	 */
	public static void createServices(Entity entity) {
		String buildServices = buildServices(entity);
		createFile(TS_SERVICES_PATH, entity, buildServices, entity.getName().toLowerCase() + ".service.ts");
	}

	/**
	 * @param entity
	 * @return
	 */
	private static String buildServices(Entity entity) {
		return "import { Injectable } from '@angular/core';\r\n"
				+ "import { HttpClient } from '@angular/common/http';\r\n" + "import {" + entity.getName()
				+ "} from 'src/app/models/" + entity.getName().toLowerCase() + "'\r\n"
				+ "import {Observable} from 'rxjs'\r\n" + " \r\n" + "@Injectable()\r\n" + "export class "
				+ entity.getName() + "Service {\r\n" + " \n\t" + "private " + ScriptBuilder.getPluralEntityName(entity)
				+ "Url: string;\r\n" + " \r\n" + "\tconstructor(private http: HttpClient) {\r\n" + "\t\tthis."
				+ ScriptBuilder.getPluralEntityName(entity) + "Url = 'http://localhost:8004/"
				+ ScriptBuilder.getSingularEntityName(entity) + "';\r\n" + "\t}\r\n" + " \r\n"
				+ "\tpublic findAll(): Observable<" + entity.getName() + "[]> {\r\n" + "\t\treturn this.http.get<"
				+ entity.getName() + "[]>(this." + ScriptBuilder.getPluralEntityName(entity) + "Url);\r\n" + "\t}\r\n"
				+ " \r\n" + "\tpublic save(" + ScriptBuilder.getPluralEntityName(entity) + ": " + entity.getName()
				+ ") {\r\n" + "\t\treturn this.http.post<" + entity.getName() + ">(this."
				+ ScriptBuilder.getPluralEntityName(entity) + "Url, " + ScriptBuilder.getPluralEntityName(entity)
				+ ");\r\n" + "\t}\r\n" + "\r\n" + "\tpublic delete("
				+ ScriptBuilder.getPrimaryKeys(entity, PrimaryKeyScriptType.TS_PARAMETER) + "){\r\n"
				+ "\t\treturn this.http.delete<" + entity.getName() + ">(this."
				+ ScriptBuilder.getPluralEntityName(entity) + "Url+"
				+ ScriptBuilder.getPrimaryKeys(entity, PrimaryKeyScriptType.TS_URL) + ");\r\n" + "\t}\r\n" + "\r\n"
				+ "\tpublic get(" + ScriptBuilder.getPrimaryKeys(entity, PrimaryKeyScriptType.TS_PARAMETER) + "){\r\n"
				+ "\t\treturn this.http.get<" + entity.getName() + ">(this." + ScriptBuilder.getPluralEntityName(entity)
				+ "Url+" + ScriptBuilder.getPrimaryKeys(entity, PrimaryKeyScriptType.TS_URL) + ");\r\n" + "\t}\r\n"
				+ "\r\n" + "\r\n" + "}";
	}

	/**
	 * Crea el modelo
	 * 
	 * @param entity la entidad a generar
	 */
	public static void createModel(Entity entity) {
		String buildModel = buildModel(entity);
		createFile(TS_MODELS_PATH, entity, buildModel, entity.getName().toLowerCase() + ".ts");
	}

	/**
	 * @param entity     la entidad
	 * @param buildModel el texto
	 */
	private static void createFile(String path, Entity entity, String buildModel, String filename) {
		File sourceFile = new File(path + "\\" + filename);
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
			tsClass += "\t" + column.getName() + ":"
					+ getTypeScriptField(ScriptBuilder.getFieldByColummn(column, entity, true)) + ";\r\n";
		}
		tsClass += "}";
		return tsClass;
	}

	/**
	 * @param field el campo
	 * @return el tipo de dato en typescript
	 */
	public static String getTypeScriptField(Field field) {
		if (field == null)
			return "any";
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
