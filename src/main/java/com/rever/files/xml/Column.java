package com.rever.files.xml;

public class Column {
	private String name;
	private String columnDefinition;
	private boolean nullable;
	private int length;
	private ColumnType columnType;
	private String fieldName;

	public enum ColumnType {
		ID, BASIC, MANY_TO_ONE, ONE_TO_MANY, FOREIGN
	}

	public String getName() {
		return this.name.contains("_") ? convert(this.name) : this.name;
	}

	/**
	 * @param input el texto a convertir
	 * @return cadena de texto donde _a se convierte a A
	 */
	public static String convert(String input) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < input.length(); i++) { 
			if (input.charAt(i) == '_') {
				stringBuilder.append((Character.toUpperCase(input.charAt(i + 1))));
				i++;
			} else 
				stringBuilder.append(input.charAt(i));
		}
		return stringBuilder.toString();
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getColumnDefinition() {
		return columnDefinition;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public ColumnType getColumnType() {
		return columnType;
	}

	public Column setColumnType(ColumnType columnType) {
		this.columnType = columnType;
		return this;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
