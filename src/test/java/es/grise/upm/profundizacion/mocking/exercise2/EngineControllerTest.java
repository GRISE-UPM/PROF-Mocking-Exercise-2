package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
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
    void whenCallingAdjustGear_recordGearIsCalled(){
        engController.adjustGear();
        Mockito.verify(logger, times(1)).log(anyString());
    }


    @Test
    void whenCallingAdjustGear_gearIsProperlyAssigned(){
        Mockito.when(speedo.getSpeed())
        .thenReturn(0.0)
        .thenReturn(10.0)
        .thenReturn(20.0);
        engController.adjustGear();
        Mockito.verify(gearBox, times(1)).setGear(GearValues.FIRST);
    }

    @Test
    void whenCallingGetInstantaneousSpeed_getSpeedIsCalledThreeTimes(){
        engController.getInstantaneousSpeed();
        Mockito.verify(speedo, times(3)).getSpeed();
    }
}
