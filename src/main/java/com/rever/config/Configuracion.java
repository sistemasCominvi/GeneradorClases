package com.rever.config;

public class Configuracion {
	private String urlHome;
	private String urlClase;
	private String nameClase;
	private String paqueteEntity;
	private String tipeId;
	private String paquete;
	private String paqueteGenericDao;
	private String paqueteGenericService;
	private String paqueteDao;
	private String paqueteController;
	private String paqueteService;
	private String paqueteServiceImpl;
	private String sessionFactoryName;
	private String modelName;
	private String project;
	private String allFields;
	private String allQuestionFields;
	private String nameClassMin;
	private String primaryKey;
	private String preparedStatementFromEntity;
	private String preparedStatementFromResultSet;
	private String preparedStatementOnlyGet;
	private String setSQLScript;
	private String entityFromResultSet;
	private String primaryKeySet;
	private String tableName;

	public Configuracion(String nameClase, String paqueteEntity, String tipeId) {
		this.nameClase = nameClase;
		this.paqueteEntity = paqueteEntity;
		this.tipeId = tipeId;
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

	public String getTipeId() {
		return tipeId;
	}

	public void setTipeId(String tipeId) {
		this.tipeId = tipeId;
	}

	public String getPaquete() {
		return paquete;
	}

	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}

	public String getPaqueteGenericDao() {
		return paqueteGenericDao;
	}

	public void setPaqueteGenericDao(String paqueteGenericDao) {
		this.paqueteGenericDao = paqueteGenericDao;
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

	public String getSessionFactoryName() {
		return sessionFactoryName;
	}

	public void setSessionFactoryName(String sessionFactoryName) {
		this.sessionFactoryName = sessionFactoryName;
	}

	public String getPaqueteGenericService() {
		return paqueteGenericService;
	}

	public void setPaqueteGenericService(String paqueteGenericService) {
		this.paqueteGenericService = paqueteGenericService;
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

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
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

}
