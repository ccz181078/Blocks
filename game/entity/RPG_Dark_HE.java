package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_Dark_HE extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Dark_HE rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Dark_HE(game.item.RPG_Dark_HE a){
		super();
		rpg=a;
	}
	@Override
	public void explode(){
		explodeDirected(this,40,0.1,1,1);
		super.explode();
	}
	@Override
	public Entity getBall(){return new HE_DarkBall();}
}
