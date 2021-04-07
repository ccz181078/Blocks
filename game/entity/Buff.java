package game.entity;

import util.BmpRes;

public abstract class Buff extends Entity{
	public Agent target;
	public Buff(Agent tg){
		target=tg;
	}
	public void update(){
		if(target.isRemoved())remove();
	}
	public abstract void draw(graphics.Canvas cv);
	public double hardness(){return game.entity.NormalAttacker.HD;}
	
	@Override
	public void update0(){
		super.update0();
		shadowed=false;
	}

	@Override
	public BmpRes getBmp(){return null;}
	@Override
	public boolean chkBlock(){return false;}
	@Override
	public boolean chkRigidBody(){return false;}
	@Override
	public boolean chkEnt(){return false;}
	@Override
	public boolean chkAgent(){return false;}
	@Override
	public double gA(){return 0;}
	
	public void move(){
		x=target.x;
		y=target.y;
	}
}