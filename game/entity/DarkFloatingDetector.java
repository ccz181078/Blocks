package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import util.BmpRes;

public class DarkFloatingDetector extends FloatingDetector{
	static BmpRes bmp=new BmpRes("Entity/DarkFloatingDetector");
	public BmpRes getBmp(){return bmp;}
	
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	public boolean chkAgent(){return true;}
	public boolean chkEnt(){return false;}
	
	public DarkFloatingDetector(double _x,double _y){super(_x,_y);}
	
}