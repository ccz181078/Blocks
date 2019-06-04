package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class CactusMonster extends GreenMonster{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/CactusMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.25;}
	public double height(){return 0.25;}
	public double maxHp(){return 6;}
	double mass(){return 0.5;}
	
	
	public CactusMonster(double _x,double _y){super(_x,_y);}
	void onKill(){
		new game.block.CactusBlock().asItem().drop(x,y,1);
	}
}
