package com.TfgBeaEst.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
import com.TfgBeaEst.Leche;
import com.TfgBeaEst.Usuario;

@Controller
public class OperationsLecheController {

	@RequestMapping(value = "/cargarventaleche", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> CargarVentaLeche(@RequestBody Usuario usuario) {

		System.out.println("INICIO sacar ventas de leche de la explotación");

		ResponseEntity<Map<String, ArrayList<String>>> responseEntity = null;
		Map<String, ArrayList<String>> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

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

						System.out.println("SELECT FechaEntrega, CodigoCisterna FROM leche WHERE NumExplotacion='"
								+ numexplotacion + "' ORDER BY FechaEntrega");

						ResultSet cont_act = s.executeQuery(
								"SELECT FechaEntrega, CodigoCisterna FROM leche WHERE NumExplotacion='"
										+ numexplotacion + "' ORDER BY FechaEntrega");

						ArrayList<String> FechaEntrega = new ArrayList<>();
						ArrayList<String> CodigoCisterna = new ArrayList<>();

						while (cont_act.next()) {
							String dato1;
							String dato2;
							dato1 = cont_act.getString("FechaEntrega");
							dato2 = cont_act.getString("CodigoCisterna");
							FechaEntrega.add(dato1);
							CodigoCisterna.add(dato2);
						}

						result.put("FechaEntrega", FechaEntrega);
						result.put("CodigoCisterna", CodigoCisterna);

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

		System.out.println("FIN sacar ventas de leche de la explotación");
		return responseEntity;

	}

	@RequestMapping(value = "/nuevaregistroleche", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> NuevaRegistroLeche(@RequestBody Animales registro) {

		System.out.println("INICIO añadir un nueva venta de cría");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String CodCisterna = registro.getRefGuiaCria();
		Date FechaEntrega = registro.getFechaVentaCria();
		String numexplotacion = registro.getNumExplotacion();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(FechaEntrega);
		
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
						System.out.println("INSERT INTO leche " + "(FechaEntrega, CodigoCisterna, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + CodCisterna + "', '"+ numexplotacion + "')");

						int resultado = s.executeUpdate("INSERT INTO leche " + "(FechaEntrega, CodigoCisterna, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + CodCisterna + "', '"+ numexplotacion + "')");

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

		System.out.println("FIN añadir un nueva venta de cria");
		return responseEntity;

	}

	@RequestMapping(value = "/filtroAnoleche", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> filtroAnoLeche(@RequestBody Animales cria) {

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
						.println("SELECT FechaEntrega, CodigoCisterna FROM leche WHERE (FechaEntrega BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FechaEntrega DESC");

						ResultSet datosovejaMuertas = s.executeQuery("SELECT FechaEntrega, CodigoCisterna FROM leche WHERE (FechaEntrega BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND"
								+ " NumExplotacion='"+ NumExplotacion + "' ORDER BY FechaEntrega DESC");

						ArrayList<String> FechaEntrega = new ArrayList<>();
						ArrayList<String> CodigoCisterna = new ArrayList<>();
						
						while (datosovejaMuertas.next()) {
							String dato;
							String dato2;
							
							dato = datosovejaMuertas.getString("FechaEntrega");
							dato2 = datosovejaMuertas.getString("CodigoCisterna");
							
							FechaEntrega.add(dato);
							CodigoCisterna.add(dato2);
						}
						
						result.put("FechaEntrega", FechaEntrega);
						result.put("CodigoCisterna", CodigoCisterna);
						
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
