package resources;

import java.awt.Point;

import map.Map;

public class Gold extends Resource {

	public static final String type = "GOLD";
	public static final boolean harvestable = true;
	public static final boolean pathable = false;

	public Gold(Point center, String shape, int size) {
		super(center, shape, size);
		super.pathable = pathable;
		super.harvestable = harvestable;
		super.type = type;
	}

}