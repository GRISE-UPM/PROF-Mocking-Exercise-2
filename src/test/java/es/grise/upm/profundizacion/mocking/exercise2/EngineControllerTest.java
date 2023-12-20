package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import org.junit.jupiter.api.BeforeEach;



public class EngineControllerTest {
    // Mocks
    private Logger mock_logger;
    private Speedometer mock_speedometer;
    private Gearbox mock_gearbox;
    private Time mock_time;

    // Test class
    private EngineController engine;

    // Test values
    private final Timestamp TIMESTAMP = new Timestamp(1701773999039L);
    private final String TIME_FORMATED = "2023-12-05 11:59:59";

    @BeforeEach
    public void setUp() {
        mock_logger = mock(Logger.class);
        mock_time = mock(Time.class);
        mock_speedometer = mock(Speedometer.class);
        mock_gearbox = mock(Gearbox.class);
        engine = new EngineController(mock_logger, mock_speedometer, mock_gearbox, mock_time);

        // Mock Time
        when(mock_time.getCurrentTime()).thenReturn(TIMESTAMP);
    }

    @Test
    public void testRecordGearTimeFormat() {
        final String expected_string = TIME_FORMATED + " Gear changed to " + GearValues.FIRST;

        ArgumentCaptor<String> argcap = ArgumentCaptor.forClass(String.class);
        doNothing().when(mock_logger).log(argcap.capture());
        engine.recordGear(GearValues.FIRST);

        assertEquals(expected_string, argcap.getValue());
    }

    @Test
    public void testGetInstantaneousSpeed() {
        final double expected_speed = 3.0; // Avarage between 2.0, 3.0 and 4.0
        when(mock_speedometer.getSpeed())
            .thenReturn(2.0)
            .thenReturn(3.0)
            .thenReturn(4.0);

        assertEquals(expected_speed, engine.getInstantaneousSpeed());
    }

    @Test
    public void testAdjustGearThreeCallsToGetInstantaneousSpeed() {
        when(mock_speedometer.getSpeed()).thenReturn(2.0);
        engine.adjustGear();
        verify(mock_speedometer, times(3)).getSpeed();
    }

    @Test
    public void testAdjustGearRecordGear() {
        final String expected_string = TIME_FORMATED + " Gear changed to " + GearValues.FIRST;
        when(mock_speedometer.getSpeed()).thenReturn(2.0);
        ArgumentCaptor<String> argcap = ArgumentCaptor.forClass(String.class);
        doNothing().when(mock_logger).log(argcap.capture());

        engine.adjustGear();
        assertEquals(expected_string, argcap.getValue());
    }

    @Test
    public void testAdjustGearSetGear() {
        when(mock_speedometer.getSpeed()).thenReturn(2.0);
        ArgumentCaptor<GearValues> argcap = ArgumentCaptor.forClass(GearValues.class);
        doNothing().when(mock_gearbox).setGear(argcap.capture());

        engine.adjustGear();
        assertEquals(GearValues.FIRST, argcap.getValue());
    }
}
