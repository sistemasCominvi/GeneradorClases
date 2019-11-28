package com.rever.config;

public class Configuracion {
	
	private String urlHome;
	private String urlClase;
	private String nameClase;
	
	private String paqueteEntity;
	private String paquete;
	private String paqueteDao;
	private String paqueteController;
	private String paqueteService;
	private String paqueteServiceImpl;
	
	private String modelName;
	private String project;
	private String allFields;
	private String allQuestionFields;
	private String nameClassMin;
	private String preparedStatementFromEntity;
	private String preparedStatementFromResultSet;
	private String preparedStatementOnlyGet;
	private String setSQLScript;
	private String entityFromResultSet;
	private String tableName;
	private String keyHolder;
	
	private String primaryKeyParameters;
	private String primaryKeyParametersWithPathVariable;
	private String primaryKeysSQL;
	private String primaryKeysSQLQuestion;
	private String primaryKeyNames;
	private String primaryKeysForMapping;
	private String primaryKeySet;
	private String primaryKeySQL;
	
	public Configuracion(String nameClase, String paqueteEntity) {
		this.nameClase = nameClase;
		this.paqueteEntity = paqueteEntity;
	}

	public String getUrlHome() {
		return urlHome;
	}

	public void setUrlHome(String urlHome) {
		this.urlHome = urlHome;
	}

	public String getNameClase() {
		return nameClase;
	}

	public void setNameClase(String nameClase) {
		this.nameClase = nameClase;
	}

	

	public String getPaquete() {
		return paquete;
	}

	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}

	

	public String getPaqueteDao() {
		return paqueteDao;
	}

	public void setPaqueteDao(String paqueteDao) {
		this.paqueteDao = paqueteDao;
	}

	public String getPaqueteController() {
		return paqueteController;
	}

	public void setPaqueteController(String paqueteController) {
		this.paqueteController = paqueteController;
	}

	public String getPaqueteServiceImpl() {
		return paqueteServiceImpl;
	}

	public void setPaqueteServiceImpl(String paqueteServiceImpl) {
		this.paqueteServiceImpl = paqueteServiceImpl;
	}

	public String getUrlClase() {
		return urlClase;
	}

	public void setUrlClase(String urlClase) {
		this.urlClase = urlClase;
	}

	public String getPaqueteEntity() {
		return paqueteEntity;
	}

	public void setPaqueteEntity(String paqueteEntity) {
		this.paqueteEntity = paqueteEntity;
	}

	public String getPaqueteService() {
		return paqueteService;
	}

	public void setPaqueteService(String paqueteService) {
		this.paqueteService = paqueteService;
	}

	public void construir() {
		this.paqueteDao = this.paquete + ".repositories";
		this.paqueteController = this.paquete + ".controllers";
		this.paqueteServiceImpl = this.paquete + ".services";
		this.paqueteService = this.paquete + ".services.impl";
		try {
			if (this.nameClassMin.charAt(this.nameClassMin.length() - 1) == 's')
				this.nameClassMin = this.nameClassMin.substring(0, this.nameClassMin.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNameClaseDao() {
		return this.nameClase + "Dao";
	}

	public String getNameClaseDaoImpl() {
		return this.nameClase + "DaoImpl";
	}

	public String getNameClaseService() {
		return this.nameClase + "Service";
	}

	public String getNameClaseServiceImpl() {
		return this.nameClase + "ServiceImpl";
	}

	public String getNameClaseLowerService() {
		String primerCaracter = this.nameClase.substring(0, 1).toLowerCase();
		StringBuilder myName = new StringBuilder(this.nameClase);
		myName.setCharAt(0, primerCaracter.charAt(0));
		return myName + "DaoImpl";
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getAllFields() {
		return allFields;
	}

	public void setAllFields(String allFields) {
		this.allFields = allFields;
	}

	public String getAllQuestionFields() {
		return allQuestionFields;
	}

	public void setAllQuestionFields(String allQuestionFields) {
		this.allQuestionFields = allQuestionFields;
	}

	public String getNameClassMin() {
		return nameClassMin;
	}

	public void setNameClassMin(String nameClassMin) {
		this.nameClassMin = nameClassMin;
	}


	public String getPreparedStatementFromResultSet() {
		return preparedStatementFromResultSet;
	}

	public void setPreparedStatementFromResultSet(String preparedStatementFromResultSet) {
		this.preparedStatementFromResultSet = preparedStatementFromResultSet;
	}

	public String getPreparedStatementFromEntity() {
		return preparedStatementFromEntity;
	}

	public void setPreparedStatementFromEntity(String preparedStatementFromEntity) {
		this.preparedStatementFromEntity = preparedStatementFromEntity;
	}

	public String getSetSQLScript() {
		return setSQLScript;
	}

	public void setSetSQLScript(String setSQLScript) {
		this.setSQLScript = setSQLScript;
	}

	public String getPreparedStatementOnlyGet() {
		return preparedStatementOnlyGet;
	}

	public void setPreparedStatementOnlyGet(String preparedStatementOnlyGet) {
		this.preparedStatementOnlyGet = preparedStatementOnlyGet;
	}

	public String getEntityFromResultSet() {
		return entityFromResultSet;
	}

	public void setEntityFromResultSet(String entityFromResultSet) {
		this.entityFromResultSet = entityFromResultSet;
	}

	public void setPrimaryKeySet(String string) {
		this.primaryKeySet = string;
		
	}

	public String getPrimaryKeySet() {
		return primaryKeySet;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKeySQL() {
		return primaryKeySQL;
	}

	public void setPrimaryKeySQL(String primaryKeySQL) {
		this.primaryKeySQL = primaryKeySQL;
	}

	public String getKeyHolder() {
		return keyHolder;
	}

	public void setKeyHolder(String keyHolder) {
		this.keyHolder = keyHolder;
	}

	public String getPrimaryKeyParameters() {
		return primaryKeyParameters;
	}

	public void setPrimaryKeyParameters(String primaryKeyParameters) {
		this.primaryKeyParameters = primaryKeyParameters;
	}

	public String getPrimaryKeysSQL() {
		return primaryKeysSQL;
	}

	public void setPrimaryKeysSQL(String primaryKeysSQL) {
		this.primaryKeysSQL = primaryKeysSQL;
	}

	public String getPrimaryKeysSQLQuestion() {
		return primaryKeysSQLQuestion;
	}

	public void setPrimaryKeysSQLQuestion(String primaryKeysSQLQuestion) {
		this.primaryKeysSQLQuestion = primaryKeysSQLQuestion;
	}

	public String getPrimaryKeyNames() {
		return primaryKeyNames;
	}

	public void setPrimaryKeyNames(String primaryKeyNames) {
		this.primaryKeyNames = primaryKeyNames;
	}

	public String getPrimaryKeyParametersWithPathVariable() {
		return primaryKeyParametersWithPathVariable;
	}

	public void setPrimaryKeyParametersWithPathVariable(String primaryKeyParametersWithPathVariable) {
		this.primaryKeyParametersWithPathVariable = primaryKeyParametersWithPathVariable;
	}

	public String getPrimaryKeysForMapping() {
		return primaryKeysForMapping;
	}

	public void setPrimaryKeysForMapping(String primaryKeysForMapping) {
		this.primaryKeysForMapping = primaryKeysForMapping;
	}

}
