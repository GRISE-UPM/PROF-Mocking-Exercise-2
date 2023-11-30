package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class CarTest {

	// Inicialización de variables globales
	private Logger logger = mock(Logger.class);
	private Speedometer speedometer = mock(Speedometer.class);
	private Gearbox gearbox = mock(Gearbox.class);
	private Time time = mock(Time.class);
	private EngineController engineController = new EngineController(logger, speedometer, gearbox, time);

	// Inicialización de constante para el formato de la fecha
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // Test para probar que el mensaje de log se genera en el formato correcto (recordGear())
	@Test
	public void testRecordGear() {
		when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
	
		engineController.recordGear(GearValues.FIRST);
	
		verify(logger).log(sdf.format(time.getCurrentTime()) + " Gear changed to " + GearValues.FIRST);
	}

    // Test para probar que se calcula correctamente la velocidad instantánea (getInstantaneousSpeed())
	@Test
	public void testGetInstantaneousSpeed() {
		when(speedometer.getSpeed()).thenReturn(10.0);
	
		double speed = engineController.getInstantaneousSpeed();
		double expectedSpeed = 10.0;
	
		assertEquals(expectedSpeed, speed);
	}

	// Test para probar que el método adjustGear() funciona correctamente invoca 3 veces a getInstantaneousSpeed()
	@Test
	public void tresinvocacionesadjustGear(){
		when(speedometer.getSpeed()).thenReturn(10.0);
		when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

		engineController.adjustGear();

		verify(speedometer, times(3)).getSpeed();
	}

    //  Test para probar que el método adjustGear() mete la nueva marcha en recordGear()
	@Test
	public void loguearNuevaMarcha(){
		when(speedometer.getSpeed()).thenReturn(10.0);
		when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

		engineController.adjustGear();

		verify(logger).log(sdf.format(time.getCurrentTime()) + " Gear changed to " + GearValues.FIRST);
	}

    // Test para probar que el método adjustGear() asigna correctamente la nueva marcha en setGear()
	@Test
	public void asignarNuevaMarcha(){
		when(speedometer.getSpeed()).thenReturn(10.0);
		when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

		engineController.adjustGear();

		verify(gearbox).setGear(GearValues.FIRST);
	}

}