package es.grise.upm.profundizacion.mocking.exercise2;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


/* *********************************************************************************
 * 
 * SUT
 */
public class EngineController {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger;
	private Speedometer speedometer;
	private Gearbox gearbox;
	private Time time;
	
	
	// Constructor, with dependency injection
	EngineController(Logger logger, Speedometer speedometer, Gearbox gearbox, Time time) {
		this.logger = logger;
		this.speedometer = speedometer;
		this.gearbox = gearbox;
		this.time = time;
	}
	
	
	/* ****************************************************************************
	 * 
	 * for: Dummy or Fake
	 *
	 * Logs the changes
	 */        
	void recordGear(GearValues newGear) {
		
		logger.log(sdf.format(time.getCurrentTime()) + " Gear changed to " + newGear);
	
	}

	
	/* ****************************************************************************
	 * 
	 * for: Mock
	 *
	 * Reads the speed three times to calculate an average speed
	 */
	double getInstantaneousSpeed() {
		
		double speed = 0;
		
		for(int i=0; i<=2; i++)
			speed += speedometer.getSpeed();
		
		
		return speed/3;
	}
	
	
	/* ****************************************************************************
	 * 
	 * for: Spy
	 *
	 * Set the current gear
	 */
	void setGear(GearValues newGear) {
		
		gearbox.setGear(newGear);
		
	}
	
	
	/* ****************************************************************************
	 * 
	 * Function to test
	 */
	public void adjustGear() {
		double instantaneousSpeed = getInstantaneousSpeed();
		
		GearValues newGear = GearValues.STOP;
		
		// Limit is a stub
		if(instantaneousSpeed <= SpeedLimit.FIRST) newGear = GearValues.FIRST;
		if(instantaneousSpeed > SpeedLimit.FIRST && instantaneousSpeed <= SpeedLimit.SECOND) newGear = GearValues.SECOND;
		// ...
		setGear(newGear);
		recordGear(newGear);
	}
}
