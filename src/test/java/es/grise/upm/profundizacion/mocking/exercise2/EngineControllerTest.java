package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.*;
import es.grise.upm.profundizacion.mocking.exercise2.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;

public class EngineControllerTest {

    private Logger loggerMock;
    private Speedometer speedometerMock;
    private Gearbox gearboxMock;
    private Time timeMock;
    private EngineController engineController;

    @BeforeEach
    public void setup() {
        loggerMock = mock(Logger.class);
        speedometerMock = mock(Speedometer.class);
        gearboxMock = mock(Gearbox.class);
        timeMock = mock(Time.class);
        engineController = new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock);
    }
    @Test
    public void RecordGearTest() {
        GearValues dummyGear = GearValues.FIRST;
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(1734412800000L));
        engineController.recordGear(dummyGear);
        String expectedMessage = "2024-12-17 06:20:00 Gear changed to " + dummyGear;
        verify(loggerMock).log(expectedMessage);
    }
    @Test
    public void GetInstantaneousSpeedTest() {
        when(speedometerMock.getSpeed()).thenReturn(30.0, 40.0, 50.0);
        double speed = engineController.getInstantaneousSpeed();
        double expectedSpeed = (30.0 + 40.0 + 50.0) / 3;
        assertEquals(expectedSpeed, speed);
    }
    @Test
    public void TresLlamadasAdjustGearTest() {
        EngineController spyController = spy(engineController);
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(1734412800000L));
        spyController.adjustGear();
        verify(spyController, times(3)).getInstantaneousSpeed();
    }
    @Test
    public void LoguearMarchaTest() {
        when(speedometerMock.getSpeed()).thenReturn(30.0, 40.0, 50.0);
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(1734412800000L));
        engineController.adjustGear();
        String expectedMessage = "2024-12-17 06:20:00 Gear changed to STOP";
        verify(loggerMock).log(expectedMessage);
    }
    @Test
    public void AsignarMarchaTest() {
        when(timeMock.getCurrentTime()).thenReturn(new Timestamp(1734412800000L));
        when(speedometerMock.getSpeed()).thenReturn(10.0);
        engineController.adjustGear();
        verify(gearboxMock).setGear(GearValues.FIRST);
    }
}
