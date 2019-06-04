package game.block;

import game.world.World;
import game.entity.Agent;
import game.item.Item;

public class StaticBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	public StaticBlock(Block _block){super(_block);}
	public Block deStatic(int x,int y){
		Block b=block.clone();
		World._.set(x,y,b);
		return b;
	}
	public void onPress(int x,int y,Item item){
		deStatic(x,y).onPress(x,y,item);
	}
	public boolean onClick(int x,int y,Agent agent){
		return deStatic(x,y).onClick(x,y,agent);
	}
	public boolean onCheck(int x,int y){
		return deStatic(x,y).onCheck(x,y);
	}
	public boolean onUpdate(int x,int y){
		if(block.updateCond(x,y))return deStatic(x,y).onUpdate(x,y);
		return false;
	}
	public void des(int x,int y,int v){
		deStatic(x,y).des(x,y,v);
	}
	public void onCircuitDestroy(int x,int y){}
	public void onFireUp(int x,int y){
		deStatic(x,y).onFireUp(x,y);
	}
	public void onBurn(int x,int y){
		deStatic(x,y).onBurn(x,y);
	}
};

