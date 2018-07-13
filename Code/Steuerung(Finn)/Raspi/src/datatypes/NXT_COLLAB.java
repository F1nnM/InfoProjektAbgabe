package datatypes;

import java.util.ArrayList;

import datatype.NXTMotor;
import datatype.SensorPort;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.Path;
import listener.FinishedListener;
import main.Logger;
import nxt.NXT;
import sql.DatabaseManager;
import utils.Converter;

public class NXT_COLLAB {

	public NXT nxt1;
	public NXT nxt2;

	boolean hasFinished1 = false;
	boolean hasFinished2 = false;
	
	public boolean isTurning = false;
	
	private int pathLength = 0;
	
	private boolean cancel = false;

	public NXT_COLLAB(String name1, String name2) throws InterruptedException {
		nxt1 = new NXT();
		nxt2 = new NXT();

		nxt1.connect(name1, 1);
		nxt2.connect(name2, 1);
		
		nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S1).continuous();
		Thread.sleep(1500);
		nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S2).continuous();
		Thread.sleep(1500);
		nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S3).continuous();
		Thread.sleep(1500);
		nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S4).continuous();
		Thread.sleep(1500);
		nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S1).continuous();
		Thread.sleep(1500);
		nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S2).continuous();
		Thread.sleep(1500);
		nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S3).continuous();
		Thread.sleep(1500);
		nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S4).continuous();
		
		/*nxt1.waitTillFinished(new FinishedListener() {
			@Override
			public void onFinish() {
				hasFinished1 = true;
				System.out.println("NXT1.finished");
			}
		});
		nxt2.waitTillFinished(new FinishedListener() {
			@Override
			public void onFinish() {
				hasFinished2 = true;
				System.out.println("NXT2.finished");
			}
		});*/

	}

	public void driveTo(Waypoint wp) {
		double xDist = wp.x - DatabaseManager.getX();
		double yDist = wp.y - DatabaseManager.getY();
		System.out.println(""+xDist);
		System.out.println(""+yDist);
		double angle = Math.toDegrees(Math.atan2(xDist, yDist));
		System.out.println(""+angle);
		if(angle!=0) turnTo(angle);
		rotateMotorByDegrees(nxt1.getMotors().C,
				Converter.centimetersToMotorRotation(Math.sqrt((xDist * xDist) + (yDist * yDist))));
		rotateMotorByDegrees(nxt2.getMotors().C,
				Converter.centimetersToMotorRotation(Math.sqrt((xDist * xDist) + (yDist * yDist))));
		waitTillActionFinished();
	}

	public void turnTo(double degrees) {
		DatabaseManager.turning();
		isTurning = true;
		double degrees45 = Converter.degreeToSteeringRotation(45);

		rotateMotorByDegrees(nxt1.getMotors().A, degrees45);
		rotateMotorByDegrees(nxt1.getMotors().B, -degrees45);
		rotateMotorByDegrees(nxt2.getMotors().A, degrees45);
		rotateMotorByDegrees(nxt2.getMotors().B, -degrees45);

		double currentAngle = DatabaseManager.getViewingDirection();
		double toTurn1 = currentAngle - degrees;
		double toTurn2 = degrees - currentAngle;
		double toTurn;
		if (Math.abs(toTurn1) > Math.abs(toTurn2))
			toTurn = toTurn2;
		else
			toTurn = toTurn1;

		waitTillActionFinished();

		rotateMotorByDegrees(nxt1.getMotors().C, Converter.degreeToRobotRotation(toTurn));
		rotateMotorByDegrees(nxt2.getMotors().C, Converter.degreeToRobotRotation(-toTurn));
		waitTillActionFinished();

		rotateMotorByDegrees(nxt1.getMotors().A, -degrees45);
		rotateMotorByDegrees(nxt1.getMotors().B, degrees45);
		rotateMotorByDegrees(nxt2.getMotors().A, -degrees45);
		rotateMotorByDegrees(nxt2.getMotors().B, degrees45);

		waitTillActionFinished();

		DatabaseManager.doneTurning(degrees);
		isTurning = false;
	}

	private void rotateMotorByDegrees(NXTMotor m, double degree) {
		m.rotate((int) degree);
	}

	public ArrayList<Node> readNewDataSet() {
		ArrayList<Node> list = new ArrayList<>();
		// 1.1 : x-9 y+15
		// 1.2 : x+9 y+15
		// 1.3 : x-15 y+9
		// 1.4 : x+15 y+9
		// 2.1 : x+9 y-15
		// 2.2 : x-9 y-15
		// 2.3 : x+15 y-9
		// 2.4 : x-15 y-9
		int val11 = nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S1).getDistance();
		int val12 = nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S2).getDistance();
		int val13 = nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S3).getDistance();
		int val14 = nxt1.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S4).getDistance();
		int val21 = nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S1).getDistance();
		int val22 = nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S2).getDistance();
		int val23 = nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S3).getDistance();
		int val24 = nxt2.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S4).getDistance();
		System.out.println("1.1: "+val11);
		System.out.println("1.2: "+val12);
		System.out.println("1.3: "+val13);
		System.out.println("1.4: "+val14);
		System.out.println("2.1: "+val21);
		System.out.println("2.2: "+val22);
		System.out.println("2.3: "+val23);
		System.out.println("2.4: "+val24);
		double x = DatabaseManager.getX();
		double y = DatabaseManager.getY();
		//TODO: consider rotation
		if(val11 >0 && val11<230)
			list.add(new SelfConnectingNode((float) x - 9, (float) y + 15 + val11, 10F));
		if(val12 >0 && val12<230)
			list.add(new SelfConnectingNode((float) x + 9, (float) y + 15 + val12, 10F));
		if(val13 >0 && val13<230)
			list.add(new SelfConnectingNode((float) x - 15 - val13, (float) y + 9, 10F));
		if(val14 >0 && val14<230)
			list.add(new SelfConnectingNode((float) x + 15 + val14, (float) y + 9, 10F));
		if(val21 >0 && val21<230)
			list.add(new SelfConnectingNode((float) x + 9, (float) y - 15 - val21, 10F));
		if(val22 >0 && val22<230)
			list.add(new SelfConnectingNode((float) x - 9, (float) y - 15 - val22, 10F));
		if(val23 >0 && val23<230)
			list.add(new SelfConnectingNode((float) x + 15 + val23, (float) y - 9, 10F));
		if(val24 >0 && val24<230)
			list.add(new SelfConnectingNode((float) x - 15 - val24, (float) y - 9, 10F));

		return list;
	}

	public Pose getPose() {
		return new Pose((float) DatabaseManager.getX(), (float) DatabaseManager.getY(),
				(float) DatabaseManager.getViewingDirection());
	}

	public void followPath(Path p) {
		cancel = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Follow path: "+p);
				p.remove(0);
				while ((p.size() != 0)&&(!cancel)) {
					pathLength = p.size();
					Waypoint currentWaypoint = p.remove(0);
					System.out.println("Go to: "+currentWaypoint);
					System.out.println("Points Left: "+p.size());
					driveTo(currentWaypoint);
				}
				pathLength=0;
			}
		}).start();
	}
	
	public void cancel() {
		cancel = true;
		while (pathLength!=0) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean finishedMoving() {
		return pathLength==0;
	}

	public void waitTillActionFinished() {
		while (true) {
			if (hasFinished1&&hasFinished2)
				break;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		hasFinished1 = false;
		hasFinished2 = false;
		
		nxt1.waitTillFinished(new FinishedListener() {
			@Override
			public void onFinish() {
				hasFinished1 = true;
				System.out.println("NXT1.finished");
			}
		});
		nxt2.waitTillFinished(new FinishedListener() {
			@Override
			public void onFinish() {
				hasFinished2 = true;
				System.out.println("NXT2.finished");
			}
		});
		
		System.out.println("NXT has finished action.");
		printTrace();
	}
	
	private void printTrace() {
		  StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		  for (int i = 1; i < elements.length; i++) {
		    StackTraceElement s = elements[i];
		    System.out.println("\tat " + s.getClassName() + "." + s.getMethodName()
		        + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
		  }
	}
}
