package com.cominvi.app.commons.models;

import java.util.*;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$ReverseCodeGenerator
 */
public class Modulostablas {
	private int accion;

	private String descripcion;

	private Date fechahoraalta;

	private Date fechahoramod;

	private long idbasedatos;

	private long idempleadoalta;

	private long idempleadomod;

	private long idmodulo;

	private long idtabla;

	private Modulos modulos;


	public Modulostablas() {
	}

	public Modulostablas(long idbasedatos, long idmodulo, long idtabla) {
		this.idbasedatos = idbasedatos;
		this.idmodulo = idmodulo;
		this.idtabla = idtabla;
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechahoraalta() {
		return fechahoraalta;
	}

	public void setFechahoraalta(Date fechahoraalta) {
		this.fechahoraalta = fechahoraalta;
	}

	public Date getFechahoramod() {
		return fechahoramod;
	}

	public void setFechahoramod(Date fechahoramod) {
		this.fechahoramod = fechahoramod;
	}

	public long getIdbasedatos() {
		return idbasedatos;
	}

	public void setIdbasedatos(long idbasedatos) {
		this.idbasedatos = idbasedatos;
	}

	public long getIdempleadoalta() {
		return idempleadoalta;
	}

	public void setIdempleadoalta(long idempleadoalta) {
		this.idempleadoalta = idempleadoalta;
	}

	public long getIdempleadomod() {
		return idempleadomod;
	}

	public void setIdempleadomod(long idempleadomod) {
		this.idempleadomod = idempleadomod;
	}

	public long getIdmodulo() {
		return idmodulo;
	}

	public void setIdmodulo(long idmodulo) {
		this.idmodulo = idmodulo;
	}

	public long getIdtabla() {
		return idtabla;
	}

	public void setIdtabla(long idtabla) {
		this.idtabla = idtabla;
	}

	public Modulos getModulos() {
		return modulos;
	}

	public void setModulos(Modulos modulos) {
		this.modulos = modulos;
	}
}