package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_HE extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_HE rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_HE(game.item.RPG_HE a){
		super();
		rpg=a;
	}
	public void explode(){
		explode(100);
		Spark.explode(x,y,0,0,20,0.1,1,this);
		ShockWave.explode(x,y,0,0,40,0.1,0.4,this);
		super.explode();
	}
}
