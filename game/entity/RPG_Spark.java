package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_Spark extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Spark rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Spark(game.item.RPG_Spark a){
		super();
		rpg=a;
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	public void explode(){
		Spark.explode(x,y,xv/10,yv/10,25+fuel/12,0.1,12,this,true,hypot(xv,yv));
		kill();
	}
}
