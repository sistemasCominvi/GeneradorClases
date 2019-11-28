package com.rever.files.scriptbuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.CaseFormat;
import com.rever.files.XMLExtractor;
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

	private static final String FECHA_HORA_MOD_FIELD = "fechahoramod";
	private static final String FECHA_HORA_ALTA_FIELD = "fechahoraalta";
	private static final String ID_EMPLEADO_ALTA_FIELD = "idempleadoalta";

	/**
	 * @param entity             las entidades
	 * @param withQuestionSymbol si se quiere en vez de los nombres el signo de
	 *                           interrogaci�n
	 * @return las columnas de la entidad concatenadas columna1,columna2 o si es
	 *         signo ?,?
	 */
	public static String getAllColumns(Entity entity, boolean withQuestionSymbol) {
		String columns = "";
		if (!entity.getColumns().isEmpty()) {
			for (Column column : entity.getColumns()) {
				if (column.getColumnType() != ColumnType.ID || !entityHasIdentity(entity)) {
					columns += (!withQuestionSymbol ? column.getName()
							: (column.getName().equals(FECHA_HORA_MOD_FIELD)
									|| column.getName().equals(FECHA_HORA_ALTA_FIELD) ? "getdate()" : "?"))
							+ ",";
				}
			}
			columns = columns.substring(0, columns.length() - 1);
		}
		return columns;
	}

	/**
	 * @param entity la entidad
	 * @return su keyholder dependiendo si tiene autoincrementable
	 */
	public static String getKeyHolder(Entity entity) {

		return entityHasIdentity(entity) ? " KeyHolder keyHolder = new GeneratedKeyHolder();\r\n" + "        \r\n"
				+ "        jdbcTemplate.update(new PreparedStatementCreator() {\r\n" + "            @Override\r\n"
				+ "            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {\r\n"
				+ "                PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);\r\n"
				+ "                @preparedSetStatement\r\n" + "                return ps;\r\n" + "            }\r\n"
				+ "        }, keyHolder);\r\n" + "        \r\n" + "         @nameClassMin.@primarySetKey" :

				"        \r\n" + "        jdbcTemplate.update(new PreparedStatementCreator() {\r\n"
						+ "            @Override\r\n"
						+ "            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {\r\n"
						+ "                PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);\r\n"
						+ "                @preparedSetStatement\r\n" + "                return ps;\r\n"
						+ "            }\r\n" + "        });\r\n";
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
		Field[] fields = getEntityFields(entity, !entityHasIdentity(entity));
		/*
		 * Inicializa el contador de posicion en el PS
		 */
		int counter = 1;
		/*
		 * Construye por cada campo
		 */
		for (int i = 0; i < fields.length; i++) {
			if (getDebuggedField(fields[i], false).equals("Set"))
				continue;
			Class foreign = isEntityType(fields[i]);
			if (fields[i].getName().equals(FECHA_HORA_MOD_FIELD) || fields[i].getName().equals(FECHA_HORA_ALTA_FIELD))
				continue;
			if (foreign != null) {
				preparedStatement += "ps.set" + getDebuggedField(getIdForeign(fields[i]).getField(), false) + "("
						+ (counter) + ", " + buildGet(entity, fields[i]) + getForeignKeyPrimaryKeyGet(fields[i])
						+ ");\n";
			} else {
				preparedStatement += "ps.set" + getDebuggedField(fields[i], false) + "(" + (counter) + ", "
						+ buildGet(entity, fields[i]) + ");\n";
			}
			counter++;
		}
		return preparedStatement;
	}

	/**
	 * @param entity la entidad
	 * @return si la entidad tiene autoincrementable
	 */
	private static boolean entityHasIdentity(Entity entity) {
		for (Column column : entity.getColumns()) {
			if (column.getColumnType() == ColumnType.ID)
				if (column.getColumnDefinition().contains("identity"))
					return true;

		}
		return false;
	}

	/**
	 * 
	 * Genera los gets de la entidad para su update
	 * 
	 * 
	 * @return un String con los(as) campos/columnas de la entidad en forma de
	 *         entidad.getNombreCampo(),
	 */
	public static String getAllColumnGets(Entity entity) {

		String columns = "";

		/*
		 * Primero se agregan los campos sin id (para cuadrar con los signos de
		 * interrogacion de jdbc
		 */

		Field[] fields = getEntityFields(entity, false);// todos los campos sin llaves primarias
		Field[] allFields = getEntityFields(entity, true);// todos con llaves primarias
		List<Field> onlyIdsFields = new ArrayList<>();

		/*
		 * Agrega ahora las llaves primarias (con las que se definen los where del
		 * update)
		 */
		for (int i = 0; i < allFields.length; i++)
			for (Column column : entity.getPrimaryKeys())
				if (allFields[i].getName().equals(column.getName()))
					onlyIdsFields.add(allFields[i]);
		/*
		 * Une la lista de campos, con la lista de llaves primarias
		 */
		List<Field> orderedFields = new ArrayList<>();
		Collections.addAll(orderedFields, fields);
		orderedFields.addAll(onlyIdsFields);

		fields = orderedFields.toArray(new Field[orderedFields.size()]);
		/*
		 * Ahora si forma el codigo
		 */
		for (int i = 0; i < fields.length; i++) {
			String getType = fields[i].getType().toString().contains("Boolean")
					|| fields[i].getType().toString().contains("boolean") ? ".is" : ".get";
			if (!fields[i].getName().equals(FECHA_HORA_MOD_FIELD) && !fields[i].getName().equals(FECHA_HORA_ALTA_FIELD)
					&& !fields[i].getName().equals(ID_EMPLEADO_ALTA_FIELD))
				columns += getSingularEntityName(entity) + getType + capitalizeFirstLetter(fields[i].getName()) + "()"
						+ getForeignKeyPrimaryKeyGet(fields[i]) + ",\n";
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
	 * @return sus campos ordenados por coma ejemplo: campo1=?,campo2=?,etc.
	 */
	public static String getAllColumnsForSQLSet(Entity entity) {
		String columns = "";
		for (Column column : entity.getColumns()) {
			
			if(column.getName().equals(FECHA_HORA_ALTA_FIELD) || column.getName().equals(ID_EMPLEADO_ALTA_FIELD)) continue;

			if (column.getColumnType() != ColumnType.ID/* || !entityHasIdentity(entity) */)
				columns += column.getName().equals(FECHA_HORA_MOD_FIELD) ? column.getName() + " = getdate(),"
						: column.getName() + " = ?,";
		}
		columns = columns.substring(0, columns.length() - 1);
		return columns;
	}


	/**
	 * @param field
	 * @return
	 */
	private static String getForeignKeyPrimaryKeyGet(Field field) {
		ColumnField columnField = getIdForeign(field);
		return columnField != null ? ".get" + capitalizeFirstLetter(columnField.getColumn().getName()) + "()" : "";
	}

	/**
	 * 
	 * Cuando el campo sea una llave foranea se debe obtener el getId de ese campo o
	 * entidad foranea
	 * 
	 * @param field el campo a verificar si es foraneo
	 * @return el get formado del id de ese campo
	 */
	private static ColumnField getIdForeign(Field field) {
		/*
		 * Si es entidad el campo
		 */
		if (isEntityType(field) != null) {
			/*
			 * Extrae las entidades nuevamente para buscar la foranea y sacar sus llaves
			 * primarias
			 */
			List<Entity> entities = new XMLExtractor(ProjectFolderConfiguration.getORMXMLAbsolutePath())
					.extractEntities();
			for (Entity entity : entities) {
				/*
				 * Si se obtuvo una entidad entonces extrae su llave primaria
				 */
				if (entity.getName().equals(getDebuggedField(field, true))) {
					/*
					 * Busca el tipo de la llave primaria de la clase foranea
					 */
					Field[] entityFields = getEntityFields(entity, true);
					for (int i = 0; i < entityFields.length; i++) {
						if (entityFields[i].getName().equals(entity.getPrimaryKeys().get(0).getName())) {
							return new ColumnField(entity.getPrimaryKeys().get(0), entityFields[i]);
						}
					}

				}
			}
		}
		return null;
	}

	public static class ColumnField {

		private Column column;
		private Field field;

		public ColumnField(Column column, Field field) {
			this.column = column;
			this.field = field;
		}

		public Column getColumn() {
			return column;
		}

		public void setColumn(Column column) {
			this.column = column;
		}

		public Field getField() {
			return field;
		}

		public void setField(Field field) {
			this.field = field;
		}

	}

	/**
	 * Se necesita verificar que el campo a crear no sea entidad, para eso se
	 * instancea con su paquete (el declarado como modelo) y el nombre de la
	 * entidad, si se comprueba que no es entidad lanza una excepcion.
	 * 
	 * @param field el campo a verificar si es entidad
	 * @return true o false dependiendo si es entidad
	 */
	private static Class isEntityType(Field field) {
		try {
			Class<?> cls = Class.forName(ProjectFolderConfiguration.getModelPackage() + "."
					+ capitalizeFirstLetter(getDebuggedField(field, true)));
			return cls;
		} catch (ClassNotFoundException e) {
			return null;
		}
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
		if (getDebuggedField(field, false).equals("Date"))
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
	 * @param field    el campo a depurar
	 * @param getClean si se desea limpio sin depurar
	 * @return el campo depurado
	 */
	public static String getDebuggedField(Field field, boolean getClean) {
		String type = field.getType().toString();
		String[] typePackages = type.split("\\.");
		/*
		 * Si se pudo hacer split es que tiene paquetes ejemplo class java.lang.Integer
		 * toma solo Integer (posici�n 2 en arreglo)
		 */
		if (typePackages.length > 0) {
			return getClean ? typePackages[typePackages.length - 1]
					: capitalizeFirstLetter(typePackages[typePackages.length - 1]).equals("Integer") ? "Int"
							: capitalizeFirstLetter(typePackages[typePackages.length - 1]);
		} else {
			return getClean ? typePackages[typePackages.length - 1]
					: capitalizeFirstLetter(type).equals("Integer") ? "Int" : capitalizeFirstLetter(type);
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
			if (getDebuggedField(fields[i], false).equals("Set"))
				continue;
			if (isEntityType(fields[i]) != null) {
				ColumnField foreign = getIdForeign(fields[i]);
				rowMapper += getSingularEntityName(entity) + ".set" + capitalizeFirstLetter(fields[i].getName())
						+ "(new " + ProjectFolderConfiguration.getModelPackage() + "."
						+ capitalizeFirstLetter(getDebuggedField(fields[i], false)) + "(rs.get"
						+ getDebuggedField(foreign.getField(), false) + "(\"" + foreign.getColumn().getName()
						+ "\"))); \n";
			} else {
				rowMapper += getSingularEntityName(entity) + ".set" + capitalizeFirstLetter(fields[i].getName())
						+ "(rs.get" + isDate(getDebuggedField(fields[i], false)) + "(\"" + fields[i].getName()
						+ "\")); \n";
			}
		}
		return rowMapper;
	}

	/**
	 * @param debuggedField el campo a comparar
	 * @return si es fecha conviertelo a timestamp
	 */
	private static String isDate(String debuggedField) {
		if (debuggedField.equals("Date"))
			return "Timestamp";
		return debuggedField;
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
				if (column.getColumnType() == ColumnType.FOREIGN) {
					if (column.getFieldName().equals(inputFields[i].getName())) {
						fields[entity.getColumns().indexOf(column)] = inputFields[i];
						continue inputCycle;
					}
				} else {
					if (column.getName().equals(inputFields[i].getName())) {
						fields[entity.getColumns().indexOf(column)] = inputFields[i];
						continue inputCycle;
					}
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

	/**
	 * Convierte las mayusculas en la misma letra con un guion bajo atras. ejemplo:
	 * columnaUno a columna_uno
	 * 
	 * @param name el texto a convertir
	 * @return
	 */
	public static String convertToSQLFormat(String name) {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
	}

	/**
	 * Devuelve el string formado de las llaves primarias dependiendo el parametro
	 * tipo eg: Tipo PARAMETER: Long llave1, Long llave2 Tipo SQL_SCRIPT: "where
	 * llave1="+llave1+" and llave2="+llave2 "where llave1=? and llave2=?"
	 * llave1,llave2,llave3
	 * 
	 * @param entity la entidad
	 * @param type   el tipo de script (variable, sql script, nombres)
	 * @return el codigo formado de las N llaves primarias.
	 */
	public static String getPrimaryKeys(Entity entity, PrimaryKeyScriptType type) {
		String result = "";
		try {
			for (Column column : entity.getPrimaryKeys()) {
				switch (type) {
				case PARAMETER:
					result += "Long " + column.getName() + ",";
					break;
				case WHERE_SCRIPT:
					result += convertToSQLFormat(column.getName()) + "=\"+" + column.getName() + "+\" and ";
					break;
				case WHERE_SCRIPT_WITH_QUESTION_MARK:
					result += convertToSQLFormat(column.getName()) + "= ? and ";
					break;
				case ONLY_NAMES:
					result += column.getName() + ",";
					break;
				case PARAMETER_WITH_PATH_VARIABLE:
					result += "@PathVariable Long " + column.getName() + ",";
					break;
				case FOR_GET_MAPPING:
					result += "/{" + column.getName() + "}";
					break;
				}
			}
			int cut = 0;
			;
			switch (type) {
			case PARAMETER:
			case PARAMETER_WITH_PATH_VARIABLE:
			case ONLY_NAMES:
				cut = 1;
				break;
			case WHERE_SCRIPT:
				cut = 7;
				break;
			case WHERE_SCRIPT_WITH_QUESTION_MARK:
				cut = 5;
				break;
			}
			result = result.substring(0, result.length() - cut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * Constantes del tipo de script de llaves primarias
	 * 
	 * @author angelo.loza
	 *
	 *
	 */
	public enum PrimaryKeyScriptType {
		PARAMETER, WHERE_SCRIPT, WHERE_SCRIPT_WITH_QUESTION_MARK, ONLY_NAMES, PARAMETER_WITH_PATH_VARIABLE,
		FOR_GET_MAPPING
	}

}
