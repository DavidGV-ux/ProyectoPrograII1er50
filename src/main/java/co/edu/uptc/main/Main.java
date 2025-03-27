package co.edu.uptc.main;

import co.edu.uptc.controlador.ReciclajeControlador;
import co.edu.uptc.servicio.ReciclajeServicio;
import co.edu.uptc.vista.ConsolaVista;

public  class Main {
    public static void main(String[] args) {
        ReciclajeServicio servicio = new ReciclajeServicio();
        ReciclajeControlador controlador = new ReciclajeControlador(servicio);
        ConsolaVista vista = new ConsolaVista(controlador);

        vista.mostrarMenu();
    }
}
