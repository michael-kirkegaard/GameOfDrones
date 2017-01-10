package expAI;

import java.awt.Point;

import map.Map;
import resources.*;
import util.Position;

public class ExpDrone extends Drone {
	int radius;
	
	public ExpDrone(Map map, Position position) {
		super(map, position);
		this.TYPE = "EXPDRONE";
		this.radius=7;
	}

	@Override
	protected void doRun() {
		while(true) {
			synchronized (map.render) {
				try {
					map.render.wait();
				} catch (InterruptedException e) {
					
				}
			}
			
			
			move(moveDrone(position,this.radius));
			move(0);
		}
	}
	
	
	/**
	 * Help function to test
	 * @param p
	 * @param dir
	 * @return
	 */
	/*
	private Position moveFieldsToCheck(Position p, int dir){
		if(dir<0){
			p.setX(p.getX()-1);
		}
		else{
			p.setY(p.getY()+1);
		}		
		return p; 
	}*/
	
	/**
	 * Help function to move drone
	 * @param d
	 * @param dir
	 * @return
	 */
	private Point moveDrone(Position d, int radius){
		int q = getQuadrant(d);
		int dir=0;
		Position[] posArr = getFieldsToCheck(d);
		dir = getDirFromRadius(posArr[1],posArr[0], radius);
		//new place for drone to be is called NP
		Point nP = new Point(d.getX(), d.getY());
		switch(q){
			case 1 : if(dir<0) nP.move(nP.x-1,nP.y);
					 else      nP.move(nP.x, nP.y+1);
					 break;
			
			case 2 : if(dir<0) nP.move(nP.x, nP.y-1); 
					 else      nP.move(nP.x-1, nP.y);
					 break;
			case 3 : if(dir<0) nP.move(nP.x+1, nP.y); 
					 else      nP.move(nP.x, nP.y-1); 
					 break;
			case 4 : if(dir<0) nP.move(nP.x, nP.y+1);
					 else      nP.move(nP.x+1, nP.y); 
			         break;
			default: nP.move(nP.x, nP.y);
					 break;
		}		
		return nP; 
	}

	/**
	 * UNUSED help function for moveDrone
	 * @param p
	 * @param x
	 * @param y
	 * @return Position
	 */
//	private Position interQuadrantMoving(Position p, int x, int y){
//		p.setX(x);
//		p.setY(y);
//		return p;
//	}
//	
	/**
	 * Gets the direction using {@link #pythagoras(Position)}method
	 * @param p1
	 * @param p2
	 * @param radius
	 * @return int of what direction to move. 1 for up, -1 for left
	 */
	private int getDirFromRadius(Position p1, Position p2, int radius){
		int dir=0;
		double c1 = 0;
		double c2 = 0;
		c1 = pythagoras(p1);
		c2 = pythagoras(p2);
		
		if(Math.abs(radius-c1)>Math.abs(radius-c2)){
			dir=-1;
		}
		else{
			dir=1;
		}
		return dir;
	}
	
	
	/**
	 * Takes p.x and p.y and returns radius using pythagoras
	 * @param p
	 * @return squared c in pythagoras
	 */
	private double pythagoras(Position p){
		int a=p.getX();
		int b=p.getY();
		
		return Math.sqrt(Math.pow(a,2)+Math.pow(b, 2));
	}
	
	
//	
//	private int[] getPointInUnknown(){
//		int[] a = new int[2];
//		return a;
//	}
	
	/**
	 * 
	 * @param dir
	 * @param p
	 * @return
	 */
	private Position[] getFieldsToCheck(Position p){
		Position[] arr = new Position[2];
		int q; 
		q=getQuadrant(p);
		switch(q){
			case 1 : arr[0]= new Position(p.getX()-1, p.getY());
					 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
			
			case 2 : arr[0]= new Position(p.getX(), p.getY()-1);
			 		 arr[1]= new Position(p.getX()-1, p.getY());
					 break;
			case 3 : arr[0]= new Position(p.getX()+1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()-1);
					 break;
			
			case 4 : arr[0]= new Position(p.getX(), p.getY()+1);
			 		 arr[1]= new Position(p.getX()+1, p.getY());
					 break;
	
			default :
					break;
		}
		
		return arr;
	}

	private int getQuadrant(Position p) {
		int q=0;
		
		if(p.getX()>0 && p.getY()>=0) q=1;
		else if(p.getX()<=0 && p.getY()>0) q=2;
		else if(p.getX()<0 && p.getY()<=0) q=3;
		else if(p.getX()>=0 && p.getY()<0) q=4;
		
		return q;
	}
	

	
}