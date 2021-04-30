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
import com.TfgBeaEst.Enfermedades;
import com.TfgBeaEst.Medicamentos;
import com.TfgBeaEst.Usuario;

@Controller
public class OperationsEnfermedades {


	@RequestMapping(value = "/cargarenfermedades", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> CargarMedicamentos(@RequestBody Usuario usuario) {

		System.out.println("INICIO sacar registros de enfermedades");

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

						System.out.println("SELECT FechaAparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, FechaDesaparicion FROM enfermedades WHERE NumExplotacion='"
								+ numexplotacion + "'");

						ResultSet cont_act = s.executeQuery("SELECT FechaAparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, FechaDesaparicion FROM enfermedades WHERE NumExplotacion='"
								+ numexplotacion + "'");

						ArrayList<String> FechaAparicion = new ArrayList<>();
						ArrayList<String> Diagnostico = new ArrayList<>();
						ArrayList<String> DocAsociado = new ArrayList<>();
						ArrayList<String> NAnimales = new ArrayList<>();
						ArrayList<String> MedidasAdoptadas = new ArrayList<>();
						ArrayList<String> FechaDesaparicion = new ArrayList<>();

						while (cont_act.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							String dato5;
							String dato6;
							
							dato1 = cont_act.getString("FechaAparicion");
							dato2 = cont_act.getString("Diagnostico");
							dato3 = cont_act.getString("DocAsociado");
							dato4 = cont_act.getString("NAnimales");
							dato5 = cont_act.getString("MedidasAdoptadas");
							dato6 = cont_act.getString("FechaDesaparicion");
							
							FechaAparicion.add(dato1);
							Diagnostico.add(dato2);
							DocAsociado.add(dato3);
							NAnimales.add(dato4);
							MedidasAdoptadas.add(dato5);
							FechaDesaparicion.add(dato6);
						}

						result.put("FechaAparicion", FechaAparicion);
						result.put("Diagnostico", Diagnostico);
						result.put("DocAsociado", DocAsociado);
						result.put("NAnimales", NAnimales);
						result.put("MedidasAdoptadas", MedidasAdoptadas);
						result.put("FechaDesaparicion", FechaDesaparicion);

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

		System.out.println("FIN sacar registros de enfermedades");
		return responseEntity;

	}
	
	@RequestMapping(value = "/nuevaenfermedad", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> NuevoMedicamento(@RequestBody Enfermedades registro) {

		System.out.println("INICIO añadir un nuevo registro de medicamentos");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		Integer NAnimales = registro.getNAnimales();
		String DocAsociado = registro.getDocAsociado();
		String Diagnostico = registro.getDiagnostico();
		String MedidasAdoptadas = registro.getMedidasAdoptadas();
		Date FechaAparicion = registro.getFechaAparicion();
		Date FechaDesaparicion = registro.getFechaDesaparicion();
		String numexplotacion = registro.getNumExplotacion();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(FechaAparicion);
		String date2 = dateFormat.format(FechaDesaparicion);
		
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
						System.out.println("INSERT INTO enfermedades " + "(FechaAparicion, FechaDesaparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + date2 + "', '"+Diagnostico+"', '"+DocAsociado+"', '"+NAnimales+"', '"+MedidasAdoptadas+"','"+ numexplotacion + "')");

						int resultado = s.executeUpdate("INSERT INTO enfermedades " + "(FechaAparicion, FechaDesaparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, NumExplotacion)"
								+ " VALUES ('" + date + "', '" + date2 + "', '"+Diagnostico+"', '"+DocAsociado+"', '"+NAnimales+"', '"+MedidasAdoptadas+"','"+ numexplotacion + "')");

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

		System.out.println("FIN añadir un nuevo registro de medicamentos");
		return responseEntity;

	}

	@RequestMapping(value = "/filtroAnEnfermedades", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<String>>> filtroAnoEnfermedades(@RequestBody Animales piensos) {

		System.out.println("INICIO mostrar resultado de registros de enfermedades por filtro de año");

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
						
						System.out
						.println("SELECT FechaAparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, FechaDesaparicion FROM enfermedades WHERE (FechaAparicion BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FechaAparicion DESC");

						ResultSet datosovejaMuertas = s.executeQuery("SELECT FechaAparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, FechaDesaparicion FROM enfermedades WHERE (FechaAparicion BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FechaAparicion DESC");
						
						ArrayList<String> FechaAparicion = new ArrayList<>();
						ArrayList<String> Diagnostico = new ArrayList<>();
						ArrayList<String> DocAsociado = new ArrayList<>();
						ArrayList<String> NAnimales = new ArrayList<>();
						ArrayList<String> MedidasAdoptadas = new ArrayList<>();
						ArrayList<String> FechaDesaparicion = new ArrayList<>();

						while (datosovejaMuertas.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							String dato5;
							String dato6;
							
							dato1 = datosovejaMuertas.getString("FechaAparicion");
							dato2 = datosovejaMuertas.getString("Diagnostico");
							dato3 = datosovejaMuertas.getString("DocAsociado");
							dato4 = datosovejaMuertas.getString("NAnimales");
							dato5 = datosovejaMuertas.getString("MedidasAdoptadas");
							dato6 = datosovejaMuertas.getString("FechaDesaparicion");
							
							FechaAparicion.add(dato1);
							Diagnostico.add(dato2);
							DocAsociado.add(dato3);
							NAnimales.add(dato4);
							MedidasAdoptadas.add(dato5);
							FechaDesaparicion.add(dato6);
						}

						result.put("FechaAparicion", FechaAparicion);
						result.put("Diagnostico", Diagnostico);
						result.put("DocAsociado", DocAsociado);
						result.put("NAnimales", NAnimales);
						result.put("MedidasAdoptadas", MedidasAdoptadas);
						result.put("FechaDesaparicion", FechaDesaparicion);
						
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

		System.out.println("FIN mostrar resultado de registros de enfermedades por filtro de año");
		return responseEntity;

	}
}
