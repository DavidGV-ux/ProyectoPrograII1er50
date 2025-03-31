package co.edu.uptc.vista;

import java.sql.Date;
import javax.swing.JOptionPane;
import co.edu.uptc.controlador.ReciclajeControlador;
import co.edu.uptc.modelo.Usuario;

public class ConsolaVista {
	private ReciclajeControlador controlador;
	private static final String[] TIPOS_RESIDUO = { "Plástico", "Vidrio", "Cartón", "Metal" };

	public ConsolaVista(ReciclajeControlador controlador) {
		this.controlador = controlador;
	}

	public void mostrarMenu() {
		while (true) {
			String opcion = JOptionPane.showInputDialog(null,
					"1. Registrar Usuario\n2. Iniciar Sesión\n3. Listar Residuos\n4. Consultar Puntos\n5. Generar Reporte PDF\n6. Salir",
					"Menú", JOptionPane.QUESTION_MESSAGE);
			if (opcion == null || opcion.equals("6"))
				return;
			switch (opcion) {
			case "1":
				registrarUsuario();
				break;
			case "2":
				iniciarSesion();
				break;
			case "3":
				listarResiduos();
				break;
			case "4":
				consultarPuntos();
				break;
			case "5":
				controlador.generarReportePDF();
				JOptionPane.showMessageDialog(null, "Reporte generado: Reporte_Reciclaje.pdf", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			}
		}
	}

	 public void registrarUsuario() {
	        String nombre, id, direccion, correo;
	        do {
	            nombre = JOptionPane.showInputDialog("Nombre:");
	            if (!co.edu.uptc.util.ValidadorEntrada.validarNombre(nombre)) {
	                JOptionPane.showMessageDialog(null, "Nombre inválido. Solo letras y espacios.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } while (!co.edu.uptc.util.ValidadorEntrada.validarNombre(nombre));

	        do {
	            id = JOptionPane.showInputDialog("Identificación:");
	            if (!co.edu.uptc.util.ValidadorEntrada.validarIdentificacion(id)) {
	                JOptionPane.showMessageDialog(null, "Identificación inválida. Solo números.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } while (!co.edu.uptc.util.ValidadorEntrada.validarIdentificacion(id));

	        direccion = JOptionPane.showInputDialog("Dirección:");

	        do {
	            correo = JOptionPane.showInputDialog("Correo:");
	            if (!co.edu.uptc.util.ValidadorEntrada.validarCorreo(correo)) {
	                JOptionPane.showMessageDialog(null, "Correo inválido. Debe empezar con 'example@'.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } while (!co.edu.uptc.util.ValidadorEntrada.validarCorreo(correo));

	        if (controlador.agregarUsuario(nombre, id, direccion, correo)) {
	            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente!");
	        } else {
	            JOptionPane.showMessageDialog(null, "Error al registrar usuario. Puede que la identificación ya exista.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	private void iniciarSesion() {
		String id = JOptionPane.showInputDialog("Ingrese su identificación:");
		if (controlador.iniciarSesion(id)) {
			JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso!");
			menuUsuario();
		} else {
			JOptionPane.showMessageDialog(null, "Usuario no encontrado.");
		}
	}

	private void menuUsuario() {
		while (true) {
			String opcion = JOptionPane.showInputDialog(null, "1. Registrar Residuo\n2. Cerrar Sesión", "Menú Usuario",
					JOptionPane.QUESTION_MESSAGE);
			if (opcion == null || opcion.equals("2"))
				return;
			if (opcion.equals("1")) {
				registrarResiduo();
			}
		}
	}

	private void registrarResiduo() {
        String tipo = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo de material:", "Registrar Residuo",
                JOptionPane.QUESTION_MESSAGE, null, TIPOS_RESIDUO, TIPOS_RESIDUO[0]);
        if (tipo == null)
            return;

        String pesoStr = JOptionPane.showInputDialog("Peso (Kg):");
        try {
            double peso = Double.parseDouble(pesoStr);
            if (peso < 0) {
                JOptionPane.showMessageDialog(null, "No puede poner pesos negativos", "Error", JOptionPane.ERROR_MESSAGE);
                registrarResiduo();
            } else {
                if (controlador.agregarResiduo(tipo, peso, new Date(System.currentTimeMillis()))) {
                    JOptionPane.showMessageDialog(null, "Residuo registrado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar el residuo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Peso inválido. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

	private void listarResiduos() {
		String datos = controlador.listarResiduosDesdeJson();
		JOptionPane.showMessageDialog(null, datos, "Lista de Residuos", JOptionPane.INFORMATION_MESSAGE);
	}

	private void consultarPuntos() {
		String id = JOptionPane.showInputDialog("Ingrese la identificación del usuario:");
		Usuario usuario = controlador.obtenerUsuario(id);
		if (usuario != null) {
			JOptionPane.showMessageDialog(null, "Nombre: " + usuario.getNombre() + "\nPuntos ecológicos acumulados: " + usuario.getPuntosEcologicos(),
					"Consulta de usuario y puntos", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
