package droneNode;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import map.Map; //TEMP
import util.AStarPoint;
import util.Position;

public class HarDrone extends DroneAI {
	public static int droneCounter = 0;
	
	boolean hasHarvested = false;
	LinkedList<Point> pathOut = new LinkedList<Point>();
	LinkedList<Point> pathHome = new LinkedList<Point>();
	
	public HarDrone(Point position) {
		super(position, "HARDRONE" + droneCounter++);
	}
	
	@Override
	protected void doRun() {
		while(true) {
			try {
				get(new Template(new ActualTemplateField("go")), Self.SELF);
				move();
				put(new Tuple("ready"),Self.SELF);
			} catch (Exception e){
				e.printStackTrace();
			}			
		}
	}
	
	private void move(){
		if (pathOut.isEmpty() && pathHome.isEmpty()) {
			//target point from base
			//call a star on target point
			//ASTAR
		}
			
		else if (!pathOut.isEmpty()) { //on way out
			Point target = pathOut.remove(0);
			super.move(target);
			pathHome.add(0, target);
			
			//////////////////////////////////////////
			// remeber to implement removing of res //
			//////////////////////////////////////////
			if (pathOut.isEmpty()) hasHarvested=true;
		}
		else { //on way home
			Point target = pathHome.remove(0);
			super.move(target);
		}
	}
	
	private static ArrayList<Point> aStar(Point pointStart, Point pointEnd, Map map){
		//Declaration of lists
		LinkedList<AStarPoint> closedSet = new LinkedList<AStarPoint>();
		LinkedList<AStarPoint> openSet = new LinkedList<AStarPoint>();
		
		//conversion of Points to AstarPoints
		AStarPoint start = new AStarPoint(pointStart.x,pointStart.y);
		AStarPoint end = new AStarPoint(pointEnd.x,pointEnd.y);
		
		//init of start node
		start.gscore=0;
		start.fscore=start.distance(end);
		//Init of openSet
		openSet.add(start);
		
		
		while (!openSet.isEmpty()){
			AStarPoint current;
			
			//Getting node with lowest fScore
			Iterator<AStarPoint> it = openSet.iterator();
			current=it.next();
			while(it.hasNext()) {
				AStarPoint temp = it.next();
				if(temp.fscore<current.fscore) current=temp;
			}
			
			if(current.equals(end)) return reconstructPath(current);
			
			//moving current to closedSet
			openSet.remove(current);
			closedSet.add(current);
			
			//hacks to get AStarPoint neigbors list
			LinkedList<AStarPoint> neighbors = AStarPoint.convertPointList(map.RetrievePathableNeighbors(current));
			
			for(AStarPoint neighbor : neighbors){
				//ignores already visited points.
				if(closedSet.contains(neighbor)) continue;
				
				int tentativeGscore = current.gscore + 1;
				
				//if neighbor is not registered before
				if(!openSet.contains(neighbor)) openSet.add(neighbor);
				
				//if neighbor is registered before
				//investigate if this path is better
				else if(tentativeGscore>=neighbor.gscore) continue;
				
				//if neighbor is not registered before or this is a better path than the excisting
				//calculate following:
				
				neighbor.cameFrom=current;
				neighbor.gscore=tentativeGscore;
				neighbor.fscore=neighbor.gscore+neighbor.distance(end);
				
			}
			
		}
		
		return null;
	}

	private static ArrayList<Point> reconstructPath(AStarPoint end) {
		ArrayList<AStarPoint> path = new ArrayList<AStarPoint>();
		AStarPoint current=end;
		path.add(current);
		while(current.cameFrom!=null){
			current=current.cameFrom;
			path.add(0,current);
		}
		
		
		return AStarPoint.convertToPointList(path);
	}

}
