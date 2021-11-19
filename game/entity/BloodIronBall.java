package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class BloodIronBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public BloodIronBall(){
		super();
		hp=16*30;
	}
	public double mass(){return 1;}
	public boolean shouldKeepAwayFrom(){return true;}
	
	protected void drop(){
		new game.item.BloodEssence().drop(x,y,max(0,min(16,f2i(hp/30))));
		kill();
	}
	
	@Override
	void touchEnt(Entity ent){
		if(ent instanceof Agent){
			Agent a=(Agent)ent;
			if(a.hasBlood()){
				double c=min(20,min(a.hp,hp));
				BloodBall.drop(a,c,this);
				hp-=c;
			}
		}
		super.touchEnt(ent);
	}
	
	@Override
	public double gA(){return 0.03;}
	
	public BmpRes getBmp(){return game.item.BloodIronBall.bmp;}
}
