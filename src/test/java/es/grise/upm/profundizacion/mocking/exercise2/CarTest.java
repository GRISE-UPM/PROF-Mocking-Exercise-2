package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class CarTest {
    // Test para probar que el mensaje de log se genera en el formato correcto (recordGear())
	@Test
	public void testRecordGear() {
		Logger logger = mock(Logger.class);
		Speedometer speedometer = mock(Speedometer.class);
		Gearbox gearbox = mock(Gearbox.class);
		Time time = mock(Time.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EngineController engineController = new EngineController(logger, speedometer, gearbox, time);
		when(time.getCurrentTime()).thenReturn(new Timestamp(0));
	
		engineController.recordGear(GearValues.FIRST);
	
		verify(logger).log(sdf.format(time.getCurrentTime()) + " Gear changed to " + GearValues.FIRST);
	}

    // Test para probar que se calcula correctamente la velocidad instantánea (getInstantaneousSpeed())
	@Test
	public void testGetInstantaneousSpeed() {
		Logger logger = mock(Logger.class);
		Speedometer speedometer = mock(Speedometer.class);
		Gearbox gearbox = mock(Gearbox.class);
		Time time = mock(Time.class);
		EngineController engineController = new EngineController(logger, speedometer, gearbox, time);
		when(speedometer.getSpeed()).thenReturn(10.0);
	
		double speed = engineController.getInstantaneousSpeed();
	
		assertEquals(10.0, speed);
	}

	// Test para probar que el método adjustGear() funciona correctamente invoca 3 veces a getInstantaneousSpeed()
	@Test
	public void tresinvocacionesadjustGear(){
		Logger logger = mock(Logger.class);
		Speedometer speedometer = mock(Speedometer.class);
		Gearbox gearbox = mock(Gearbox.class);
		Time time = mock(Time.class);
		EngineController engineController = new EngineController(logger, speedometer, gearbox, time);

		when(speedometer.getSpeed()).thenReturn(10.0);
		when(time.getCurrentTime()).thenReturn(new Timestamp(0));

		engineController.adjustGear();

		verify(speedometer, times(3)).getSpeed();
	}

    //  Test para probar que el método adjustGear() mete la nueva marcha en recordGear()
	@Test
	public void loguearNuevaMarcha(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Logger logger = mock(Logger.class);
		Speedometer speedometer = mock(Speedometer.class);
		Gearbox gearbox = mock(Gearbox.class);
		Time time = mock(Time.class);
		EngineController engineController = new EngineController(logger, speedometer, gearbox, time);

		when(speedometer.getSpeed()).thenReturn(10.0);
		when(time.getCurrentTime()).thenReturn(new Timestamp(0));

		engineController.adjustGear();

		verify(logger).log(sdf.format(time.getCurrentTime()) + " Gear changed to " + GearValues.FIRST);
	}

    // Test para probar que el método adjustGear() asigna correctamente la nueva marcha en setGear()
	@Test
	public void asignarNuevaMarcha(){
		Logger logger = mock(Logger.class);
		Speedometer speedometer = mock(Speedometer.class);
		Gearbox gearbox = mock(Gearbox.class);
		Time time = mock(Time.class);
		EngineController engineController = new EngineController(logger, speedometer, gearbox, time);

		when(speedometer.getSpeed()).thenReturn(10.0);
		when(time.getCurrentTime()).thenReturn(new Timestamp(0));

		engineController.adjustGear();

		verify(gearbox).setGear(GearValues.FIRST);
	}

}