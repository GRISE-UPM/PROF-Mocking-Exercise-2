package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEngineController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Timestamp ts = new Timestamp(System.currentTimeMillis());
	private Logger logger;
	private Speedometer speedometer;
	private Gearbox gearbox;
	private Time time;
    private EngineController c;

    @BeforeEach
    public void init(){
        this.logger = mock(Logger.class);
        this.speedometer = mock(Speedometer.class);
        this.gearbox = mock(Gearbox.class);
        this.time = mock(Time.class);
        this.c = new EngineController(logger, speedometer, gearbox, time);
    }


    @Test
    public void testRecordGear() {
        // Estimate the expected result
        when(time.getCurrentTime()).thenReturn(ts);
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).log(valueCapture.capture());

        // Test
        GearValues gearValueFirst = GearValues.FIRST;
        this.c.recordGear(gearValueFirst);

        // Verify
        String expectedLoggerArg = sdf.format(ts) + " Gear changed to " + gearValueFirst;
        assertEquals(valueCapture.getValue(), expectedLoggerArg);
    }

    @Test
    public void testSetGear(){
        // Expected
        GearValues expectedGear = GearValues.FIRST;
        // Test
        c.setGear(expectedGear);
        // Verify
        verify(gearbox).setGear(expectedGear);
    }    

    @Test
    public void testGetInstantaneousSpeed(){
        when(speedometer.getSpeed()).thenReturn(100.0);

        Double expectedSpeed = 100.0;
        assertEquals(expectedSpeed, this.c.getInstantaneousSpeed());
    }

    // technically, adjustGear calls getInstantaneousSpeed only 1 time. getSpeed is called 3 times
    @Test
    public void testGetInstantaneousSpeed3Times() {
        when(time.getCurrentTime()).thenReturn(ts);
        c.adjustGear();
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    public void testAdjustGearRecord() {
        when(time.getCurrentTime()).thenReturn(ts);
        when(speedometer.getSpeed()).thenReturn(0.0);

        c.adjustGear();
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(logger).log(valueCapture.capture());

        GearValues expectedGear = GearValues.FIRST;
        String expectedLoggerArg = sdf.format(ts) + " Gear changed to " + expectedGear;
        assertEquals(expectedLoggerArg, valueCapture.getValue());
    }

    @Test
    public void testAdjustGearSet() {
        when(time.getCurrentTime()).thenReturn(ts);

        GearValues g = GearValues.FIRST;
        c.setGear(g);
        verify(gearbox).setGear(GearValues.FIRST);

        when(speedometer.getSpeed()).thenReturn(100.0);
        c.adjustGear();
        verify(gearbox).setGear(GearValues.STOP);
    }
}