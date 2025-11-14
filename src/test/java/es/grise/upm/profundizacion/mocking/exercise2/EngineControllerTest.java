package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EngineControllerTest {

	Logger logger;
	Speedometer speedometer;
	Gearbox gearbox;

    EngineController engineController;

    @BeforeEach
    public void setup(){
        logger = mock(Logger.class);
        speedometer = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);

        engineController = new EngineController(logger, speedometer, gearbox, new Time());
    }
    
    // El mensaje de log tiene el formato correcto (método recordGear())
    @Test
    public void recordGearTest(){

        engineController.recordGear(GearValues.STOP);

        verify(logger).log(argThat(msg -> 
            Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to STOP", msg)
        ));
    }

    // Se calcula correctamente la velocidad instantánea (método getInstantaneousSpeed())
    @Test
    public void getInstantaneousSpeedTest(){

        when(speedometer.getSpeed()).thenReturn(78.6)
                                    .thenReturn(0.001)
                                    .thenReturn(11.0);
        
        assertEquals(29.867, engineController.getInstantaneousSpeed());
    }

    // El método adjustGear invoca exactamente tres veces al método getInstantaneousSpeed()
    @Test
    public void adjustGearThreeTimeTest(){
        EngineController spyController = spy(new EngineController(logger, speedometer, gearbox, new Time()));

        doReturn(0.0).when(spyController).getInstantaneousSpeed();

        spyController.adjustGear();
        spyController.adjustGear();
        spyController.adjustGear();

        verify(spyController, times(3)).getInstantaneousSpeed();
    }

    // El método adjustGear loguea la nueva marcha (método recordGear())
    @Test
    public void adjustGearNewGearValueTest(){

        when(speedometer.getSpeed()).thenReturn(0.1)
                                    .thenReturn(0.1)
                                    .thenReturn(0.1);
        
        engineController.adjustGear();
        
        verify(logger).log(argThat(msg -> 
            msg.contains(GearValues.FIRST.name())
        ));
    }

    // El método adjustGear asigna correctamente la nueva marcha (método setGear())
    @Test
    public void adjustGearSetGearTest(){

        when(speedometer.getSpeed()).thenReturn(0.1)
                                    .thenReturn(0.1)
                                    .thenReturn(0.1);
        
        engineController.adjustGear();

        verify(gearbox).setGear(GearValues.FIRST);
    }
}