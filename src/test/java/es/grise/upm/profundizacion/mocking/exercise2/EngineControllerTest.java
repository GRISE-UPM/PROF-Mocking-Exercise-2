package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class EngineControllerTest{
    
	// El SUT
    private  EngineController engine;


    // Collaborator que se van a mockear
    private Logger logger;
	private Speedometer speedometer;
	private Gearbox gearbox;
	private Time time;
	
	//Variables para los Collaborator
	SimpleDateFormat sdf;
	Timestamp timeStamp;

    // Estado inicial de la prueba
    @BeforeEach
    public void setUp ()
    {
    	sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	timeStamp= new Timestamp(System.currentTimeMillis());
    	
    	logger = mock(Logger.class);
    	speedometer = mock(Speedometer.class);
    	gearbox = mock(Gearbox.class);
    	time = mock(Time.class);
    	
    	engine = new EngineController(logger, speedometer,gearbox,time);
    }

    //Comprobar que el mensaje de log tieme el formato correcto
    @Test
    public void test_recordGear() 
    {
    	
    	when(time.getCurrentTime()).thenReturn(timeStamp);

    	String log_correcto = sdf.format(timeStamp)+" Gear changed to FIRST";
    	
    	engine.recordGear(GearValues.FIRST);
    	
    	verify(logger).log(log_correcto);
    }

    //Comprobar que se calcula correctamente la velocidad instantánea
    @Test
    public void test_getInstantaneousSpeed ()
    {
    	when(speedometer.getSpeed()).thenReturn(20.0);
    	
    	assertEquals(20, speedometer.getSpeed());
    }

    //Comprobar que el método adjustGear invoca exactamente tres veces al método getInstantaneousSpeed()
    @Test
    public void test_getInstantaneousSpeed_checkThree ()
    {
    	when(speedometer.getSpeed()).thenReturn(20.0);
    	when(time.getCurrentTime()).thenReturn(timeStamp);
    	
    	engine.adjustGear();
    	
    	verify(speedometer,times(3)).getSpeed();
    	
    }

    //Comprobar que el método adjustGear loguea la nueva marcha
    @Test
    public void test_logNewGear ()
    {	
    	when(speedometer.getSpeed()).thenReturn(10.0); 
    	when(time.getCurrentTime()).thenReturn(timeStamp);
    	
    	//Si nuestra velocidad es 10.0 entonces debería poner la primera marcha
    	String log_correcto = sdf.format(timeStamp)+" Gear changed to FIRST";
    	
    	engine.adjustGear();

    	verify(logger).log(log_correcto);
    	verifyNoMoreInteractions(logger);
    }
    
    //Comprobar que el método adjustGear asigna correctamente la nueva marcha 
    @Test
    public void test_setGear ()
    {
    	when(speedometer.getSpeed()).thenReturn(20.0);
    	when(time.getCurrentTime()).thenReturn(timeStamp);
    	
    	engine.adjustGear();
    	
    	verify(gearbox).setGear(GearValues.FIRST);
    	verifyNoMoreInteractions(gearbox);
    }
    
}
