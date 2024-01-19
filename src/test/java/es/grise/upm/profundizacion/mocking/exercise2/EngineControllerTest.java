package es.grise.upm.profundizacion.mocking.exercise2;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

public class EngineControllerTest {
	
	
		EngineController engineController;
		Logger mockLogger;
		Speedometer mockSpeedometer;
		Gearbox mockGearbox;
		Time mockTime;

     	@BeforeEach
     	public void init() {
    	
    	 	mockLogger = mock(Logger.class);
        	mockSpeedometer = mock(Speedometer.class);
        	mockGearbox = mock(Gearbox.class);
        	mockTime = mock(Time.class);

        	engineController = new EngineController(mockLogger, mockSpeedometer, mockGearbox, mockTime);
     	}
     	
     	@Test
	    void testRecordGearFormat() {

	        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(0));

	        engineController.recordGear(GearValues.FIRST);
	        String formato = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to FIRST";
	        verify(mockLogger).log(matches(formato));
	        verifyNoMoreInteractions(mockLogger);
	    }

	    @Test
	    void testGetInstantaneousSpeedCalculation() {
	    
	    	when(mockSpeedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0);
	        
	        double result = engineController.getInstantaneousSpeed();
	        assertEquals(20.0, result, 0.01);
	    }

	    @Test
	    void testAdjustGearInvokesGetInstantaneousSpeedThreeTimes() {
	        
	    	when(mockTime.getCurrentTime()).thenReturn(new Timestamp(0));
	        when(mockSpeedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0);
	        engineController.adjustGear();
	        verify(mockSpeedometer, times(3)).getSpeed();
	        verifyNoMoreInteractions(mockSpeedometer);
	        
	    }

	    @Test
	    void testAdjustGearLogsNewGear() {

	    	when(mockTime.getCurrentTime()).thenReturn(new Timestamp(0));
	        when(mockSpeedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0);
	        
	        engineController.adjustGear();
	        String formato = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to FIRST";
	        verify(mockLogger).log(matches(formato));
	        verifyNoMoreInteractions(mockLogger);
	    }

	    @Test
	    void testAdjustGearSetsCorrectNewGear() {

	    	when(mockTime.getCurrentTime()).thenReturn(new Timestamp(0));
	        when(mockSpeedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0);
	        
	        engineController.adjustGear();
	        verify(mockGearbox).setGear(GearValues.FIRST);
	        verifyNoMoreInteractions(mockGearbox);
	    }
}