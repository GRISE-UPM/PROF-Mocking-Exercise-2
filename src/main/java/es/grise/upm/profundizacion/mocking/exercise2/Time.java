package es.grise.upm.profundizacion.mocking.exercise2;

import java.sql.Timestamp;

public class Time {
	
	Timestamp getCurrentTime() {
		
		return new Timestamp(System.currentTimeMillis());
		
	}

}
