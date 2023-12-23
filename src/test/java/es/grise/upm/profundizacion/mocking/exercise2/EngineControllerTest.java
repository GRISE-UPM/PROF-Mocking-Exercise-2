package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class EngineControllerTest {
	private Logger mockLogger;
	private Speedometer speedometer;
	private Gearbox gearbox;
	private Time time;
	private EngineController engineController;
	@BeforeEach
	void setUp() {
		mockLogger = mock(Logger.class);
		speedometer = mock(Speedometer.class);
		gearbox = mock(Gearbox.class);
		time = mock(Time.class);

		engineController = new EngineController(mockLogger, speedometer, gearbox, time);
	}
	@Test
    void GearLogsTest() {

        GearValues gearValue = GearValues.FIRST;
        when(time.getCurrentTime()).thenReturn(new Timestamp(1637670000000L)); 
        engineController.recordGear(gearValue);

        verify(mockLogger).log("2021-11-23 13:20:00 Gear changed to FIRST"); 
    }
	@Test
    void SpeedTest() {
        when(speedometer.getSpeed()).thenReturn(20.0, 25.0, 30.0); 
        double result = engineController.getInstantaneousSpeed();
        assertEquals(25.0, result); 
       
    }
    @Test
    void ThreeCallsTest() {
        when(speedometer.getSpeed()).thenReturn(20.0, 25.0, 30.0); 
       engineController.getInstantaneousSpeed();
      
        verify(speedometer, times(3)).getSpeed(); 
    }

    @Test
    void testAdjustGearLogsNewGear() {
    	 GearValues gearValue = GearValues.FIRST;
    	 
        when(speedometer.getSpeed()).thenReturn(15.0, 25.0, 30.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(1637670000000L));
        engineController.adjustGear();
        engineController.recordGear(gearValue);
        verify(mockLogger).log("2021-11-23 13:20:00 Gear changed to STOP");
    }

    @Test
    void AdjustGearTest() {
        when(speedometer.getSpeed()).thenReturn(15.0, 15.0, 20.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(1637670000000L));
        engineController.adjustGear();
        verify(gearbox).setGear(GearValues.FIRST); 
    }

   
}
