package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EngineControllerTest {

    @Mock
    private Logger logger;

    @Mock
    private Speedometer speedometer;

    @Mock
    private Gearbox gearbox;

    @Mock
    private Time time;

    private EngineController engineController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        engineController = new EngineController(logger, speedometer, gearbox, time);
    }

    @Test
    void recordGearLogFormatTest() {
        Timestamp t = Timestamp.valueOf("2025-01-01 12:00:00");
        when(time.getCurrentTime()).thenReturn(t);

        engineController.recordGear(GearValues.FIRST);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logger).log(captor.capture());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expected = sdf.format(t) + " Gear changed to " + GearValues.FIRST;

        assertEquals(expected, captor.getValue());
    }

    @Test
    void instantaneousSpeedAverageTest() {
        when(speedometer.getSpeed()).thenReturn(20.0, 30.0, 40.0);

        double result = engineController.getInstantaneousSpeed();

        assertEquals(30.0, result, 0.0001);
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    void adjustGearCallsGetInstantaneousSpeedOnce() {
        EngineController spyController = spy(engineController);

        doReturn(10.0).when(spyController).getInstantaneousSpeed();
        doNothing().when(spyController).setGear(any());
        doNothing().when(spyController).recordGear(any());

        spyController.adjustGear();

        assertEquals(1, mockingDetails(spyController).getInvocations().stream()
                .filter(i -> i.getMethod().getName().equals("getInstantaneousSpeed")).count());
    }

    @Test
    void adjustGearLogsNewGear() {
        EngineController spyController = spy(engineController);

        doReturn(10.0).when(spyController).getInstantaneousSpeed();
        doNothing().when(spyController).setGear(any());
        doNothing().when(spyController).recordGear(any());

        spyController.adjustGear();

        verify(spyController).recordGear(GearValues.FIRST);
    }

    @Test
    void adjustGearSetsNewGear() {
        EngineController spyController = spy(engineController);

        doReturn(10.0).when(spyController).getInstantaneousSpeed();
        doNothing().when(spyController).setGear(any());
        doNothing().when(spyController).recordGear(any());

        spyController.adjustGear();

        verify(spyController).setGear(GearValues.FIRST);
    }
}
