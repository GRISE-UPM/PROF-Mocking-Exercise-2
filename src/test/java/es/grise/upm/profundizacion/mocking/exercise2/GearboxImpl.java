package es.grise.upm.profundizacion.mocking.exercise2;

public class GearboxImpl implements Gearbox{

    public GearValues gear;

    @Override
    public
    void setGear(GearValues gear){
        this.gear = gear;
    }
}
