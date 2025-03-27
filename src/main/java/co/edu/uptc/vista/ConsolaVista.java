package co.edu.uptc.vista;

import java.sql.Date;
import javax.swing.JOptionPane;
import co.edu.uptc.controlador.ReciclajeControlador;
import co.edu.uptc.modelo.Usuario;

public class ConsolaVista {
    private ReciclajeControlador controlador;
    private static final String[] TIPOS_RESIDUO = {"Plástico", "Vidrio", "Cartón", "Metal"};

    public ConsolaVista(ReciclajeControlador controlador) {
        this.controlador = controlador;
    }

    public void mostrarMenu() {
        while (true) {
            String opcion = JOptionPane.showInputDialog(null, 
                "1. Registrar Usuario\n2. Iniciar Sesión\n3. Listar Residuos\n4. Consultar Puntos\n5. Generar Reporte PDF\n6. Salir", 
                "Menú", JOptionPane.QUESTION_MESSAGE);
            if (opcion == null || opcion.equals("6")) return;
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
                    JOptionPane.showMessageDialog(null, "Reporte generado: Reporte_Reciclaje.pdf", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
    }

    private void registrarUsuario() {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String id = JOptionPane.showInputDialog("Identificación:");
        String direccion = JOptionPane.showInputDialog("Dirección:");
        String correo = JOptionPane.showInputDialog("Correo:");
        controlador.agregarUsuario(nombre, id, direccion, correo);
        JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente!");
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
            String opcion = JOptionPane.showInputDialog(null, "1. Registrar Residuo\n2. Cerrar Sesión", "Menú Usuario", JOptionPane.QUESTION_MESSAGE);
            if (opcion == null || opcion.equals("2")) return;
            if (opcion.equals("1")) {
                registrarResiduo();
            }
        }
    }

    private void registrarResiduo() {
        String tipo = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo de material:", "Registrar Residuo", JOptionPane.QUESTION_MESSAGE, null, TIPOS_RESIDUO, TIPOS_RESIDUO[0]);
        if (tipo == null) return;
        String pesoStr = JOptionPane.showInputDialog("Peso (Kg):");
        try {
            double peso = Double.parseDouble(pesoStr);
            controlador.agregarResiduo(tipo, peso, new Date(System.currentTimeMillis()));
            JOptionPane.showMessageDialog(null, "Residuo registrado exitosamente!");
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
            JOptionPane.showMessageDialog(null, "Puntos ecológicos acumulados: " + usuario.getPuntosEcologicos(), 
                "Consulta de Puntos", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
