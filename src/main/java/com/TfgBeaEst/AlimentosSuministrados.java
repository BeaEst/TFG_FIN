package com.TfgBeaEst;

import java.util.Date;

public class AlimentosSuministrados {

	public String NDocumento;
	public Double Cantidad;
	public String Naturaleza;
	public Date FechaCompra;
	public String NumExplotacion;
	
	public String getNDocumento() {
		return NDocumento;
	}
	
	public void setNDocumento(String nDocumento) {
		NDocumento = nDocumento;
	}
	
	public Double getCantidad() {
		return Cantidad;
	}
	
	public void setCantidad(Double cantidad) {
		Cantidad = cantidad;
	}
	
	public String getNaturaleza() {
		return Naturaleza;
	}
	
	public void setNaturaleza(String naturaleza) {
		Naturaleza = naturaleza;
	}
	
	public Date getFechaCompra() {
		return FechaCompra;
	}
	
	public void setFechaCompra(Date fechaCompra) {
		FechaCompra = fechaCompra;
	}

	public String getNumExplotacion() {
		return NumExplotacion;
	}

	public void setNumExplotacion(String numExplotacion) {
		NumExplotacion = numExplotacion;
	}
	
	
}
