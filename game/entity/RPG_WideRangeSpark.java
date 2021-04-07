package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_WideRangeSpark extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_WideRangeSpark rpg;
	int time=0;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_WideRangeSpark(game.item.RPG_WideRangeSpark a){
		super();
		rpg=a;
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	
	@Override
	public void update(){
		super.update();
		if(++time>9)
		while(fuel>0&&rnd()<0.8){
			fuel-=1;
			Spark.explode_adhesive(x,y,xv,yv,1,0.1,4,this);
		}
	}
	
	public void explode(){
		Spark.explode_adhesive(x,y,xv,yv,fuel,0.1,4,this);
		kill();
	}
}
