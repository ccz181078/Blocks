package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.*;
import util.BmpRes;

public class GreenGuidedBullet extends GuidedBullet{
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public GreenGuidedBullet(game.item.GreenGuidedBullet b){
		super(b);
	}
	public void explode(){
	}
	public void update(){
		super.update();
		if(World.cur.get(x,y) instanceof game.block.PlantType){
			World.cur.setAir(f2i(x),f2i(y));
			hp=min(hp+50,200);
		}
		double xds=0,yds=0;
		for(int i=0;i<5;++i){
			double xd=rnd_gaussion()*3,yd=rnd_gaussion()*3;
			if(World.cur.get(x+xd,y+yd) instanceof game.block.PlantType){
				xds+=xd;
				yds+=yd;
				fc+=0.05/mass();
			}
		}
		double d=0.05/(hypot(xds,yds)+1e-8);
		xa+=xds*d;
		ya+=yds*d;
	}
	@Override
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof GuidedBullet))return;
		double v2=v2rel(ent);
		ent.onAttacked(max(0,v2+0.05)*4*60,this);
		exchangeVel(ent,0.1);
		hp-=3;
	}
}
