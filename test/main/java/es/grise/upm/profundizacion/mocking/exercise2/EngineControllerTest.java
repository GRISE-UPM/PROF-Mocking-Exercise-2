package es.grise.upm.profundizacion.mocking.exercise2;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EngineControllerTest {
    private Logger logger;
    private Speedometer speedometer;
    private Gearbox gearbox;
    private Time time;

    private EngineController engineController;

    @BeforeEach
    void setUp(){
        logger = mock(Logger.class);
        speedometer = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);
        time = mock(Time.class);

        engineController = new EngineController(logger, speedometer, gearbox, time);
    }

    //DL - Comprueba que el mensaje de log tiene el formato correcto
    @Test
    void testRecordGearLogs(){
        Timestamp fixedTime = Timestamp.valueOf("2026-01-20 10:30:00");
        when(time.getCurrentTime()).thenReturn(fixedTime);

        engineController.recordGear(GearValues.FIRST);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logger).log(captor.capture());

        String loggedMessage = captor.getValue();
        assertEquals("2026-01-20 10:30:00 Gear changed to FIRST", loggedMessage);
    }

    //DL - Comprueba que se calcula correctamente la velocidad instantánea
    @Test
    void testGetInstantaneousSpeed(){
        when(speedometer.getSpeed())
            .thenReturn(10.0)
            .thenReturn(20.0)
            .thenReturn(30.0);

            double result = engineController.getInstantaneousSpeed();

            assertEquals(20.0, result);
    }

    //DL - Comprueba que el método adjustGear invoca exactamente tres veces al método getInstantaneousSpeed()
    @Test
    void testAdjustGearCallsGetSpeedThreeTimes(){
        when(speedometer.getSpeed()).thenReturn(15.0);
        when(time.getCurrentTime()).thenReturn(Timestamp.valueOf("2026-01-20 10:30:00"));

        engineController.adjustGear();

        verify(speedometer, times(3)).getSpeed();
    }

    //DL - Comprueba que el método adjustGear loguea la nueva marcha
    @Test 
    void testAdjustGearLogsNewGear(){
        when(speedometer.getSpeed()).thenReturn(15.0);
        when(time.getCurrentTime()).thenReturn(Timestamp.valueOf("2026-01-20 10:30:00"));

        engineController.adjustGear();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logger).log(captor.capture());

        String loggedMessage = captor.getValue();
        assertEquals("2026-01-20 10:30:00 Gear changed to FIRST", loggedMessage);
    }

    //DL - Comprueba que el método adjustGear asigna correctamente la nueva marcha
    @Test
    void testAdjustGearSetsCorrectGear(){
        when(speedometer.getSpeed()).thenReturn(15.0);
        when(time.getCurrentTime()).thenReturn(Timestamp.valueOf("2026-01-20 10:30:00"));
        
        engineController.adjustGear();

        verify(gearbox).setGear(GearValues.FIRST);
    }
}
