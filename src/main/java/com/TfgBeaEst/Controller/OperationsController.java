package com.TfgBeaEst.Controller;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.util.Matrix;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.TfgBeaEst.Animales;
import com.TfgBeaEst.Usuario;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;

@Controller
public class OperationsController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> IniciarSesion(@RequestBody Usuario usuario) {
		System.out.println("INICIO de inicio de sesion");

		String user = usuario.getUsuario();
		String pass = usuario.getContrasena();

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
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
						// Primero sacamos si existe en la tabla algún usuario como el que hemos
						// indicado (user)
						System.out.println("INICIO - Comprobar usuario");

						ResultSet comp_user = s
								.executeQuery("SELECT COUNT(Usuario) FROM usuarios WHERE Usuario='" + user + "'");

						System.out.println("SELECT COUNT(Usuario) FROM usuarios WHERE Usuario='" + user + "'");

						// Para obtener los resultados
						if (comp_user.next()) {
							String recuento = comp_user.getString(1);
							if (recuento.equals("1")) {
								System.out.println("El usuario con el que intentas acceder existe");

								System.out.println("FIN - Comprobar usuario");

								// Comprobamos si la contraseña añadida corresponde con la que se encuentra en
								// la tabla
								System.out.println("INICIO - Comprobar contraseña");

								ResultSet comp_cont = s
										.executeQuery("SELECT Contrasena FROM usuarios WHERE Usuario='" + user + "'");

								System.out.println("SELECT Contrasena FROM usuarios WHERE Usuario='" + user + "'");

								if (comp_cont.next()) {
									String pass_tabla = comp_cont.getString(1);

									if (pass_tabla.equals(pass)) {
										System.out.println(
												"La contraseña del usuario " + user + " corresponde con la añadida");

										System.out.println(
												"SELECT Contrasena FROM usuarios WHERE Usuario='" + user + "'");
										result.put("login", "correcto");
									} else {
										System.out.println(
												"La contraseña del usuario " + user + " NO corresponde con la añadida");
										result.put("login", "incorrecto");
									}
								} else {
									System.out.println(
											"La contraseña del usuario " + user + " NO corresponde con la añadida");
									result.put("login", "incorrecto");
								}

								System.out.println("FIN - Comprobar contraseña");
							} else {
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

	@RequestMapping(value = "/sacardatos", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> SacarDatos(@RequestBody Usuario usuario) {

		System.out.println("INICIO de sacar datos del usuario para Ver Perfil");

		String user = usuario.getUsuario();

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
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
						// Sacamos todos los datos del usuario conectado
						ResultSet comp_user = s.executeQuery(
								"SELECT Nombre, PrimerApellido, SegundoApellido, DNINIF, CorreoElectronico FROM usuarios WHERE Usuario='"
										+ user + "'");

						System.out.println("SELECT * FROM usuarios WHERE Usuario='" + user + "'");

						// Para obtener los resultados del usuario conectado
						if (comp_user.next()) {
							String nombre = comp_user.getString("Nombre");
							String PApellido = comp_user.getString("PrimerApellido");
							String SApellido = comp_user.getString("SegundoApellido");
							String DNINIF = comp_user.getString("DNINIF");
							String CElectronico = comp_user.getString("CorreoElectronico");

							System.out.println("Nombre: " + nombre + " Apellidos: " + PApellido + " " + SApellido
									+ " DNI: " + DNINIF + " Correo Electrónico: " + CElectronico);

							result.put("Nombre", nombre);
							result.put("PApellido", PApellido);
							result.put("SApellido", SApellido);
							result.put("DNINIF", DNINIF);
							result.put("CElectronico", CElectronico);
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

	@RequestMapping(value = "/recogerExplotaciones", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> RecogerExplotaciones(@RequestBody Animales animal) {

		System.out.println("INICIO recoger datos explotacion");

		ResponseEntity<Map<String, ArrayList<String>>> responseEntity = null;
		Map<String, ArrayList<String>> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Integer AnoNacimiento = animal.getAnoNacimiento();
		String user = animal.getUsuario();

		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Si se marca la opcion de año de nacimiento
						List<String> datos = new ArrayList<String>();

						// Seleccionar todos los registros
						ResultSet datosexplotaciones = s.executeQuery(
								"SELECT NumExplotacion, TipoAnimal FROM explotaciones WHERE Usuario='" + user + "'");

						System.out.println(
								"SELECT NumExplotacion, TipoAnimal FROM explotaciones WHERE Usuario='" + user + "'");

						Statement st;
						ResultSet rs;
						ResultSetMetaData md;
						// st = cn.createStatement();
						// rs = st.executeQuery(s_sql);
						md = datosexplotaciones.getMetaData();
						int columnas = md.getColumnCount();
						ArrayList<String> ListaExplotaciones = new ArrayList<>();
						ArrayList<String> ListaTipoAnimal = new ArrayList<>();
						// List<String> ListaOvejas = new List();
						while (datosexplotaciones.next()) {
							String dato;
							String dato2;
							/*
							 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
							 */
							dato = datosexplotaciones.getString("NumExplotacion");
							dato2 = datosexplotaciones.getString("TipoAnimal");
							ListaExplotaciones.add(dato);
							ListaTipoAnimal.add(dato2);
						}
						result.put("ListaExplotaciones", ListaExplotaciones);
						result.put("ListaTipoAnimal", ListaTipoAnimal);

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

		System.out.println("FIN recoger datos explotacion");
		return responseEntity;

	}

	@RequestMapping(value = "/guardarperfil", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> GuardarDatos(@RequestBody Usuario usuario) {

		System.out.println("INICIO modificación de datos de perfil");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
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
						// Guardamos los datos en la tabla usuarios
						System.out.println("UPDATE `usuarios` SET `Nombre`='" + Nombre + "',`PrimerApellido`='"
								+ PApellido + "',`SegundoApellido`='" + SApellido + "',`DNINIF`='" + DNINIF
								+ "',`CorreoElectronico`='" + CElectronico + "' WHERE Usuario = '" + user + "'");

						int resultado = s.executeUpdate("UPDATE `usuarios` SET `Nombre`='" + Nombre
								+ "',`PrimerApellido`='" + PApellido + "',`SegundoApellido`='" + SApellido
								+ "',`DNINIF`='" + DNINIF + "',`CorreoElectronico`='" + CElectronico
								+ "' WHERE Usuario = '" + user + "'");

						// Comprobar si se ha insertado correctamente el update.
						if (resultado == 1) {
							System.out.println("Se han modificado los datos correctamente");
							result.put("QueryOk", "correcto");

						} else {
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

	@RequestMapping(value = "/comprobarpass", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ComprobarPass(@RequestBody Usuario usuario) {

		System.out.println("INICIO comprobación contraseña actual");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
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
						ResultSet cont_act = s
								.executeQuery("SELECT Contrasena FROM usuarios WHERE Usuario='" + user + "'");

						System.out.println("SELECT Contrasena FROM usuarios WHERE Usuario='" + user + "'");

						if (cont_act.next()) {
							String pass_tabla = cont_act.getString(1);

							if (pass_tabla.equals(pass)) {
								System.out.println("La contraseña del usuario " + user + " corresponde con la añadida");

								result.put("comp_pass", "correcto");
							} else {
								System.out.println(
										"La contraseña del usuario " + user + " NO corresponde con la añadida");
								result.put("comp_pass", "incorrecto");
							}
						} else {
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

	@RequestMapping(value = "/guardarpass", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> GuardarPass(@RequestBody Usuario usuario) {

		System.out.println("INICIO modificación de la contraseña");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
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
						// Guardamos los datos en la tabla usuarios
						System.out.println(
								"UPDATE `usuarios` SET `Contrasena`='" + pass + "' WHERE Usuario = '" + user + "'");

						int resultado = s.executeUpdate(
								"UPDATE `usuarios` SET `Contrasena`='" + pass + "' WHERE Usuario = '" + user + "'");

						// Comprobar si se ha insertado correctamente el update.
						if (resultado == 1) {
							System.out.println("Se han modificado los datos correctamente");
							result.put("QueryOk", "correcto");

						} else {
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

	@RequestMapping(value = "/cargarovejas", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> CargarOvejas(@RequestBody Usuario usuario) {

		System.out.println("INICIO sacar ovejar pertenecientes al usuario");

		ResponseEntity<Map<String, ArrayList<String>>> responseEntity = null;
		Map<String, ArrayList<String>> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String user = usuario.getUsuario();
		String pass = usuario.getContrasena();
		String numexplotacion = usuario.getNumExplotacion();

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

						/*
						 * ResultSet cont_act =
						 * s.executeQuery("SELECT NumIdentificacion FROM animales WHERE Usuario='" +
						 * user + "' AND Muerta='0' AND Venta='0'");
						 * 
						 * System.out.println("SELECT NumIdentificacion FROM animales WHERE Usuario='" +
						 * user + "' AND Muerta='0' AND Venta='0'");
						 */

						ResultSet cont_act = s
								.executeQuery("SELECT NumIdentificacion FROM animales WHERE NumExplotacion='"
										+ numexplotacion + "' AND Muerta='0' AND Venta='0'");

						System.out.println("SELECT NumIdentificacion FROM animales WHERE NumExplotacion='"
								+ numexplotacion + "' AND Muerta='0' AND Venta='0'");

						Statement st;
						ResultSet rs;
						ResultSetMetaData md;
						// st = cn.createStatement();
						// rs = st.executeQuery(s_sql);
						md = cont_act.getMetaData();
						int columnas = md.getColumnCount();
						ArrayList<String> ListaOvejas = new ArrayList<>();
						// List<String> ListaOvejas = new List();
						while (cont_act.next()) {
							String dato;
							/*
							 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
							 */
							dato = cont_act.getString("NumIdentificacion");
							ListaOvejas.add(dato);
						}
						result.put("ListaOvejas", ListaOvejas);

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						// result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		System.out.println("FIN sacar ovejar pertenecientes al usuario");
		return responseEntity;

	}

	@RequestMapping(value = "/visualizaroveja", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> VisualizarOveja(@RequestBody Animales oveja) {

		System.out.println("INICIO sacar datos perteneciente a la oveja");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
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

						/*
						 * ResultSet datosoveja = s.executeQuery(
						 * "SELECT Sexo, AnoNacimiento, Muerta, FechaMuerte, Venta, FechaVenta, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
						 * + NumIdentificacion + "'");
						 * 
						 * System.out.println(
						 * "SELECT Sexo, AnoNacimiento, Muerta, FechaMuerte, Venta, FechaVenta, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
						 * + NumIdentificacion + "'");
						 */

						ResultSet datosoveja = s.executeQuery(
								"SELECT Sexo, AnoNacimiento, Muerta, Venta, FechaBaja, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
										+ NumIdentificacion + "'");

						System.out.println(
								"SELECT Sexo, AnoNacimiento, Muerta, Venta, FechaBaja, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
										+ NumIdentificacion + "'");

						// Para obtener los resultados de las ovejas
						if (datosoveja.next()) {
							String Sexo = datosoveja.getString("Sexo");
							String AnoNacimiento = datosoveja.getString("AnoNacimiento");
							String Muerta = datosoveja.getString("Muerta");
							// String FechaMuerte = datosoveja.getString("FechaMuerte");
							String FechaBaja = datosoveja.getString("FechaBaja");
							String Venta = datosoveja.getString("Venta");
							// String FechaVenta = datosoveja.getString("FechaVenta");
							String TieneBolo = datosoveja.getString("TieneBolo");
							String TieneCrotal = datosoveja.getString("TieneCrotal");
							String NumExplotacion = datosoveja.getString("NumExplotacion");

							/*
							 * System.out.println("Nombre: " + nombre + " Apellidos: " + PApellido + " " +
							 * SApellido + " DNI: " + DNINIF + " Correo Electrónico: " + CElectronico +
							 * " NumExplotacion: " + NumExplotacion);
							 */

							result.put("Sexo", Sexo);
							result.put("AnoNacimiento", AnoNacimiento);
							result.put("Muerta", Muerta);
							// result.put("FechaMuerte", FechaMuerte);
							result.put("Venta", Venta);
							// result.put("FechaVenta", FechaVenta);
							result.put("FechaBaja", FechaBaja);
							result.put("TieneBolo", TieneBolo);
							result.put("TieneCrotal", TieneCrotal);
							result.put("NumExplotacion", NumExplotacion);
						}

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						// result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		System.out.println("INICIO sacar datos perteneciente a la oveja");
		return responseEntity;

	}

	@RequestMapping(value = "/marcarmuerta", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> MarcarMuerta(@RequestBody Animales animal) {

		System.out.println("INICIO marcar oveja como muerta");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Date fechaMuerte = animal.getFechaMuerte();
		String NumIdentificacion = animal.getNumIdentificacion();

		// String ProcDestino = animal.getProcDestino();
		String NDocumento = animal.getNDocumento();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(fechaMuerte);

		String NumExplotacion = animal.getNumExplotacion();
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Guardar la baja-muerte en animales
						System.out.println("UPDATE `animales` SET `Muerta`='1' AND `FechaBaja`='" + fechaMuerte
								+ "' WHERE NumIdentificacion = '" + NumIdentificacion + "'");

						int resultado = s.executeUpdate("UPDATE `animales` SET `Muerta`=1, `FechaBaja`='" + date
								+ "' WHERE NumIdentificacion = '" + NumIdentificacion + "'");

						// Sacar el balance final de la explotación
						// Seleccionar todos los registros
						ResultSet balancetotal = s.executeQuery(
								"SELECT COUNT(NumIdentificacion) AS BalanceTotal FROM animales WHERE Venta = '0' AND Muerta = '0' AND NumExplotacion='"
										+ NumExplotacion + "'");

						System.out.println(
								"SELECT COUNT(NumIdentificacion) AS BalanceTotal FROM animales WHERE Venta = '0' AND Muerta = '0' AND NumExplotacion='"
										+ NumExplotacion + "'");

						String balance_total = null;
						while (balancetotal.next()) {

							balance_total = balancetotal.getString("BalanceTotal");
						}

						/*System.out.println("SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet numHojas = s
								.executeQuery("SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						String numhojas = null;
						while (numHojas.next()) {
							numhojas = numHojas.getString("NumHojas");
						}
						
						System.out.println("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet numHojas1 = s
								.executeQuery("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						String NumHoja = null;
						while (numHojas1.next()) {
							NumHoja = numHojas1.getString("NumHoja");
						}

						System.out.println("SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FECHA ASC");
						
						ResultSet antTotal = s.executeQuery(
								"SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						ArrayList<String> AnimalesHojAnt = new ArrayList<>();
						String totan = null;
						while (antTotal.next()) {
							String dato;
							dato = antTotal.getString("AnimalesHojaAnt");
							AnimalesHojAnt.add(dato);
						}

						int tam = AnimalesHojAnt.size();

						int numhojaS;
						numhojaS = Integer.parseInt(numhojas);

						double NumHojaS = (double)numhojaS / 16;
						int hoja = 0;
						String total;
						total = AnimalesHojAnt.get(tam - 1);
						int total2;
						total2 = Integer.parseInt(total);
						int total_final = total2;
						if (NumHojaS % 1 == 0) {
							hoja = Integer.parseInt(NumHoja) + 1;
							total_final = Integer.parseInt(balance_total);
						} else {
							hoja = Integer.parseInt(NumHoja);
						}*/

						// Comprobar si se ha insertado correctamente el update.
						if (resultado == 1) {
	
							// Guardamos los datos en la tabla altas_bajas
							System.out.println("INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
									+ "VALUES ('"+ date + "', 'BAJA - MUERTE', '-', '"+NDocumento+"', '1', '" + balance_total
									+ "', '" + NumExplotacion + "')");

							int resultado1 = s.executeUpdate(
									"INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
											+ "VALUES ('"+ date + "', 'BAJA - MUERTE', '-', '"+NDocumento+"', '1', '" + balance_total
											+ "', '" + NumExplotacion + "')");
							
							if (resultado1 == 1) {
								System.out.println("Se han modificado los datos correctamente");
								result.put("QueryOk", "correcto");
							}else {
								System.out.println("ERROR al modificar los datos");
								result.put("QueryOk", "incorrecto");
							}

						} else {
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

	@RequestMapping(value = "/marcarventa", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> MarcarVenta(@RequestBody Animales animal) {

		System.out.println("INICIO marcar oveja como vendida");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Date fechaVenta = animal.getFechaVenta();
		String NumIdentificacion = animal.getNumIdentificacion();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(fechaVenta);

		String NDocumento = animal.getNDocumento();
		String Destino = animal.getProcDestino();
		String NumExplotacion = animal.getNumExplotacion();

		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {

						System.out.println("UPDATE `animales` SET `Venta`='1' AND `FechaBaja`='" + date
								+ "' WHERE NumIdentificacion = '" + NumIdentificacion + "'");

						int resultado = s.executeUpdate("UPDATE `animales` SET `Venta`=1, `FechaBaja`='" + date
								+ "' WHERE NumIdentificacion = '" + NumIdentificacion + "'");

						// Sacar el balance final de la explotación
						// Seleccionar todos los registros
						ResultSet balancetotal = s.executeQuery(
								"SELECT COUNT(NumIdentificacion) AS BalanceTotal FROM animales WHERE Venta = '0' AND Muerta = '0' AND NumExplotacion='"
										+ NumExplotacion + "'");

						System.out.println(
								"SELECT COUNT(NumIdentificacion) AS BalanceTotal FROM animales WHERE Venta = '0' AND Muerta = '0' AND NumExplotacion='"
										+ NumExplotacion + "'");

						String balance_total = null;
						while (balancetotal.next()) {

							balance_total = balancetotal.getString("BalanceTotal");
						}

						/*System.out.println("SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet numHojas = s
								.executeQuery("SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						String numhojas = null;
						while (numHojas.next()) {
							numhojas = numHojas.getString("NumHojas");
						}
						
						System.out.println("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet numHojas1 = s
								.executeQuery("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						String NumHoja = null;
						while (numHojas1.next()) {
							NumHoja = numHojas1.getString("NumHoja");
						}

						System.out.println("SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FECHA ASC");
						
						ResultSet antTotal = s.executeQuery(
								"SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						ArrayList<String> AnimalesHojAnt = new ArrayList<>();
						String totan = null;
						while (antTotal.next()) {
							String dato;
							dato = antTotal.getString("AnimalesHojaAnt");
							AnimalesHojAnt.add(dato);
						}

						int tam = AnimalesHojAnt.size();

						int numhojaS;
						numhojaS = Integer.parseInt(numhojas);

						double NumHojaS = (double)numhojaS / 16;
						int hoja = 0;
						String total;
						total = AnimalesHojAnt.get(tam - 1);
						int total2;
						total2 = Integer.parseInt(total);
						int total_final = total2;
						if (NumHojaS % 1 == 0) {
							hoja = Integer.parseInt(NumHoja) + 1;
							total_final = Integer.parseInt(balance_total);
						} else {
							hoja = Integer.parseInt(NumHoja);
						}*/

						

						// Comprobar si se ha insertado correctamente el update.
						if (resultado == 1) {
							
							// Guardamos los datos en la tabla altas_bajas
							System.out.println("INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
									+ "VALUES ('"+ date + "', 'BAJA - SALIDA', '"+Destino+"', '" + NDocumento + "', '1', '" + balance_total
									+ "', '" + NumExplotacion + "')");

							int resultado1 = s.executeUpdate(
									"INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
											+ "VALUES ('"+ date + "', 'BAJA - SALIDA', '"+Destino+"', '" + NDocumento + "', '1', '" + balance_total
											+ "', '" + NumExplotacion + "')");
							if (resultado1 == 1) {
								System.out.println("Se han modificado los datos correctamente");
								result.put("QueryOk", "correcto");
							}else{
								System.out.println("ERROR al modificar los datos");
								result.put("QueryOk", "incorrecto");
							}
							

						} else {
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

		System.out.println("INICIO marcar oveja como vendida");
		return responseEntity;

	}

	@RequestMapping(value = "/filtro4", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> Filtro4(@RequestBody Animales animal) {

		System.out.println("INICIO mostrar filtro 4");

		ResponseEntity<Map<String, ArrayList<String>>> responseEntity = null;
		Map<String, ArrayList<String>> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Date fechaMuerteFiltro = animal.getFechaMuerte();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaMuerteFiltro);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();
		Boolean MuertaFiltro = animal.getMuerta();
		Boolean VentaFiltro = animal.getVenta();
		String user = animal.getUsuario();
		String numexplotacion = animal.getNumExplotacion();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fechaMuerteFiltro);
		String strDate2 = dateFormat.format(fechaMuerte2Filtro);

		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Si se marca la opcion de mostrar las muertas y el año
						if (MuertaFiltro && !VentaFiltro) {
							List<String> datos = new ArrayList<String>();

							// Seleccionar todos los registros
							/*
							 * ResultSet datosovejaMuertas = s
							 * .executeQuery("SELECT NumIdentificacion FROM animales WHERE Usuario='" + user
							 * + "' AND Muerta='1' AND FechaBaja>='" + fechaMuerteFiltro +
							 * "' ORDER BY FechaBaja");
							 * 
							 * System.out.println("SELECT NumIdentificacion FROM animales WHERE Usuario='" +
							 * user + "' AND Muerta='1' AND FechaBaja>='" + fechaMuerteFiltro + "'");
							 */

							ResultSet datosovejaMuertas = s
									.executeQuery("SELECT NumIdentificacion FROM animales WHERE (FechaBaja "
											+ "BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
											+ numexplotacion + "' AND Muerta='1'" + " ORDER BY FechaBaja");

							System.out.println("SELECT NumIdentificacion FROM animales WHERE (FechaBaja " + "BETWEEN '"
									+ strDate + "' AND '" + strDate2 + "') AND NumExplotacion='" + numexplotacion
									+ "' AND Muerta='1'" + " ORDER BY FechaBaja");

							Statement st;
							ResultSet rs;
							ResultSetMetaData md;
							// st = cn.createStatement();
							// rs = st.executeQuery(s_sql);
							md = datosovejaMuertas.getMetaData();
							int columnas = md.getColumnCount();
							ArrayList<String> ListaOvejas = new ArrayList<>();
							// List<String> ListaOvejas = new List();
							while (datosovejaMuertas.next()) {
								String dato;
								/*
								 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
								 */
								dato = datosovejaMuertas.getString("NumIdentificacion");
								ListaOvejas.add(dato);
							}
							result.put("ListaOvejas", ListaOvejas);
						}

						// Si se marca la opcion de mostrar las muertas y el año
						if (VentaFiltro && !MuertaFiltro) {
							List<String> datos = new ArrayList<String>();

							// Seleccionar todos los registros
							/*
							 * ResultSet datosovejaMuertas = s
							 * .executeQuery("SELECT NumIdentificacion FROM animales WHERE Usuario='" + user
							 * + "' AND Venta='1' AND FechaBaja>='" + fechaMuerteFiltro +
							 * "' ORDER BY FechaBaja");
							 * 
							 * System.out.println("SELECT NumIdentificacion FROM animales WHERE Usuario='" +
							 * user + "' AND Venta='1' AND FechaBaja>='" + fechaMuerteFiltro +
							 * "' ORDER BY FechaBaja");
							 */

							ResultSet datosovejaMuertas = s
									.executeQuery("SELECT NumIdentificacion FROM animales WHERE (FechaBaja "
											+ "BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
											+ numexplotacion + "' AND Venta='1'" + " ORDER BY FechaBaja");

							System.out.println("SELECT NumIdentificacion FROM animales WHERE (FechaBaja " + "BETWEEN '"
									+ strDate + "' AND '" + strDate2 + "') AND NumExplotacion='" + numexplotacion
									+ "' AND Venta='1'" + " ORDER BY FechaBaja");

							Statement st;
							ResultSet rs;
							ResultSetMetaData md;
							// st = cn.createStatement();
							// rs = st.executeQuery(s_sql);
							md = datosovejaMuertas.getMetaData();
							int columnas = md.getColumnCount();
							ArrayList<String> ListaOvejas = new ArrayList<>();
							// List<String> ListaOvejas = new List();
							while (datosovejaMuertas.next()) {
								String dato;
								/*
								 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
								 */
								dato = datosovejaMuertas.getString("NumIdentificacion");
								ListaOvejas.add(dato);
							}
							result.put("ListaOvejas", ListaOvejas);
						}

						// Si se marca la opcion de mostrar las muertas y el año
						if (VentaFiltro && MuertaFiltro) {
							List<String> datos = new ArrayList<String>();

							// Seleccionar todos los registros
							/*
							 * ResultSet datosovejaMuertas = s
							 * .executeQuery("SELECT NumIdentificacion FROM animales WHERE Usuario='" + user
							 * + "' AND FechaBaja>='" + fechaMuerteFiltro +
							 * "' AND (Muerta='1' OR Venta='1') ORDER BY FechaBaja");
							 * 
							 * System.out.println("SELECT NumIdentificacion FROM animales WHERE Usuario='" +
							 * user + "' AND FechaBaja>='" + fechaMuerteFiltro +
							 * "' AND (Muerta='1' OR Venta='1') ORDER BY FechaBaja");
							 */

							ResultSet datosovejaMuertas = s.executeQuery(
									"SELECT NumIdentificacion FROM animales WHERE (FechaBaja " + "BETWEEN '" + strDate
											+ "' AND '" + strDate2 + "') AND NumExplotacion='" + numexplotacion
											+ "' AND (Muerta='1' OR Venta='1')" + " ORDER BY FechaBaja");

							Statement st;
							ResultSet rs;
							ResultSetMetaData md;
							// st = cn.createStatement();
							// rs = st.executeQuery(s_sql);
							md = datosovejaMuertas.getMetaData();
							int columnas = md.getColumnCount();
							ArrayList<String> ListaOvejas = new ArrayList<>();
							// List<String> ListaOvejas = new List();
							while (datosovejaMuertas.next()) {
								String dato;
								/*
								 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
								 */
								dato = datosovejaMuertas.getString("NumIdentificacion");
								ListaOvejas.add(dato);
							}
							result.put("ListaOvejas", ListaOvejas);
						}

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						// result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		System.out.println("FIN mostrar filtro 4");
		return responseEntity;

	}

	@RequestMapping(value = "/filtro2", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> Filtro2(@RequestBody Animales animal) {

		System.out.println("INICIO mostrar filtro 2");

		ResponseEntity<Map<String, ArrayList<String>>> responseEntity = null;
		Map<String, ArrayList<String>> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Integer AnoNacimiento = animal.getAnoNacimiento();
		String user = animal.getUsuario();
		String NumExplotacion = animal.getNumExplotacion();

		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Si se marca la opcion de año de nacimiento
						List<String> datos = new ArrayList<String>();

						// Seleccionar todos los registros
						/*
						 * ResultSet datosovejaMuertas = s
						 * .executeQuery("SELECT NumIdentificacion, Muerta, Venta FROM animales WHERE Usuario='"
						 * + user + "' AND AnoNacimiento='" + AnoNacimiento + "'");
						 * 
						 * System.out.println("SELECT NumIdentificacion FROM animales WHERE Usuario='" +
						 * user + "' AND AnoNacimiento='" + AnoNacimiento + "'");
						 */

						ResultSet datosovejaMuertas = s.executeQuery(
								"SELECT NumIdentificacion, Muerta, Venta FROM animales WHERE NumExplotacion='"
										+ NumExplotacion + "' AND AnoNacimiento='" + AnoNacimiento + "'");

						System.out
								.println("SELECT NumIdentificacion, Muerta, Venta FROM animales WHERE NumExplotacion='"
										+ NumExplotacion + "' AND AnoNacimiento='" + AnoNacimiento + "'");

						Statement st;
						ResultSet rs;
						ResultSetMetaData md;
						// st = cn.createStatement();
						// rs = st.executeQuery(s_sql);
						md = datosovejaMuertas.getMetaData();
						int columnas = md.getColumnCount();
						ArrayList<String> ListaOvejas = new ArrayList<>();
						ArrayList<String> ListaOvejas2 = new ArrayList<>();
						ArrayList<String> ListaOvejas3 = new ArrayList<>();
						// List<String> ListaOvejas = new List();
						while (datosovejaMuertas.next()) {
							String dato;
							String dato2;
							String dato3;
							/*
							 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
							 */
							dato = datosovejaMuertas.getString("NumIdentificacion");
							dato2 = datosovejaMuertas.getString("Muerta");
							dato3 = datosovejaMuertas.getString("Venta");
							ListaOvejas.add(dato);
							ListaOvejas2.add(dato2);
							ListaOvejas3.add(dato3);
						}
						result.put("ListaOvejas", ListaOvejas);
						result.put("ListaOvejas2", ListaOvejas2);
						result.put("ListaOvejas3", ListaOvejas3);
						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						// result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		System.out.println("FIN mostrar filtro 2");
		return responseEntity;

	}

	@RequestMapping(value = "/filtro3", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> filtro3(@RequestBody Animales oveja) {

		System.out.println("INICIO sacar datos perteneciente a la oveja en el filtro 3");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
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

						/*
						 * ResultSet datosoveja = s.executeQuery(
						 * "SELECT Sexo, AnoNacimiento, Muerta, FechaMuerte, Venta, FechaVenta, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
						 * + NumIdentificacion + "'");
						 * 
						 * System.out.println(
						 * "SELECT Sexo, AnoNacimiento, Muerta, FechaMuerte, Venta, FechaVenta, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
						 * + NumIdentificacion + "'");
						 */

						ResultSet datosoveja = s.executeQuery(
								"SELECT Sexo, AnoNacimiento, Muerta, Venta, FechaBaja, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
										+ NumIdentificacion + "'");

						System.out.println(
								"SELECT Sexo, AnoNacimiento, Muerta, Venta, FechaBaja, TieneBolo, TieneCrotal, NumExplotacion FROM animales WHERE NumIdentificacion='"
										+ NumIdentificacion + "'");

						// Para obtener los resultados de las ovejas
						if (datosoveja.next()) {
							String Sexo = datosoveja.getString("Sexo");
							String AnoNacimiento = datosoveja.getString("AnoNacimiento");
							String Muerta = datosoveja.getString("Muerta");
							// String FechaMuerte = datosoveja.getString("FechaMuerte");
							String FechaBaja = datosoveja.getString("FechaBaja");
							String Venta = datosoveja.getString("Venta");
							// String FechaVenta = datosoveja.getString("FechaVenta");
							String TieneBolo = datosoveja.getString("TieneBolo");
							String TieneCrotal = datosoveja.getString("TieneCrotal");
							String NumExplotacion = datosoveja.getString("NumExplotacion");

							/*
							 * System.out.println("Nombre: " + nombre + " Apellidos: " + PApellido + " " +
							 * SApellido + " DNI: " + DNINIF + " Correo Electrónico: " + CElectronico +
							 * " NumExplotacion: " + NumExplotacion);
							 */

							result.put("Sexo", Sexo);
							result.put("AnoNacimiento", AnoNacimiento);
							result.put("Muerta", Muerta);
							// result.put("FechaMuerte", FechaMuerte);
							result.put("Venta", Venta);
							// result.put("FechaVenta", FechaVenta);
							result.put("FechaBaja", FechaBaja);
							result.put("TieneBolo", TieneBolo);
							result.put("TieneCrotal", TieneCrotal);
							result.put("NumExplotacion", NumExplotacion);
						}

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
						// result.put("QueryOk", "incorrecto");
						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		System.out.println("FIN sacar datos perteneciente a la oveja en el filtro 3");
		return responseEntity;

	}

	@RequestMapping(value = "/nuevoanimal", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> NuevoAnimal(@RequestBody Animales oveja) {

		System.out.println("INICIO añadir un nuevo animal");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = oveja.getNumExplotacion();
		String NumIdentificacion = oveja.getNumIdentificacion();
		String Destino = oveja.getProcDestino();
		String usuario = oveja.getUsuario();
		String sexo = oveja.getSexo();
		Integer anonacimiento = oveja.getAnoNacimiento();
		Boolean tienebolo = oveja.getTieneBolo();
		Integer v_tienebolo;
		if (tienebolo) {
			v_tienebolo = 1;
		} else {
			v_tienebolo = 0;
		}
		Boolean tienecrotal = oveja.getTieneCrotal();
		Integer v_tienecrotal;
		if (tienecrotal) {
			v_tienecrotal = 1;
		} else {
			v_tienecrotal = 0;
		}

		Date fechaMuerte = oveja.getFechaBaja();

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
						// Guardamos los datos en la tabla animales
						System.out.println("INSERT INTO animales "
								+ "(NumIdentificacion, Sexo, AnoNacimiento, Usuario, Muerta, Venta, TieneBolo, TieneCrotal,NumExplotacion)"
								+ " VALUES ('" + NumIdentificacion + "','" + sexo + "','" + anonacimiento + "','"
								+ usuario + "','0','0','" + v_tienebolo + "','" + v_tienecrotal + "','" + NumExplotacion
								+ "')");

						int resultado = s.executeUpdate("INSERT INTO animales "
								+ "(NumIdentificacion, Sexo, AnoNacimiento, Usuario, Muerta, Venta, TieneBolo, TieneCrotal,NumExplotacion)"
								+ " VALUES ('" + NumIdentificacion + "','" + sexo + "','" + anonacimiento + "','"
								+ usuario + "','0','0','" + v_tienebolo + "','" + v_tienecrotal + "','" + NumExplotacion
								+ "')");

						// Sacar el balance final de la explotación
						// Seleccionar todos los registros
						System.out.println(
								"SELECT COUNT(NumIdentificacion) AS BalanceTotal FROM animales WHERE Venta = '0' AND Muerta = '0' AND NumExplotacion='"
										+ NumExplotacion + "'");
						
						ResultSet balancetotal = s.executeQuery(
								"SELECT COUNT(NumIdentificacion) AS BalanceTotal FROM animales WHERE Venta = '0' AND Muerta = '0' AND NumExplotacion='"
										+ NumExplotacion + "'");

						String balance_total = null;
						while (balancetotal.next()) {

							balance_total = balancetotal.getString("BalanceTotal");
						}

						/*System.out.println("SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet numHojas = s
								.executeQuery("SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						String numhojas = null;
						while (numHojas.next()) {
							numhojas = numHojas.getString("NumHojas");
						}
						
						System.out.println("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet numHojas1 = s
								.executeQuery("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						String NumHoja = null;
						while (numHojas1.next()) {
							NumHoja = numHojas1.getString("NumHoja");
						}

						System.out.println("SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FECHA ASC");
						
						ResultSet antTotal = s.executeQuery(
								"SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						ArrayList<String> AnimalesHojAnt = new ArrayList<>();
						String totan = null;
						while (antTotal.next()) {
							String dato;
							dato = antTotal.getString("AnimalesHojaAnt");
							AnimalesHojAnt.add(dato);
						}

						int tam = AnimalesHojAnt.size();

						int numhojaS;
						numhojaS = Integer.parseInt(numhojas);

						double NumHojaS = (double)numhojaS / 16;
						int hoja = 0;
						String total;
						total = AnimalesHojAnt.get(tam - 1);
						int total2;
						total2 = Integer.parseInt(total);
						int total_final = total2;
						if (NumHojaS % 1 == 0) {
							hoja = Integer.parseInt(NumHoja) + 1;
							total_final = Integer.parseInt(balance_total);
						} else {
							hoja = Integer.parseInt(NumHoja);
						}*/
						
						// Comprobar si se ha insertado correctamente el update.
						if (resultado == 1) {
							
							// Guardamos los datos en la tabla altas_bajas
							System.out.println("INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
									+ "VALUES ('"+ date + "', 'ALTA', '"+Destino+"', '-', '1', '" + balance_total
									+ "', '" + NumExplotacion + "')");

							int resultado1 = s.executeUpdate(
									"INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
											+ "VALUES ('"+ date + "', 'ALTA', '"+Destino+"', '-', '1 OVEJA', '" + balance_total
											+ "', '" + NumExplotacion + "')");
							
							if(resultado1 == 1) {
								System.out.println("Se han modificado los datos correctamente");
								result.put("QueryOk", "correcto");
							}else {
								System.out.println("ERROR al modificar los datos");
								result.put("QueryOk", "incorrecto");
							}
							

						} else {
							System.out.println("ERROR al modificar los datos");
							result.put("QueryOk", "incorrecto");
						}

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						result.put("QueryOk", "incorrecto");
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		System.out.println("FIN añadir un nuevo animal");
		return responseEntity;

	}

	@RequestMapping(value = "/modificaranimal", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ModificarAnimal(@RequestBody Animales oveja) {

		System.out.println("INICIO modificar animal");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = oveja.getNumExplotacion();
		String NumIdentificacion = oveja.getNumIdentificacion();
		String usuario = oveja.getUsuario();
		String sexo = oveja.getSexo();
		Integer anonacimiento = oveja.getAnoNacimiento();
		Boolean tienebolo = oveja.getTieneBolo();
		Integer v_tienebolo;
		if (tienebolo) {
			v_tienebolo = 1;
		} else {
			v_tienebolo = 0;
		}
		Boolean tienecrotal = oveja.getTieneCrotal();
		Integer v_tienecrotal;
		if (tienecrotal) {
			v_tienecrotal = 1;
		} else {
			v_tienecrotal = 0;
		}

		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Guardamos los datos en la tabla usuarios
						System.out.println("UPDATE animales SET Sexo='" + sexo + "', AnoNacimiento='" + anonacimiento
								+ "', TieneBolo='" + v_tienebolo + "', TieneCrotal='" + v_tienecrotal + "'"
								+ "WHERE NumIdentificacion='" + NumIdentificacion + "'");

						int resultado = s.executeUpdate("UPDATE animales SET Sexo='" + sexo + "', AnoNacimiento='"
								+ anonacimiento + "', TieneBolo='" + v_tienebolo + "', TieneCrotal='" + v_tienecrotal
								+ "'" + "WHERE NumIdentificacion='" + NumIdentificacion + "'");

						// Comprobar si se ha insertado correctamente el update.
						if (resultado == 1) {
							System.out.println("Se han modificado los datos correctamente");
							result.put("QueryOk", "correcto");

						} else {
							System.out.println("ERROR al modificar los datos");
							result.put("QueryOk", "incorrecto");
						}

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						result.put("QueryOk", "incorrecto");
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		System.out.println("FIN modificar animal");
		return responseEntity;

	}

	@RequestMapping(value = "/exportarSinCrotal/{numexplotacion}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ExportarSinCrotal(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String numexplotacion) throws ServletException, IOException {
		System.out.println("INICIO exportar animales sin crotal");
		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> ListaOvejas = new ArrayList<>();
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						ResultSet cont_act = s
								.executeQuery("SELECT NumIdentificacion FROM animales WHERE NumExplotacion='"
										+ numexplotacion + "' AND Muerta='0' AND Venta='0' AND TieneCrotal='0'");

						System.out.println("SELECT NumIdentificacion FROM animales WHERE NumExplotacion='"
								+ numexplotacion + "' AND Muerta='0' AND Venta='0' AND TieneCrotal='0'");

						Statement st;
						ResultSet rs;
						ResultSetMetaData md;
						// st = cn.createStatement();
						// rs = st.executeQuery(s_sql);
						md = cont_act.getMetaData();
						int columnas = md.getColumnCount();
						// List<String> ListaOvejas = new List();
						while (cont_act.next()) {
							String dato;
							/*
							 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
							 */
							dato = cont_act.getString("NumIdentificacion");
							ListaOvejas.add(dato);
						}
						// result.put("ListaOvejas", ListaOvejas);

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						result.put("QueryOk", "incorrecto");
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		// Creación del Excel
		Workbook wb = new XSSFWorkbook();

		String sheetName = WorkbookUtil.createSafeSheetName("Sin Crotal");
		Sheet sheet = wb.createSheet(sheetName);

		// Encabezado del fichero

		// CellStyle estilo = this.estiloCabeceraPrincipal(wb);
		int columna = 0;

		int tam = ListaOvejas.size();

		for (int i = 0; i < tam; i++) {
			Row fila = sheet.createRow(i);
			Cell celda = fila.createCell(columna);
			celda.setCellValue(ListaOvejas.get(i));
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			wb.write(stream);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// StringBuilder uriBuilder = new StringBuilder();
		ServletContext servletContext = request.getSession().getServletContext();
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(servletContext.getRealPath(File.separator));

		File folder = new File(uriBuilder.toString());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File fichero = new File(uriBuilder.toString() + "/SinCrotal.xlsx");
		if (!fichero.exists()) {
			FileOutputStream elFichero = new FileOutputStream(uriBuilder.toString() + "/SinCrotal.xlsx");
			try {
				elFichero.write(stream.toByteArray());
			} finally {
				elFichero.close();
			}
		}

		String nombreFichero = "SinCrotal";
		result.put("exported", nombreFichero);
		responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
		System.out.println("FIN exportar animales sin crotal");
		return responseEntity;
	}

	@RequestMapping(value = "/exportarSinBolo/{numexplotacion}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ExportarSinBolo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String numexplotacion) throws ServletException, IOException {
		System.out.println("INICIO exportar animales sin bolo");
		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> ListaOvejas = new ArrayList<>();
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						ResultSet cont_act = s
								.executeQuery("SELECT NumIdentificacion FROM animales WHERE NumExplotacion='"
										+ numexplotacion + "' AND Muerta='0' AND Venta='0' AND TieneBolo='0'");

						System.out.println("SELECT NumIdentificacion FROM animales WHERE NumExplotacion='"
								+ numexplotacion + "' AND Muerta='0' AND Venta='0' AND TieneBolo='0'");

						Statement st;
						ResultSet rs;
						ResultSetMetaData md;
						// st = cn.createStatement();
						// rs = st.executeQuery(s_sql);
						md = cont_act.getMetaData();
						int columnas = md.getColumnCount();
						// List<String> ListaOvejas = new List();
						while (cont_act.next()) {
							String dato;
							/*
							 * for (int i = 1; i <= columnas; i++) { dato = cont_act.getObject(i); }
							 */
							dato = cont_act.getString("NumIdentificacion");
							ListaOvejas.add(dato);
						}
						// result.put("ListaOvejas", ListaOvejas);

						responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
					} catch (SQLException e) {
						result.put("QueryOk", "incorrecto");
						// TODO Auto-generated catch block
						e.printStackTrace();
						responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// result.put("QueryOk", "incorrecto");
					responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// result.put("QueryOk", "incorrecto");
				responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			// result.put("QueryOk", "incorrecto");
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.println("ERROR al cargar el driver de sql");
		}

		// Creación del Excel
		Workbook wb = new XSSFWorkbook();

		String sheetName = WorkbookUtil.createSafeSheetName("SinBolo");
		Sheet sheet = wb.createSheet(sheetName);

		// Encabezado del fichero

		// CellStyle estilo = this.estiloCabeceraPrincipal(wb);
		int columna = 0;

		int tam = ListaOvejas.size();

		for (int i = 0; i < tam; i++) {
			Row fila = sheet.createRow(i);
			Cell celda = fila.createCell(columna);
			celda.setCellValue(ListaOvejas.get(i));
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			wb.write(stream);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// StringBuilder uriBuilder = new StringBuilder();
		ServletContext servletContext = request.getSession().getServletContext();
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(servletContext.getRealPath(File.separator));

		File folder = new File(uriBuilder.toString());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File fichero = new File(uriBuilder.toString() + "/SinBolo.xlsx");
		if (!fichero.exists()) {
			FileOutputStream elFichero = new FileOutputStream(uriBuilder.toString() + "/SinBolo.xlsx");
			try {
				elFichero.write(stream.toByteArray());
			} finally {
				elFichero.close();
			}
		}

		String nombreFichero = "SinBolo";
		result.put("exported", nombreFichero);
		responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
		System.out.println("FIN exportar animales sin bolo");
		return responseEntity;
	}

	/*
	 * MODULOS PARA EXPORTAR EL ARCHIVO EXCEL, EN ESTE CASO EL SIN CROTAL O EL SIN
	 * BOLO
	 */
	@RequestMapping(value = "/exportarExcel/{param}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> recuperarInforme(HttpServletRequest request, @PathVariable String param) {
		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=" + param + "_" + date + ".xlsx");
		byte[] exportacion = null;

		ServletContext servletContext = request.getSession().getServletContext();
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(servletContext.getRealPath(File.separator));
		String path = uriBuilder.toString() + "/" + param + ".xlsx";
		exportacion = OperationsController.descargarInformeValidacion(path);
		result = new ResponseEntity<byte[]>(exportacion, header, HttpStatus.OK);

		return result;
	}

	public static byte[] descargarInformeValidacion(String path) {
		try {
			// Recuperamos el fichero para mandarlo
			File informe = new File(path);
			byte[] byteInforme = new byte[(int) informe.length()];
			FileInputStream fis = new FileInputStream(informe);
			fis.read(byteInforme);
			fis.close();
			return byteInforme;
		} catch (Exception e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			return null;
		}
	}
	/*
	 * FIN
	 */

	@RequestMapping(value = "/PlantillaExcel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> DescargarPlantillaExcel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("INICIO descarga altas y bajas de animales");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=PlantillaImportaciones.xlsx");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/PlantillaImportaciones.xlsx");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga altas y bajas de animales");
		return result;
	}
}
