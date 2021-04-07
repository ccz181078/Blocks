package game.block;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class BloodBlock extends WoodenType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/BloodBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public int fuelVal(){return 1280;}
	double state=0;
	public void touchEnt(int x,int y,Entity ent){
		super.touchEnt(x,y,ent);
		if(!(ent instanceof Agent))return;
		Agent a=(Agent)ent;
		double v=intersectionLength(x,y,ent);
		if(state>=0){
			v=max(0,min(v,a.maxHp()-a.hp));
			a.addHp(v);
			state+=v;
		}else{
			v=max(0,min(v,a.hp));
			a.loseHp(v,SourceTool.block(x,y,this));
			state+=v;
		}
		if(state>30)state=-state;
	}
};
