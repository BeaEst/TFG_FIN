package com.TfgBeaEst.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InspeccionesController {

	@GetMapping("/Inspecciones.html")
	public String IniciarSesion() {
		//model.addAttribute("usuario", new Usuario());
		return "Inspecciones";
	}
}
