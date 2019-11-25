package com.rever;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rever.config.Configuracion;
import com.rever.files.FileCreator;
import com.rever.files.XMLExtractor;
import com.rever.files.scriptbuilder.ScriptBuilder;
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
			 * Se asume que las tablas tienen un tipo de dato entero.
			 */
			String primaryKeyType = "Long";

			/*
			 * Obtiene los nombres a partir de los model generados por JPA
			 * 
			 */
			List<Entity> entities = new XMLExtractor(ProjectFolderConfiguration.getORMXMLAbsolutePath())
					.extractEntities();
			/*
			 * Recorre cada entity y genera su dao y servicio con su correspondiente
			 * implementaciï¿½n
			 */
			if (!entities.isEmpty()) {
				int counter = 0;
				for (Entity entity : entities) {

					Configuracion config = new Configuracion(entity.getName(),
							ProjectFolderConfiguration.getBasePackage(), primaryKeyType);
					
					String databaseName = new XMLExtractor(ProjectFolderConfiguration.REVERSE_PERSISTENCE_XML_LOCATION).getDatabaseName();
					
					config.setUrlClase(ProjectFolderConfiguration.getModelPath());
					config.setModelName(ProjectFolderConfiguration.getModelPackage());
					config.setUrlHome(ProjectFolderConfiguration.getBaseURI());
					config.setProject(databaseName);
					config.setNameClassMin(entity.getName().toLowerCase());
					config.setTableName(entity.getTableName());
					config.setPrimaryKey(entity.getPrimaryKeys().get(0).getName());
					config.setPrimaryKeySet(
							ScriptBuilder.getDynamicIDAssignation(entity.getPrimaryKeys().get(0), entity));
					config.setAllFields(ScriptBuilder.getAllColumns(entity, false));
					config.setAllQuestionFields(ScriptBuilder.getAllColumns(entity, true));
					config.setSetSQLScript(ScriptBuilder.getAllColumnsForSQLSet(entity));
					config.setPreparedStatementOnlyGet(ScriptBuilder.getAllColumnGets(entity));
					config.setPreparedStatementFromEntity(ScriptBuilder.createPreparedStatementFromEntity(entity));
					config.setEntityFromResultSet(ScriptBuilder.buildRowMapper(entity));
					config.setPaquete(ProjectFolderConfiguration.getBasePackage());
					config.setPaqueteGenericDao(ProjectFolderConfiguration.getGenericDaoPackage());
					config.setPaqueteGenericService(ProjectFolderConfiguration.getGenericServicePackage());
					FileCreator obj = new FileCreator(config);
					if (obj.createFilesDaoAndService())
						counter++;
				}
				System.out.println("Se generó la estructura dao,service,controller para " + counter + " de "
						+ entities.size() + " entidades\nFallaron " + (entities.size() - counter));
			}
		}

	}

}
