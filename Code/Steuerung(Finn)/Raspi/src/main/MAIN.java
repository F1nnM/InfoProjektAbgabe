package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import datatypes.LineMapRefined;
import datatypes.NXT_COLLAB;
import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.pc.comm.NXTCommException;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.PathFinder;
import sql.SQL;

public class MAIN {

	static LineMapRefined map = new LineMapRefined();
	
	static double minX = -100;
	static double maxX = 100;
	static double minY = -100;
	static double maxY = 100;
	static Waypoint wp;

	public static void main(String[] args) throws DestinationUnreachableException, IOException, NXTCommException, InterruptedException {
		
		main.Logger.debug();
		main.Logger.log("Fuck yeah");
		NXT_COLLAB robot = new NXT_COLLAB("NXT-06", "NXTCore2");
		
		SQL.connect("localhost", 3306, "data", "robot", "InsaneSecurePassword123");
		SQL.init();

		new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean foundSuitableWaypoint = false;
				while (true) {
					if (robot.isTurning)
						continue;
					Logger.log("1");
					updateLineMap(updateSQL(robot.readNewDataSet()));
					Logger.log("2");
					PathFinder pf = new NodePathFinder(new AstarSearchAlgorithm(), new FourWayGridMesh(map, 10F, 30F));
					Logger.log("3");
					while (!foundSuitableWaypoint) {
						try {
							Logger.log("4");
							if(robot.finishedMoving()) wp = getNextWaypoint();
							else robot.cancel();
							//main.Logger.log(""+wp);
							System.out.println(wp);
							robot.followPath(pf.findRoute(robot.getPose(), wp));
							foundSuitableWaypoint = true;
							//main.Logger.log(""+true);
							System.out.println("true");
						} catch (DestinationUnreachableException e) {
							foundSuitableWaypoint = false;
							System.out.println("false");
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		Logger.log("0");
	}

	private static Waypoint getNextWaypoint() {
		Random r = new Random();
		return new Waypoint(minX+r.nextInt((int) ( maxX - minX + 1)), minX+r.nextInt((int) (maxX  - minX + 1)) );
	}

	private static void updateLineMap(ArrayList<Node> newNodes) {
		
		ArrayList<Line> newLines = new ArrayList<>();
		for (Node n1 : newNodes) {
			for (Node n2 : n1.getNeighbors()) {
				newLines.add(new Line(n1.x, n1.y, n2.x, n2.y));
				maxX = Math.max(n1.x, maxX);
				maxY = Math.max(n1.y, maxY);
				minX = Math.min(n1.x, minX);
				minY = Math.min(n1.y, minY);
			}
		}
		map = new LineMapRefined(newLines.toArray(new Line[0]), new Rectangle((float) minX, (float) minY, (float) (maxX - minX), (float) (maxY - minY)));
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					map.createSVGFile("outfile"+System.currentTimeMillis()+".svg");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();*/
	}
	
	private static ArrayList<Node> updateSQL(ArrayList<Node> cloud) {
		for (Node dot : cloud) {
			SQL.save(dot.x, dot.y);
		}
		return cloud;
	}
}
