package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EngineControllerTest {
    private Logger mockedLogger;
    private Speedometer mockedSpeedometer;
    private Gearbox mockedGearbox;
    private Time mockedTime;

    SimpleDateFormat testFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private EngineController mockedEngineController;
    @BeforeEach
    void setUp() {
        mockedLogger = mock(Logger.class);
        mockedSpeedometer = mock(Speedometer.class);
        mockedGearbox = mock(Gearbox.class);
        mockedTime = mock(Time.class);
        mockedEngineController = spy(new EngineController(mockedLogger,mockedSpeedometer,mockedGearbox,mockedTime));
    }

    @Test
    void testRecordGear() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        GearValues testGear = GearValues.FIRST; // Cualquier Gear nos vale

        when(mockedTime.getCurrentTime()).thenReturn(currentTime);
        mockedEngineController.recordGear(testGear);

        String expectedLogMessage = testFormat.format(currentTime) + " Gear changed to " + testGear;

        verify(mockedLogger).log(eq(expectedLogMessage)); // Comprueba que la funcion log dentro de logger es llamado con el mensaje valido.
    }



    @Test
    void testGetInstantaneousSpeed() {
        double[] speed = {12,7,6};
        when(mockedSpeedometer.getSpeed()).thenReturn(speed[0], speed[1], speed[2]);

        double expectedSpeed = (speed[0]+speed[1]+speed[2]) / 3;
        assertEquals(expectedSpeed, mockedEngineController.getInstantaneousSpeed());
    }


    @Test
    // Estoy asumiendo que en el enunciado cuando dice:
    // "El método adjustGear invoca exactamente tres veces al método getInstantaneousSpeed()."
    // deberia decir
    // "El método getInstantaneousSpeed invoca exactamente tres veces al método getSpeed()"
    void testGetInstantaneousSpeedInvokes() {
        mockedEngineController.getInstantaneousSpeed();
        verify(mockedSpeedometer, times(3)).getSpeed();
    }

    @Test
    void testAdjustGearLogsNewGear() {

        when(mockedEngineController.getInstantaneousSpeed()).thenReturn(0.0);
        when(mockedTime.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        mockedEngineController.adjustGear();

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        GearValues testGear = GearValues.FIRST;
        when(mockedTime.getCurrentTime()).thenReturn(currentTime);
        String expectedLogMessage = testFormat.format(currentTime) + " Gear changed to " + testGear;

        verify(mockedLogger).log(eq(expectedLogMessage)); // Comprueba que la funcion log dentro de logger es llamado con el mensaje valido.
    }


    @Test
    void testAdjustGear() {

        when(mockedEngineController.getInstantaneousSpeed()).thenReturn(0.0);
        when(mockedTime.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        mockedEngineController.adjustGear();
        verify(mockedGearbox).setGear(eq(GearValues.FIRST)); // Comprueba que la setGear llamado con el mensaje valido.
    }
}