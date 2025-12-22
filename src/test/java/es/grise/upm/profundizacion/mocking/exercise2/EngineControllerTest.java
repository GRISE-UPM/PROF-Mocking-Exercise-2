package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

class EngineControllerTest {

    @Mock
    private Speedometer speedometer;
    @Mock
    private Logger logger;
    @Mock
    private Time time;
    @Mock
    private Gearbox gearbox;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private EngineController engineController;

    @BeforeEach
    void setup() {
        logger = mock(Logger.class);
        speedometer = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);
        time = mock(Time.class);

        engineController = new EngineController(logger, speedometer, gearbox, time);
    }

    @Test
    void recordGear_logsCorrectMessageFormat() {
        Timestamp fixedTimestamp = new Timestamp(0);
        when(time.getCurrentTime()).thenReturn(fixedTimestamp);

        engineController.recordGear(GearValues.FIRST);

        String expectedMessage =
                sdf.format(fixedTimestamp) + " Gear changed to FIRST";

        verify(logger, times(1)).log(expectedMessage);
    }

    @Test
    void getInstantaneousSpeed_returnsAverageOfThreeMeasurements() {

        when(speedometer.getSpeed())
                .thenReturn(30.0, 60.0, 90.0);

        double result = engineController.getInstantaneousSpeed();

        assertEquals(60.0, result);
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    void adjustGear_logsNewGear() {
        when(speedometer.getSpeed()).thenReturn(SpeedLimit.FIRST - 1);

        Timestamp fixedTimestamp = new Timestamp(0);
        when(time.getCurrentTime()).thenReturn(fixedTimestamp);

        engineController.adjustGear();

        String expectedMessage = sdf.format(fixedTimestamp) + " Gear changed to FIRST";
        verify(logger, times(1)).log(expectedMessage);

    }


    @Test
    void adjustGear_setsCorrectGearBasedOnSpeed() {

        when(speedometer.getSpeed()).thenReturn(SpeedLimit.FIRST - 1);

        Timestamp fixedTimestamp = new Timestamp(0);
        when(time.getCurrentTime()).thenReturn(fixedTimestamp);

        engineController.adjustGear();

        verify(gearbox, times(1)).setGear(GearValues.FIRST);
    }
}
