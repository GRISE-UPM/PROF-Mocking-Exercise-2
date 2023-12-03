package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class EngineContreller {

    //Constantes
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Timestamp hora = new Timestamp(System.currentTimeMillis());

    //Mocks
    Logger loggerMock;
    Speedometer speedometerMock;
    Gearbox gearboxMock;
    Time timeMock;

    EngineController engineContreller;

    @BeforeEach
    public void setUp(){
        loggerMock = mock(Logger.class);
        speedometerMock = mock(Speedometer.class);
        gearboxMock = mock(Gearbox.class);
        timeMock = mock(Time.class);

        engineContreller = new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock);
    }

    @Test
    public void testRecordGear(){
        when(timeMock.getCurrentTime()).thenReturn(hora);
        ArgumentCaptor<String> logMarchas = ArgumentCaptor.forClass(String.class); 
        doNothing().when(loggerMock).log(logMarchas.capture());
        
        engineContreller.recordGear(GearValues.STOP);
        assertEquals(formatoFecha.format(hora) + " Gear changed to " + GearValues.STOP,
            logMarchas.getValue());
    }

    @Test
    public void testGetInstantaneousSpeed(){
        when(speedometerMock.getSpeed()).thenReturn(10.0, 20.0, 30.0);
        assertEquals(20.0, engineContreller.getInstantaneousSpeed());
    }
    
    @Test
    public void testAdjustGearNumberofInvocations(){
        when(timeMock.getCurrentTime()).thenReturn(hora);
        when(speedometerMock.getSpeed()).thenReturn(10.0, 20.0, 30.0);
        engineContreller.adjustGear();
        verify(speedometerMock, times(3)).getSpeed();
    }

    @Test
    public void testChangeToNewGear(){
        when(timeMock.getCurrentTime()).thenReturn(hora);
        when(speedometerMock.getSpeed()).thenReturn(30.0, 30.0, 29.0);
        ArgumentCaptor<String> logMarchas = ArgumentCaptor.forClass(String.class); 
        doNothing().when(loggerMock).log(logMarchas.capture());
        
        engineContreller.adjustGear();
        assertEquals(formatoFecha.format(hora) + " Gear changed to " + GearValues.STOP,
            logMarchas.getValue());
    }
    
    @Test
    public void testSetGear(){
        when(timeMock.getCurrentTime()).thenReturn(hora);
        when(speedometerMock.getSpeed()).thenReturn(30.);

        engineContreller.adjustGear();
        verify(gearboxMock).setGear(GearValues.STOP);
    }
}
