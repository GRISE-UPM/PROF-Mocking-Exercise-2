package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;

public class EngineControllerTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Timestamp time = new Timestamp(System.currentTimeMillis());
    private EngineController engineController;
    private Logger logger;
    private Time timeProvider;
    private Speedometer speedometerMock;
    private Gearbox gearboxMock;

    @BeforeEach
    public void setUp() {
        logger = mock(Logger.class);
        timeProvider = mock(Time.class);
        speedometerMock = mock(Speedometer.class);
        gearboxMock = mock(Gearbox.class);
        engineController = new EngineController(logger,speedometerMock,gearboxMock, timeProvider);
    }

    @Test
    public void testRecordGear() {
        when(timeProvider.getCurrentTime()).thenReturn(time);
        ArgumentCaptor<String> log = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).log(log.capture());
        engineController.recordGear(GearValues.FIRST);
        assertEquals(sdf.format(time)+" Gear changed to "+GearValues.FIRST, log.getValue());
    }
    
    @Test
    public void testGetInstantaneousSpeed() {
        when(speedometerMock.getSpeed()).thenReturn(50.0);

        double actualSpeed = engineController.getInstantaneousSpeed();
        
        assertEquals(50.0, actualSpeed);
    }
    @Test
    public void testAdjustGear() {
        when(speedometerMock.getSpeed()).thenReturn(30.0);
        when(timeProvider.getCurrentTime()).thenReturn(time);
        engineController.adjustGear();
        verify(speedometerMock, times(3)).getSpeed();
    }
    @Test
    public void testAdjustGearRecordGear() {
        when(speedometerMock.getSpeed()).thenReturn(30.0);
        when(timeProvider.getCurrentTime()).thenReturn(time);
        ArgumentCaptor<String> log = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).log(log.capture());
        engineController.adjustGear();
        assertEquals(sdf.format(time)+" Gear changed to "+GearValues.STOP, log.getValue());
    }
    @Test
    public void testAdjustGearSetGear(){
        when(speedometerMock.getSpeed()).thenReturn(30.0);
        when(timeProvider.getCurrentTime()).thenReturn(time);
        engineController.adjustGear();
        verify(gearboxMock).setGear(GearValues.STOP);
    }
}