
package es.grise.upm.profundizacion.mocking.exercise2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EngineControllerTest {

	private Logger logger;
	private Speedometer speedometer;
	private Gearbox gearbox;
	private Time time;
	private EngineController engineController;

	@BeforeEach
	public void setUp() {
		logger = mock(Logger.class);
		speedometer = mock(Speedometer.class);
		gearbox = mock(Gearbox.class);
		time = mock(Time.class);
		engineController = new EngineController(logger, speedometer, gearbox, time);
	}

	@Test
	public void testRecordGearCorrectFormat() {
        GearValues gear = GearValues.FIRST;
        Timestamp timestamp = new Timestamp(1000);
        when(time.getCurrentTime()).thenReturn(timestamp);

		//el mensaje logueado debe ser el correcto con el tiempo de timestamp y la marcha de gear
        engineController.recordGear(gear);
        verify(logger).log("1970-01-01 01:00:01 Gear changed to FIRST");
		
	}
	@Test
	public void testCorrectInstantaneousSpeed() {
		// simulando que la velocidad aumenta de 0 a 15 a 30 (con un promedio de 15)
		when(speedometer.getSpeed()).thenReturn(0.0)
			.thenReturn(15.0)
			.thenReturn(30.0);
		assertEquals(15.0, engineController.getInstantaneousSpeed());

		//para verificar que se llamo 3 veces a speedometer.getSpeed()
		verify(speedometer, times(3)).getSpeed();
	}
	@Test
	public void testInstantaneousSpeedAtAdjustGear() {
		// dada una velocidad de 0
		when(speedometer.getSpeed()).thenReturn(0.0);
		
		// y simulando que el tiempo es 1000
		Timestamp timestamp = new Timestamp(1000);
		when(time.getCurrentTime()).thenReturn(timestamp);

		// cuando se ajusta la velocidad, se ha tenido que llamar 3 veces a getSpeed
		engineController.adjustGear();
		verify(speedometer, times(3)).getSpeed();
	}
	@Test
	public void testRecordGearAtAdjustGear(){
		// dada una velocidad de 10
		when(speedometer.getSpeed()).thenReturn(10.0);
		
		// y simulando que el tiempo es 1000
		Timestamp timestamp = new Timestamp(1000);
		when(time.getCurrentTime()).thenReturn(timestamp);
		
		// cuando se llama a adjustgear, el mensaje de log debe ser correcto
		engineController.adjustGear();
		verify(logger).log("1970-01-01 01:00:01 Gear changed to FIRST");
	}
	@Test
	public void testSetGearAtAdjustGear(){
		// dada una velocidad de 10
		when(speedometer.getSpeed()).thenReturn(10.0);
		
		// y simulando que el tiempo es 1000
		Timestamp timestamp = new Timestamp(1000);
		when(time.getCurrentTime()).thenReturn(timestamp);
		
		// cuando se llama a adjustgear, se ha tenido que cambiar la marcha a primera
		engineController.adjustGear();
		verify(gearbox).setGear(GearValues.FIRST);
	}
}