package com.TfgBeaEst.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlimentosSuministradosController {

	@GetMapping("/AlimentosSuministrados.html")
	public String VerPerfil() {
		//model.addAttribute("usuario", new Usuario());
		return "AlimentosSuministrados";
	}
}
