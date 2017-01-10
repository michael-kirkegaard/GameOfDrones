package resources;

import java.awt.Point;
import java.util.UUID;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import map.*;
import util.Position;

public class Drone extends Agent {
	
	protected Map map;
	protected String TYPE;
	protected Position position;
	
	public Drone(Map map, Position position) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.position = position;
	}
	
	protected void doRun() {
		while(true) {
			synchronized (map.render) {
				try {
					map.render.wait();
				} catch (InterruptedException e) {
					
				}
			}
			move(0);			
		}
	}
	
	protected void explore() throws Exception {
		for (Point p : World.getNeighbors(position.toPoint())) {
			Template t = new Template(new ActualTemplateField(p.x), new ActualTemplateField(p.y));
			boolean b = (queryp(t) == null) ? put(new Tuple(p.x, p.y), Self.SELF) : false;
		}
	}
	
	protected void move(int dir) {
		int[] xy = getDirection(dir, position.x, position.y);
		move(new Point(xy[0], xy[1]));
	}
	
	protected void move(Point p) {
		if (p.distance(position.toPoint()) > 1.21)
			return;
		try {
			Template template = new Template(new ActualTemplateField(TYPE),
					new ActualTemplateField(position.x),
					new ActualTemplateField(position.y));
			get(template, Self.SELF);
			int[] xy = new int[]{p.x,p.y};
			position.move(xy[0], xy[1]);
			Tuple t2 = new Tuple(TYPE, xy[0], xy[1]);
			put(t2, Self.SELF);
			explore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int[] getDirection(int dir, int x, int y){
		switch(dir){
			case 0: x-=1; break; // LEFT
			case 1: x+=1; break; // RIGHT
			case 2: y-=1; break; // UP
			case 3: y+=1; break; // DOWN
		}
		return new int[]{x,y};
	}
}
