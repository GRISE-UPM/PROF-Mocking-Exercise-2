package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.*;

public class EngineControllerTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Timestamp timestamp = new Timestamp(0); //01-01-1970

    private AutoCloseable mockCloseable;

    private EngineController engineController;

    @Mock
    private Logger logger;

    @Mock
    private Speedometer speedometer;

    @Mock
    private Gearbox gearbox;

    @Mock
    private Time time;

    @BeforeEach
    void setUp(){
        mockCloseable = MockitoAnnotations.openMocks(this);
        engineController = new EngineController(logger,speedometer,gearbox,time);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockCloseable.close();
    }

    @Test
    void testLogOnRecordGear(){

        when(time.getCurrentTime()).thenReturn(timestamp);
        engineController.recordGear(GearValues.FIRST);

        String date = sdf.format(timestamp);

        verify(logger).log(date + " Gear changed to " + GearValues.FIRST);
    }

    @Test
    void testInstantaneousSpeed(){
        when(speedometer.getSpeed()).thenReturn(1.0,2.0,3.0);
        double gotSpeed = engineController.getInstantaneousSpeed();

        Assertions.assertEquals(2.0, gotSpeed);
    }

    @Test
    void testAdjustGearCallsToSpeedometer(){
        when(time.getCurrentTime()).thenReturn(timestamp);
        engineController.adjustGear();

        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    void testAdjustGearLogs(){
        when(time.getCurrentTime()).thenReturn(timestamp);
        when(speedometer.getSpeed()).thenReturn(100.0,101.0,103.0);

        engineController.adjustGear(); // Must adjust to stop

        String date = sdf.format(timestamp);

        verify(logger).log(date + " Gear changed to " + GearValues.STOP);
    }

    @Test
    void testAdjustGearSetGearCall(){
        when(time.getCurrentTime()).thenReturn(timestamp);
        when(speedometer.getSpeed()).thenReturn(10.0,11.0,13.0);

        engineController.adjustGear(); // Must adjust to FIRST

        verify(gearbox).setGear(GearValues.FIRST);


    }
}
