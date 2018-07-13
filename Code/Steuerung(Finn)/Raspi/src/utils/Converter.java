package utils;

public class Converter {
	
	public static double degreeToSteeringRotation(double degree) {
		return 20160/360*degree;
	}
	
	public static double degreeToRobotRotation(double degree) {
		return centimetersToMotorRotation((27.2*Math.PI)/360*degree);
	}
	
	public static double centimetersToMotorRotation(double centimeters) {
		return 190.98593171*centimeters;
	}
	
}
