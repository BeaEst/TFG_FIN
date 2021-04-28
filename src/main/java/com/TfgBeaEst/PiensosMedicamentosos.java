package com.TfgBeaEst;

import java.util.Date;

public class PiensosMedicamentosos {

	public Date FechaCompra;
	public String PiensoMedicamentoso;
	public String CReceta;
	public String NumExplotacion;
	
	public Date getFechaCompra() {
		return FechaCompra;
	}
	
	public void setFechaCompra(Date fechaCompra) {
		FechaCompra = fechaCompra;
	}
	
	public String getPiensoMedicamentoso() {
		return PiensoMedicamentoso;
	}
	
	public void setPiensoMedicamentoso(String piensoMedicamentoso) {
		PiensoMedicamentoso = piensoMedicamentoso;
	}
	
	public String getCReceta() {
		return CReceta;
	}
	
	public void setCReceta(String cReceta) {
		CReceta = cReceta;
	}
	
	public String getNumExplotacion() {
		return NumExplotacion;
	}
	
	public void setNumExplotacion(String numExplotacion) {
		NumExplotacion = numExplotacion;
	}
	
}
