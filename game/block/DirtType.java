package game.block;

import game.item.Item;
import game.world.World;
import game.entity.FallingBlock;
import static util.MathUtil.*;


public abstract class DirtType extends Block{
	private static final long serialVersionUID=1844677L;
	public void onPress(int x,int y,Item item){
		des(x,y,item.shovelVal(),item);
		item.onDesBlock(this);
	}
	public double hardness(){return game.entity.NormalAttacker.DIRT;}
	double minEnterVel(){return 1;}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(World.cur.get(x,y-1).isCoverable()){
			World.cur.setAir(x,y);
			new FallingBlock(x,y,this).add();
			return true;
		}
		if(rnd()<0.3){
			int dir=rnd()<0.5?-1:1;
			Block b=World.cur.get(x,y-1);
			if(b.rootBlock() instanceof DirtType
			&& World.cur.get(x+dir,y-1).isCoverable()
			){
				World.cur.setAir(x,y-1);
				new FallingBlock(x,y-1,b).initPos(x+0.5+dir*0.3,y-0.5,dir*rnd(0.01,0.1),0,b.src).add();
				World.cur.setAir(x,y);
				new FallingBlock(x,y,this).add();
				return true;
			}
		}
		if(damage>0)--damage;
		return false;
	}

	public boolean updateCond(int x,int y){
		return 
			World.cur.get(x,y-1).isCoverable()
			|| World.cur.get(x+1,y-1).isCoverable()
			|| World.cur.get(x-1,y-1).isCoverable();
	}
	@Override
	double friction(){return 0.14;}
	@Override
	double frictionIn1(){return 0.5;}
	@Override
	double frictionIn2(){return 200;}
};
