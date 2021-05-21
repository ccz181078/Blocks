package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.*;
import util.BmpRes;

public class DarkGuidedBullet extends GuidedBullet{
	public boolean chkEnt(){return false;}
	public boolean chkBlock(){return false;}
	public DarkGuidedBullet(game.item.DarkGuidedBullet b){
		super(b);
	}
	public void explode(){
	}
	@Override
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof GuidedBullet))return;
		double v2=v2rel(ent);
		ent.onAttacked(max(0,v2+0.05)*4*30,this);
		exchangeVel(ent,0.1);
		hp-=3;
	}
}
