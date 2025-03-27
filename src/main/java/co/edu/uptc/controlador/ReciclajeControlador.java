package co.edu.uptc.controlador;

import java.sql.Date;
import co.edu.uptc.modelo.Residuo;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.servicio.ReciclajeServicio;

public class ReciclajeControlador {
    private ReciclajeServicio servicio;
    private Usuario usuarioActual;

    public ReciclajeControlador(ReciclajeServicio servicio) {
        this.servicio = servicio;
    }

    public void agregarUsuario(String nombre, String identificacion, String direccion, String correo) {
        if (servicio.obtenerUsuario(identificacion) == null) {
            servicio.registrarUsuario(new Usuario(nombre, identificacion, direccion, correo));
        }
    }

    public Usuario obtenerUsuario(String identificacion) {
        return servicio.obtenerUsuario(identificacion);
    }

    public boolean iniciarSesion(String identificacion) {
        usuarioActual = servicio.obtenerUsuario(identificacion);
        return usuarioActual != null;
    }

    public void agregarResiduo(String tipoMaterial, double peso, Date fechaEntrega) {
        if (usuarioActual != null) {
            usuarioActual.agregarResiduo(new Residuo(tipoMaterial, peso, fechaEntrega));
            servicio.guardarResiduosEnJson(); // Guardar cambios en el JSON
        }
    }

    public String listarResiduosDesdeJson() {
        return servicio.listarResiduosDesdeJson();
    }
    
    public void generarReportePDF() {
        servicio.generarReporte(); // ✅ Llamar a la función sin parámetros
    }

}
