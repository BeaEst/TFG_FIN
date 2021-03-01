package com.TfgBeaEst.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnimalesController {

	@GetMapping("/Animales.html")
	public String IniciarSesion() {
		//model.addAttribute("usuario", new Usuario());
		return "Animales";
	}
}
