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

import com.TfgBeaEst.AlimentosSuministrados;
import com.TfgBeaEst.Animales;
import com.TfgBeaEst.Usuario;

@Controller
public class OperationsAlimentosSuministrados {

	@RequestMapping(value = "/cargaralimentossuministrados", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> CargarVentaCrias(@RequestBody Usuario usuario) {

		System.out.println("INICIO sacar registros de alimentos suministrados");

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

						System.out.println("SELECT FechaCompra, NaturalezaAlimento, Cantidad, NDocumento FROM alimentos_suministrados WHERE NumExplotacion='"
								+ numexplotacion + "' ORDER BY FechaCompra");

						ResultSet cont_act = s.executeQuery("SELECT FechaCompra, NaturalezaAlimento, Cantidad, NDocumento FROM alimentos_suministrados WHERE NumExplotacion='"
								+ numexplotacion + "' ORDER BY FechaCompra");

						ArrayList<String> FechaCompra = new ArrayList<>();
						ArrayList<String> NaturalezaAlimento = new ArrayList<>();
						ArrayList<String> Cantidad = new ArrayList<>();
						ArrayList<String> NDocumento = new ArrayList<>();

						while (cont_act.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							
							dato1 = cont_act.getString("FechaCompra");
							dato2 = cont_act.getString("NaturalezaAlimento");
							dato3 = cont_act.getString("Cantidad");
							dato4 = cont_act.getString("NDocumento");
							
							FechaCompra.add(dato1);
							NaturalezaAlimento.add(dato2);
							Cantidad.add(dato3);
							NDocumento.add(dato4);
						}

						result.put("FechaCompra", FechaCompra);
						result.put("NaturalezaAlimento", NaturalezaAlimento);
						result.put("Cantidad", Cantidad);
						result.put("NDocumento", NDocumento);

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

		System.out.println("FIN sacar registros de alimentos suministrados");
		return responseEntity;

	}
	
	@RequestMapping(value = "/nuevoalimentosuministrado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> NuevoAlimentoSuministrado(@RequestBody AlimentosSuministrados registro) {

		System.out.println("INICIO añadir un nueva venta de cría");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String Naturaleza = registro.getNaturaleza();
		Double Cantidad = registro.getCantidad();
		Date FechaCompra = registro.getFechaCompra();
		String numexplotacion = registro.getNumExplotacion();
		String NDocumento = registro.getNDocumento();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(FechaCompra);
		
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
						System.out.println("INSERT INTO alimentos_suministrados " + "(FechaCompra, Cantidad, NaturalezaAlimento, NDocumento, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + Cantidad + "', '"+Naturaleza+"', '"+NDocumento+"', '"+ numexplotacion + "')");

						int resultado = s.executeUpdate("INSERT INTO alimentos_suministrados " + "(FechaCompra, Cantidad, NaturalezaAlimento, NDocumento, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + Cantidad + "', '"+Naturaleza+"', '"+NDocumento+"', '"+ numexplotacion + "')");

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
	
	@RequestMapping(value = "/filtroAnoAlimentos", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> filtroAnoAlimentos(@RequestBody Animales cria) {

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
						.println("SELECT FechaCompra, NaturalezaAlimento, Cantidad, NDocumento FROM alimentos_suministrados WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FechaCompra DESC");

						ResultSet datosovejaMuertas = s.executeQuery("SELECT FechaCompra, NaturalezaAlimento, Cantidad, NDocumento FROM alimentos_suministrados WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FechaCompra DESC");

						ArrayList<String> FechaCompra = new ArrayList<>();
						ArrayList<String> NaturalezaAlimento = new ArrayList<>();
						ArrayList<String> Cantidad = new ArrayList<>();
						ArrayList<String> NDocumento = new ArrayList<>();
						
						while (datosovejaMuertas.next()) {
							String dato;
							String dato2;
							String dato3;
							String dato4;
							
							dato = datosovejaMuertas.getString("FechaCompra");
							dato2 = datosovejaMuertas.getString("NaturalezaAlimento");
							dato3 = datosovejaMuertas.getString("Cantidad");
							dato4 = datosovejaMuertas.getString("NDocumento");
							
							FechaCompra.add(dato);
							NaturalezaAlimento.add(dato2);
							Cantidad.add(dato3);
							NDocumento.add(dato4);
						}
						
						result.put("FechaCompra", FechaCompra);
						result.put("NaturalezaAlimento", NaturalezaAlimento);
						result.put("Cantidad", Cantidad);
						result.put("NDocumento", NDocumento);
						
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
