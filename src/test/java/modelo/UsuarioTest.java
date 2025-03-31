package modelo;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import co.edu.uptc.modelo.Residuo;
import co.edu.uptc.modelo.Usuario;

import java.sql.Date;
import java.util.List;

public class UsuarioTest {
    private Usuario usuario;
    private Residuo residuo;

    @Before
    public void setUp() {
        usuario = new Usuario("Juan Pérez", "123456", "Calle 123", "juan@example.com");
        residuo = new Residuo("Vidrio", 3.0, Date.valueOf("2024-03-31"));
    }

    @Test
    public void testGetNombre() {
        assertEquals("Juan Pérez", usuario.getNombre());
    }

    @Test
    public void testGetIdentificacion() {
        assertEquals("123456", usuario.getIdentificacion());
    }

    @Test
    public void testAgregarResiduo() {
        usuario.agregarResiduo(residuo);
        List<Residuo> residuos = usuario.getResiduos();
        assertEquals(1, residuos.size());
        assertEquals("Vidrio", residuos.get(0).getTipoMaterial());
    }

    @Test
    public void testCalcularPuntos() {
        usuario.agregarResiduo(new Residuo("Papel", 4.0, Date.valueOf("2024-03-31")));
        usuario.agregarResiduo(new Residuo("Metal", 2.0, Date.valueOf("2024-03-31")));
        assertEquals(60, usuario.getPuntosEcologicos()); // 4.0 * 10 + 2.0 * 10 = 60
    }
}
