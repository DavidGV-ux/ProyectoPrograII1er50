package co.edu.uptc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombre;
    private String identificacion;
    private String direccion;
    private String correo;
    private List<Residuo> residuos;
    private int puntosEcologicos;

    public Usuario(String nombre, String identificacion, String direccion, String correo) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.correo = correo;
        this.residuos = new ArrayList<>();
        this.puntosEcologicos = 0;
    }

    public String getNombre() { return nombre; }
    public String getIdentificacion() { return identificacion; }
    public int getPuntosEcologicos() { return puntosEcologicos; }
    public List<Residuo> getResiduos() { return residuos; }

    public void agregarResiduo(Residuo residuo) {
        residuos.add(residuo);
        calcularPuntos();
    }

    private void calcularPuntos() {
        this.puntosEcologicos = 0;
        for (Residuo r : residuos) {
            this.puntosEcologicos += (int) (r.getPeso() * 10); // 10 puntos por Kg
        }
    }
}