package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.*;
import util.BmpRes;

public class TextGuidedBullet extends GuidedBullet{
	public boolean chkEnt(){return false;}
	public boolean chkBlock(){return false;}
	public TextGuidedBullet(game.item.TextGuidedBullet b){
		super(b);
	}
	public void explode(){
	}
	@Override
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof GuidedBullet))return;
		if(checkRecycle(ent))return;
		double v2=v2rel(ent);
		ent.onAttacked(max(0,v2+0.05)*4*100,this);
		ent.onAttackedByEnergy(max(0,v2+0.05)*4*100,this);
		ent.onAttackedByFire(max(0,v2+0.05)*4*100,this);
		ent.onAttackedByDark(max(0,v2+0.05)*4*100,this);
		exchangeVel(ent,0.02);
		hp-=1;
	}
	public void draw(Canvas cv){
		cv.save();{
			float k=World.cur.setting.BW/12f;
			cv.translate(0,-0.15f);
			cv.scale(k,-k);
			cv.drawText(bullet.getName(),0,0,0.3f,0);
		}cv.restore();
	}
}
