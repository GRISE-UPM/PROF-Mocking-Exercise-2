package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class EngineControllerTest {

    // Creamos los distintos mock necesarios de la clase EngineController
    @Mock private Logger mockLogger;
    @Mock private Speedometer mockSpeedometer;
    @Mock private Gearbox mockGearbox;
    @Mock private Time mockTime;
    
    // Inyectamos los mocks en la clase EngineController
    @InjectMocks private EngineController engController;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Timestamp time = new Timestamp(System.currentTimeMillis());

    private double inicio = 0.0, intermedio = 2.0, fin = 5.0;

    // Inicializamos todos los MockitosAnnotations de esta clase
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(value = "Test 1 : El mensaje de log tiene que estar en un formato correcto")
    public void testRecordGear() throws Exception {
        // Comportamiento
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        when(mockTime.getCurrentTime()).thenReturn(this.time);
        doNothing().when(mockLogger).log(argumentCaptor.capture());
        
        // Llamada
        engController.recordGear(GearValues.FIRST);
 
        // Captura
        String capturedMessage = argumentCaptor.getValue();
        
        // Formato
        String expectedMessage = sdf.format(this.time) + " Gear changed to " + GearValues.FIRST;

        // Comprobaciones
        assertEquals(capturedMessage, expectedMessage);
        verify(mockLogger, times(1)).log(argumentCaptor.capture());
    }

    @Test
    @DisplayName(value = "Test 2: Se calcula correctamente la velocidad instantánea")
    public void testGetInstantaneousSpeed() throws Exception {
        // Comportamiento del mockito
        Mockito.when(mockSpeedometer.getSpeed())
        .thenReturn(inicio)
        .thenReturn(intermedio)
        .thenReturn(fin);
        
        // Llamada y resultado esperado
        double captureResult = engController.getInstantaneousSpeed();
        double exceptedResult = (Double) (inicio + intermedio + fin) / 3;

        // Comprobaciones
        assertEquals(captureResult, exceptedResult);
        verify(mockSpeedometer,times(3)).getSpeed();
    }

    @Test
    @DisplayName(value = "Test 3: Comprobar tres veces que se calcula correctamente la velocidad instantánea")
    // Comprobar tres veces que se calcula correctamente la velocidad instantánea (GetInstantaneousSpeed)
    public void testAdjustGear1() throws Exception {
        // Comportamiento del mockito
        when(mockTime.getCurrentTime()).thenReturn(this.time);
        Mockito.when(mockSpeedometer.getSpeed())
        .thenReturn(inicio)
        .thenReturn(intermedio)
        .thenReturn(fin);

        // Llamada
        engController.adjustGear();

        // Comprobaciones
        Mockito.verify(mockSpeedometer, times(3)).getSpeed();
    }


    @Test
    @DisplayName(value = "Test 4: Loguear la nueva marcha")
    public void testAdjustGear2() throws Exception {
        // Comportamiento del mockito
        when(mockTime.getCurrentTime()).thenReturn(this.time);
        when(mockSpeedometer.getSpeed())
        .thenReturn(inicio)
        .thenReturn(intermedio)
        .thenReturn(fin);
        // Llamada
        engController.adjustGear();

        // Comprobaciones
        Mockito.verify(mockLogger, times(1)).log(anyString());

    }

    @Test
    @DisplayName(value = "Test 5: Asigna correctamente la nueva marcha")
    public void testAdjustGear3() throws Exception {
        // Comportamiento del mockito
        when(mockTime.getCurrentTime()).thenReturn(this.time);
        doNothing().when(mockGearbox).setGear(GearValues.FIRST);
        when(mockSpeedometer.getSpeed())
        .thenReturn(inicio)
        .thenReturn(intermedio)
        .thenReturn(fin);
        // Llamada
        engController.adjustGear();

        // Comprobaciones
        Mockito.verify(mockGearbox, times(1)).setGear(GearValues.FIRST);
    }
}
