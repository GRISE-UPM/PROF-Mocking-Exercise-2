package es.grise.upm.profundizacion.mocking.exercise2;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EngineControllerTest {

    EngineController engineController;

    private Logger logger;
    private Speedometer speedometer;
    private Gearbox gearbox;
    private Time time;


    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        speedometer = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);
        time = mock(Time.class);
        engineController = new EngineController(logger, speedometer, gearbox, time);
    }

    @Test
    void recordGear() {
        Timestamp timestamp = new Timestamp(123, 00, 01, 00, 00, 00,00);
        when(time.getCurrentTime()).thenReturn(timestamp);
        engineController.recordGear(GearValues.FIRST);
        verify(logger).log("2023-01-01 00:00:00 Gear changed to FIRST");
    }

    @Test
    void getInstantaneousSpeed() {
        double speed1 = 10.0;
        double speed2 = 20.0;
        double speed3 = 30.0;
        when(speedometer.getSpeed()).thenReturn(speed1, speed2, speed3);
        assertEquals((speed1+speed2+speed3)/3 , engineController.getInstantaneousSpeed());
    }
// He asumido que se referian que getInstantaneousSpeed() invoca tres veces a getSpeed()
    @Test
    void getInstantaneous3Times() {
        when(speedometer.getSpeed()).thenReturn(10.0);
        engineController.getInstantaneousSpeed();
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    void adjustGearLog() {
        Timestamp timestamp = new Timestamp(123, 00, 01, 00, 00, 00,00) ;
        when(time.getCurrentTime()).thenReturn(timestamp);
        engineController = new EngineController(logger, speedometer, gearbox, time);
        when(speedometer.getSpeed()).thenReturn(10.0);
        engineController.adjustGear();
        verify(logger).log("2023-01-01 00:00:00 Gear changed to FIRST");
    }

    @Test
    void adjustGearSet() {
        Timestamp timestamp = new Timestamp(123, 00, 01, 00, 00, 00,00) ;
        when(time.getCurrentTime()).thenReturn(timestamp);
        engineController = new EngineController(logger, speedometer, gearbox, time);
        when(speedometer.getSpeed()).thenReturn(5.0);
        engineController.adjustGear();
        verify(gearbox).setGear(GearValues.FIRST);
    }

}