package com.TfgBeaEst;

import java.util.Date;

import org.springframework.stereotype.Controller;

@Controller
public class Incidencias {

	public String Descripcion;
	public Integer NAnimales;
	public String CodIdentAnt;
	public String CodIdentNew;
	public String NDocumento;
	public String NumExplotacion;
	public Date Fecha;
	
	public String getDescripcion() {
		return Descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
	public Integer getNAnimales() {
		return NAnimales;
	}
	
	public void setNAnimales(Integer nAnimales) {
		NAnimales = nAnimales;
	}
	
	public String getCodIdentAnt() {
		return CodIdentAnt;
	}
	
	public void setCodIdentAnt(String codIdentAnt) {
		CodIdentAnt = codIdentAnt;
	}
	
	public String getCodIdentNew() {
		return CodIdentNew;
	}
	
	public void setCodIdentNew(String codIdentNew) {
		CodIdentNew = codIdentNew;
	}
	
	public String getNDocumento() {
		return NDocumento;
	}
	
	public void setNDocumento(String nDocumento) {
		NDocumento = nDocumento;
	}
	
	public String getNumExplotacion() {
		return NumExplotacion;
	}
	
	public void setNumExplotacion(String numExplotacion) {
		NumExplotacion = numExplotacion;
	}

	public Date getFecha() {
		return Fecha;
	}

	public void setFecha(Date fecha) {
		Fecha = fecha;
	}
	
}
