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
	public EnergyMonster(double _x,double _y){
		super(_x,_y);
		es=new Enemy[4];
	}
	double gA(){return 0.006;}
	protected Entity getBall(){return new EnergyBall();}

	public AttackFilter getAttackFilter(){return energy_filter;}
	
	void onKill(){
		new game.item.EnergyStone().drop(x,y,rndi(1,2));
	}
}


