package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_Empty extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Empty rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Empty(game.item.RPG_Empty a){
		super();
		rpg=a;
	}
}
