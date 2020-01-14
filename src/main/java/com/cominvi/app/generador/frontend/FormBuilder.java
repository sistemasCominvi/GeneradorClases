package com.cominvi.app.generador.frontend;

import java.lang.reflect.Field;

import com.cominvi.app.generador.files.scriptbuilder.ScriptBuilder;
import com.cominvi.app.generador.frontend.FrontendGenerator.FormType;
import com.cominvi.app.generador.xml.Column;
import com.cominvi.app.generador.xml.Entity;

public class FormBuilder {
	/**
	 * @param columns
	 * @return
	 */
	public static String getFormsHTML(Entity entity, FormType formType) {
		String form = "";
		for (Column column : entity.getColumns()) {
			if (column.isAuditoria())
				continue;
			if (column.getColumnDefinition().contains("identity"))
				continue;
			
			String input = null;
			String required = !column.isNullable() ? " required" : "";
			switch (formType) {
			case REACTIVE_FORM:
				input = "\t\t\t\t<input matInput " + getInputType(column, entity) + " formControlName=\""
						+ column.getName() + "\"" + required + ">\r\n";
				break;
			case NG_MODEL:
				input = "\t\t\t\t<input matInput " + getInputType(column, entity) + " [(ngModel)]=\""
						+ ScriptBuilder.getSingularEntityName(entity) + "." + column.getName() + "\"" + required
						+ ">\r\n";
				break;
			default:
				break;
			}
			form += "<p>\r\n" + "\t\t\t<mat-form-field appearance=\"legacy\">\r\n" + "\t\t\t\t<mat-label>"
					+ ScriptBuilder.capitalizeFirstLetter(column.getName()) + "</mat-label>\r\n" + input
					+ "\t\t\t\t<mat-hint>Ingresa " + column.getName() + "</mat-hint>\r\n"
					+ "\t\t\t</mat-form-field>\r\n" + "\t\t</p>";
		}
		return form;
	}

	/**
	 * @param column
	 * @param entity
	 * @return
	 */
	private static String getInputType(Column column, Entity entity) {
		
		Field field = ScriptBuilder.getFieldByColumn(column, entity, true);
		
		if(field == null) return "";

		String inputType = "type=\"text\"";

		String type = field.getType().toString().toLowerCase();
		if (type.contains("int") || type.contains("long") || type.contains("short") || type.contains("byte"))
			inputType = "type=\"number\"";
		if (type.contains("float") || type.contains("double"))
			inputType = "type=\"number\" step=\"0.1\"";
		if (type.contains("date"))
			inputType = "type=\"datetime-local\"";

		return inputType;
	}
}
