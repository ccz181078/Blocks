package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_Dark extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Dark rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Dark(game.item.RPG_Dark a){
		super();
		rpg=a;
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	public void explode(){
		explode(100);
		super.explode();
	}
	@Override
	public Entity getBall(){return new DarkBall();}
}
