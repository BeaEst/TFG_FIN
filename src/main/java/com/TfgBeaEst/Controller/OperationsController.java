package com.TfgBeaEst.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.TfgBeaEst.Animales;
import com.TfgBeaEst.Usuario;

@Controller
public class OperationsController {

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<Map<String,String>> IniciarSesion(@RequestBody Usuario usuario) {
		System.out.println("INICIO de inicio de sesion");
		
		String user = usuario.getUsuario();
		String pass = usuario.getContrasena();
		
		ResponseEntity<Map<String,String>> responseEntity = null;
		Map<String,String> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
			Connection conexion = null;
			
			// Cargar el driver
	        try {
				Class.forName("com.mysql.jdbc.Driver");
				
				try {
					conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
					
					Statement s = null;
					try {
						s = conexion.createStatement();
						
						try {
				        	//Primero sacamos si existe en la tabla algún usuario como el que hemos indicado (user)
				        		System.out.println("INICIO - Comprobar usuario");
				        		
								ResultSet comp_user = s.executeQuery("SELECT COUNT(Usuario) FROM usuarios WHERE Usuario='"+user+"'");
								
								System.out.println("SELECT COUNT(Usuario) FROM usuarios WHERE Usuario='"+user+"'");
								
								//Para obtener los resultados
								if(comp_user.next()){
									String recuento = comp_user.getString(1);
									if(recuento.equals("1")) {
										System.out.println("El usuario con el que intentas acceder existe");
										
										System.out.println("FIN - Comprobar usuario");
										
										//Comprobamos si la contraseña añadida corresponde con la que se encuentra en la tabla
										System.out.println("INICIO - Comprobar contraseña");
										
										ResultSet comp_cont = s.executeQuery("SELECT Contrasena FROM usuarios WHERE Usuario='"+user+"'");
										
										System.out.println("SELECT Contrasena FROM usuarios WHERE Usuario='"+user+"'");
										
										if(comp_cont.next()){
											String pass_tabla = comp_cont.getString(1);
											
											if(pass_tabla.equals(pass)) {
												System.out.println("La contraseña del usuario " + user + " corresponde con la añadida");
												
												result.put("login", "correcto");
											}else {
												System.out.println("La contraseña del usuario " + user + " NO corresponde con la añadida");
												result.put("login", "incorrecto");
											}
										}else {
											System.out.println("La contraseña del usuario " + user + " NO corresponde con la añadida");
											result.put("login", "incorrecto");
										}
										
										System.out.println("FIN - Comprobar contraseña");
									}else {
										System.out.println("El usuario con el que intentas acceder NO existe");
										
										System.out.println("FIN - Comprobar usuario");
										
										result.put("login", "incorrecto");
									}
									
									responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
									
									
								}
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
							
							System.out.println("ERROR al hacer las consultas SQL");
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						
						System.out.println("ERROR al crear el estamento de la consulta sql");
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al hacer la conexión a la base de datos");
				} 
				
			} catch (ClassNotFoundException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al cargar el driver de sql");
			}
				
		System.out.println("FIN de inicio de sesion");
		
		return responseEntity;
	}
	
	
	@RequestMapping(value="/sacardatos", method=RequestMethod.POST)
	public ResponseEntity<Map<String,String>> SacarDatos(@RequestBody Usuario usuario) {
		
		System.out.println("INICIO de sacar datos del usuario para Ver Perfil");
		
		String user = usuario.getUsuario();
		
		ResponseEntity<Map<String,String>> responseEntity = null;
		Map<String,String> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		// Cargar el driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
				
				Statement s = null;
				try {
					s = conexion.createStatement();
					
					try {
			        	//Sacamos todos los datos del usuario conectado
							ResultSet comp_user = s.executeQuery("SELECT Nombre, PrimerApellido, SegundoApellido, DNINIF, CorreoElectronico, NumExplotacion FROM usuarios WHERE Usuario='"+user+"'");
							
							System.out.println("SELECT * FROM usuarios WHERE Usuario='"+user+"'");
							
							//Para obtener los resultados del usuario conectado
							if(comp_user.next()){
								String nombre = comp_user.getString("Nombre");
								String PApellido = comp_user.getString("PrimerApellido");
								String SApellido = comp_user.getString("SegundoApellido");
								String DNINIF = comp_user.getString("DNINIF");
								String CElectronico = comp_user.getString("CorreoElectronico");
								String NumExplotacion = comp_user.getString("NumExplotacion");
								
								System.out.println("Nombre: " + nombre + " Apellidos: " + PApellido + " " + SApellido + " DNI: " + DNINIF + " Correo Electrónico: " + CElectronico
										+ " NumExplotacion: " + NumExplotacion);
								
								result.put("Nombre", nombre);
								result.put("PApellido", PApellido);
								result.put("SApellido", SApellido);
								result.put("DNINIF", DNINIF);
								result.put("CElectronico", CElectronico);
								result.put("NumExplotacion", NumExplotacion);
							}
							
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			} 
			
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}
		
		System.out.println("FIN de sacar datos del usuario para Ver Perfil");
		return responseEntity;
		
	}
	
	@RequestMapping(value="/guardarperfil", method=RequestMethod.POST)
	public ResponseEntity<Map<String,String>> GuardarDatos(@RequestBody Usuario usuario) {
		
		System.out.println("INICIO modificación de datos de perfil");
		
		ResponseEntity<Map<String,String>> responseEntity = null;
		Map<String,String> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		String user = usuario.getUsuario();
		String Nombre = usuario.getNombre();
		String PApellido = usuario.getPrimerApellido();
		String SApellido = usuario.getSegundoApellido();
		String DNINIF = usuario.getDNINIF();
		String CElectronico = usuario.getCorreoElectronico();
		
		// Cargar el driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
				
				Statement s = null;
				try {
					s = conexion.createStatement();
					
					try {
			        	//Guardamos los datos en la tabla usuarios
						System.out.println("UPDATE `usuarios` SET `Nombre`='"+Nombre+"',`PrimerApellido`='"+PApellido
								+"',`SegundoApellido`='"+SApellido+"',`DNINIF`='"+DNINIF+"',`CorreoElectronico`='"+CElectronico+"' WHERE Usuario = '"+user+"'");
						
						int resultado = s.executeUpdate("UPDATE `usuarios` SET `Nombre`='"+Nombre+"',`PrimerApellido`='"+PApellido
								+"',`SegundoApellido`='"+SApellido+"',`DNINIF`='"+DNINIF+"',`CorreoElectronico`='"+CElectronico+"' WHERE Usuario = '"+user+"'");
						
						//Comprobar si se ha insertado correctamente el update.
						if(resultado == 1) {
							System.out.println("Se han modificado los datos correctamente");
							result.put("QueryOk", "correcto");
							
						}else {
							System.out.println("ERROR al modificar los datos");
							result.put("QueryOk", "incorrecto");
						}
						
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			} 
			
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}
		
        System.out.println("FIN modificación de datos de perfil");
		return responseEntity;
		
	}
	
	@RequestMapping(value="/comprobarpass", method=RequestMethod.POST)
	public ResponseEntity<Map<String,String>> ComprobarPass(@RequestBody Usuario usuario) {
		
		System.out.println("INICIO comprobación contraseña actual");
		
		ResponseEntity<Map<String,String>> responseEntity = null;
		Map<String,String> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		String user = usuario.getUsuario();
		String pass = usuario.getContrasena();
		
		// Cargar el driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
				
				Statement s = null;
				try {
					s = conexion.createStatement();
					
					try {
						ResultSet cont_act = s.executeQuery("SELECT Contrasena FROM usuarios WHERE Usuario='"+user+"'");
						
						System.out.println("SELECT Contrasena FROM usuarios WHERE Usuario='"+user+"'");
						
						if(cont_act.next()){
							String pass_tabla = cont_act.getString(1);
							
							if(pass_tabla.equals(pass)) {
								System.out.println("La contraseña del usuario " + user + " corresponde con la añadida");
								
								result.put("comp_pass", "correcto");
							}else {
								System.out.println("La contraseña del usuario " + user + " NO corresponde con la añadida");
								result.put("comp_pass", "incorrecto");
							}
						}else {
							System.out.println("La contraseña del usuario " + user + " NO corresponde con la añadida");
							result.put("comp_pass", "incorrecto");
						}
						
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			} 
			
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}
		
        System.out.println("FIN comprobación contraseña actual");
		return responseEntity;
		
	}

	@RequestMapping(value="/guardarpass", method=RequestMethod.POST)
	public ResponseEntity<Map<String,String>> GuardarPass(@RequestBody Usuario usuario) {
		
		System.out.println("INICIO modificación de la contraseña");
		
		ResponseEntity<Map<String,String>> responseEntity = null;
		Map<String,String> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		String user = usuario.getUsuario();
		String pass = usuario.getContrasena();
		
		// Cargar el driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
				
				Statement s = null;
				try {
					s = conexion.createStatement();
					
					try {
			        	//Guardamos los datos en la tabla usuarios
						System.out.println("UPDATE `usuarios` SET `Contrasena`='"+pass+"' WHERE Usuario = '"+user+"'");
						
						int resultado = s.executeUpdate("UPDATE `usuarios` SET `Contrasena`='"+pass+"' WHERE Usuario = '"+user+"'");
						
						//Comprobar si se ha insertado correctamente el update.
						if(resultado == 1) {
							System.out.println("Se han modificado los datos correctamente");
							result.put("QueryOk", "correcto");
							
						}else {
							System.out.println("ERROR al modificar los datos");
							result.put("QueryOk", "incorrecto");
						}
						
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			} 
			
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}
		
        System.out.println("INICIO modificación de la contraseña");
		return responseEntity;
		
	}
	
	@RequestMapping(value="/cargarovejas", method=RequestMethod.POST)
	public ResponseEntity<Map<String,ArrayList<String>>> CargarOvejas(@RequestBody Usuario usuario) {
		
		System.out.println("INICIO sacar ovejar pertenecientes al usuario");
		
		ResponseEntity<Map<String,ArrayList<String>>> responseEntity = null;
		Map<String,ArrayList<String>> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		String user = usuario.getUsuario();
		String pass = usuario.getContrasena();
		
		// Cargar el driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
				
				Statement s = null;
				try {
					s = conexion.createStatement();
					
					try {
						List<String> datos = new ArrayList<String>();
						 
						ResultSet cont_act = s.executeQuery("SELECT NumIdentificacion FROM animales WHERE Usuario='"+user+"' AND Muerta='0' AND Venta='0'");
						
						System.out.println("SELECT NumIdentificacion FROM animales WHERE Usuario='"+user+"'");
						
						Statement st;
				        ResultSet rs;
				        ResultSetMetaData md;
				        //st = cn.createStatement();              
				        //rs = st.executeQuery(s_sql);
				        md = cont_act.getMetaData();
				        int columnas = md.getColumnCount();
				        ArrayList<String> ListaOvejas = new ArrayList<>();
				        //List<String> ListaOvejas = new List();
				        while(cont_act.next()){
				        	String dato; 
				            /*for (int i = 1; i <= columnas; i++) {                      
				            	dato = cont_act.getObject(i);
				            }*/
				        	dato = cont_act.getString("NumIdentificacion");
				        	ListaOvejas.add(dato);
				        }
				        result.put("ListaOvejas", ListaOvejas);
				        
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						//result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				//result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			} 
			
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			//result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}
		
        System.out.println("FIN sacar ovejar pertenecientes al usuario");
		return responseEntity;
		
	}
	
	@RequestMapping(value="/visualizaroveja", method=RequestMethod.POST)
	public ResponseEntity<Map<String, String>> VisualizarOveja(@RequestBody Animales oveja) {
		
		System.out.println("INICIO sacar datos perteneciente a la oveja");
		
		ResponseEntity<Map<String,String>> responseEntity = null;
		Map<String,String> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		String NumIdentificacion = oveja.getNumIdentificacion();
		
		// Cargar el driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
				
				Statement s = null;
				try {
					s = conexion.createStatement();
					
					try {
						List<String> datos = new ArrayList<String>();
						 
						ResultSet datosoveja = s.executeQuery("SELECT Sexo, AnoNacimiento, Muerta, FechaMuerte, Venta, FechaVenta, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"+NumIdentificacion+"'");
						
						System.out.println("SELECT Sexo, AnoNacimiento, Muerta, FechaMuerte, Venta, FechaVenta, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"+NumIdentificacion+"'");
				        
						//Para obtener los resultados del usuario conectado
						if(datosoveja.next()){
							String Sexo = datosoveja.getString("Sexo");
							String AnoNacimiento = datosoveja.getString("AnoNacimiento");
							String Muerta = datosoveja.getString("Muerta");
							String FechaMuerte = datosoveja.getString("FechaMuerte");
							String Venta = datosoveja.getString("Venta");
							String FechaVenta = datosoveja.getString("FechaVenta");
							String TieneBolo = datosoveja.getString("TieneBolo");
							String TieneCrotal = datosoveja.getString("TieneCrotal");
							String NumExplotacion = datosoveja.getString("NumExplotacion");
							
							/*System.out.println("Nombre: " + nombre + " Apellidos: " + PApellido + " " + SApellido + " DNI: " + DNINIF + " Correo Electrónico: " + CElectronico
									+ " NumExplotacion: " + NumExplotacion);*/
							
							result.put("Sexo", Sexo);
							result.put("AnoNacimiento", AnoNacimiento);
							result.put("Muerta", Muerta);
							result.put("FechaMuerte", FechaMuerte);
							result.put("Venta", Venta);
							result.put("FechaVenta", FechaVenta);
							result.put("TieneBolo", TieneBolo);
							result.put("TieneCrotal", TieneCrotal);
							result.put("NumExplotacion", NumExplotacion);
						}
						
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						//result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				//result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			} 
			
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			//result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}
		
        System.out.println("INICIO sacar datos perteneciente a la oveja");
		return responseEntity;
		
	}
	
	@RequestMapping(value="/marcarmuerta", method=RequestMethod.POST)
	public ResponseEntity<Map<String,String>> MarcarMuerta(@RequestBody Animales animal) {
		
		System.out.println("INICIO marcar oveja como muerta");
		
		ResponseEntity<Map<String,String>> responseEntity = null;
		Map<String,String> result = new HashMap<>();
		
		//Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		Date fechaMuerte = animal.getFechaMuerte();
		String NumIdentificacion = animal.getNumIdentificacion();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(fechaMuerte);
		
		// Cargar el driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");
				
				Statement s = null;
				try {
					s = conexion.createStatement();
					
					try {
			        	//Guardamos los datos en la tabla usuarios
						System.out.println("UPDATE `animales` SET `Muerta`='1' AND `FechaMuerte`='"+fechaMuerte+"' WHERE NumIdentificacion = '"+NumIdentificacion+"'");
						
						int resultado = s.executeUpdate("UPDATE `animales` SET `Muerta`=1, `FechaMuerte`='"+date+"' WHERE NumIdentificacion = '"+NumIdentificacion+"'");
						
						//Comprobar si se ha insertado correctamente el update.
						if(resultado == 1) {
							System.out.println("Se han modificado los datos correctamente");
							result.put("QueryOk", "correcto");
							
						}else {
							System.out.println("ERROR al modificar los datos");
							result.put("QueryOk", "incorrecto");
						}
						
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			} 
			
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}
		
        System.out.println("INICIO marcar oveja como muerta");
		return responseEntity;
		
	}
}
