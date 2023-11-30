package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class EngineControllerTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Timestamp ts = new Timestamp(System.currentTimeMillis());

    @Mock
    private Logger loggerMock;

    @Mock
    private Speedometer speedometerMock;

    @Mock
    private Gearbox gearboxMock;

    @Mock
    private Time timeMock;

    @InjectMocks
    private EngineController engineController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // El mensaje de log tieme el formato correcto (método recordGear()).
    public void test_recordGear() {
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(loggerMock).log(valueCapture.capture());

        when(timeMock.getCurrentTime()).thenReturn(ts);
        this.engineController.recordGear(GearValues.FIRST);

        String expectedLog = sdf.format(ts) + " Gear changed to " + GearValues.FIRST;
        assertEquals(valueCapture.getValue(), expectedLog);

        verify(loggerMock, times(1)).log(anyString());
        verify(timeMock, times(1)).getCurrentTime();
    }

    @Test //Se calcula correctamente la velocidad instantánea (método getInstantaneousSpeed()).
    public void test_getInstantaneousSpeed() {
        when(speedometerMock.getSpeed()).thenReturn(3.0, 10.0, 20.0);
        
        double expectedAverageSpeed = 11.0; //speed (3+10+20)/ 3 = 11
        double actualAverageSpeed = engineController.getInstantaneousSpeed();

        assertEquals(expectedAverageSpeed, actualAverageSpeed);

        verify(speedometerMock, times(3)).getSpeed();
    }

    @Test //El método adjustGear invoca exactamente tres veces al método getInstantaneousSpeed().
    public void test_adjustGear_getInstantaneousSpeed() {
        when(timeMock.getCurrentTime()).thenReturn(ts);
        when(speedometerMock.getSpeed()).thenReturn(15.0);
        this.engineController.adjustGear();

        verify(speedometerMock, times(3)).getSpeed();
        verify(gearboxMock, times(1)).setGear(GearValues.FIRST);
    }

    @Test // El método adjustGear loguea la nueva marcha (método recordGear()).
    public void test_adjustGear_recordGear() {
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(loggerMock).log(valueCapture.capture());

        when(timeMock.getCurrentTime()).thenReturn(ts);
        when(speedometerMock.getSpeed()).thenReturn(30.0);
        this.engineController.adjustGear();

        String expectedLog = sdf.format(ts) + " Gear changed to " + GearValues.SECOND;
        assertEquals(valueCapture.getValue(), expectedLog);

        verify(loggerMock, times(1)).log(anyString());
        verify(timeMock, times(1)).getCurrentTime();
        verify(speedometerMock, times(3)).getSpeed();
    }

    @Test //El método adjustGear asigna correctamente la nueva marcha (método setGear()).
    public void test_adjustGear_setGear() {
        when(timeMock.getCurrentTime()).thenReturn(ts);
        when(speedometerMock.getSpeed()).thenReturn(50.0);
        this.engineController.adjustGear();

        verify(gearboxMock, times(1)).setGear(GearValues.STOP);
        verify(timeMock, times(1)).getCurrentTime();
        verify(loggerMock, times(1)).log(anyString());
        verify(speedometerMock, times(3)).getSpeed();
    }

}