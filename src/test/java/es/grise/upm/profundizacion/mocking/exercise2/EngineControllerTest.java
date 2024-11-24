package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class EngineControllerTest {

    @Mock
    private Logger logger;

    @Mock
    private Speedometer speedometer;

    @Mock
    private Gearbox gearbox;

    @Mock
    private Time time;

    @InjectMocks
    private EngineController engineController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRecordGearLogsCorrectMessage() {
        GearValues newGear = GearValues.FIRST;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(time.getCurrentTime()).thenReturn(timestamp);

        engineController.recordGear(newGear);

        verify(logger).log(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp) + " Gear changed to " + newGear);
    }

    @Test
    public void testGetInstantaneousSpeedCalculatesCorrectly() {
        when(speedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0);

        double speed = engineController.getInstantaneousSpeed();

        assertEquals(20.0, speed);
    }

    @Test
    public void testAdjustGearInvokesGetInstantaneousSpeedThreeTimes() {
        when(speedometer.getSpeed()).thenReturn(10.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0));

        engineController.adjustGear();

        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    public void testAdjustGearLogsNewGear() {
        when(speedometer.getSpeed()).thenReturn(10.0, 10.0, 10.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0));

        engineController.adjustGear();

        verify(logger).log(anyString());
    }

    @Test
    public void testAdjustGearSetsCorrectGear() {
        when(speedometer.getSpeed()).thenReturn(10.0, 10.0, 10.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0));

        engineController.adjustGear();

        verify(gearbox).setGear(GearValues.FIRST);
    }
}
