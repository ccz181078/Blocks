package game.entity;

import util.BmpRes;
import game.item.SelectableItemList;
import game.item.Iron;

public class ZombieRobot extends Zombie{
	private static final long serialVersionUID=1844677L;

	static BmpRes
	body=new BmpRes("Entity/Zombie/robot_body"),
	hand=new BmpRes("Entity/Zombie/robot_hand"),
	leg=new BmpRes("Entity/Zombie/robot_leg");

	BmpRes bodyBmp(){return body;}
	BmpRes handBmp(){return hand;}
	BmpRes legBmp(){return leg;}
	@Override
	public double light(){
		return 0.3;
	}
	public ZombieRobot(double _x,double _y){
		super(_x,_y);
	}
	public double hardness(){return game.entity.NormalAttacker.IRON;}

	@Override
	public double maxHp(){
		return super.maxHp()*2;
	}
	
	@Override
	void upgrade(){
		Zombie z=new FloatingZombie(x,y);
		z.dir=dir;

		z.add();
		kill();
	}

	@Override
	void onKill(){
		super.onKill();
		new Iron().drop(x,y,8);
	}
	
	
}
