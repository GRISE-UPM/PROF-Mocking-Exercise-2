package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EngineControllerTest {
    
    EngineController engineController;
    Logger logger;
    Speedometer speedometer;
    Gearbox gearbox;
    Time time;

    @BeforeEach
    public void setUp() {
        logger = mock(Logger.class);
        speedometer = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);
        time = mock(Time.class);
        engineController = new EngineController(logger, speedometer, gearbox, time);
    }

    //Implement the following tests on the EngineController class, using mocks:

    //a) The log message has the correct format (recordGear() method).
    @Test
    public void testRecordGear() {

        engineController.recordGear(GearValues.FIRST);
        verify(logger).log("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$\r\n" + 
                " Gear changed to FIRST");
        verifyNoMoreInteractions(logger);
    }

    //b) The instantaneous speed is correctly calculated (method getInstantaneousSpeed()).
    @Test
    public void testGetInstantaneousSpeed() {
        when(speedometer.getSpeed()).thenReturn(10.0);
        assertEquals( 10.0, engineController.getInstantaneousSpeed());
        verify(speedometer).getSpeed();
        verifyNoMoreInteractions(speedometer);
    }

    //c) The adjustGear method invokes the getInstantaneousSpeed() method exactly three times.
    @Test
    public void testAdjustGear() {
        engineController.adjustGear();
        verifyNoMoreInteractions(speedometer);
    }
}
