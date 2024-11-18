package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EngineControllerTest {

    private Logger logger;
    private Speedometer speedometer;
    private Gearbox gearbox;
    private Time time;
    private EngineController engineController;

    @BeforeEach
    public void setup() {
        this.logger = mock(Logger.class);
        this.speedometer = mock(Speedometer.class);
        this.gearbox = mock(Gearbox.class);
        this.time = mock(Time.class);
        this.engineController = new EngineController(logger, speedometer, gearbox, time);
    }

    @Test
    public void recordGearTest() {
        GearValues mockGear = GearValues.FIRST;
        
        when(time.getCurrentTime()).thenReturn(new Timestamp(0));

        engineController.recordGear(mockGear);

        verify(logger).log("1970-01-01 01:00:00 Gear changed to " + mockGear);
    }

    @Test
    public void instantaneousSpeedTest() {

        when(speedometer.getSpeed()).
        thenReturn(10.0).
        thenReturn(20.0).
        thenReturn(30.0);

        double result = engineController.getInstantaneousSpeed();

        //verificamos que se ha llamado 3 veces al método getSpeed
        verify(speedometer, times(3)).getSpeed();

        //verificamos que el resultado es el esperado
        assertEquals(20.0, result, "Compara resultado esperado y el obtenido" );
    }

    @Test
    public void setnumllamadas() {
        when(speedometer.getSpeed()).thenReturn(10.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0)); //Sino falla `por que lo llama en recordgear
        engineController.adjustGear();

        verify(speedometer, times(3)).getSpeed();
    }

    @Test
    public void setMarchaTest() {
        when(speedometer.getSpeed()).thenReturn(10.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0)); //Sino falla `por que lo llama en recordgear
        engineController.adjustGear();

        verify(gearbox).setGear(GearValues.FIRST);
    }

    @Test
    public void setrecordgear() {
        when(speedometer.getSpeed()).thenReturn(15.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0)); //Sino falla `por que lo llama en recordgear
        engineController.adjustGear();

        verify(logger).log(Mockito.anyString());
    }
    
}
