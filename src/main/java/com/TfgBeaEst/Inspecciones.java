package com.TfgBeaEst;

import java.util.Date;

public class Inspecciones {

	public Date Fecha;
	public Boolean Oficial;
	public String TipoActuacion;
	public String NumActa;
	public String NumExplotacion;
	public Date getFecha() {
		return Fecha;
	}
	public void setFecha(Date fecha) {
		Fecha = fecha;
	}
	public Boolean getOficial() {
		return Oficial;
	}
	public void setOficial(Boolean oficial) {
		Oficial = oficial;
	}
	public String getTipoActuacion() {
		return TipoActuacion;
	}
	public void setTipoActuacion(String tipoActuacion) {
		TipoActuacion = tipoActuacion;
	}
	public String getNumActa() {
		return NumActa;
	}
	public void setNumActa(String numActa) {
		NumActa = numActa;
	}
	public String getNumExplotacion() {
		return NumExplotacion;
	}
	public void setNumExplotacion(String numExplotacion) {
		NumExplotacion = numExplotacion;
	}
	
}
