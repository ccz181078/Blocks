package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_Energy_HE extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Energy_HE rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Energy_HE(game.item.RPG_Energy_HE a){
		super();
		rpg=a;
	}
	@Override
	public void explode(){
		explodeDirected(this,40,0.1,1,0,1);
		super.explode();
	}
	@Override
	public Entity getBall(){return new HE_EnergyBall();}
}
