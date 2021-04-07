package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import java.util.Arrays;
import java.util.Collections;

public class EnergyMonster extends NormalAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/EnergyMonster");
	public BmpRes getBmp(){return bmp;}
	public double maxHp(){return 20;}
	Group group(){return Group.ENERGY;}
	@Override
	public double light(){
		return 0.5;
	}
	public EnergyMonster(double _x,double _y){
		super(_x,_y);
	}
	public double gA(){return 0.006;}
	public Entity getBall(){return new EnergyBall();}

	public AttackFilter getAttackFilter(){return energy_filter;}
	
	void onKill(){
		new game.item.EnergyStone().drop(x,y,rndi(1,2));
		super.onKill();
	}
}


