package resources;

import java.util.UUID;
import java.awt.Point;
import java.io.Serializable;

/** Base class representing all resources and their properties. */
public abstract class Resource implements Serializable {
	private static final long serialVersionUID = 1L;

	UUID cluster;
	
	public String type;
	public boolean harvestable;
	public boolean pathable;
	
	public Point center;
	public int shape;
	public int size;
	
	protected Resource(Point center, String shape, int size, boolean pathable, boolean harvestable, String type) {
		cluster = UUID.randomUUID();
		this.center = center;
		this.shape = evaluateShape(shape);
		this.size = size;
		this.pathable = pathable;
		this.harvestable = harvestable;
		this.type = type;
	}
	
	public static Class<?> toClass(String name) {
		String s = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		Class<?> c = null;
		try {
			c = Class.forName("resources." + s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	} 

	public static boolean isPathable(String name) {
		try {
			return toClass(name).getField("pathable").getBoolean(null);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int evaluateShape(String shape) {
		switch (shape) {
		case "circular":return 0;
		case "square"  :return 1;
		case "scatter" :return 2;
		case "polygon" :return 3;
		default		   :return 0;
		}
	}	
}