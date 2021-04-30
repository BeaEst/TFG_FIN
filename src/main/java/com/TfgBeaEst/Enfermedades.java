package com.TfgBeaEst;

import java.util.Date;

import org.springframework.stereotype.Controller;

@Controller
public class Enfermedades {

	public Date FechaAparicion;
	public String Diagnostico;
	public String DocAsociado;
	public Integer NAnimales;
	public String MedidasAdoptadas;
	public Date FechaDesaparicion;
	public String NumExplotacion;
	
	public Date getFechaAparicion() {
		return FechaAparicion;
	}
	
	public void setFechaAparicion(Date fechaAparicion) {
		FechaAparicion = fechaAparicion;
	}
	
	public String getDiagnostico() {
		return Diagnostico;
	}
	
	public void setDiagnostico(String diagnostico) {
		Diagnostico = diagnostico;
	}
	
	public String getDocAsociado() {
		return DocAsociado;
	}
	
	public void setDocAsociado(String docAsociado) {
		DocAsociado = docAsociado;
	}
	
	public Integer getNAnimales() {
		return NAnimales;
	}
	
	public void setNAnimales(Integer nAnimales) {
		NAnimales = nAnimales;
	}
	
	public String getMedidasAdoptadas() {
		return MedidasAdoptadas;
	}
	
	public void setMedidasAdoptadas(String medidasAdoptadas) {
		MedidasAdoptadas = medidasAdoptadas;
	}
	
	public Date getFechaDesaparicion() {
		return FechaDesaparicion;
	}
	
	public void setFechaDesaparicion(Date fechaDesaparicion) {
		FechaDesaparicion = fechaDesaparicion;
	}

	public String getNumExplotacion() {
		return NumExplotacion;
	}

	public void setNumExplotacion(String numExplotacion) {
		NumExplotacion = numExplotacion;
	}
	
}
