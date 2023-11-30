package es.grise.upm.profundizacion.mocking.exercise2;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

public class EngineControllerTest {

    // Global variables:

    private Logger mockLogger = mock(Logger.class);
    private Speedometer mockSpeedometer = mock(Speedometer.class);
    private Gearbox mockGearbox = mock(Gearbox.class);
    private Time mockTime = mock(Time.class);

    private EngineController realController = new EngineController(mockLogger, mockSpeedometer, mockGearbox, mockTime); //Real controller


    @Test
    public void correctLogFormat(){
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1701029476L)); //fake time value
        realController.recordGear(GearValues.FIRST); //call the real recorder method, so as to call "log"

        verify(mockLogger).log("1970-01-20 17:30:29 Gear changed to FIRST"); //check that mockLogger's log call matches the fake time value
    }

    @Test
    public void correctInstantSpeed(){
        EngineController mockController = mock(EngineController.class); //Mock Controller

        when(mockController.getInstantaneousSpeed()).thenReturn(100.0); //set-up for the mock

        when(mockSpeedometer.getSpeed()).thenReturn(0.0, 300.0, 0.0);//set-up for the real controller

        //Verify that getInstantaneousSpeed returned the right value:
        assertEquals(mockController.getInstantaneousSpeed(), realController.getInstantaneousSpeed()); //Compare real vs mock execution

    }

    @Test
    public void correctCallsGetSpeed(){
        Timestamp mockTimestamp = mock(Timestamp.class);

        EngineController spyController = spy(realController); //A spy, so as to actually use methods 

        when(mockTime.getCurrentTime()).thenReturn(mockTimestamp); //Necessary so that recordGear does not fail

        // Call adjustGear 3 times
        spyController.adjustGear();
        spyController.adjustGear();
        spyController.adjustGear();

       verify(spyController, times(3)).getInstantaneousSpeed(); //make sure getInstantaneousSpeed was called 3 times
    }

    @Test
    public void correctRecordGear(){
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1701029476L)); //fake time value

        EngineController spyController = spy(realController); //Spy Controller

        spyController.adjustGear(); // Call adjustGear

        verify(spyController, times(1)).recordGear(GearValues.FIRST); // Verify that recordGear has been called
    }

    @Test
    public void correctSetGear(){
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1701029476L)); //fake time value

        EngineController spyController = spy(realController); //Spy Controller

        spyController.adjustGear(); // Call adjustGear

        verify(spyController, times(1)).setGear(GearValues.FIRST); //check that the setGear method was called
        
        ArgumentCaptor<GearValues> argumentCaptor = ArgumentCaptor.forClass(GearValues.class); //set up an argument captor
        verify(spyController).setGear(argumentCaptor.capture()); //spy on the execution of setGear, and have its argument be captured

        GearValues value = argumentCaptor.getValue(); // the captured value has to be the one used by the controller, "GearValues.FIRST"
        assertEquals(value, GearValues.FIRST); //Verify that the GearValue set was the correct one
    }
}
