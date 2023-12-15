package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EngineControllerTest {
    Logger log;
    Speedometer speed;
    Gearbox gearbox;
    Time time;
    EngineController c;

    @BeforeEach
    public void resetEngine() {
        log = mock(Logger.class);
        speed = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);
        time = spy(Time.class);
        c = spy(new EngineController(log, speed, gearbox, time));
    }

    @Test
    public void testLogFormat() {        
        c.recordGear(GearValues.STOP);
        c.recordGear(GearValues.FIRST);
        c.recordGear(GearValues.SECOND);
        
        verify(log, times(3)).log(matches("\\d{4}-[01]\\d-[0-3]\\d [0-2]\\d:[0-5]\\d:[0-5]\\d Gear changed to (STOP|FIRST|SECOND)"));
    }
    
    @Test
    public void testCorrectInstSpeed() {
        double realVal = 20.0;
        double val1 = realVal / 2;
        double val2 = realVal * 2;
        double val3 = realVal;
        
        when(speed.getSpeed()).thenReturn(val1);
        when(speed.getSpeed()).thenReturn(val2);
        when(speed.getSpeed()).thenReturn(val3);

        assertEquals(realVal, c.getInstantaneousSpeed());
    }
    
    @Test
    public void testAdjGearThreeTimes() {
        int desiredTimes = 3;

        c.adjustGear();
        verify(speed, times(desiredTimes)).getSpeed();
    }
    
    @Test
    public void testAdjGearRecords() {
        double realVal = 10.0;
        double val1 = realVal / 2;
        double val2 = realVal * 2;
        double val3 = realVal;
        
        when(speed.getSpeed()).thenReturn(val1);
        when(speed.getSpeed()).thenReturn(val2);
        when(speed.getSpeed()).thenReturn(val3);

        c.adjustGear();

        verify(c).recordGear(GearValues.FIRST);
    }
    
    @Test
    public void testAdjGearSetsFirst() {
        double realSpeed = 10.0;
        double val1 = realSpeed / 2;
        double val2 = realSpeed * 2;
        double val3 = realSpeed;
        
        when(speed.getSpeed()).thenReturn(val1);
        when(speed.getSpeed()).thenReturn(val2);
        when(speed.getSpeed()).thenReturn(val3);

        c.adjustGear();

        verify(c).setGear(GearValues.FIRST);
    }
    
    @Test
    public void testAdjGearSetsSecond() {
        double realSpeed = 21.0;
        double val1 = realSpeed / 2;
        double val2 = realSpeed * 2;
        double val3 = realSpeed;
        
        when(speed.getSpeed()).thenReturn(val1);
        when(speed.getSpeed()).thenReturn(val2);
        when(speed.getSpeed()).thenReturn(val3);

        c.adjustGear();

        verify(c).setGear(GearValues.SECOND);
    }
}