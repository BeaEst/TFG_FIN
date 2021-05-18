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
import java.util.Calendar;
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

import com.TfgBeaEst.Animales;
import com.TfgBeaEst.Usuario;

@Controller
public class OperationCriasController {

	@RequestMapping(value = "/cargarventacrias", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> CargarVentaCrias(@RequestBody Usuario usuario) {

		System.out.println("INICIO sacar ventas de crías de la explotación");

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

						System.out.println("SELECT NGuia, FechaVenta, NAnimales FROM venta_crias WHERE NumExplotacion='"
								+ numexplotacion + "' ORDER BY FechaVenta");

						ResultSet cont_act = s.executeQuery(
								"SELECT NGuia, FechaVenta, NAnimales FROM venta_crias WHERE NumExplotacion='"
										+ numexplotacion + "' ORDER BY FechaVenta");

						ArrayList<String> NumGuia = new ArrayList<>();
						ArrayList<String> FVenta = new ArrayList<>();
						ArrayList<String> NumAnimales = new ArrayList<>();

						while (cont_act.next()) {
							String dato1;
							String dato2;
							String dato3;
							dato1 = cont_act.getString("NGuia");
							dato2 = cont_act.getString("FechaVenta");
							dato3 = cont_act.getString("NAnimales");
							NumGuia.add(dato1);
							FVenta.add(dato2);
							NumAnimales.add(dato3);
						}

						result.put("NumGuia", NumGuia);
						result.put("FVenta", FVenta);
						result.put("NumAnimales", NumAnimales);

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

		System.out.println("FIN sacar ventas de crías de la explotación");
		return responseEntity;

	}

	@RequestMapping(value = "/nuevaventacria", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> NuevaVentaCria(@RequestBody Animales cria) {

		System.out.println("INICIO añadir un nueva venta de cría");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = cria.getNumExplotacion();
		String RefGuia = cria.getRefGuiaCria();
		String Destino = cria.getProcDestino();
		Integer NumAnimales = cria.getNumAnimalesCria();
		Date FechaVenta = cria.getFechaVentaCria();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(FechaVenta);
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
						System.out.println("INSERT INTO venta_crias " + "(NGuia, NAnimales, FechaVenta, NumExplotacion)"
								+ " VALUES ('" + RefGuia + "','" + NumAnimales + "','" +date+ "','"
								+ NumExplotacion + "')");

						int resultado = s.executeUpdate("INSERT INTO venta_crias "
								+ "(NGuia, NAnimales, FechaVenta, NumExplotacion)" + " VALUES ('" + RefGuia + "','"
								+ NumAnimales + "','" + date + "','" + NumExplotacion + "')");

						/*System.out.println(
								"SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						ResultSet numHojas = s.executeQuery(
								"SELECT COUNT(NumHoja) AS NumHojas FROM Altas_Bajas_Animales WHERE NumExplotacion='"
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

						System.out.println(
								"SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						ResultSet antTotal = s.executeQuery(
								"SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						ArrayList<String> AnimalesHojAnt = new ArrayList<>();
						//String totan = null;
						while (antTotal.next()) {
							String dato;
							dato = antTotal.getString("AnimalesHojaAnt");
							AnimalesHojAnt.add(dato);
						}

						int tam = AnimalesHojAnt.size();

						int numhojaS;
						numhojaS = Integer.parseInt(numhojas);

						double NumHojaS = (double) numhojaS / 16;
						int hoja = 0;
						String total;
						total = AnimalesHojAnt.get(tam - 1);
						int total2;
						total2 = Integer.parseInt(total);
						int total_final = total2;
						if (NumHojaS % 1 == 0) {
							hoja = Integer.parseInt(NumHoja) + 1;
						} else {
							hoja = Integer.parseInt(NumHoja);
						}*/

						// Comprobar si se ha insertado correctamente el update.
						if (resultado == 1) {

							// Guardamos los datos en la tabla altas_bajas
							System.out.println(
									"INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
											+ "VALUES ('" + date + "', 'BAJA - SALIDA', '" + Destino + "', '" + RefGuia
											+ "', '" + NumAnimales + " CRIAS', '', '" + NumExplotacion + "')");

							int resultado1 = s.executeUpdate(
									"INSERT INTO `altas_bajas_animales` (`Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) "
											+ "VALUES ('" + date + "', 'BAJA - SALIDA', '" + Destino + "', '" + RefGuia
											+ "', '" + NumAnimales + " CORDEROS', '', '" + NumExplotacion + "')");

							if (resultado1 == 1) {
								System.out.println("Se han modificado los datos correctamente");
								result.put("QueryOk", "correcto");
							} else {
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

		System.out.println("FIN añadir un nueva venta de cria");
		return responseEntity;

	}
	
	@RequestMapping(value = "/filtroGuia", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> filtroGuia(@RequestBody Animales cria) {

		System.out.println("INICIO sacar datos pertenecientes de la venta por número de referencia");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String RefGuiaCria = cria.getRefGuiaCria();
		
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

						System.out.println("SELECT NGuia, NAnimales, FechaVenta FROM venta_crias WHERE NGuia='"
										+ RefGuiaCria + "'");
						
						ResultSet datosoveja = s.executeQuery("SELECT NGuia, NAnimales, FechaVenta FROM venta_crias WHERE NGuia='"
								+ RefGuiaCria + "'");

						

						// Para obtener los resultados de las ovejas
						if (datosoveja.next()) {
							String NGuia = datosoveja.getString("NGuia");
							String NAnimales = datosoveja.getString("NAnimales");
							String FechaVenta = datosoveja.getString("FechaVenta");


							result.put("NGuia", NGuia);
							result.put("NAnimales", NAnimales);
							result.put("FechaVenta", FechaVenta);
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

		System.out.println("FIN sacar datos pertenecientes de la venta por número de referencia");
		return responseEntity;

	}

	@RequestMapping(value = "/filtroAno", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> filtroAno(@RequestBody Animales cria) {

		System.out.println("INICIO mostrar resultado de venta de crías por filtro de año");

		ResponseEntity<Map<String, ArrayList<String>>> responseEntity = null;
		Map<String, ArrayList<String>> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Date FechaVentaCria = cria.getFechaVentaCria();
		String NumExplotacion = cria.getNumExplotacion();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(FechaVentaCria);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(FechaVentaCria);
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
						// Si se marca la opcion de año de nacimiento
						List<String> datos = new ArrayList<String>();
						
						System.out
						.println("SELECT NGuia, NAnimales, FechaVenta FROM venta_crias WHERE (FechaVenta BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");

						ResultSet datosovejaMuertas = s.executeQuery("SELECT NGuia, NAnimales, FechaVenta FROM venta_crias WHERE (FechaVenta BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND"
								+ " NumExplotacion='"+ NumExplotacion + "'");

						ArrayList<String> NGuia = new ArrayList<>();
						ArrayList<String> NAnimales = new ArrayList<>();
						ArrayList<String> FechaVenta = new ArrayList<>();
						
						while (datosovejaMuertas.next()) {
							String dato;
							String dato2;
							String dato3;
							
							dato = datosovejaMuertas.getString("NGuia");
							dato2 = datosovejaMuertas.getString("NAnimales");
							dato3 = datosovejaMuertas.getString("FechaVenta");
							
							NGuia.add(dato);
							NAnimales.add(dato2);
							FechaVenta.add(dato3);
						}
						
						result.put("NGuia", NGuia);
						result.put("NAnimales", NAnimales);
						result.put("FechaVenta", FechaVenta);
						
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

		System.out.println("FIN mostrar resultado de venta de crías por filtro de año");
		return responseEntity;

	}
}
