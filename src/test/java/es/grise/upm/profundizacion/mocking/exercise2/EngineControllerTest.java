package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.sql.Timestamp;

class EngineControllerTest {

	//@Mock
	Logger loggerMock;
	Speedometer speedometerMock;
	Gearbox gearboxMock;
	Time timeMock;

	//@InjectMocks
	EngineController engineController;

	@BeforeEach
	public void setUp() 
	{
		loggerMock = Mockito.mock(Logger.class);
		speedometerMock = Mockito.mock(Speedometer.class);
		gearboxMock = Mockito.mock(Gearbox.class);
		timeMock = Mockito.mock(Time.class);

		engineController = new EngineController(
				loggerMock, speedometerMock, gearboxMock, timeMock);
	}
	
	@Test
	public void correctFormatRecordGearTest() 
	{
        Mockito.when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(speedometerMock.getSpeed()).thenReturn(30.0); 

        engineController.adjustGear();
        
        // regex follows: yyyy-mm-dd hh:mm:ss after sdf.format(date)
        Mockito.verify(loggerMock).log( Mockito.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to .*"));
	}
	
	@Test
	public void getInstantaneousSpeedTest() 
	{		
		Mockito.when(speedometerMock.getSpeed()).thenReturn(30.0);				
	    assertEquals(30.0, engineController.getInstantaneousSpeed()); 
	}
	
	@Test
	public void adjustGearCalls3TimesGetSpeedTest() 
	{	
		Mockito.when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
		
		engineController.adjustGear();
		
		Mockito.verify(speedometerMock, Mockito.times(3)).getSpeed();
		}
	
	@Test
	public void adjustGearLogsRecordGearTest() 
	{		
		Mockito.when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
		Mockito.when(speedometerMock.getSpeed()).thenReturn(10.0);
		
		engineController.adjustGear();
		
		Mockito.verify(loggerMock).log(Mockito.anyString());
	}
	
	@Test
	public void adjustGearSetsCorrectGearTest() 
	{
		Mockito.when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
		Mockito.when(speedometerMock.getSpeed()).thenReturn(20.0);
		
		engineController.adjustGear();
		
		Mockito.verify(gearboxMock).setGear(GearValues.FIRST);
		
	}
	
}

