package es.grise.upm.profundizacion.mocking.exercise2;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EngineControllerTest {

private final Logger loggerMock = mock(Logger.class);
private final Speedometer speedometerMock = mock(Speedometer.class);
private final Gearbox gearboxMock = mock(Gearbox.class);
private final Time timeMock = mock(Time.class);

private final EngineController engineController = new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock);

   
    @Test
    public void test_recordGear(){
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(speedometerMock.getSpeed()).thenReturn(50.0,40.0,60.0);
        // fecha yyyy-MM-dd HH:mm:ss seguido de texto
        String formato = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to [a-zA-Z ]+";
        engineController.adjustGear();
        ArgumentCaptor<String> logAc = ArgumentCaptor.forClass(String.class);
        verify(loggerMock,times(1)).log(logAc.capture());
        assertTrue(logAc.getValue().matches(formato));
    }
    
    @Test
    public void test_getInstantaneousSpeed(){
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(speedometerMock.getSpeed()).thenReturn(50.0,40.0,60.0);
        Double spavg = engineController.getInstantaneousSpeed();
        assertEquals(50.0, spavg,0.001);    
    }

     @Test
    public void test_adjustGear_getInstantaneousSpeed(){
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(speedometerMock.getSpeed()).thenReturn(50.0,40.0,60.0);
        engineController.adjustGear();
        verify(speedometerMock, times(3)).getSpeed();
    }
    @Test
    public void test_adjustGear_recordGear(){
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(speedometerMock.getSpeed()).thenReturn(50.0,40.0,60.0);
        engineController.adjustGear();
        ArgumentCaptor<String> logAc = ArgumentCaptor.forClass(String.class);
        verify(loggerMock).log(logAc.capture());
        System.out.println(logAc.getValue()); 
    }

    @Test
    public void test_adjustGear_setGear(){
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(speedometerMock.getSpeed()).thenReturn(50.0,40.0,60.0);
        engineController.adjustGear();
        ArgumentCaptor<GearValues> logAc = ArgumentCaptor.forClass(GearValues.class);
        verify(gearboxMock,times(1)).setGear(logAc.capture());
        System.out.println(logAc.getValue().toString()); 

    }

}
