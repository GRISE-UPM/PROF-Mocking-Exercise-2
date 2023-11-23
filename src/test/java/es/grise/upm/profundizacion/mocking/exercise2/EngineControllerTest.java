package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class EngineControllerTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Timestamp ts = new Timestamp(System.currentTimeMillis());
    private Logger logger;
    private Speedometer speedometer;
    private Gearbox gearbox;
    private Time time;
    private EngineController engineController;

    @BeforeEach
    public void init() {
        this.logger = mock(Logger.class);
        this.speedometer = mock(Speedometer.class);
        this.gearbox = mock(Gearbox.class);
        this.time = mock(Time.class);
        this.engineController = new EngineController(this.logger, this.speedometer, this.gearbox, this.time);
    }

    @Test
    public void recordGearTest() {
        GearValues gearValueFirst = GearValues.FIRST;
        when(time.getCurrentTime()).thenReturn(ts);
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).log(valueCapture.capture());

        this.engineController.recordGear(gearValueFirst);

        String expectedLoggerArg = sdf.format(ts) + " Gear changed to " + gearValueFirst;
        assertEquals(valueCapture.getValue(), expectedLoggerArg);
    }

    @Test
    public void getInstantaneousSpeedTest() {
        when(speedometer.getSpeed()).thenReturn(1.0);
        when(speedometer.getSpeed()).thenReturn(1.0);
        when(speedometer.getSpeed()).thenReturn(1.0);

        Double expectedSpeed = 1.0;
        assertEquals(expectedSpeed, this.engineController.getInstantaneousSpeed());
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    public void setGearTest() {
        GearboxImpl gearbox = new GearboxImpl();
        GearboxImpl gearboxSpy = spy(gearbox);
        this.engineController = new EngineController(this.logger, this.speedometer, gearboxSpy, this.time);

        GearValues gearValueFirst = GearValues.FIRST;
        this.engineController.setGear(gearValueFirst);
        assertEquals(gearValueFirst, gearboxSpy.gear);
    }

    @Test
    public void adjustGearTest() {
        GearboxImpl gearbox = new GearboxImpl();
        GearboxImpl gearboxSpy = spy(gearbox);
        this.engineController = new EngineController(this.logger, this.speedometer, gearboxSpy, this.time);

        when(time.getCurrentTime()).thenReturn(ts);
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).log(valueCapture.capture());

        GearValues newGear = GearValues.STOP;
        this.engineController.recordGear(newGear);

        this.engineController.adjustGear();
        verify(speedometer, times(3)).getSpeed();
        GearValues expectedGear = GearValues.FIRST;
        assertTrue(valueCapture.getValue().contains(expectedGear.toString()));
        assertEquals(expectedGear, gearboxSpy.gear);
    }
}
