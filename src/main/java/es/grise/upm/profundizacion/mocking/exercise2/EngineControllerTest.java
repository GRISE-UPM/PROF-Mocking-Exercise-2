package es.grise.upm.profundizacion.mocking.exercise2;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class EngineControllerTest {
    private Logger mockLogger = mock(Logger.class);
    private Speedometer mockSpeedometer = mock(Speedometer.class);
    private Gearbox mockGearbox = spy(Gearbox.class);
    private Time mockTime = mock(Time.class);
    private EngineController engineController = new EngineController(mockLogger, mockSpeedometer, mockGearbox, mockTime);
    @DisplayName("Comprueba si mensaje de log tiene el formato correcto (método recordGear())")
    @Test
    void testLogMessageWithCorrectFormat() {
        GearValues gearValue = GearValues.FIRST;
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1679786700000L));
        engineController.recordGear(gearValue);

        verify(mockLogger).log("2023-03-26 00:25:00 Gear changed to FIRST");
    }

    @DisplayName("Comprueba si se calcula correctamente la velocidad instantánea (método getInstantaneousSpeed()).")
    @Test
    void testInstantaneousSpeedIsCalculatedCorrectly() {
        reset(mockSpeedometer);

        when(mockSpeedometer.getSpeed()).thenReturn(1.0, 5.0, 77.0);
        double result = engineController.getInstantaneousSpeed();

        assertEquals((double) 83/3, result);
        verify(mockSpeedometer, times(3)).getSpeed();
    }

    @DisplayName("Comprueba si adjustGear invoca exactamente tres veces al método getInstantaneousSpeed().\n" +
            "Comprueba si adjustGear loguea la nueva marcha (método recordGear()).\n" +
            "Comprueba si adjustGear asigna correctamente la nueva marcha (método setGear()).")
    @Test
    void testProbesAllMethods() {
        reset(mockLogger);
        reset(mockSpeedometer);
        reset(mockTime);
        reset(mockGearbox);

        when(mockSpeedometer.getSpeed()).thenReturn(1.0, 5.0, 77.0);
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1679786700000L));
        engineController.adjustGear();

        verify(mockLogger).log("2023-03-26 00:25:00 Gear changed to STOP");
        verify(mockSpeedometer, times(3)).getSpeed();
        verify(mockGearbox).setGear(GearValues.STOP);

        reset(mockGearbox);
        when(mockSpeedometer.getSpeed()).thenReturn(1.0, 2.0, 3.0);
        engineController.adjustGear();
        verify(mockGearbox).setGear(GearValues.FIRST);
    }
}
