package game.block;

import game.item.Item;
import game.world.World;
import game.entity.FallingBlock;


public abstract class DirtType extends Block{
	private static final long serialVersionUID=1844677L;
	public void onPress(int x,int y,Item item){
		des(x,y,item.shovelVal());
		item.onDesBlock(this);
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(World.cur.get(x,y-1).isCoverable()){
			World.cur.setAir(x,y);
			new FallingBlock(x,y,this).add();
			return true;
		}
		if(damage>0)--damage;
		return false;
	}

	public boolean updateCond(int x,int y){
		return World.cur.get(x,y-1).isCoverable();
	}
};
