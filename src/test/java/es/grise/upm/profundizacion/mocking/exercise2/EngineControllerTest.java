package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class EngineControllerTest {

    @Mock
    private Logger loggerMock;

    @Mock
    private Speedometer speedometerMock;

    @Mock
    private Gearbox gearboxMock;

    @Mock
    private Time timeMock;

    private EngineController engineController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        engineController = new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock);
    }

    @Test
    public void testRecordGear() {
        GearValues newGear = GearValues.FIRST_GEAR;

        engineController.recordGear(newGear);

        // Verify that the logger's log method is called with the expected parameters
        verify(loggerMock).log("Gear changed to: " + newGear);
    }

    @Test
    public void testGetInstantaneousSpeed() {
        double expectedSpeed = 50.0;

        // Configure the speedometer mock to return the expected speed
        when(speedometerMock.getSpeed()).thenReturn(expectedSpeed);

        double result = engineController.getInstantaneousSpeed();

        // Verify that the speedometer's getSpeed method is called
        verify(speedometerMock).getSpeed();

        // Verify that the returned speed matches the expected speed
        assertEquals(expectedSpeed, result, 0.01);
    }

    @Test
    public void testSetGear() {
        GearValues newGear = GearValues.SECOND_GEAR;

        engineController.setGear(newGear);

        // Verify that the gearbox's setGear method is called with the expected gear
        verify(gearboxMock).setGear(newGear);
    }

    @Test
    public void testAdjustGear() {
        // Add your test case for the adjustGear method here
    }
}


