package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_EnergyBarrier extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_EnergyBarrier(game.item.RPG_EnergyBarrier a){
		super(a);
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	public void explode(){
		game.item.EnergyBarrier.explode(x,y,xv,yv,0,0,false,this);
		super.explode();
	}
}
