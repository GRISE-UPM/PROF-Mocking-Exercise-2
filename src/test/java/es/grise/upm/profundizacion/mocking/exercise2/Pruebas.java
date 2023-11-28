package es.grise.upm.profundizacion.mocking.exercise2;

import es.grise.upm.profundizacion.mocking.exercise2.*;

public class Pruebas {
    
    public static void main(String[] args) {
        Logger logger = new LoggerImp();
        Speedometer speedometer = new SpeedometerImp();
        Gearbox gearbox = new GearboxImp();

        EngineController engineController = 
            new EngineController(logger, speedometer, gearbox, new Time());
        engineController.recordGear(GearValues.STOP);
    }

    /**
     * LogerImp implements Logger
     */
    public class LoggerImp implements Logger {
        public void log(String logMessage) {
            System.out.println(logMessage);
        }
    }

    /**
     * SpeedometerImp implements Speedometer
     */

    public class SpeedometerImp implements Speedometer {
        public double getSpeed() {
            return 0.0;
        }
    }


    /**
     * GearboxImp implements Gearbox
     */
    public class GearboxImp implements Gearbox {
        private GearValues gear;

        public void setGear(GearValues gear) {
            this.gear = gear;
        }

        public GearValues getGear() {
            return gear;
        }
    }
    
}
