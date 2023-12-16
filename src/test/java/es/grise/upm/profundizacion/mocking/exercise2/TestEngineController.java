package es.grise.upm.profundizacion.mocking.exercise2;



import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.DisplayName;
import java.util.List;



public class TestEngineController {

    // El SUT
    private  EngineController engine;


    // Collaborator que se van a mockear
    private Logger logger;
	private Speedometer speedometer;
	private Gearbox gearbox;
	private Time time;

    // Estado inicial de la prueba
    @BeforeEach
    public void edoInicial (){

        // mockeamos los collaborators
        logger = mock(Logger.class);
        speedometer = mock(Speedometer.class);
        gearbox = mock(Gearbox.class);
        time = mock(Time.class);

        // el SUT creado con collaborator mockeados
        engine = new EngineController(logger, speedometer, gearbox, time);
        
    }

    // Comprobamos que el mensaje de log (DENTRO DE recordGear()) tiene el formato correcto.
    // Para ello utilizamos el ArgumentCapture
    @Test
    @DisplayName("- - - Metodo recordGear() - - -")
    public void recordGear_method() {

        // Para evitar NPE en recordGear() al realizar la llamada de log() producto de no tener un timestamp
        // realizamos un when que cuando se llame al colaborator del mock de time, devuelva lo minimo necesario
        when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

        // Llamadas a recordGear
        engine.recordGear(GearValues.FIRST);
        engine.recordGear(GearValues.SECOND);
        engine.recordGear(GearValues.STOP);

        // ArgumentCaptor del metodo Log para ver si los parametros de entrada del metodo log() cumplen el formato pertinente
        ArgumentCaptor<String> argLog = ArgumentCaptor.forClass(String.class);

        // Verficiar que hemos llamado al metodo log( < SimpleDateFormat(  Time <mockeado> ) + " Gear changed to " + GearValue.enum > ) 
        // mediante logger (mockeado) 3 veces y capturamos los argumentos con los que se ha realizado la llamada
        verify(logger, times(3)).log(argLog.capture());


        List<String> logParameters = argLog.getAllValues();
        for (String s : logParameters){
            System.out.println(s);
        }
        assertTrue(logParameters.get(0).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to (STOP|FIRST|SECOND)"));
        assertTrue(logParameters.get(1).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to (STOP|FIRST|SECOND)"));
        assertTrue(logParameters.get(2).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to (STOP|FIRST|SECOND)"));

    }


    @Test
    @DisplayName("- - - Metodo getInstantaneousSpeed() - - -")
    public void getInstantaneousSpeed_method (){
        // Reads the speed three times to calculate an average speed.
        // Por ello: "They are returned in the specified sequence". Habra que poner 3 returns, 
        // ya que se llama tres veces a getSpeed() 
        when(speedometer.getSpeed()).thenReturn(1.0).thenReturn(2.0).thenReturn(3.0);
        double velocidadActual = engine.getInstantaneousSpeed();
        double velocidadEsperada = 2.0;

        // verificamos que se ha llamado tres veces a getSpeed()
        verify(speedometer, times(3)).getSpeed();


        assertEquals(velocidadEsperada, velocidadActual);
    }


    @Test
    @DisplayName("- - - Metodo adjustGear()  invoca exactamente tres veces getInstantaneousSpeed() - - -")
    public void adjustGear_method_checkThree (){


        // Para que cada vez que llame a getSpeed() no salga NPE
        when(speedometer.getSpeed()).thenReturn(1.0).thenReturn(2.0).thenReturn(3.0);

        // Al final del metodo, al llamar a recordGear() usa Time, por lo que para evitar NPE hay que utilizar un when/thenReturn
        when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        
        engine.adjustGear();
        // verificamos que se ha llamado tres veces a getSpeed()
        verify(speedometer, times(3)).getSpeed();


    }

    @Test
    @DisplayName("- - - Metodo adjustGear() loggea la marcha - - -")
    public void adjustGear_method_logGear (){


        // Para que cada vez que llame a getSpeed() no salga NPE
        when(speedometer.getSpeed()).thenReturn(1.0).thenReturn(2.0).thenReturn(3.0);

        // Al final del metodo, al llamar a recordGear() usa Time, por lo que para evitar NPE hay que utilizar un when/thenReturn
        when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        
        engine.adjustGear();

        // ArgumentCaptor del metodo Log para ver si los parametros de entrada del metodo log() cumplen el formato pertinente
        ArgumentCaptor<String> argLog = ArgumentCaptor.forClass(String.class);
        verify(logger, times(1)).log(argLog.capture());

        // Comprobamos el resultado, a pesar que se comprueba en el otro metodo de prueba de recordGear_method() lo valores de entrada del metodo log()
        // Igualmente comprobamos el contenido. Con verify(logger, times(1)).log(anyString()) pudiesemos hacerlo, pero queremos comprobar el contenido
        assertTrue(argLog.getValue().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to (STOP|FIRST|SECOND)"));

    }
    @Test
    @DisplayName("- - - Metodo adjustGear() asigna correctamente la nueva marcha con setGear() - - -")
    public void adjustGear_method_setGear (){


        // Para que cada vez que llame a getSpeed() no salga NPE
        when(speedometer.getSpeed()).thenReturn(1.0).thenReturn(2.0).thenReturn(3.0);

        // Al final del metodo, al llamar a recordGear() usa Time, por lo que para evitar NPE hay que utilizar un when/thenReturn
        when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        
        engine.adjustGear();

        // Al ir a 6km/hora, debe meter primera ya que la velocidad es menor a 20km/hora
        verify(gearbox, times(1)).setGear(GearValues.FIRST);
        

    }

}
