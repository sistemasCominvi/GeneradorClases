package com.rever.files.scriptbuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.rever.files.xml.Column;
import com.rever.files.xml.Column.ColumnType;
import com.rever.files.xml.Entity;
import com.rever.folders.ProjectFolderConfiguration;

/**
 * Clase para construir la estructura del repositorio
 * 
 * @author angelo.loza
 *
 */
public class ScriptBuilder {

	/**
	 * @param entities           las entidades
	 * @param withQuestionSymbol si se quiere en vez de los nombres el signo de
	 *                           interrogaci�n
	 * @return las columnas de la entidad concatenadas columna1,columna2 o si es
	 *         signo ?,?
	 */
	public static String getAllColumns(Entity entities, boolean withQuestionSymbol) {
		String columns = "";
		if (!entities.getColumns().isEmpty()) {
			for (Column column : entities.getColumns()) {
				if (column.getColumnType() != ColumnType.ID)
					columns += (!withQuestionSymbol ? column.getName() : "?") + ",";
			}
			columns = columns.substring(0, columns.length() - 1);
		}
		return columns;
	}

	/**
	 * @param entity la entidad
	 * @return sus campos ordenados por coma ejemplo: campo1=?,campo2=?,etc.
	 */
	public static String getAllColumnsForSQLSet(Entity entity) {
		String columns = "";
		for (Column column : entity.getColumns()) {
			if (column.getColumnType() != ColumnType.ID)
				columns += column.getName() + " = ?,";
		}
		columns = columns.substring(0, columns.length() - 1);
		return columns;
	}

	/**
	 * Construye el prepared statement en forma de: ps.setTipoDeDato(posicion,
	 * entidad.getNombreColumna()); Debe de estar ordenado conforme a la base de
	 * datos, para ello se obtiene el orden desde el orm.xml, para ordenar los
	 * campos del modelo conforme al orden de la base de datos.
	 * 
	 * @param entity
	 * @return
	 */
	public static String createPreparedStatementFromEntity(Entity entity) {
		String preparedStatement = "";
		/*
		 * 
		 * Extrae los campos de ese modelo (el tipo de dato y el nombre)
		 * 
		 */
		Field[] fields = getEntityFields(entity, false);
		/*
		 * Inicializa el contador de posicion en el PS
		 */
		int counter = 1;
		/*
		 * Construye por cada campo
		 */
		for (int i = 0; i < fields.length; i++) {
			if (getDebuggedField(fields[i]).equals("Set"))
				continue;
			preparedStatement += "ps.set" + getDebuggedField(fields[i]) + "(" + (counter) + ", "
					+ buildGet(entity, fields[i]) + ");\n";
			counter++;
		}
		return preparedStatement;
	}

	/**
	 * @return un String con los(as) campos/columnas de la entidad en forma de
	 *         entidad.getNombreCampo(),
	 */
	public static String getAllColumnGets(Entity entity) {

		String columns = "";

		Field[] fields = getEntityFields(entity, false);

		for (int i = 0; i < fields.length; i++) {
			String getType = fields[i].getType().toString().contains("Boolean")
					|| fields[i].getType().toString().contains("boolean") ? ".is" : ".get";
			columns += getSingularEntityName(entity) + getType + capitalizeFirstLetter(fields[i].getName()) + "(),\n";
		}
		String result = null;
		try {
			result = columns.substring(0, columns.length() - 2);
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println(
					"Ocurri� un error al obtener los get de la entidad " + entity.getName() + ", result:" + columns);
		}
		return result;
	}

	/**
	 * @param entity la entidad
	 * @return los campos de la entidad en funcion del paquete configurado del
	 *         modelo y de la entidad enviada como par�metro
	 */
	private static Field[] getEntityFields(Entity entity, boolean withIds) {
		Field[] fields = new Field[] {};
		try {
			Class<?> cls = Class.forName(
					ProjectFolderConfiguration.getModelPackage() + "." + capitalizeFirstLetter(entity.getName()));
			fields = orderFields(cls.getDeclaredFields(), entity, withIds);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return fields;
	}

	/**
	 * Regresa el get de la entidad ejemplo: entidad.getNombreCampo() si es fecha
	 * regresa new java.sql.Date(entidad.getFecha().getTime())
	 * 
	 * @param entity la entidad
	 * @param field  el campo
	 * @return si quiere ".set" o ".get"
	 */
	public static String buildGet(Entity entity, Field field) {
		String getType = field.getType().toString().contains("Boolean")
				|| field.getType().toString().contains("boolean") ? ".is" : ".get";
		String baseGet = getSingularEntityName(entity) + getType + capitalizeFirstLetter(field.getName()) + "()";
		if (getDebuggedField(field).equals("Date"))
			return "new java.sql.Date(" + baseGet + ".getTime())";
		else
			return baseGet;
	}

	/**
	 * @param entity la entidad
	 * @return el nombre de la entidad en singular (si es Entidades regresa entidad)
	 */
	private static String getSingularEntityName(Entity entity) {
		String entityName = entity.getName().toLowerCase();
		if (entityName.charAt(entityName.length() - 1) == 's')
			entityName = entityName.substring(0, entityName.length() - 1);
		return entityName;
	}

	/**
	 * M�todo para depurar el nombre del campo de la entidad si tiene paquetes toma
	 * solo su nombre, si es nativo hace may�scula la primera letra y lo regresa, si
	 * es Integer lo hace Int (para los m�todos set y get del Prepared Statement)
	 * 
	 * @param field el campo a depurar
	 * @return el campo depurado
	 */
	public static String getDebuggedField(Field field) {
		String type = field.getType().toString();
		String[] typePackages = type.split("\\.");
		/*
		 * Si se pudo hacer split es que tiene paquetes ejemplo class java.lang.Integer
		 * toma solo Integer (posici�n 2 en arreglo)
		 */
		if (typePackages.length > 0) {
			return capitalizeFirstLetter(typePackages[typePackages.length - 1]).equals("Integer") ? "Int"
					: capitalizeFirstLetter(typePackages[typePackages.length - 1]);
		} else {
			return capitalizeFirstLetter(type).equals("Integer") ? "Int" : capitalizeFirstLetter(type);
		}
	}

	/**
	 * @param str el String a convertir
	 * @return el string con la primera letra may�scula
	 */
	public static String capitalizeFirstLetter(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * @param entity la entidad a convertir
	 * @return el string formado
	 */
	public static String buildRowMapper(Entity entity) {
		String rowMapper = "";
		Field[] fields = getEntityFields(entity, true);
		for (int i = 0; i < fields.length; i++) {
			if (getDebuggedField(fields[i]).equals("Set"))
				continue;
			rowMapper += getSingularEntityName(entity) + ".set" + capitalizeFirstLetter(fields[i].getName()) + "(rs.get"
					+ getDebuggedField(fields[i]) + "(\"" + fields[i].getName() + "\")); \n";
		}
		return rowMapper;
	}

	/**
	 * Ordena los campos de acuerdo al orden del archivo orm.xml
	 * 
	 * @param inputFields los campos a ordenar
	 * @param entity      la entidad para extrae las columnas y su orden original
	 * @return los campos ordenados
	 */
	public static Field[] orderFields(Field[] inputFields, Entity entity, boolean withIds) {
		/*
		 * Crea un arreglo vacio para los nuevos campos
		 */
		Field[] fields = new Field[inputFields.length];
		/*
		 * Por cada campo buscalo en las columnas y obten su indice y asignalo
		 */
		inputCycle: for (int i = 0; i < inputFields.length; i++) {
			columnCycle: for (Column column : entity.getColumns()) {
				if (column.getColumnType() == ColumnType.ID && !withIds)
					continue columnCycle;
				if (column.getName().equals(inputFields[i].getName())) {
					fields[entity.getColumns().indexOf(column)] = inputFields[i];
					continue inputCycle;
				}
			}
		}
		/*
		 * Limpia los nulos
		 */
		ArrayList<Field> cleanFields = new ArrayList<Field>();
		for (int i = 0; i < fields.length; i++)
			if (fields[i] != null)
				cleanFields.add(fields[i]);

		return cleanFields.toArray(new Field[0]);
	}

	/**
	 * @param column la columna que se desea obtener el tipo o campo
	 * @param entity la entidad a extraer
	 * @return el campo correspondiente a la columna
	 */
	public static Field getColumnField(Column column, Entity entity, boolean withIds) {
		Field[] fields = getEntityFields(entity, withIds);
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(column.getName()))
				return fields[i];
		}
		return null;
	}

	/**
	 * @param primaryKey la llave primaria a convertir
	 * @param entity     la entidad
	 * @return la asiganci�n dinamica segun su tipo (Long, int o string)
	 */
	public static String getDynamicIDAssignation(Column primaryKey, Entity entity) {
		Field field = getColumnField(primaryKey, entity, true);
		if (field != null) {
			if (field.getType().toString().contains("Long") || field.getType().toString().contains("long")) {
				return "set" + ScriptBuilder.capitalizeFirstLetter(primaryKey.getName())
						+ "(keyHolder.getKey().longValue());";
			} else if (field.getType().toString().contains("Integer") || field.getType().toString().contains("int")) {
				return "set" + ScriptBuilder.capitalizeFirstLetter(primaryKey.getName())
						+ "(keyHolder.getKey().intValue());";
			} else if (field.getType().toString().contains("String")) {
				return "set" + ScriptBuilder.capitalizeFirstLetter(primaryKey.getName())
						+ "(keyHolder.getKey().toString());";
			} else if (field.getType().toString().contains("Double") || field.getType().toString().contains("double")) {
				return "set" + ScriptBuilder.capitalizeFirstLetter(primaryKey.getName())
						+ "(keyHolder.getKey().doubleValue());";
			}
		}
		return null;
	}
}
