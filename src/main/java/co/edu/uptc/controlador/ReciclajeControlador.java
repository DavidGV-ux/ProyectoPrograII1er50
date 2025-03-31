package co.edu.uptc.controlador;

import java.sql.Date;

import javax.swing.JOptionPane;

import co.edu.uptc.modelo.Residuo;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.servicio.ReciclajeServicio;
import co.edu.uptc.util.ValidadorEntrada;
import co.edu.uptc.util.ValidadorResiduo;

public class ReciclajeControlador {
    private ReciclajeServicio servicio;
    private Usuario usuarioActual;

    public ReciclajeControlador(ReciclajeServicio servicio) {
        this.servicio = servicio;
    }

    public boolean agregarUsuario(String nombre, String identificacion, String direccion, String correo) {
        if (!ValidadorEntrada.validarNombre(nombre)) {
            return false;
        }
        if (!ValidadorEntrada.validarIdentificacion(identificacion)) {
            return false;
        }
        if (!ValidadorEntrada.validarCorreo(correo)) {
            return false;
        }
        if (servicio.obtenerUsuario(identificacion) == null) {
            servicio.registrarUsuario(new Usuario(nombre, identificacion, direccion, correo));
            return true;
        }
        return false;
    }

    public Usuario obtenerUsuario(String identificacion) {
        return servicio.obtenerUsuario(identificacion);
    }

    public boolean iniciarSesion(String identificacion) {
        usuarioActual = servicio.obtenerUsuario(identificacion);
        return usuarioActual != null;
    }

    public boolean agregarResiduo(String tipoMaterial, double peso, Date fechaEntrega) {
        if (usuarioActual == null) {
            return false; // No hay sesión iniciada
        }

        // Validar el tipo de material y el peso antes de agregarlo
        if (!ValidadorResiduo.validarTipoMaterial(tipoMaterial)) {
            return false; // Tipo de material inválido
        }
        if (!ValidadorResiduo.validarPeso(peso)) {
            return false; // Peso inválido
        }

        // Si las validaciones son correctas, agregar el residuo
        usuarioActual.agregarResiduo(new Residuo(tipoMaterial, peso, fechaEntrega));
        servicio.guardarResiduosEnJson(); // Guardar cambios en el JSON
        return true; // Residuo agregado exitosamente
    }

    public String listarResiduosDesdeJson() {
        return servicio.listarResiduosDesdeJson();
    }
    
    public void generarReportePDF() {
        servicio.generarReporte(); // ✅ Llamar a la función sin parámetros
    }

}
