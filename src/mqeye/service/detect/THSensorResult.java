package mqeye.service.detect;

import mqeye.service.serial.AbstractSensorResult;

public class THSensorResult extends AbstractSensorResult{

	private double temperature ;	//ÎÂ¶È
	
	private int humidity ;		//Êª¶È
	
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	
}
