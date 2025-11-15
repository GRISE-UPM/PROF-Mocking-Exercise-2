package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Timestamp;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class EngineControllerTest {

    Logger logger;
    Speedometer speedometer;
    Gearbox gearbox;
    Time time;
    EngineController engineController;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        speedometer = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);
        time = mock(Time.class);
        engineController = new EngineController(logger, speedometer, gearbox, time);
        when(time.getCurrentTime()).thenReturn(Timestamp.valueOf("2025-11-15 17:00:00"));
    }

    static Stream<Arguments> logs() {
        return Stream.of(
                arguments("2025-11-15 17:00:00", GearValues.FIRST, "2025-11-15 17:00:00 Gear changed to FIRST"),
                arguments("2025-11-15 17:00:00", GearValues.SECOND, "2025-11-15 17:00:00 Gear changed to SECOND"),
                arguments("2023-11-15 17:00:00", GearValues.SECOND, "2023-11-15 17:00:00 Gear changed to SECOND"));
    }

    @ParameterizedTest
    @MethodSource("logs")
    void logFormatTestFirst(String timeString, GearValues gear, String res) {
        when(time.getCurrentTime()).thenReturn(Timestamp.valueOf(timeString));

        engineController.recordGear(gear);
        verify(logger).log(res);
    }

    @Test
    void correctInstantaneousSpeedTest() {

        when(speedometer.getSpeed()).thenReturn(30.0);

        assertEquals(30.0, engineController.getInstantaneousSpeed());
    }

    @Test
    void correctMultipleInstantaneousSpeedTest() {

        when(speedometer.getSpeed()).thenReturn(20.0, 30.0, 40.0);

        assertEquals(30.0, engineController.getInstantaneousSpeed());
    }

    @Test
    void adjustGear3TimesInvokesSpeedTest() {

        when(speedometer.getSpeed()).thenReturn(10.0);

        engineController.getInstantaneousSpeed();
        //Specification says method InstantaneousSpeed, but we assume it is wrong
        //as it is only called 1 time
        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    void adjustGearInvokesRecordGearTest() {
        when(speedometer.getSpeed()).thenReturn(10.0);

        engineController = spy(engineController);

        engineController.adjustGear();

        verify(engineController, times(1)).recordGear(any());
    }

    @Test
    void adjustGearInvokesSetGearTest() {

        when(speedometer.getSpeed()).thenReturn(10.0);

        engineController = spy(engineController);

        engineController.adjustGear();

        verify(engineController, times(1)).setGear(any());
    }
}
