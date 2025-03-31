package modelo;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import co.edu.uptc.modelo.Residuo;

import java.sql.Date;
import java.util.List;

public class ResiduoTest {
    private Residuo residuo;

    @Before
    public void setUp() {
        residuo = new Residuo("Plástico", 2.5, Date.valueOf("2024-03-31"));
    }

    @Test
    public void testGetTipoMaterial() {
        assertEquals("Plástico", residuo.getTipoMaterial());
    }

    @Test
    public void testGetPeso() {
        assertEquals(2.5, residuo.getPeso(), 0.01);
    }

    @Test
    public void testGetFechaEntrega() {
        assertEquals(Date.valueOf("2024-03-31"), residuo.getFechaEntrega());
    }
}