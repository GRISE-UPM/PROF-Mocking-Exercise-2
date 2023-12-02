package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

public class EngineControllerTest {

    private EngineController engineController;
    private Logger mockLogger;
    private Speedometer mockSpeedometer;
    private Gearbox mockGearbox;
    private Time mockTime;

    @Before
    public void init() {
    	
        mockLogger = mock(Logger.class);
        mockSpeedometer = mock(Speedometer.class);
        mockGearbox = mock(Gearbox.class);
        mockTime = mock(Time.class);

        engineController = new EngineController(mockLogger, mockSpeedometer, mockGearbox, mockTime);
    }

    @Test
    public void testRecordGearFormat() {
        
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1234567890L));
        engineController.adjustGear();
        ArgumentCaptor<String> logArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger).log(logArgumentCaptor.capture());
        String logMessage = logArgumentCaptor.getValue();
        String expectedFormat = "2009-02-13 23:31:30 Gear changed to .*";
        assertTrue(logMessage.matches(expectedFormat));
    }

    @Test
    public void testGetInstantaneousSpeedCalculation() {
      
        when(mockSpeedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0);
       
        double result = engineController.getInstantaneousSpeed();
        assertEquals(20.0, result, 0.01);
    }

    @Test
    public void testAdjustGearInvokesGetInstantaneousSpeedThreeTimes() {
       
        engineController.adjustGear();
        verify(mockSpeedometer, times(3)).getSpeed();
    }

    @Test
    public void testAdjustGearLogsNewGear() {
        
        engineController.adjustGear();
        verify(mockLogger).log(anyString());
    }

    @Test
    public void testAdjustGearSetsCorrectNewGear() {
        
        when(mockSpeedometer.getSpeed()).thenReturn(15.0);
        engineController.adjustGear();
        verify(mockGearbox).setGear(GearValues.FIRST);
    }
}
