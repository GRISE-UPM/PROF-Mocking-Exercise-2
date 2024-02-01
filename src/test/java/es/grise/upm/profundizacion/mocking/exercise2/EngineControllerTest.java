package es.grise.upm.profundizacion.mocking.exercise2;

// Disculpa por el retraso 
// No habia visto el mensaje en moodle

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EngineControllerTest {
    private Time time = new Time();  
    private Logger logger = mock(Logger.class);
    private Gearbox gearbox = mock(Gearbox.class);
    private Speedometer speedometer = mock(Speedometer.class);
    private EngineController engineController = new EngineController(logger, speedometer, gearbox, time);

    @Test
    public void recordGear_Test() {
        GearValues gearValueFirst = GearValues.FIRST;
        engineController.recordGear(gearValueFirst);
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(logger, times(1)).log(valueCapture.capture());
        String expectedLoggerArg = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to " + gearValueFirst;
        assertTrue(valueCapture.getValue().matches(expectedLoggerArg));
        // verificación que faltaba en entrega anterior
        when(speedometer.getSpeed()).thenReturn(40.0);
        engineController.adjustGear();
        assertTrue(valueCapture.getValue().matches(expectedLoggerArg));}

    @Test
    public void setGear_Test() {
        GearValues expectedGear = GearValues.FIRST;
        engineController.setGear(expectedGear);
        verify(gearbox, times(1)).setGear(expectedGear);}

    @Test
    public void getInstantaneousSpeed_Test() {
        double expectedSpeed = 20.0;
        when(speedometer.getSpeed()).thenReturn(0.0, 20.0, 40.0);
        assertEquals(expectedSpeed, engineController.getInstantaneousSpeed());
        verify(speedometer, times(3)).getSpeed();}

    @Test
    public void adjustGear_Test() {
        GearValues expectedGear = GearValues.FIRST;
        when(speedometer.getSpeed()).thenReturn(0.0, 20.0, 40.0);
        engineController.adjustGear();
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(logger, times(1)).log(valueCapture.capture());
        String capturedLoggerArg = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to " + expectedGear;
        assertTrue(valueCapture.getValue().matches(capturedLoggerArg));
        verify(gearbox, times(1)).setGear(expectedGear);}
}