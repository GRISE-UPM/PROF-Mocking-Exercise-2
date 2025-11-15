package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.beans.Transient;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class EngineControllerTest {

    @Test
	void LogFormatTest() {
        Logger logger = Mockito.mock(Logger.class);
        Speedometer speedometer = Mockito.mock(Speedometer.class);
        Gearbox gearbox = Mockito.mock(Gearbox.class);
        Time time = new Time();

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).log(valueCapture.capture());

        EngineController engineController =
                new EngineController(logger, speedometer, gearbox, time);

        engineController.recordGear(GearValues.FIRST);

        //Correct format is "YYYY-MM-DD HH-MM-SS Gear changed to FIRST"
        assertTrue(valueCapture.getValue().matches(
                "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to FIRST"));
	}

    @Test
    void CorrectInstantaneousSpeedTest(){
        Logger logger = Mockito.mock(Logger.class);
        Speedometer speedometer = Mockito.mock(Speedometer.class);
        Gearbox gearbox = Mockito.mock(Gearbox.class);
        Time time = new Time();

        when(speedometer.getSpeed()).thenReturn(30.0);

        EngineController engineController =
                new EngineController(logger, speedometer, gearbox, time);

        assertEquals(30.0, engineController.getInstantaneousSpeed());
    }

    @Test
    void AdjustGear3TimesInvokesInstSpeedTest(){
        Logger logger = mock(Logger.class);
        Speedometer speedometer = mock(Speedometer.class);
        Gearbox gearbox = mock(Gearbox.class);
        Time time = new Time();

        when(speedometer.getSpeed()).thenReturn(10.0);

        EngineController engineController =
                new EngineController(logger, speedometer, gearbox, time);
        EngineController spyController = spy(engineController);

        spyController.adjustGear();

        //TODO: el test pide verificar que getInstantaneousSpeed
        // se ejecuta 3 veces cuando lo hace 1 vez
        verify(spyController, times(1)).getInstantaneousSpeed();

        //TODO: lo que si se ejecuta 3 veces es getSpeed dentro de getInstantaneousSpeed
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    void AdjustGearInvokesRecordGearTest(){
        Logger logger = mock(Logger.class);
        Speedometer speedometer = mock(Speedometer.class);
        Gearbox gearbox = mock(Gearbox.class);
        Time time = new Time();

        when(speedometer.getSpeed()).thenReturn(10.0);

        EngineController engineController =
                new EngineController(logger, speedometer, gearbox, time);
        EngineController spyController = spy(engineController);

        spyController.adjustGear();

        verify(spyController, times(1)).recordGear(any());

    }

    @Test
    void AdjustGearInvokesSetGearTest(){
        Logger logger = mock(Logger.class);
        Speedometer speedometer = mock(Speedometer.class);
        Gearbox gearbox = mock(Gearbox.class);
        Time time = new Time();

        when(speedometer.getSpeed()).thenReturn(10.0);

        EngineController engineController =
                new EngineController(logger, speedometer, gearbox, time);
        EngineController spyController = spy(engineController);

        spyController.adjustGear();

        verify(spyController, times(1)).setGear(any());
    }
}
