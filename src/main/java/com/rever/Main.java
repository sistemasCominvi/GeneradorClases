package com.rever;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rever.config.Configuracion;
import com.rever.files.FileCreator;
import com.rever.files.XMLExtractor;
import com.rever.files.scriptbuilder.ScriptBuilder;
import com.rever.files.scriptbuilder.ScriptBuilder.PrimaryKeyScriptType;
import com.rever.files.xml.Column;
import com.rever.files.xml.Entity;
import com.rever.folders.ProjectFolderConfiguration;
import com.rever.pom.POMExplorer;

public class Main {
	public static void main(String[] args) {
		Runner runner = new Runner();
		runner.run();
	}

	public static class Runner {
		public void run() {

			/*
			 * Obtiene los nombres a partir de los model generados por JPA
			 * 
			 */
			List<Entity> entities = new XMLExtractor(ProjectFolderConfiguration.getORMXMLAbsolutePath())
					.extractEntities();
			/*
			 * Recorre cada entity y genera su dao y servicio con su correspondiente
			 * implementaci�n
			 */
			if (!entities.isEmpty()) {
				int counter = 0;
				for (Entity entity : entities) {
					try {

					Configuracion config = new Configuracion(entity.getName(),
							ProjectFolderConfiguration.getBasePackage());
					
					String databaseName = new XMLExtractor(ProjectFolderConfiguration.REVERSE_PERSISTENCE_XML_LOCATION).getDatabaseName();
					
					config.setUrlClase(ProjectFolderConfiguration.getModelPath());
					config.setModelName(ProjectFolderConfiguration.getModelPackage());
					config.setUrlHome(ProjectFolderConfiguration.getBaseURI());
					config.setProject(databaseName);
					config.setNameClassMin(entity.getName().toLowerCase());
					config.setTableName(entity.getTableName());
					config.setPaquete(ProjectFolderConfiguration.getBasePackage());

					config.setAllFields(ScriptBuilder.getAllColumns(entity, false));
					config.setAllQuestionFields(ScriptBuilder.getAllColumns(entity, true));
					config.setSetSQLScript(ScriptBuilder.getAllColumnsForSQLSet(entity));
					config.setPreparedStatementOnlyGet(ScriptBuilder.getAllColumnGets(entity));
					config.setPreparedStatementFromEntity(ScriptBuilder.createPreparedStatementFromEntity(entity));
					config.setEntityFromResultSet(ScriptBuilder.buildRowMapper(entity));
					config.setKeyHolder(ScriptBuilder.getKeyHolder(entity));
					
					//TODO: Hacer dinamico el keyholder
					config.setPrimaryKeySet(
							ScriptBuilder.getDynamicIDAssignation(entity.getPrimaryKeys().get(0), entity));
					
					config.setPrimaryKeyParameters(ScriptBuilder.getPrimaryKeys(entity,PrimaryKeyScriptType.PARAMETER));
					config.setPrimaryKeySQL(ScriptBuilder.getPrimaryKeys(entity,PrimaryKeyScriptType.WHERE_SCRIPT));
					config.setPrimaryKeysSQLQuestion(ScriptBuilder.getPrimaryKeys(entity,PrimaryKeyScriptType.WHERE_SCRIPT_WITH_QUESTION_MARK));
					config.setPrimaryKeyNames(ScriptBuilder.getPrimaryKeys(entity,PrimaryKeyScriptType.ONLY_NAMES));
					config.setPrimaryKeyParametersWithPathVariable(ScriptBuilder.getPrimaryKeys(entity,PrimaryKeyScriptType.PARAMETER_WITH_PATH_VARIABLE));
					config.setPrimaryKeysForMapping(ScriptBuilder.getPrimaryKeys(entity,PrimaryKeyScriptType.FOR_GET_MAPPING));
					
					FileCreator obj = new FileCreator(config);
					if (obj.createFilesDaoAndService())
						counter++;
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("Se gener� la estructura dao,service,controller para " + counter + " de "
						+ entities.size() + " entidades\nFallaron " + (entities.size() - counter));
			}
		}

	}

}
