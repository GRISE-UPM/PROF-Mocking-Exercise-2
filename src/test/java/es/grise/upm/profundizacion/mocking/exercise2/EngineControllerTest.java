package es.grise.upm.profundizacion.mocking.exercise2;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class EngineControllerTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Timestamp ts = new Timestamp(System.currentTimeMillis());

    private Logger loggerMock;
    private Speedometer speedometerMock;
    private Gearbox gearboxMock;
    private Time timeMock;
    private EngineController engineController;

    @BeforeEach
    public void setUp() {
        this.loggerMock = mock(Logger.class);
        this.speedometerMock = mock(Speedometer.class);
        this.gearboxMock = mock(Gearbox.class);
        this.timeMock = mock(Time.class);
        engineController = new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock);
    }

    @Test //El mensaje de log tieme el formato correcto (método recordGear()).
    public void test_recordGear() {
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(loggerMock).log(valueCapture.capture());

        when(timeMock.getCurrentTime()).thenReturn(ts);
        this.engineController.recordGear(GearValues.FIRST);

        String expectedLog = sdf.format(ts) + " Gear changed to " + GearValues.FIRST;
        assertEquals(valueCapture.getValue(), expectedLog);
    }

    @Test //Se calcula correctamente la velocidad instantánea (método getInstantaneousSpeed()).
    public void test_getInstantaneousSpeed() {
        when(speedometerMock.getSpeed()).thenReturn(10.0, 20.0, 30.0);

        double expectedAverageSpeed = 20.0;
        double actualAverageSpeed = engineController.getInstantaneousSpeed();

        assertEquals(expectedAverageSpeed, actualAverageSpeed);
    }

    @Test //El método adjustGear invoca exactamente tres veces al método getInstantaneousSpeed().
    public void test_adjustGear_getInstantaneousSpeed() {
        when(timeMock.getCurrentTime()).thenReturn(ts);
        when(speedometerMock.getSpeed()).thenReturn(25.0);
        this.engineController.adjustGear();

        verify(speedometerMock, times(3)).getSpeed();
    }

    @Test //El método adjustGear loguea la nueva marcha (método recordGear()).
    public void test_adjustGear_recordGear() {
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(loggerMock).log(valueCapture.capture());

        when(timeMock.getCurrentTime()).thenReturn(ts);
        when(speedometerMock.getSpeed()).thenReturn(25.0);
        this.engineController.adjustGear();

        String expectedLog = sdf.format(ts) + " Gear changed to " + GearValues.STOP;
        assertEquals(valueCapture.getValue(), expectedLog);
    }

    @Test //El método adjustGear asigna correctamente la nueva marcha (método setGear()).
    public void test_adjustGear_setGear() {
        when(timeMock.getCurrentTime()).thenReturn(ts);
        when(speedometerMock.getSpeed()).thenReturn(25.0);
        this.engineController.adjustGear();

        verify(gearboxMock, times(1)).setGear(GearValues.STOP);
    }


}


