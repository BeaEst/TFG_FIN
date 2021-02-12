package com.TfgBeaEst.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {

	@GetMapping("/IniciarSesion.html")
	public String IniciarSesion() {
		//model.addAttribute("usuario", new Usuario());
		return "IniciarSesion";
	}
}
