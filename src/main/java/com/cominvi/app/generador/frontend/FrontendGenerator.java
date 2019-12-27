package com.cominvi.app.generador.frontend;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

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
	public static final String TS_COMPONENT_PATH = TS_PATH + "/catalogos";
	public static int MAXIMUM_FORM_TYPE_THRESHOLD = 5;


	/**
	 * Inicializa las carpetas
	 */
	public static void init() {
		mkdir(TS_PATH);
		mkdir(TS_MODELS_PATH);
		mkdir(TS_SERVICES_PATH);
		mkdir(TS_COMPONENT_PATH);
	}

	/**
	 * @param path
	 * @return
	 */
	private static boolean mkdir(String path) {
		File tsDirectory = new File(path);
		if (tsDirectory.mkdir()) {
			System.out.println("Carpeta de archivos TS creada en "+path);
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
	 * Construye los formularios
	 * 
	 * @param entity la entidad a construir
	 */
	public static void createForms(Entity entity) {
		FormType formType = getTableSizeWithoutCamposAuditoria(entity) > MAXIMUM_FORM_TYPE_THRESHOLD ? FormType.NG_MODEL : FormType.REACTIVE_FORM;
		
		String componentPath = TS_COMPONENT_PATH+"/"+entity.getName().toLowerCase();
		mkdir(componentPath);
		mkdir(componentPath+"/formulario");
		
		String buildForms = buildComponentFormHTML(entity, formType);
		createFile(componentPath+"/formulario", entity, buildForms, "formulario-"+entity.getName().toLowerCase() + ".component.html");
		buildForms = buildComponentFormTS(entity, formType);
		createFile(componentPath+"/formulario", entity, buildForms, "formulario-"+entity.getName().toLowerCase() + ".component.ts");
	}
	
	/**
	 * @param entity
	 * @return
	 */
	private static int getTableSizeWithoutCamposAuditoria(Entity entity) {
		int i = 0;
		for(Column column : entity.getColumns()) {
			if(column.isAuditoria())continue;
			i++;
		}
		return i;
	}
	
	/**
	 * Construye los formularios
	 * 
	 * @param entity la entidad a construir
	 */
	public static void createLists(Entity entity) {
		
		String componentPath = TS_COMPONENT_PATH+"/"+entity.getName().toLowerCase();
		mkdir(componentPath);
		mkdir(componentPath+"/listado");
		
		String buildForms = buildComponentListHTML(entity);
		createFile(componentPath+"/listado", entity, buildForms, "listado-"+entity.getName().toLowerCase() + ".component.html");
		buildForms = buildComponentListTS(entity);
		createFile(componentPath+"/listado", entity, buildForms, "listado-"+entity.getName().toLowerCase() + ".component.ts");
	}

	private static String buildComponentListTS(Entity entity) {
		return "import { Component, OnInit } from '@angular/core';\r\n" + 
				"import { "+entity.getName()+"Service } from 'src/app/services/service/catalogo/"+entity.getName().toLowerCase()+".service';\r\n" + 
				"import { "+entity.getName()+" } from 'src/app/models/catalogos/"+entity.getName().toLowerCase()+"';\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"\tselector: 'app-listado-"+entity.getName().toLowerCase()+"',\r\n" + 
				"\ttemplateUrl: './listado-"+entity.getName().toLowerCase()+".component.html',\r\n" + 
				"\tstyles: []\r\n" + 
				"})\r\n" + 
				"export class Listado"+entity.getName()+"Component implements OnInit {\r\n" + 
				"\tpublic "+entity.getName().toLowerCase()+" : "+entity.getName()+"[];\r\n" + 
				"\tpublic cols:any[];"+
				"\r\n" + 
				"\tconstructor(private "+entity.getName().toLowerCase()+"Service : "+entity.getName()+"Service) { }\r\n" + 
				"\r\n" + 
				"\tngOnInit() {\r\n" + 
				"\t\tthis."+entity.getName().toLowerCase()+"Service.findAll().subscribe(data => {\r\n" + 
				"\t\t\tthis."+entity.getName().toLowerCase()+" = data;\r\n" + 
				"\t\t});\r\n" + 
				"\t\tthis.cols = ["+getTSCols(entity)+"];"+
				"\t}\r\n" + 
				"\r\n" + 
				"}\r\n" + 
				"";
	}

	/**
	 * @param entity
	 * @return
	 */
	private static String getTSCols(Entity entity) {
		String cols = "";
		for(Column column : entity.getColumns()) 
			cols+="\n\t\t\t{field: '"+column.getName()+"'"+", header:'"+ScriptBuilder.capitalizeFirstLetter(column.getName())+"'},";
		cols = cols.substring(0, cols.length()-1);
		return cols;
	}

	/**
	 * @param entity
	 * @return
	 */
	private static String buildComponentListHTML(Entity entity) {
		return "<mat-card class=\""+entity.getName().toLowerCase()+"-list-card\">\r\n" + 
				"\t\t<mat-card-header>\r\n" + 
				"\t\t\t\t<mat-card-title>"+entity.getName()+"</mat-card-title>\r\n" + 
				"\t\t\t\t<mat-card-subtitle>"+entity.getName().toLowerCase()+"</mat-card-subtitle>\r\n" + 
				"\t\t</mat-card-header>\r\n" + 
				"\r\n" + 
				"\t\t<mat-card-content>\r\n" + 
				"\t\t\t\t<p-table [value]=\""+entity.getName().toLowerCase()+"\">\r\n" + 
				"\t\t\t\t\t\t<ng-template pTemplate=\"header\">\r\n" + 
				"\t\t\t\t\t\t\t\t<tr>\r\n" + 
				"\t\t\t\t\t\t\t\t\t<th *ngFor=\"let col of cols\">\r\n" + 
				"\t\t\t\t\t\t\t\t\t\t{{col.header}}\r\n" + 
				"\t\t\t\t\t\t\t\t\t</th>"+
				//getTableHeaders(entity)+
				"\t\t\t\t\t\t\t\t</tr>\r\n" + 
				"\t\t\t\t\t\t</ng-template>\r\n" + 
				"\t\t\t\t\t\t<ng-template pTemplate=\"body\" let-"+ScriptBuilder.getSingularEntityName(entity)+">\r\n" + 
				"\t\t\t\t\t\t\t\t<tr>\r\n" + 
				"\t\t\t\t\t\t\t\t\t<td *ngFor=\"let col of cols\">\r\n" + 
				"\t\t\t\t\t\t\t\t\t\t{{"+ScriptBuilder.getSingularEntityName(entity)+"[col.field]}}\r\n" + 
				"\t\t\t\t\t\t\t\t\t</td>"+
				//getTableColumns(entity)+
				"\t\t\t\t\t\t</ng-template>\r\n" + 
				"\t\t\t\t</p-table>\r\n" + 
				"\t\t</mat-card-content>\r\n" + 
				"\t\t<mat-card-actions>\r\n" + 
				"\t\t\t\t<button mat-button>REGRESAR</button>\r\n" + 
				"\t\t</mat-card-actions>\r\n" + 
				"</mat-card>";
	}
	

	/**
	 * @param entity
	 * @return
	 */
	private static String getTableColumns(Entity entity) {
		String rows = "";
		for(Column column : entity.getColumns()) 
			rows+="\t\t\t\t\t\t\t\t\t\t<td>{{"+ScriptBuilder.getSingularEntityName(entity)+"."+column.getName()+"}}</td>\r\n"; 
		return rows;
	}

	/**
	 * @param entity
	 * @return
	 */
	private static String getTableHeaders(Entity entity) {
		String headers = "";
		for(Column column : entity.getColumns()) 
			headers+="\t\t\t\t\t\t\t\t\t\t<th>"+ScriptBuilder.capitalizeFirstLetter(column.getName())+"</th>\r\n";
		return headers;
	}

	/**
	 * @param entity
	 * @return
	 */
	private static String buildServices(Entity entity) {
		return "import { Injectable } from '@angular/core';\r\n"
				+ "import { HttpClient } from '@angular/common/http';\r\n" + "import { " + entity.getName()
				+ " } from 'src/app/models/" + entity.getName().toLowerCase() + "';\r\n"
				+ "import { Observable } from 'rxjs';\r\n"
				+ "import { environment } from 'src/environments/environment';\r\n\n" + "@Injectable()\r\n"
				+ "export class " + entity.getName() + "Service {\r\n" + " \n\t" + "private "
				+ ScriptBuilder.getPluralEntityName(entity) + "Url: string;\r\n" + " \r\n"
				+ "\tconstructor(private http: HttpClient) {\r\n" + "\t\tthis."
				+ ScriptBuilder.getPluralEntityName(entity) + "Url = environment.urlpruebas + '"
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
	 * @param entity
	 * @return
	 */
	private static String buildComponentFormHTML(Entity entity, FormType formType) {
		return "<mat-card class=\"" + entity.getName().toLowerCase() + "-card\">\r\n" + "\t<mat-card-header>\r\n"
				+ "\t\t<mat-card-title>" + entity.getName() + "</mat-card-title>\r\n"
				+ "\t\t<mat-card-subtitle>{{action}} " + ScriptBuilder.getSingularEntityName(entity)
				+ "</mat-card-subtitle>\r\n" + "\t</mat-card-header>\r\n" + "\r\n" + "\t<mat-card-content>\r\n"
				+ FormBuilder.getFormsHTML(entity, formType) + "\t</mat-card-content>\r\n" + "\t<mat-card-actions>\r\n"
				+ "\t\t<button mat-button>Aceptar</button>\r\n" + "\t\t<button mat-button>Cancelar</button>\r\n"
				+ "\t</mat-card-actions>\r\n" + "</mat-card>";
	}

	public enum FormType {
		REACTIVE_FORM, NG_MODEL
	}


	
	private static String buildComponentFormTS(Entity entity, FormType formType) {
		return "import { Component, OnInit } from '@angular/core';\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"\tselector: 'app-formulario-"+entity.getName().toLowerCase()+"',\r\n" + 
				"\ttemplateUrl: './formulario-"+entity.getName().toLowerCase()+".component.html',\r\n" + 
				"\tstyles: []\r\n" + 
				"})\r\n" + 
				"export class Formulario"+entity.getName()+"Component implements OnInit {\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\tconstructor() { }\r\n" + 
				"\r\n" + 
				"\tngOnInit() {\r\n" + 
				"\t}\r\n" + 
				"\r\n" + 
				"}\r\n" ;
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
	 * @param entity   la entidad
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
					+ getTypeScriptField(ScriptBuilder.getFieldByColumn(column, entity, true)) + ";\r\n";
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
		if (fieldType.equals("date"))
			return "string";
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
