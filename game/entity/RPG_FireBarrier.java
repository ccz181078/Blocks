package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_FireBarrier extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_FireBarrier(game.item.RPG_FireBarrier a){
		super(a);
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	public void explode(){
		game.item.FireBarrier.explode(x,y,xv,yv,0,0,false,this);
		kill();
	}
}
