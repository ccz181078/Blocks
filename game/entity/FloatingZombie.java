package game.entity;

import util.BmpRes;

public class FloatingZombie extends ZombieRobot{
	private static final long serialVersionUID=1844677L;

	static BmpRes
	body=new BmpRes("Entity/Zombie/floating_body");

	BmpRes bodyBmp(){return body;}

	public FloatingZombie(double _x,double _y){
		super(_x,_y);
	}

	@Override
	public double maxHp(){return super.maxHp()*2;}

	@Override
	public void update(){
		super.update();
		if(!armor.isEmpty()&&armor.get().isClosed())return;
		climbable=true;
		if(f<0.04)f=0.04;
		if(ydep<0)ydir=1;
		if(ydep>0)ydir=-1;
	}

	@Override
	void upgrade(){
		
	}

	@Override
	void onKill(){
		new game.item.EnergyStone().drop(x,y,4);
		super.onKill();
	}
	
	
}
