package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.*;
import util.BmpRes;

public class BloodGuidedBullet extends GuidedBullet{
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public BloodGuidedBullet(game.item.BloodGuidedBullet b){
		super(b);
	}
	public void explode(){
	}
	@Override
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof GuidedBullet))return;
		if(checkRecycle(ent))return;
		if(ent instanceof Agent)BloodBall.drop((Agent)ent,rnd(2,4),this);
		hp-=3;
	}
}
