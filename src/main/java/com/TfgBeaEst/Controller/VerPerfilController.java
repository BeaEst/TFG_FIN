package com.TfgBeaEst.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VerPerfilController {

	@GetMapping("/VerPerfil.html")
	public String VerPerfil() {
		//model.addAttribute("usuario", new Usuario());
		return "VerPerfil";
	}
}
