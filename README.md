#Generador de clases
_Con este generador se pretende automatizar la generación de entidades y estructura de controladores, servicios y repositorios._
### Pre-requisitos 📋

```
* Eclipse o Spring Tools
```
##Uso 🔧
_Configurar su conexión en el archivo reverse-persistence.xml localizado en src/main/resources/META-INF/reverse-persistence.xml, ejemplo:_
```
<persistence-unit name="foo"> <!-- only used to reverse the db -->
	<properties>
		<property name="openjpa.ConnectionUserName" value="sa" />
		<property name="openjpa.ConnectionPassword" value="sa" />
		<property name="openjpa.ConnectionURL" value="jdbc:jtds:sqlserver://servidor/basededatos" />
		<property name="javax.persistence.jdbc.driver"
			value="net.sourceforge.jtds.jdbc.Driver" />

	</properties>
</persistence-unit>
```
_Configurar los paquetes en las propiedades del pom.xml_
```
<properties>
	<postgresql.version>9.2-1003-jdbc4</postgresql.version>
	<main-package>com.cominvi.app</main-package>
	<entity-package>com.cominvi.app.commons.models</entity-package>
</properties>
```
_Ejecutar como Maven->Run As->Maven Build y escribir el siguiente goal:_
```
exec:java@rmt
```
_Debe de conectarse a la base de datos y construir las entidades, refrescar el paquete src/main/java con el botón *refresh*_
_Ejecutar nuevamente otro Maven Build para construir los repositorios y controladores pero con este goal:_
```
exec:java@rmt
```