package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.world.NearbyInfo;
import game.block.Block;
import util.BmpRes;
import game.item.Warhead;

public class DarkRPG extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.DarkRPG rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public DarkRPG(game.item.DarkRPG a){
		super();
		rpg=a;
	}
	public boolean chkEnt(){return false;}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	
	public void explode(){
		Warhead wh=rpg.wh.popItem();
		if(wh!=null){
			wh.explode(x,y,xv,yv,this,null);
		}
		super.explode();
	}
}
