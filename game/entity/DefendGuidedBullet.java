package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.*;
import util.BmpRes;

public class DefendGuidedBullet extends GuidedBullet{
	public DefendGuidedBullet(game.item.DefendGuidedBullet b){
		super(b);
	}
	public void explode(){
	}
	public void update(){
		super.update();
	}
	@Override
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof GuidedBullet))return;
		if(checkRecycle(ent))return;
		double v2=v2rel(ent);
		ent.onAttacked(max(0,v2+0.05)*4*60,this);
		exchangeVel(ent,0.1);
		hp-=3;
	}
}
