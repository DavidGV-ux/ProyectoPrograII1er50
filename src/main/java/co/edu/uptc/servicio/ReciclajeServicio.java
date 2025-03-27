package co.edu.uptc.servicio;

import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.Residuo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReciclajeServicio {
    private Map<String, Usuario> usuarios;

    public ReciclajeServicio() {
        this.usuarios = new HashMap<>();
        cargarDatosDesdeJson(); // Cargar los datos al iniciar el servicio
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.put(usuario.getIdentificacion(), usuario);
        guardarResiduosEnJson(); // Guardar cambios en el archivo JSON
    }

    public Usuario obtenerUsuario(String identificacion) {
        return usuarios.get(identificacion);
    }

    public void guardarResiduosEnJson() {
        try (Writer writer = new FileWriter("residuos.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosDesdeJson() {
        File file = new File("residuos.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Map<String, Usuario> data = gson.fromJson(reader, new TypeToken<Map<String, Usuario>>() {}.getType());
                if (data != null) {
                    this.usuarios = data;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String listarResiduosDesdeJson() {
        if (usuarios.isEmpty()) {
            return "No hay usuarios registrados.";
        }

        StringBuilder sb = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            sb.append("Usuario: ").append(usuario.getNombre()).append(" (ID: ").append(usuario.getIdentificacion()).append(")\n");
            sb.append("Puntos ecológicos: ").append(usuario.getPuntosEcologicos()).append("\n");
            for (Residuo residuo : usuario.getResiduos()) {
                sb.append("  - ").append(residuo.getTipoMaterial()).append(", Peso: ").append(residuo.getPeso()).append(" Kg, Fecha: ").append(residuo.getFechaEntrega()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public Collection<Usuario> obtenerTodosLosUsuarios() {
        return usuarios.values();
    }

    public void generarReporte() { 
        ReporteServicio reporte = new ReporteServicio(this); // ✅ Pasar `this` que es un ReciclajeServicio
        reporte.generarReporte("Reporte_Reciclaje.pdf");
    }
	

}
