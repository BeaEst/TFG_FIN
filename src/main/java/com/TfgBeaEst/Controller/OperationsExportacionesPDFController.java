package com.TfgBeaEst.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.TfgBeaEst.Animales;

@Controller
public class OperationsExportacionesPDFController {

	@RequestMapping(value = "/exportacionAltasyBajas/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionAltasyBajas(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de altas y bajas de animales");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> Fecha = new ArrayList<>();
		ArrayList<String> Motivo = new ArrayList<>();
		ArrayList<String> Procedencia_Destino = new ArrayList<>();
		ArrayList<String> NDocumento = new ArrayList<>();
		ArrayList<String> NAnimales = new ArrayList<>();
		ArrayList<String> BalanceFinal = new ArrayList<>();
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		/**/
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						ResultSet datosexplotaciones = s.executeQuery(
								"SELECT Fecha, Motivo, Procedencia_Destino, NDocumento, NAnimales, BalanceFinal FROM altas_bajas_animales WHERE "
								+ "(Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						System.out.println(
								"SELECT Fecha, Motivo, Procedencia_Destino, NDocumento, NAnimales, BalanceFinal FROM altas_bajas_animales WHERE NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						while (datosexplotaciones.next()) {
							String Fecha_;
							String Motivo_;
							String Procedencia_Destino_;
							String NDocumento_;
							String NAnimales_;
							String BalanceFinal_;

							Fecha_ = datosexplotaciones.getString("Fecha");
							Motivo_ = datosexplotaciones.getString("Motivo");
							Procedencia_Destino_ = datosexplotaciones.getString("Procedencia_Destino");
							NDocumento_ = datosexplotaciones.getString("NDocumento");
							NAnimales_ = datosexplotaciones.getString("NAnimales");
							BalanceFinal_ = datosexplotaciones.getString("BalanceFinal");

							Fecha.add(Fecha_);
							Motivo.add(Motivo_);
							Procedencia_Destino.add(Procedencia_Destino_);
							NDocumento.add(NDocumento_);
							NAnimales.add(NAnimales_);
							BalanceFinal.add(BalanceFinal_);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}
						
						/*System.out.println("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet numHojas = s
								.executeQuery("SELECT NumHoja FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "'");

						while (numHojas.next()) {

							NumHoja = numHojas.getString("NumHoja");
						}
						
						System.out.println("SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
								+ NumExplotacion + "' ORDER BY FECHA ASC");
						
						ResultSet antTotal = s.executeQuery(
								"SELECT Fecha, AnimalesHojaAnt FROM Altas_Bajas_Animales WHERE NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FECHA ASC");

						while (antTotal.next()) {
							String dato;
							dato = antTotal.getString("AnimalesHojaAnt");
							AnimalesHojAnt.add(dato);
						}
						
						tam1 = AnimalesHojAnt.size();*/

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaAltasYBajas.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(142, 460);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(457, 460);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(720, 460);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();

		

		// Rellenar tabla
		// Posiciones y
		int y1 = 387;int y9 = 237;
		int y2 = 367;int y10 = 217;
		int y3 = 350;int y11 = 197;
		int y4 = 330;int y12 = 177;
		int y5 = 310;int y13 = 160;
		int y6 = 290;int y14 = 140;
		int y7 = 273;int y15 = 120;
		int y8 = 253;int y16 = 100;

		// Sacar tamaño
		int tam = Fecha.size();

		int y = 0;

		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 16);
		}
		
		int total;
 		total = doc + 16;
		
		String balance_hoja_anterior = null;
		for (int j = total-17; j > 0; j--) {
			if(BalanceFinal.get(j) != "") {
				balance_hoja_anterior = BalanceFinal.get(j);
				break;
			}
		}
		
		if(balance_hoja_anterior != null) {
			// Campo Balance en la hoja anterior
			contents.beginText();
			contents.newLineAtOffset(305, 442);
			contents.setFont(font, 12);
			contents.showText(""+balance_hoja_anterior+"");
			contents.endText();
		}
		
		
		for (int i = doc; i < total; i++) {

			if(i < tam) {
				if ((i-doc) == 0) {
					y = y1;
				} else if ((i-doc) == 1) {
					y = y2;
				} else if ((i-doc) == 2) {
					y = y3;
				} else if ((i-doc) == 3) {
					y = y4;
				} else if ((i-doc) == 4) {
					y = y5;
				} else if ((i-doc) == 5) {
					y = y6;
				} else if ((i-doc) == 6) {
					y = y7;
				} else if ((i-doc) == 7) {
					y = y8;
				} else if ((i-doc) == 8) {
					y = y9;
				} else if ((i-doc) == 9) {
					y = y10;
				} else if ((i-doc) == 10) {
					y = y11;
				} else if ((i-doc) == 11) {
					y = y12;
				} else if ((i-doc) == 12) {
					y = y13;
				} else if ((i-doc) == 13) {
					y = y14;
				} else if ((i-doc) == 14) {
					y = y15;
				} else if ((i-doc) == 15) {
					y = y16;
				}

			
				contents.beginText();
				contents.newLineAtOffset(50, y);
				contents.setFont(font, 12);
				contents.showText("" + Fecha.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(135, y);
				contents.setFont(font, 12);
				contents.showText("" + Motivo.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(265, y);
				contents.setFont(font, 7);
				contents.showText("" + Procedencia_Destino.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(410, y);
				contents.setFont(font, 12);
				contents.showText("" + NDocumento.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(598, y);
				contents.setFont(font, 12);
				contents.showText("" + NAnimales.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(700, y);
				contents.setFont(font, 12);
				contents.showText("" + BalanceFinal.get(i) + "");
				contents.endText();
			}
			
		}

		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaAltasYBajas2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN  creación del archivo altas y bajas de animales");
		return result;
	}

	@RequestMapping(value = "/exportacionAltasyBajasDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionAltasyBajasDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga altas y bajas de animales");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Altas_Bajas_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaAltasYBajas2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga altas y bajas de animales");
		return result;
	}
	
	@RequestMapping(value = "/exportacionAltasyBajasListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> exportacionAltasyBajasListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar ovejar pertenecientes al usuario");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM Altas_Bajas_Animales "
								+ "WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM Altas_Bajas_Animales "
										+ "WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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
	
	@RequestMapping(value = "/exportacionRegistrosLeche/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionRegistrosLeche(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de registros de leche");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> CodigoCisterna = new ArrayList<>();
		ArrayList<String> FechaEntrega = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		/**/
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						ResultSet datosexplotaciones = s.executeQuery(
								"SELECT FechaEntrega, CodigoCisterna FROM leche WHERE "
								+ "(FechaEntrega BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaEntrega ASC");

						System.out.println(
								"SELECT FechaEntrega, CodigoCisterna FROM leche WHERE "
								+ "(FechaEntrega BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaEntrega ASC");

						while (datosexplotaciones.next()) {
							String FechaEntrega_;
							String CodigoCisterna_;

							FechaEntrega_ = datosexplotaciones.getString("FechaEntrega");
							CodigoCisterna_ = datosexplotaciones.getString("CodigoCisterna");

							FechaEntrega.add(FechaEntrega_);
							CodigoCisterna.add(CodigoCisterna_);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaRegistrosLeche.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(140, 725);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(327, 725);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(520, 725);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();
		
		int tam = FechaEntrega.size();
		
		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 50);
		}
		
		int total;
 		total = doc + 50;
 		
 		// Rellenar tabla
 		// Posiciones y
 			int y1 = 675;int y14 = 375;
 			int y2 = 652;int y15 = 352;
 			int y3 = 630;int y16 = 328;
 			int y4 = 605;int y17 = 306;
 			int y5 = 582;int y18 = 280;
 			int y6 = 560;int y19 = 258;
 			int y7 = 537;int y20 = 235;
 			int y8 = 512;int y21 = 210;
 			int y9 = 492;int y22 = 188;
 			int y10 = 467;int y23 = 165;
 			int y11 = 445;int y24 = 142;
 			int y12 = 422;int y25 = 118;
 			int y13 = 398;
 			
 		int y = 0;
 		
 		int j=0;
		for (int i = doc; i < total; i++) {

			if(i < tam) {
				
				if(j < 25) {
					if (j == 0) {
						y = y1;
					} else if (j == 1) {
						y = y2;
					} else if (j == 2) {
						y = y3;
					} else if (j == 3) {
						y = y4;
					} else if (j == 4) {
						y = y5;
					} else if (j == 5) {
						y = y6;
					} else if (j == 6) {
						y = y7;
					} else if (j == 7) {
						y = y8;
					} else if (j == 8) {
						y = y9;
					} else if (j == 9) {
						y = y10;
					} else if (j == 10) {
						y = y11;
					} else if (j == 11) {
						y = y12;
					} else if (j == 12) {
						y = y13;
					} else if (j == 13) {
						y = y14;
					} else if (j == 14) {
						y = y15;
					} else if (j == 15) {
						y = y16;
					} else if (j == 16) {
						y = y17;
					} else if (j == 17) {
						y = y18;
					} else if (j == 18) {
						y = y19;
					} else if (j == 19) {
						y = y20;
					} else if (j == 20) {
						y = y21;
					} else if (j == 21) {
						y = y22;
					} else if (j == 22) {
						y = y23;
					} else if (j == 23) {
						y = y24;
					} else if (j == 24) {
						y = y25;
					}
					
					contents.beginText();
					contents.newLineAtOffset(45, y);
					contents.setFont(font, 12);
					contents.showText(""+FechaEntrega.get(i)+"");
					contents.endText();
					
					contents.beginText();
					contents.newLineAtOffset(160, y);
					contents.setFont(font, 12);
					contents.showText(""+CodigoCisterna.get(i)+"");
					contents.endText();
					
				}else if(j < 50) {
					if (j == 25) {
						y = y1;
					} else if (j == 26) {
						y = y2;
					} else if (j == 27) {
						y = y3;
					} else if (j == 28) {
						y = y4;
					} else if (j == 29) {
						y = y5;
					} else if (j == 30) {
						y = y6;
					} else if (j == 31) {
						y = y7;
					} else if (j == 32) {
						y = y8;
					} else if (j == 33) {
						y = y9;
					} else if (j == 34) {
						y = y10;
					} else if (j == 35) {
						y = y11;
					} else if (j == 36) {
						y = y12;
					} else if (j == 37) {
						y = y13;
					} else if (j == 38) {
						y = y14;
					} else if (j == 39) {
						y = y15;
					} else if (j == 40) {
						y = y16;
					} else if (j == 41) {
						y = y17;
					} else if (j == 42) {
						y = y18;
					} else if (j == 43) {
						y = y19;
					} else if (j == 44) {
						y = y20;
					} else if (j == 45) {
						y = y21;
					} else if (j == 46) {
						y = y22;
					} else if (j == 47) {
						y = y23;
					} else if (j == 48) {
						y = y24;
					} else if (j == 49) {
						y = y25;
					}
					
					contents.beginText();
					contents.newLineAtOffset(305, y);
					contents.setFont(font, 12);
					contents.showText(""+FechaEntrega.get(i)+"");
					contents.endText();
					
					contents.beginText();
					contents.newLineAtOffset(420, y);
					contents.setFont(font, 12);
					contents.showText(""+CodigoCisterna.get(i)+"");
					contents.endText();
				}
				
				j++;
			}
		}
		
		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaRegistrosLeche2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN creación del archivo de registros de leche");
		return result;
	}

	@RequestMapping(value = "/exportacionRegistroLecheDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionRegistrosLecheDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga registros de leche");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Registros_Leche_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaRegistrosLeche2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga registros de leche");
		return result;
	}
	
	@RequestMapping(value = "/exportacionRegistroLecheListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ExportacionRegistrosLecheListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar registros de leche pertenecientes a la explotación");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM leche "
								+ "WHERE (FechaEntrega BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM leche "
										+ "WHERE (FechaEntrega BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar registros de leche pertenecientes a la explotación");
		return responseEntity;

	}
	
	@RequestMapping(value = "/ExportacionAlimentosSuministrados/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionAlimentosSuministrados(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de registros de leche");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> FechaCompra = new ArrayList<>();
		ArrayList<String> NaturalezaAlimento = new ArrayList<>();
		ArrayList<String> Cantidad = new ArrayList<>();
		ArrayList<String> NDocumento = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		/**/
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						ResultSet datosexplotaciones = s.executeQuery(
								"SELECT FechaCompra, NaturalezaAlimento, Cantidad, NDocumento FROM alimentos_suministrados WHERE "
								+ "(FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaCompra ASC");

						System.out.println(
								"SELECT FechaCompra, NaturalezaAlimento, Cantidad, NDocumento FROM alimentos_suministrados WHERE "
								+ "(FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaCompra ASC");

						while (datosexplotaciones.next()) {
							String FechaCompra_;
							String NaturalezaAlimento_;
							String Cantidad_;
							String NDocumento_;

							FechaCompra_ = datosexplotaciones.getString("FechaCompra");
							NaturalezaAlimento_ = datosexplotaciones.getString("NaturalezaAlimento");
							Cantidad_ = datosexplotaciones.getString("Cantidad");
							NDocumento_ = datosexplotaciones.getString("NDocumento");

							FechaCompra.add(FechaCompra_);
							NaturalezaAlimento.add(NaturalezaAlimento_);
							Cantidad.add(Cantidad_);
							NDocumento.add(NDocumento_);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaAlimentosSuministrados.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(140, 725);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(327, 725);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(520, 725);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();
		
		int tam = FechaCompra.size();
		
		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 25);
		}
		
		int total;
 		total = doc + 25;
 		
 		// Rellenar tabla
 			// Posiciones y
 			int y1 = 670;int y9 = 485;int y17 = 300;
 			int y2 = 647;int y10 = 462;int y18 = 277;
 			int y3 = 625;int y11 = 438;int y19 = 252;
 			int y4 = 600;int y12 = 415;int y20 = 230;
 			int y5 = 577;int y13 = 392;int y21 = 205;
 			int y6 = 555;int y14 = 368;int y22 = 183;
 			int y7 = 532;int y15 = 345;int y23 = 160;
 			int y8 = 508;int y16 = 322;int y24 = 138;
 			
 		int y = 0;
 		
 			for (int i = doc; i < total; i++) {

 				if(i < tam) {
 					if ((i-doc) == 0) {
 						y = y1;
 					} else if ((i-doc) == 1) {
 						y = y2;
 					} else if ((i-doc) == 2) {
 						y = y3;
 					} else if ((i-doc) == 3) {
 						y = y4;
 					} else if ((i-doc) == 4) {
 						y = y5;
 					} else if ((i-doc) == 5) {
 						y = y6;
 					} else if ((i-doc) == 6) {
 						y = y7;
 					} else if ((i-doc) == 7) {
 						y = y8;
 					} else if ((i-doc) == 8) {
 						y = y9;
 					} else if ((i-doc) == 9) {
 						y = y10;
 					} else if ((i-doc) == 10) {
 						y = y11;
 					} else if ((i-doc) == 11) {
 						y = y12;
 					} else if ((i-doc) == 12) {
 						y = y13;
 					} else if ((i-doc) == 13) {
 						y = y14;
 					} else if ((i-doc) == 14) {
 						y = y15;
 					} else if ((i-doc) == 15) {
 						y = y16;
 					} else if ((i-doc) == 16) {
 						y = y17;
 					} else if ((i-doc) == 17) {
 						y = y18;
 					} else if ((i-doc) == 18) {
 						y = y19;
 					} else if ((i-doc) == 19) {
 						y = y20;
 					} else if ((i-doc) == 20) {
 						y = y21;
 					} else if ((i-doc) == 21) {
 						y = y22;
 					} else if ((i-doc) == 22) {
 						y = y23;
 					} else if ((i-doc) == 23) {
 						y = y24;
 					}

 				
 					contents.beginText();
 					contents.newLineAtOffset(50, y);
 					contents.setFont(font, 12);
 					contents.showText("" + FechaCompra.get(i) + "");
 					contents.endText();

 					contents.beginText();
 					contents.newLineAtOffset(170, y);
 					contents.setFont(font, 12);
 					contents.showText("" + NaturalezaAlimento.get(i) + "");
 					contents.endText();

 					contents.beginText();
 					contents.newLineAtOffset(340, y);
 					contents.setFont(font, 12);
 					contents.showText("" + Cantidad.get(i) + "");
 					contents.endText();

 					contents.beginText();
 					contents.newLineAtOffset(430, y);
 					contents.setFont(font, 12);
 					contents.showText("" + NDocumento.get(i) + "");
 					contents.endText();
 				}
 				
 			}
		
		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaAlimentosSuministrados2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN creación del archivo de registros de leche");
		return result;
	}

	@RequestMapping(value = "/ExportacionAlimentosSuministradosDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionAlimentosSuministradosDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga registros de leche");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Alimentos_Suministrados_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaAlimentosSuministrados2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga registros de leche");
		return result;
	}
	
	@RequestMapping(value = "/ExportacionAlimentosSuministradosListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ExportacionAlimentosSuministradosListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar registros de leche pertenecientes a la explotación");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM alimentos_suministrados "
								+ "WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM alimentos_suministrados "
										+ "WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar registros de leche pertenecientes a la explotación");
		return responseEntity;

	}
	
	@RequestMapping(value = "/ExportacionPiensosMedicamentosos/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionPiensosMedicamentosos(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de piensos medicamentosos");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> FechaCompra = new ArrayList<>();
		ArrayList<String> PiensoMedicamentoso = new ArrayList<>();
		ArrayList<String> CodigoReceta = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						ResultSet datosexplotaciones = s.executeQuery(
								"SELECT FechaCompra, CodigoReceta, PiensoMedicamentoso FROM piensos_medicamentosos WHERE "
								+ "(FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaCompra ASC");

						System.out.println(
								"SELECT FechaCompra, CodigoReceta, PiensoMedicamentoso FROM alimentos_suministrados WHERE "
								+ "(FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaCompra ASC");

						while (datosexplotaciones.next()) {
							String FechaCompra_;
							String CodigoReceta_;
							String PiensoMedicamentoso_;

							FechaCompra_ = datosexplotaciones.getString("FechaCompra");
							CodigoReceta_ = datosexplotaciones.getString("CodigoReceta");
							PiensoMedicamentoso_ = datosexplotaciones.getString("PiensoMedicamentoso");
							
							FechaCompra.add(FechaCompra_);
							CodigoReceta.add(CodigoReceta_);
							PiensoMedicamentoso.add(PiensoMedicamentoso_);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaPiensosMedicamentosos.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(140, 707);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(327, 707);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(520, 707);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();
		
		int tam = FechaCompra.size();
		
		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 25);
		}
		
		int total;
 		total = doc + 25;
 		
 		// Rellenar tabla
 			// Posiciones y
 			int y1 = 660;int y9 = 475;int y17 = 290;
 			int y2 = 637;int y10 = 452;int y18 = 267;
 			int y3 = 615;int y11 = 428;int y19 = 242;
 			int y4 = 590;int y12 = 405;int y20 = 220;
 			int y5 = 567;int y13 = 382;int y21 = 195;
 			int y6 = 545;int y14 = 358;int y22 = 173;
 			int y7 = 522;int y15 = 335;int y23 = 150;
 			int y8 = 498;int y16 = 312;int y24 = 128;
 			
 		int y = 0;
 		
 			for (int i = doc; i < total; i++) {

 				if(i < tam) {
 					if ((i-doc) == 0) {
 						y = y1;
 					} else if ((i-doc) == 1) {
 						y = y2;
 					} else if ((i-doc) == 2) {
 						y = y3;
 					} else if ((i-doc) == 3) {
 						y = y4;
 					} else if ((i-doc) == 4) {
 						y = y5;
 					} else if ((i-doc) == 5) {
 						y = y6;
 					} else if ((i-doc) == 6) {
 						y = y7;
 					} else if ((i-doc) == 7) {
 						y = y8;
 					} else if ((i-doc) == 8) {
 						y = y9;
 					} else if ((i-doc) == 9) {
 						y = y10;
 					} else if ((i-doc) == 10) {
 						y = y11;
 					} else if ((i-doc) == 11) {
 						y = y12;
 					} else if ((i-doc) == 12) {
 						y = y13;
 					} else if ((i-doc) == 13) {
 						y = y14;
 					} else if ((i-doc) == 14) {
 						y = y15;
 					} else if ((i-doc) == 15) {
 						y = y16;
 					} else if ((i-doc) == 16) {
 						y = y17;
 					} else if ((i-doc) == 17) {
 						y = y18;
 					} else if ((i-doc) == 18) {
 						y = y19;
 					} else if ((i-doc) == 19) {
 						y = y20;
 					} else if ((i-doc) == 20) {
 						y = y21;
 					} else if ((i-doc) == 21) {
 						y = y22;
 					} else if ((i-doc) == 22) {
 						y = y23;
 					} else if ((i-doc) == 23) {
 						y = y24;
 					}

 				
 					contents.beginText();
 					contents.newLineAtOffset(50, y);
 					contents.setFont(font, 12);
 					contents.showText("" + FechaCompra.get(i) + "");
 					contents.endText();

 					contents.beginText();
 					contents.newLineAtOffset(123, y);
 					contents.setFont(font, 12);
 					contents.showText("" + CodigoReceta.get(i) + "");
 					contents.endText();

 					contents.beginText();
 					contents.newLineAtOffset(305, y);
 					contents.setFont(font, 12);
 					contents.showText("" + PiensoMedicamentoso.get(i) + "");
 					contents.endText();
 				}
 				
 			}
		
		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaPiensosMedicamentosos2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN creación del archivo de registros de leche");
		return result;
	}

	@RequestMapping(value = "/ExportacionPiensosMedicamentososDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionPiensosMedicamentososDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga registros de leche");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Piensos_Medicamentosos_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaPiensosMedicamentosos2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga registros de leche");
		return result;
	}
	
	@RequestMapping(value = "/ExportacionPiensosMedicamentososListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ExportacionPiensosMedicamentososListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar registros de piensos medicamentosos pertenecientes a la explotación");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM piensos_medicamentosos "
								+ "WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM piensos_medicamentosos "
										+ "WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar registros de piensos medicamentosos pertenecientes a la explotación");
		return responseEntity;

	}
	
	@RequestMapping(value = "/ExportacionMedicamentos/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionMedicamentos(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de medicamentos");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> FechaCompra = new ArrayList<>();
		ArrayList<String> Medicamento = new ArrayList<>();
		ArrayList<String> CodigoReceta = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						ResultSet datosexplotaciones = s.executeQuery(
								"SELECT FechaCompra, CodigoReceta, Medicamento FROM medicamentos WHERE "
								+ "(FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaCompra ASC");

						System.out.println(
								"SELECT FechaCompra, CodigoReceta, Medicamento FROM medicamentos WHERE "
								+ "(FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaCompra ASC");

						while (datosexplotaciones.next()) {
							String FechaCompra_;
							String CodigoReceta_;
							String Medicamento_;

							FechaCompra_ = datosexplotaciones.getString("FechaCompra");
							CodigoReceta_ = datosexplotaciones.getString("CodigoReceta");
							Medicamento_ = datosexplotaciones.getString("Medicamento");
							
							FechaCompra.add(FechaCompra_);
							CodigoReceta.add(CodigoReceta_);
							Medicamento.add(Medicamento_);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaMedicamentos.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(140, 707);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(327, 707);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(520, 707);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();
		
		int tam = FechaCompra.size();
		
		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 25);
		}
		
		int total;
 		total = doc + 25;
 		
 		// Rellenar tabla
 			// Posiciones y
 			int y1 = 653;int y9 = 470;int y17 = 285;
 			int y2 = 630;int y10 = 445;int y18 = 260;
 			int y3 = 605;int y11 = 420;int y19 = 235;
 			int y4 = 583;int y12 = 400;int y20 = 215;
 			int y5 = 560;int y13 = 375;int y21 = 190;
 			int y6 = 537;int y14 = 350;int y22 = 165;
 			int y7 = 515;int y15 = 330;int y23 = 145;
 			int y8 = 490;int y16 = 305;int y24 = 115;
 			
 		int y = 0;
 		
 			for (int i = doc; i < total; i++) {

 				if(i < tam) {
 					if ((i-doc) == 0) {
 						y = y1;
 					} else if ((i-doc) == 1) {
 						y = y2;
 					} else if ((i-doc) == 2) {
 						y = y3;
 					} else if ((i-doc) == 3) {
 						y = y4;
 					} else if ((i-doc) == 4) {
 						y = y5;
 					} else if ((i-doc) == 5) {
 						y = y6;
 					} else if ((i-doc) == 6) {
 						y = y7;
 					} else if ((i-doc) == 7) {
 						y = y8;
 					} else if ((i-doc) == 8) {
 						y = y9;
 					} else if ((i-doc) == 9) {
 						y = y10;
 					} else if ((i-doc) == 10) {
 						y = y11;
 					} else if ((i-doc) == 11) {
 						y = y12;
 					} else if ((i-doc) == 12) {
 						y = y13;
 					} else if ((i-doc) == 13) {
 						y = y14;
 					} else if ((i-doc) == 14) {
 						y = y15;
 					} else if ((i-doc) == 15) {
 						y = y16;
 					} else if ((i-doc) == 16) {
 						y = y17;
 					} else if ((i-doc) == 17) {
 						y = y18;
 					} else if ((i-doc) == 18) {
 						y = y19;
 					} else if ((i-doc) == 19) {
 						y = y20;
 					} else if ((i-doc) == 20) {
 						y = y21;
 					} else if ((i-doc) == 21) {
 						y = y22;
 					} else if ((i-doc) == 22) {
 						y = y23;
 					} else if ((i-doc) == 23) {
 						y = y24;
 					}

 				
 					contents.beginText();
 					contents.newLineAtOffset(50, y);
 					contents.setFont(font, 12);
 					contents.showText("" + FechaCompra.get(i) + "");
 					contents.endText();

 					contents.beginText();
 					contents.newLineAtOffset(125, y);
 					contents.setFont(font, 12);
 					contents.showText("" + CodigoReceta.get(i) + "");
 					contents.endText();

 					contents.beginText();
 					contents.newLineAtOffset(307, y);
 					contents.setFont(font, 12);
 					contents.showText("" + Medicamento.get(i) + "");
 					contents.endText();
 				}
 				
 			}
		
		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaMedicamentos2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN creación del archivo de registros de medicamentos");
		return result;
	}

	@RequestMapping(value = "/ExportacionMedicamentosDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionMedicamentosDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga registros de medicamentos");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Medicamentos_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaMedicamentos2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga registros de medicamentos");
		return result;
	}
	
	@RequestMapping(value = "/ExportacionMedicamentosListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> ExportacionMedicamentosListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar registros de medicamentos pertenecientes a la explotación");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM medicamentos "
								+ "WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM medicamentos "
										+ "WHERE (FechaCompra BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar registros de medicamentos pertenecientes a la explotación");
		return responseEntity;

	}
	
	@RequestMapping(value = "/exportacionEnfermedades/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionEnfermedades(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de enfermedades");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> FechaAparicion = new ArrayList<>();
		ArrayList<String> Diagnostico = new ArrayList<>();
		ArrayList<String> DocAsociado = new ArrayList<>();
		ArrayList<String> NAnimales = new ArrayList<>();
		ArrayList<String> MedidasAdoptadas = new ArrayList<>();
		ArrayList<String> FechaDesaparicion = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		/**/
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						ResultSet datosexplotaciones = s.executeQuery(
								"SELECT FechaAparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, FechaDesaparicion FROM enfermedades WHERE "
								+ "(FechaAparicion BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaAparicion ASC");

						System.out.println(
								"SELECT FechaAparicion, Diagnostico, DocAsociado, NAnimales, MedidasAdoptadas, FechaDesaparicion FROM enfermedades WHERE "
								+ "(FechaAparicion BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaAparicion ASC");

						while (datosexplotaciones.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							String dato5;
							String dato6;
							
							dato1 = datosexplotaciones.getString("FechaAparicion");
							dato2 = datosexplotaciones.getString("Diagnostico");
							dato3 = datosexplotaciones.getString("DocAsociado");
							dato4 = datosexplotaciones.getString("NAnimales");
							dato5 = datosexplotaciones.getString("MedidasAdoptadas");
							dato6 = datosexplotaciones.getString("FechaDesaparicion");
							
							FechaAparicion.add(dato1);
							Diagnostico.add(dato2);
							DocAsociado.add(dato3);
							NAnimales.add(dato4);
							MedidasAdoptadas.add(dato5);
							FechaDesaparicion.add(dato6);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaEnfermedades.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(142, 460);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(457, 460);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(720, 460);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();

		

		// Rellenar tabla
		// Posiciones y
		int y1 = 389;int y9 = 208;
		int y2 = 367;int y10 = 183;
		int y3 = 345;int y11 = 159;
		int y4 = 320;int y12 = 137;
		int y5 = 297;
		int y6 = 275;
		int y7 = 252;
		int y8 = 230;

		// Sacar tamaño
		int tam = FechaAparicion.size();

		int y = 0;

		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 12);
		}
		
		int total;
 		total = doc + 12;
		
		for (int i = doc; i < total; i++) {

			if(i < tam) {
				if ((i-doc) == 0) {
					y = y1;
				} else if ((i-doc) == 1) {
					y = y2;
				} else if ((i-doc) == 2) {
					y = y3;
				} else if ((i-doc) == 3) {
					y = y4;
				} else if ((i-doc) == 4) {
					y = y5;
				} else if ((i-doc) == 5) {
					y = y6;
				} else if ((i-doc) == 6) {
					y = y7;
				} else if ((i-doc) == 7) {
					y = y8;
				} else if ((i-doc) == 8) {
					y = y9;
				} else if ((i-doc) == 9) {
					y = y10;
				} else if ((i-doc) == 10) {
					y = y11;
				} else if ((i-doc) == 11) {
					y = y12;
				} 

			
				contents.beginText();
				contents.newLineAtOffset(50, y);
				contents.setFont(font, 12);
				contents.showText("" + FechaAparicion.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(120, y);
				contents.setFont(font, 12);
				contents.showText("" + Diagnostico.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(365, y);
				contents.setFont(font, 12);
				contents.showText("" + NAnimales.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(460, y);
				contents.setFont(font, 12);
				contents.showText("" + MedidasAdoptadas.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(723, y);
				contents.setFont(font, 12);
				contents.showText("" + FechaDesaparicion.get(i) + "");
				contents.endText();
			}
			
		}

		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaEnfermedades2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN  creación del archivo de enfermedades");
		return result;
	}

	@RequestMapping(value = "/exportacionEnfermedadesDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionEnfermedadesDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga enfermedades");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Enfermedades_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaEnfermedades2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga enfermedades");
		return result;
	}
	
	@RequestMapping(value = "/exportacionEnfermedadesListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> exportacionEnfermedadesListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar número de archivos de enfermedades");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM enfermedades "
								+ "WHERE (FechaAparicion BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM enfermedades "
										+ "WHERE (FechaAparicion BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar número de archivos de enfermedades");
		return responseEntity;

	}
	
	@RequestMapping(value = "/exportacionPastos/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionPastos(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de pastos");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> FechaInicio = new ArrayList<>();
		ArrayList<String> FechaFin = new ArrayList<>();
		ArrayList<String> CodigoPasto = new ArrayList<>();
		ArrayList<String> NAnimales = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		/**/
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						ResultSet cont_act = s.executeQuery(
								"SELECT FechaInicio, FechaFin, CodigoPasto, NAnimales FROM pastos WHERE "
								+ "(FechaInicio BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaInicio ASC");

						System.out.println(
								"SELECT FechaInicio, FechaFin, CodigoPasto, NAnimales FROM pastos WHERE "
								+ "(FechaInicio BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "' ORDER BY FechaInicio ASC");

						while (cont_act.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							
							dato1 = cont_act.getString("CodigoPasto");
							dato2 = cont_act.getString("FechaInicio");
							dato3 = cont_act.getString("FechaFin");
							dato4 = cont_act.getString("NAnimales");
							
							CodigoPasto.add(dato1);
							FechaInicio.add(dato2);
							FechaFin.add(dato3);
							NAnimales.add(dato4);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaPastos.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(140, 725);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(327, 725);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(520, 725);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();

		

		// Rellenar tabla
		// Posiciones y
			int y1 = 670;int y14 = 370;
			int y2 = 649;int y15 = 348;
			int y3 = 627;int y16 = 324;
			int y4 = 600;int y17 = 301;
			int y5 = 580;int y18 = 275;
			int y6 = 557;int y19 = 254;
			int y7 = 535;int y20 = 230;
			int y8 = 509;int y21 = 205;
			int y9 = 490;int y22 = 184;
			int y10 = 464;int y23 = 160;
			int y11 = 440;int y24 = 138;
			int y12 = 418;int y25 = 114;
			int y13 = 395;

		// Sacar tamaño
		int tam = FechaInicio.size();

		int y = 0;

		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 25);
		}
		
		int total;
 		total = doc + 25;
		
		for (int i = doc; i < total; i++) {

			if(i < tam) {
				if ((i-doc) == 0) {
						y = y1;
					} else if ((i-doc) == 1) {
						y = y2;
					} else if ((i-doc) == 2) {
						y = y3;
					} else if ((i-doc) == 3) {
						y = y4;
					} else if ((i-doc) == 4) {
						y = y5;
					} else if ((i-doc) == 5) {
						y = y6;
					} else if ((i-doc) == 6) {
						y = y7;
					} else if ((i-doc) == 7) {
						y = y8;
					} else if ((i-doc) == 8) {
						y = y9;
					} else if ((i-doc) == 9) {
						y = y10;
					} else if ((i-doc) == 10) {
						y = y11;
					} else if ((i-doc) == 11) {
						y = y12;
					} else if ((i-doc) == 12) {
						y = y13;
					} else if ((i-doc) == 13) {
						y = y14;
					} else if ((i-doc) == 14) {
						y = y15;
					} else if ((i-doc) == 15) {
						y = y16;
					} else if ((i-doc) == 16) {
						y = y17;
					} else if ((i-doc) == 17) {
						y = y18;
					} else if ((i-doc) == 18) {
						y = y19;
					} else if ((i-doc) == 19) {
						y = y20;
					} else if ((i-doc) == 20) {
						y = y21;
					} else if ((i-doc) == 21) {
						y = y22;
					} else if ((i-doc) == 22) {
						y = y23;
					} else if ((i-doc) == 23) {
						y = y24;
					} else if ((i-doc) == 24) {
						y = y25;
					}

			
				contents.beginText();
				contents.newLineAtOffset(50, y);
				contents.setFont(font, 12);
				contents.showText("" + CodigoPasto.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(205, y);
				contents.setFont(font, 12);
				contents.showText("" + FechaInicio.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(305, y);
				contents.setFont(font, 12);
				contents.showText("" + FechaFin.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(460, y);
				contents.setFont(font, 12);
				contents.showText("" + NAnimales.get(i) + "");
				contents.endText();
			}
			
		}

		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaPastos2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN  creación del archivo de pastos");
		return result;
	}

	@RequestMapping(value = "/exportacionPastosDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionPastosDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga pastos");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Pastos_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaPastos2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga pastos");
		return result;
	}
	
	@RequestMapping(value = "/exportacionPastosListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> exportacionPastosListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar número de archivos de pastos");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM pastos "
								+ "WHERE (FechaInicio BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM pastos "
										+ "WHERE (FechaInicio BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar número de archivos de pastos");
		return responseEntity;

	}
	
	@RequestMapping(value = "/exportacionInspecciones/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> Exportacioninspecciones(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de inspecciones");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		ArrayList<String> Fecha = new ArrayList<>();
		ArrayList<String> Oficial = new ArrayList<>();
		ArrayList<String> TipoActuacion = new ArrayList<>();
		ArrayList<String> NumActa = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		/**/
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						System.out.println("SELECT Fecha, Oficial, TipoActuacion, NumActa FROM inspecciones WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY Fecha");

						ResultSet cont_act = s.executeQuery("SELECT Fecha, Oficial, TipoActuacion, NumActa FROM inspecciones WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY Fecha");

						

						while (cont_act.next()) {
							String dato1;
							String dato2;
							String dato3;
							String dato4;
							
							dato1 = cont_act.getString("Fecha");
							dato2 = cont_act.getString("Oficial");
							dato3 = cont_act.getString("TipoActuacion");
							dato4 = cont_act.getString("NumActa");
							
							Fecha.add(dato1);
							Oficial.add(dato2);
							TipoActuacion.add(dato3);
							NumActa.add(dato4);
						}

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaInspecciones.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(142, 460);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(457, 460);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(720, 460);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();

		

		// Rellenar tabla
		// Posiciones y
		int y1 = 400;int y9 = 213;
		int y2 = 372;int y10 = 187;
		int y3 = 350;int y11 = 164;
		int y4 = 325;int y12 = 143;
		int y5 = 303;
		int y6 = 280;
		int y7 = 257;
		int y8 = 235;

		// Sacar tamaño
		int tam = Fecha.size();

		int y = 0;

		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 12);
		}
		
		int total;
 		total = doc + 12;
		
		for (int i = doc; i < total; i++) {

			if(i < tam) {
				if ((i-doc) == 0) {
					y = y1;
				} else if ((i-doc) == 1) {
					y = y2;
				} else if ((i-doc) == 2) {
					y = y3;
				} else if ((i-doc) == 3) {
					y = y4;
				} else if ((i-doc) == 4) {
					y = y5;
				} else if ((i-doc) == 5) {
					y = y6;
				} else if ((i-doc) == 6) {
					y = y7;
				} else if ((i-doc) == 7) {
					y = y8;
				} else if ((i-doc) == 8) {
					y = y9;
				} else if ((i-doc) == 9) {
					y = y10;
				} else if ((i-doc) == 10) {
					y = y11;
				} else if ((i-doc) == 11) {
					y = y12;
				} 

			
				contents.beginText();
				contents.newLineAtOffset(50, y);
				contents.setFont(font, 12);
				contents.showText("" + Fecha.get(i) + "");
				contents.endText();

				if(Oficial.get(i) == "1") {
					contents.beginText();
					contents.newLineAtOffset(130, y);
					contents.setFont(font, 12);
					contents.showText("Si");
					contents.endText();
				}else {
					contents.beginText();
					contents.newLineAtOffset(130, y);
					contents.setFont(font, 12);
					contents.showText("No");
					contents.endText();
				}
				

				contents.beginText();
				contents.newLineAtOffset(170, y);
				contents.setFont(font, 12);
				contents.showText("" + TipoActuacion.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(635, y);
				contents.setFont(font, 12);
				contents.showText("" + NumActa.get(i) + "");
				contents.endText();
			}
			
		}

		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaInspecciones2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN  creación del archivo de inspecciones");
		return result;
	}

	@RequestMapping(value = "/exportacionInspeccionesDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacioninspeccionesDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga inspecciones");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Inspecciones_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaInspecciones2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga inspecciones");
		return result;
	}
	
	@RequestMapping(value = "/exportacionInspeccionesListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> exportacioninspeccionessListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar número de archivos de inspecciones");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM inspecciones "
								+ "WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM inspecciones "
										+ "WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar número de archivos de inspecciones");
		return responseEntity;

	}
	
	@RequestMapping(value = "/exportacionIncidencias/{NumExplotacion}/{num}/{Busqueda}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionIncidencias(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String NumExplotacion, @PathVariable int num, @PathVariable String Busqueda) throws ServletException, IOException {
		System.out.println("INICIO creación del archivo de inspecciones");

		// Sacar los datos de altas y bajas de la explotacion
		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;
		
		ArrayList<String> Fecha = new ArrayList<>();
		ArrayList<String> Descripcion = new ArrayList<>();
		ArrayList<String> NAnimales = new ArrayList<>();
		ArrayList<String> CodIdentAnt = new ArrayList<>();
		ArrayList<String> CodIdentNew = new ArrayList<>();
		ArrayList<String> NDocumento = new ArrayList<>();
		
		String tipoAnimal = null;
		String NumHoja = null;
		ArrayList<String> AnimalesHojAnt = new ArrayList<>();
		int tam1 = 0;
		
		/**/
		DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
		Date convertido = null;
		try {
			convertido = fechaHora.parse(Busqueda);
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertido);
		calendar.add(calendar.YEAR, 1);
		Date Busqueda2 = calendar.getTime();
		String strDate = fechaHora.format(convertido);
		String strDate2 = fechaHora.format(Busqueda2);
		
		// Cargar el driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tfg_v1", "root", "");

				Statement s = null;
				try {
					s = conexion.createStatement();

					try {
						// Seleccionar todos los registros
						System.out.println("SELECT Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento FROM incidencias WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY Fecha");

						
						ResultSet cont_act = s.executeQuery("SELECT Fecha, Descripcion, NAnimales, CodIdentAnt, CodIdentNew, NDocumento FROM incidencias WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "' ORDER BY Fecha");

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

						// Seleccionar el tipo de animal
						ResultSet datostipoanimal = s.executeQuery(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						System.out.println(
								"SELECT TipoAnimal FROM explotaciones WHERE NumExplotacion='" + NumExplotacion + "'");

						while (datostipoanimal.next()) {
							tipoAnimal = datostipoanimal.getString("TipoAnimal");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("ERROR al hacer las consultas SQL");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					System.out.println("ERROR al crear el estamento de la consulta sql");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("ERROR al hacer la conexión a la base de datos");
			}

		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.out.println("ERROR al cargar el driver de sql");
		}
		// Fin sacar los datos de altas y bajas de la explotacion

		// Creación del archivo
		File resourcesDirectory = new File("./src/main/resources/static/HojaIncidencias.pdf");

		PDDocument pd = PDDocument.load(resourcesDirectory);
		PDPage pg = pd.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(pd, pg, AppendMode.PREPEND, false);
		PDFont font = PDType1Font.HELVETICA;

		// Campo código de explotacion
		contents.beginText();
		contents.newLineAtOffset(142, 460);
		contents.setFont(font, 12);
		contents.showText("" + NumExplotacion + "");
		contents.endText();

		// Campo Especie
		contents.beginText();
		contents.newLineAtOffset(457, 460);
		contents.setFont(font, 12);
		contents.showText("" + tipoAnimal + "");
		contents.endText();

		// Campo Número de hoja
		contents.beginText();
		contents.newLineAtOffset(720, 460);
		contents.setFont(font, 12);
		contents.showText(""+(num+1)+"");
		contents.endText();

		

		// Rellenar tabla
		// Posiciones y
		int y1 = 390;int y9 = 204;
		int y2 = 364;int y10 = 179;
		int y3 = 343;int y11 = 155;
		int y4 = 318;int y12 = 135;
		int y5 = 295;
		int y6 = 273;
		int y7 = 250;
		int y8 = 227;

		// Sacar tamaño
		int tam = Fecha.size();

		int y = 0;

		int doc;
		if(num == 0) {
			doc = 0;
		}else {
			doc = (num * 12);
		}
		
		int total;
 		total = doc + 12;
		
		for (int i = doc; i < total; i++) {

			if(i < tam) {
				if ((i-doc) == 0) {
					y = y1;
				} else if ((i-doc) == 1) {
					y = y2;
				} else if ((i-doc) == 2) {
					y = y3;
				} else if ((i-doc) == 3) {
					y = y4;
				} else if ((i-doc) == 4) {
					y = y5;
				} else if ((i-doc) == 5) {
					y = y6;
				} else if ((i-doc) == 6) {
					y = y7;
				} else if ((i-doc) == 7) {
					y = y8;
				} else if ((i-doc) == 8) {
					y = y9;
				} else if ((i-doc) == 9) {
					y = y10;
				} else if ((i-doc) == 10) {
					y = y11;
				} else if ((i-doc) == 11) {
					y = y12;
				} 

			
				contents.beginText();
				contents.newLineAtOffset(43, y);
				contents.setFont(font, 12);
				contents.showText("" + Fecha.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(110, y);
				contents.setFont(font, 7);
				contents.showText("" + Descripcion.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(305, y);
				contents.setFont(font, 12);
				contents.showText("" + NAnimales.get(i) + "");
				contents.endText();

				contents.beginText();
				contents.newLineAtOffset(405, y);
				contents.setFont(font, 12);
				contents.showText("" + CodIdentAnt.get(i) + "");
				contents.endText();
				
				contents.beginText();
				contents.newLineAtOffset(565, y);
				contents.setFont(font, 12);
				contents.showText("" + CodIdentNew.get(i) + "");
				contents.endText();
				
				contents.beginText();
				contents.newLineAtOffset(680, y);
				contents.setFont(font, 12);
				contents.showText("" + NDocumento.get(i) + "");
				contents.endText();
			}
			
		}

		contents.close();
		// pd.save ("x.pdf");

		// FileOutputStream fOut = new FileOutputStream();
		pd.save("./src/main/resources/static/HojaIncidencias2.pdf");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		byte[] Archivo = null;

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN  creación del archivo de incidencias");
		return result;
	}

	@RequestMapping(value = "/exportacionIncidenciasDescarga/{i}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> ExportacionIncidenciasDescarga(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int i) throws ServletException, IOException {
		System.out.println("INICIO descarga incidencias");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=Incidencias_" + i + ".pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/HojaIncidencias2.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga incidencias");
		return result;
	}
	
	@RequestMapping(value = "/exportacionIncidenciasListado", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> exportacionIncidenciasListado(@RequestBody Animales animal) {

		System.out.println("INICIO sacar número de archivos de incidencias");

		ResponseEntity<Map<String, String>> responseEntity = null;
		Map<String, String> result = new HashMap<>();

		// Consulta a base de datos para comprobar si existe en la tabla usuarios.
		Connection conexion = null;

		String NumExplotacion = animal.getNumExplotacion();
		Date fecha = animal.getFechaVenta();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(calendar.YEAR, 1);
		Date fechaMuerte2Filtro = calendar.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(fecha);
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

						System.out.println("SELECT COUNT(Id) AS NumHojas FROM incidencias "
								+ "WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
								+ NumExplotacion + "'");
						
						ResultSet recuento = s
								.executeQuery("SELECT COUNT(Id) AS NumHojas FROM incidencias "
										+ "WHERE (Fecha BETWEEN '" + strDate + "' AND '" + strDate2 + "') AND NumExplotacion='"
										+ NumExplotacion + "'");

						String recuento_ = null;
						while (recuento.next()) {
							recuento_ = recuento.getString("NumHojas");
						}

						result.put("recuento", recuento_);

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

		System.out.println("FIN sacar número de archivos de incidencias");
		return responseEntity;

	}
	
	@RequestMapping(value = "/DescargarFormulario", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> DescargarFormulario(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("INICIO descarga formulario registro");

		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "x-download"));
		String date = new SimpleDateFormat().format(new Date());
		header.set("Content-Disposition", "attachment; filename=FormularioRegistro.pdf");

		// Recoger los bytes del archivo
		File file = new File("./src/main/resources/static/LIBRO_DE_REGISTRO.pdf");
		byte[] Archivo = Files.readAllBytes(file.toPath());

		result = new ResponseEntity<>(Archivo, header, HttpStatus.OK);
		System.out.println("FIN descarga formulario registro");
		return result;
	}
}
