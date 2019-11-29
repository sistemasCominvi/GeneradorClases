package com.cominvi.app.generador.xml;

import java.util.ArrayList;
import java.util.List;

import com.cominvi.app.generador.xml.Column.ColumnType;

/**
 * Clase para instanciar objetos de tipo entidad desde el orm.xml
 * 
 * @author angelo.loza
 *
 */
public class Entity {
	
	private List<Column> columns;
	private String name;
	private String tableName;
	
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Column> getPrimaryKeys() {
		List<Column> primaryKeys = new ArrayList<Column>();
		for(Column column : this.columns) 
			if(column.getColumnType() == ColumnType.ID)
				primaryKeys.add(column);
		
		return primaryKeys;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
