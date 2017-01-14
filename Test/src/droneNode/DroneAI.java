package droneNode;

import java.awt.Point;
import java.util.LinkedList;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.Self;

import baseNode.MapMerger;
import map.*;
import util.Position;

public abstract class DroneAI extends Agent {
	
	//Assigned by Main class
	public static PointToPoint self2base;
	public static PointToPoint self2map;
	public static PointToPoint[] self2drone;
	//--------------------------------------
	
	public String type;
	public String id;
	public Point position;
	
	public DroneAI(Point position, String type, String id) {
		super(id);
		this.type = type;
		this.id = id;
		this.position = position;
	}
	
	//Main move method for drones
	protected abstract Point moveDrone() throws Exception;
	
	@Override
	protected final void doRun() throws Exception {
		while(true){
			try {
				//wait for go signal
				get(new Template(new ActualTemplateField("go")), Self.SELF);
				//moves
				move(moveDrone());
				//put rdy signal in own tuplespace
				put(new Tuple("ready"),Self.SELF);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private final void move(Point p) {
		if (p.distance(position) > 1.21)
			return;
		try {
			Template template = new Template(
							new ActualTemplateField(type),
							new ActualTemplateField(position.x),
							new ActualTemplateField(position.y));
			
			get(template, self2base);
			position.move(p.x, p.y);
			Tuple t2 = new Tuple(type, p.x, p.y);
			put(t2, self2base);
			if(type.equals(ExpDrone.type)) explore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void explore() throws Exception {
		for (Point p : getNeighbors(position)) {
			Template t0 = new Template(new FormalTemplateField(String.class), new ActualTemplateField(p.x), new ActualTemplateField(p.y));
			Template t1 = new Template(new ActualTemplateField(MapMerger.MAP_EDGE), new FormalTemplateField(Integer.class));
			int radius = query(t1, self2base).getElementAt(Integer.class, 1);
			if(p.distance(new Point(0,0)) > radius && queryp(t0) == null)
				put(new Tuple(MapMerger.ACTION_NEW, p.x, p.y), self2map);
		}
	}
	
	private LinkedList<Point> getNeighbors(Point p) {
		return getNeighbors(p, 1);
	}
	private LinkedList<Point> getNeighbors(Point p, int dist) {
		LinkedList<Point> list = new LinkedList<Point>();
		for (int y = p.y-dist; y <= p.y+dist; y+=dist)
			for (int x = p.x-dist; x <= p.x+dist; x+=dist) 
				if (!(x == p.x && y == p.y))
					list.add(new Point(x,y));
		return list;
	}
}

