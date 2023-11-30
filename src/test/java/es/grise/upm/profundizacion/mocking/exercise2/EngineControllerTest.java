package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EngineControllerTest {

    @Mock
    Logger logger;

    @Mock
    Gearbox gearBox;

    @Mock
    Speedometer speedo;

    Time time = new Time();  

    EngineController engController;

    @BeforeEach
    void initController(){
        engController = new EngineController(logger, speedo, gearBox, time);
    }

    @Test
    @DisplayName(value = "Test 1 : El mensaje de log tiene el formato correcto (método recordGear())")
    void whenCallingRecordGearOutputIsCorrect(){
        engController.recordGear(GearValues.FIRST);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(logger, times(1)).log(argumentCaptor.capture());
        String capturedLogMessage = argumentCaptor.getValue();
        String expectedLogMessagePattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to FIRST";
        assertTrue(capturedLogMessage.matches(expectedLogMessagePattern),
                "Log message does not match the expected pattern: " + capturedLogMessage);
    }

    @Test
    @DisplayName(value = "Test 2 : Se calcula correctamente la velocidad instantánea (método getInstantaneousSpeed())")
    void whenCallingGetInstantaneousSpeed_SpeedIsCorrect(){
        Mockito.when(speedo.getSpeed())
        .thenReturn(0.0)
        .thenReturn(10.0)
        .thenReturn(20.0);
        double expected = 10.0;
        double actual = engController.getInstantaneousSpeed();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Test 3 : El método adjustGear invoca exactamente tres veces al método getSpeed()")
    void whenCallingAdjustGear_getSpeedIsCalledThreeTimes(){
        engController.adjustGear();
        Mockito.verify(speedo, times(3)).getSpeed();
    }


    @Test
    @DisplayName(value = "Test 4 : El método adjustGear registra la nueva marcha (método recordGear())")
    void whenCallingAdjustGear_recordGearIsCalled(){
        engController.adjustGear();
        Mockito.verify(logger, times(1)).log(anyString());
    }


    @Test
    @DisplayName(value = "Test 5 : El método adjustGear asigna correctamente la nueva marcha (método setGear())")
    void whenCallingAdjustGear_gearIsProperlyAssigned(){
        Mockito.when(speedo.getSpeed())
        .thenReturn(0.0)
        .thenReturn(10.0)
        .thenReturn(20.0);
        engController.adjustGear();
        Mockito.verify(gearBox, times(1)).setGear(GearValues.FIRST);
    }

    @Test
    @DisplayName(value = "Test 6 : El método getInstantaneousSpeed invoca exactamente getSpeed() tres veces")
    void whenCallingGetInstantaneousSpeed_getSpeedIsCalledThreeTimes(){
        engController.getInstantaneousSpeed();
        Mockito.verify(speedo, times(3)).getSpeed();
    }
}
