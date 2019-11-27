package com.rever.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.rever.config.Configuracion;
import com.rever.folders.ProjectFolderConfiguration;

public class FileCreator {

	private Configuracion configuracion;

	public FileCreator(Configuracion configuracion) {
		this.configuracion = configuracion;
	}

	public FileCreator() {
		// TODO Auto-generated constructor stub
	}

	private String readFileToString(String fileName, File file) {
		String text = "";
		ClassLoader classLoader = getClass().getClassLoader();
		if (file == null)
			file = new File(classLoader.getResource(fileName).getFile());
		try (FileInputStream f = new FileInputStream(file)) {
			BufferedReader in = new BufferedReader(new InputStreamReader(f, "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				text = text.concat(str.concat("\n"));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	/**
	 * 
	 */
	public void addJSONAnnotation() {
		String entityPath = ProjectFolderConfiguration.getModelPath() + this.configuracion.getNameClase() + ".java";
		File file = new File(entityPath);
		String srcFile = readFileToString(null, file);
		
		String importText = "java.util.*;\nimport com.fasterxml.jackson.annotation.JsonFormat;";
		String annotationText = "@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = \"yyyy-MM-dd hh:mm:ss\")\nprivate Date";
		
		if (!srcFile.contains(importText))
			srcFile = srcFile.replaceAll("java.util.*;", importText);
		if (!srcFile.contains(annotationText))
			srcFile = srcFile.replaceAll("private Date", annotationText);

		try {
			srcFile = new Formatter().formatSource(srcFile);
			writeFile(srcFile, entityPath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FormatterException e) {
			e.printStackTrace();
		}

	}

	public boolean createFilesDaoAndService() {
		try {

			addJSONAnnotation();
			this.configuracion.construir();
			String urlDao = this.configuracion.getUrlHome() + "/repositories/";
			String urlController = this.configuracion.getUrlHome() + "/controllers/";
			String urlService = this.configuracion.getUrlHome() + "/services/impl/";
			String urlServiceImpl = this.configuracion.getUrlHome() + "/services/";
			createFile(urlDao);
			createFile(urlController);
			createFile(urlService);
			createFile(urlServiceImpl);
			String fileDao = urlDao + "Jdbc" + this.configuracion.getNameClase() + "Repository.java";
			String dao = readFileToString("repository.txt", null);
			dao = dao.replaceAll("@keyHolder", this.configuracion.getKeyHolder());
			dao = dao.replaceAll("@paqueteDao", this.configuracion.getPaqueteDao());
			dao = dao.replaceAll("@paqueteGenericDao", this.configuracion.getPaqueteGenericDao());
			dao = dao.replaceAll("@paqueteClass", this.configuracion.getPaqueteEntity());
			dao = dao.replaceAll("@nameClaseDao", this.configuracion.getNameClaseDao());
			dao = dao.replaceAll("@project", this.configuracion.getProject());
			dao = dao.replaceAll("@nameClassMin", this.configuracion.getNameClassMin());
			dao = dao.replaceAll("@namePluralClass", this.configuracion.getNameClassMin() + "s");
			dao = dao.replaceAll("@allFields", this.configuracion.getAllFields());
			dao = dao.replaceAll("@allQuestionFields", this.configuracion.getAllQuestionFields());
			dao = dao.replaceAll("@primaryKey", this.configuracion.getPrimaryKey());
			dao = dao.replaceAll("@primarySQLKey", this.configuracion.getPrimaryKeySQL());
			dao = dao.replaceAll("@setScript", this.configuracion.getSetSQLScript());
			dao = dao.replaceAll("@preparedGetStatement", configuracion.getPreparedStatementOnlyGet());
			dao = dao.replaceAll("@preparedSetStatement", configuracion.getPreparedStatementFromEntity());
			dao = dao.replaceAll("@primarySetKey", configuracion.getPrimaryKeySet());
			dao = dao.replaceAll("@entityFromRS", configuracion.getEntityFromResultSet());
			dao = dao.replaceAll("@nameClase", this.configuracion.getNameClase());
			dao = dao.replaceAll("@nameModel", this.configuracion.getModelName());
			dao = dao.replaceAll("@tableName", this.configuracion.getTableName());
			try {
				dao = new Formatter().formatSource(dao);
			} catch (FormatterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				writeFile(dao, fileDao);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			String fileController = urlController + this.configuracion.getNameClase() + "Controller.java";
			String controller = readFileToString("controller.txt", null);
			controller = controller.replaceAll("@paqueteController", this.configuracion.getPaqueteController());
			controller = controller.replaceAll("@paqueteClass", this.configuracion.getPaqueteEntity());
			controller = controller.replaceAll("@nameClaseDaoImpl", this.configuracion.getNameClaseDaoImpl());
			controller = controller.replaceAll("@nameClaseDao", this.configuracion.getNameClaseDao());
			controller = controller.replaceAll("@nameClase", this.configuracion.getNameClase());
			controller = controller.replaceAll("@nameClassMin", this.configuracion.getNameClassMin());
			controller = controller.replaceAll("@nameModel", this.configuracion.getModelName());
			controller = controller.replaceAll("@primaryKey", this.configuracion.getPrimaryKey());
			controller = controller.replaceAll("@paqueteService", this.configuracion.getPaqueteServiceImpl());

			try {
				writeFile(controller, fileController);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			String FileService = urlService + this.configuracion.getNameClase() + "Service.java";
			String service = readFileToString("service.txt", null);
			service = service.replaceAll("@paqueteServiceImpl", this.configuracion.getPaqueteServiceImpl());
			service = service.replaceAll("@paqueteService", this.configuracion.getPaqueteService());
			service = service.replaceAll("@paqueteGenericService", this.configuracion.getPaqueteGenericService());
			service = service.replaceAll("@paqueteClass", this.configuracion.getPaqueteEntity());
			service = service.replaceAll("@nameClaseService", this.configuracion.getNameClaseService());
			service = service.replaceAll("@nameClase", this.configuracion.getNameClase());
			service = service.replaceAll("@tipeId", this.configuracion.getTipeId());
			service = service.replaceAll("@nameClassMin", this.configuracion.getNameClassMin());
			service = service.replaceAll("@namePluralClass", this.configuracion.getNameClassMin() + "s");
			service = service.replaceAll("@primaryKey", this.configuracion.getPrimaryKey());
			service = service.replaceAll("@nameModel", this.configuracion.getModelName());

			try {
				writeFile(service, FileService);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			String fileServiceImpl = urlServiceImpl + "I" + this.configuracion.getNameClase() + "Service.java";
			String serviceImpl = readFileToString("serviceImpl.txt", null);
			serviceImpl = serviceImpl.replaceAll("@paqueteServiceImpl", this.configuracion.getPaqueteServiceImpl());
			serviceImpl = serviceImpl.replaceAll("@paqueteGenericDao", this.configuracion.getPaqueteGenericDao());
			serviceImpl = serviceImpl.replaceAll("@paqueteGenericService",
					this.configuracion.getPaqueteGenericService());
			serviceImpl = serviceImpl.replaceAll("@paqueteService", this.configuracion.getPaqueteService());
			serviceImpl = serviceImpl.replaceAll("@paqueteClass", this.configuracion.getPaqueteEntity());
			serviceImpl = serviceImpl.replaceAll("@nameClaseServiceImpl", this.configuracion.getNameClaseServiceImpl());
			serviceImpl = serviceImpl.replaceAll("@nameClaseService", this.configuracion.getNameClaseService());
			serviceImpl = serviceImpl.replaceAll("@nameClaseLowerService",
					this.configuracion.getNameClaseLowerService());
			serviceImpl = serviceImpl.replaceAll("@nameClase", this.configuracion.getNameClase());
			serviceImpl = serviceImpl.replaceAll("@tipeId", this.configuracion.getTipeId());
			serviceImpl = serviceImpl.replaceAll("@nameClassMin", this.configuracion.getNameClassMin());
			serviceImpl = serviceImpl.replaceAll("@namePluralClass", this.configuracion.getNameClassMin() + "s");
			serviceImpl = serviceImpl.replaceAll("@primaryKey", this.configuracion.getPrimaryKey());
			serviceImpl = serviceImpl.replaceAll("@nameModel", this.configuracion.getModelName());
			try {
				writeFile(serviceImpl, fileServiceImpl);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} catch (NullPointerException e) {
			System.err.println(
					"Ocurri� un error al generar los archivos de la entidad: " + this.configuracion.getNameClase());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean move(String sourceFile, String destFile) {
		File dest = new File(destFile);
		dest.mkdirs();
		try {
			Files.move(new File(sourceFile).toPath(), new File(destFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public void deleteDirectoryRecursion(Path path) {
		try {
			if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
				try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
					for (Path entry : entries) {
						deleteDirectoryRecursion(entry);
					}
				}
			}
			Files.delete(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean deleteFile(File file) {
		return file.delete();
	}

	private boolean createFile(String file) {
		File files = new File(file);
		if (!files.exists()) {
			return files.mkdirs();
		} else {
			return false;
		}
	}

	private void writeFile(String aString, String pathFile) throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathFile), "UTF-8"));
		try {
			out.write(aString);
		} finally {
			out.close();
		}
	}

	/**
	 * @param path la ruta del archivo orm.xml generado por jpa
	 * @return una lista de entidades
	 */
	public ArrayList<String> extractEntityNames(String path) {
		ArrayList<String> entities = new ArrayList();
		try {
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("entity");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					entities.add(eElement.getAttribute("class"));
				}
			}
		} catch (Exception e) {
			System.err.println("Hola, necesitas construir primero tus entidades, para ello: \n"
					+ "1.- Configura tu conexion en reverseIngeopenjpa\\src\\main\\resources\\META-INF\\reverse-persistence.xml\n"
					+ "2.- Configura tu paquete en el pom.xml en la configuraci�n de reverseIngeopenjpa (al final) en el parametro de paquete.\n"
					+ "3.- Configura tus rutas en el main de este paquete.\n"
					+ "4.- Corre este proyecto (reverseIngeopenjpa) como maven build.");
			// e.printStackTrace();
		}
		return entities;
	}

}
