package com.TfgBeaEst;

import java.util.Date;

public class Medicamentos {

	public Date FechaCompra;
	public String CReceta;
	public String Medicamento;
	public String NumExplotacion;
	
	public Date getFechaCompra() {
		return FechaCompra;
	}
	
	public void setFechaCompra(Date fechaCompra) {
		FechaCompra = fechaCompra;
	}
	
	public String getCReceta() {
		return CReceta;
	}
	
	public void setCReceta(String cReceta) {
		CReceta = cReceta;
	}
	
	public String getMedicamento() {
		return Medicamento;
	}
	
	public void setMedicamento(String medicamento) {
		Medicamento = medicamento;
	}
	
	public String getNumExplotacion() {
		return NumExplotacion;
	}
	
	public void setNumExplotacion(String numExplotacion) {
		NumExplotacion = numExplotacion;
	}
}
