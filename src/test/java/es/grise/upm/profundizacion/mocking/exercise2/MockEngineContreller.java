package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import es.grise.upm.profundizacion.mocking.exercise2.EngineController;

public class MockEngineContreller {
    
    @Test
    public void testRecordGearStop(){
        EngineController engineController = mock(EngineController.class);
        engineController.recordGear(GearValues.STOP);

        verify(engineController, times(1)).recordGear(GearValues.STOP);
    }
}
