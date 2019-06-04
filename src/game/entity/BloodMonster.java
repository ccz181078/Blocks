package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.world.World;
import game.world.Weather;

public class BloodMonster extends GreenMonster{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/BloodMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public BloodMonster(double _x,double _y){super(_x,_y);}
	void onKill(){
		new game.item.BloodEssence().drop(x,y,rndi(1,2));
	}

	public void update(){
		super.update();
		if(World._.weather==Weather._blood)hp=Math.min(maxHp(),hp+0.06);
	}
	

	void touchAgent(Agent ent){
		if(!(ent instanceof GreenMonster)){
			BloodBall.drop(ent,1);
		}
		super.touchAgent(ent);
	}
}
