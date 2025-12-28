package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EngineControllerTest {

    private static final SimpleDateFormat SDF =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private EngineController engine;
    private Timestamp now;

    @Mock private Logger logger;
    @Mock private Speedometer speedometer;
    @Mock private Gearbox gearbox;
    @Mock private Time time;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        engine = new EngineController(logger, speedometer, gearbox, time);

        now = new Timestamp(System.currentTimeMillis());
        when(time.getCurrentTime()).thenReturn(now);

        when(speedometer.getSpeed()).thenReturn(2.0, 4.0, 9.0);
    }


    @Test
    public void comprobarFormatoLog() {
        engine.recordGear(GearValues.FIRST);

        verify(logger).log(eq(mensajeEsperado(GearValues.FIRST)));
    }

    @Test
    public void comprobarVelocidadInstantanea() {
        double velocidad = engine.getInstantaneousSpeed();
        assertEquals(5.0, velocidad);
    }

    @Test
    public void comprobarInvocacionesSpeedometer() {
        engine.adjustGear();
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    public void comprobarAsignacionMarcha() {
        when(speedometer.getSpeed()).thenReturn(10.0);

        engine.adjustGear();

        verify(gearbox).setGear(GearValues.FIRST);
    }

    @Test
    public void comprobarLogNuevaMarcha() {
        engine.adjustGear();

        verify(logger).log(eq(mensajeEsperado(GearValues.FIRST)));
    }


    private String mensajeEsperado(GearValues gear) {
        return SDF.format(now) + " Gear changed to " + gear;
    }
}
