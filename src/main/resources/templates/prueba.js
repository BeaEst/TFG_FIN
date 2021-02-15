function VerPerfil(){
	//Primero hay que sacar los datos de base de datos de el usuario que se encuentra conectado
	var usuario = sessionStorage.getItem("usuario_activo");
	
	var usuarioDatos= {
		'usuario' : usuario,
	};

	var usuarioLoginJSON = JSON.stringify(usuarioDatos);

	var url = './sacardatos';

	$.ajax({
		type : 'post',
		url : url,
		contentType : 'application/json',
		dataType : 'json',
		data : usuarioLoginJSON,
		success : function(data, textStatus, jqXHR) {
			//Guardar las variables globales para mostrarlas en los input de Ver Perfil
			sessionStorage.setItem('Nombre', data.Nombre);
			sessionStorage.setItem('PApellido', data.PApellido);
			sessionStorage.setItem('SApellido', data.SApellido);
			sessionStorage.setItem('DNINIF', data.DNINIF);
			sessionStorage.setItem('CElectronico', data.CElectronico);
		},
		error : function(jqXHR, exception) {
			// mostrarPantallaError(url, jqXHR);
		},
		statuscode : {
			404 : function() {
				//mostrarPantallaError(url);
			}
		}
	});
	
	//Luego redirigir a la página
	var url = "./VerPerfil.html";
    window.location.href = url;
}