package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;

public class EngineControllerTest {

    @Mock private Logger mockLogger;
    @Mock private Speedometer mockSpeedometer;
    @Mock private Gearbox mockGearbox;
    @Mock private Time mockTime;

    private EngineController engineController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); 
        engineController = new EngineController(mockLogger, mockSpeedometer, mockGearbox, mockTime);
    }

    @Test
    public void testGetInstantaneousSpeed() {
        when(mockSpeedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0);
        double speed = engineController.getInstantaneousSpeed();
        assertEquals(20.0, speed, 0.01);
    }

    @Test
    public void testGetInstantaneousSpeedInvocations() {
        when(mockSpeedometer.getSpeed()).thenReturn(10.0);
        engineController.getInstantaneousSpeed();
        verify(mockSpeedometer, times(3)).getSpeed();
    }

    @Test
    public void testRecordGearLogFormat() {
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        engineController.recordGear(GearValues.FIRST);
        verify(mockLogger).log(argThat(s -> s.contains("Gear changed to FIRST")));
    }

    @Test
    public void testAdjustGearLogsNewGear() {
        // Stub necesarios para evitar NullPointer
        when(mockSpeedometer.getSpeed()).thenReturn(10.0);
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

        engineController.adjustGear();

        // Verifica que se hizo log del cambio de marcha
        verify(mockLogger).log(anyString());
    }

    @Test
    public void testAdjustGearSetsCorrectGear() {
        // Stub necesarios para evitar NullPointer
        when(mockSpeedometer.getSpeed()).thenReturn(10.0);
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

        engineController.adjustGear();

        // Verifica que se asignó la marcha correcta
        verify(mockGearbox).setGear(GearValues.FIRST);
    }
}
