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
import com.TfgBeaEst.Incidencias;
import com.TfgBeaEst.Inspecciones;
import com.TfgBeaEst.Usuario;

@Controller
public class OperationsIncidencias {


	@RequestMapping(value = "/incidencias", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> CargarIncidencias(@RequestBody Usuario usuario) {

		System.out.println("INICIO sacar registros de incidencias");

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

						System.out.println("SELECT Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento FROM incidencias WHERE NumExplotacion='"
								+ numexplotacion + "' ORDER BY Fecha");

						ResultSet cont_act = s.executeQuery("SELECT Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento FROM incidencias WHERE NumExplotacion='"
								+ numexplotacion + "' ORDER BY Fecha");

						ArrayList<String> Fecha = new ArrayList<>();
						ArrayList<String> Descripcion = new ArrayList<>();
						ArrayList<String> NAnimales = new ArrayList<>();
						ArrayList<String> CodIdentAnt = new ArrayList<>();
						ArrayList<String> CodIdentNew = new ArrayList<>();
						ArrayList<String> NDocumento = new ArrayList<>();

						while (cont_act.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							
							dato1 = cont_act.getString("Fecha");
							dato2 = cont_act.getString("Descripcion");
							dato3 = cont_act.getString("NAnimales");
							dato4 = cont_act.getString("CodIdentAnt");
							dato3 = cont_act.getString("CodIdentNew");
							dato4 = cont_act.getString("NDocumento");
							
							Fecha.add(dato1);
							Descripcion.add(dato2);
							NAnimales.add(dato3);
							CodIdentAnt.add(dato4);
							CodIdentNew.add(dato3);
							NDocumento.add(dato4);
						}

						result.put("Fecha", Fecha);
						result.put("Descripcion", Descripcion);
						result.put("NAnimales", NAnimales);
						result.put("CodIdentAnt", CodIdentAnt);
						result.put("CodIdentNew", CodIdentNew);
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

		System.out.println("FIN sacar registros de incidencia");
		return responseEntity;

	}
	
	@RequestMapping(value = "/nuevaincidencia", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> NuevaIncidencia(@RequestBody Incidencias registro) {

		System.out.println("INICIO añadir un nuevo registro de incidencia");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String Descripcion = registro.getDescripcion();
		String NDocumento = registro.getNDocumento();
		String CodIdentAnt = registro.getCodIdentAnt();
		String CodIdentNew = registro.getCodIdentNew();
		Integer NAnimales = registro.getNAnimales();
		
		Date Fecha = registro.getFecha();
		String numexplotacion = registro.getNumExplotacion();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(Fecha);
		
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
						System.out.println("INSERT INTO incidencias " + "(Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + Descripcion + "', '"+NAnimales+"', '"+ CodIdentAnt +"','"+ CodIdentNew +"','"+ NDocumento +"','"+ numexplotacion + "')");

						int resultado = s.executeUpdate("INSERT INTO incidencias " + "(Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + Descripcion + "', '"+NAnimales+"', '"+ CodIdentAnt +"','"+ CodIdentNew +"','"+ NDocumento +"','"+ numexplotacion + "')");

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

		System.out.println("FIN añadir un nuevo registro de inspeccion");
		return responseEntity;

	}

	@RequestMapping(value = "/filtroAnIncidencia", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> filtroAnoIncidencias(@RequestBody Animales piensos) {

		System.out.println("INICIO mostrar resultado de registros de incidencias por filtro de año");

		ResponseEntity<Map<String, ArrayList<String>>> responseEntity = null;
		Map<String, ArrayList<String>> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Date FechaVentaCria = piensos.getFechaVentaCria();
		String NumExplotacion = piensos.getNumExplotacion();
		
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
						
						System.out.println("SELECT Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento FROM incidencias WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY Fecha");

						ResultSet cont_act = s.executeQuery("SELECT Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento FROM incidencias WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY Fecha");

						ArrayList<String> Fecha = new ArrayList<>();
						ArrayList<String> Descripcion = new ArrayList<>();
						ArrayList<String> NAnimales = new ArrayList<>();
						ArrayList<String> CodIdentAnt = new ArrayList<>();
						ArrayList<String> CodIdentNew = new ArrayList<>();
						ArrayList<String> NDocumento = new ArrayList<>();

						while (cont_act.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							
							dato1 = cont_act.getString("Fecha");
							dato2 = cont_act.getString("Descripcion");
							dato3 = cont_act.getString("NAnimales");
							dato4 = cont_act.getString("CodIdentAnt");
							dato3 = cont_act.getString("CodIdentNew");
							dato4 = cont_act.getString("NDocumento");
							
							Fecha.add(dato1);
							Descripcion.add(dato2);
							NAnimales.add(dato3);
							CodIdentAnt.add(dato4);
							CodIdentNew.add(dato3);
							NDocumento.add(dato4);
						}

						result.put("Fecha", Fecha);
						result.put("Descripcion", Descripcion);
						result.put("NAnimales", NAnimales);
						result.put("CodIdentAnt", CodIdentAnt);
						result.put("CodIdentNew", CodIdentNew);
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
		
		System.out.println("FIN mostrar resultado de registros de incidencias por filtro de año");
		return responseEntity;
	}
}
