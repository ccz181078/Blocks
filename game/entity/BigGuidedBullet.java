package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.*;
import util.BmpRes;

public class BigGuidedBullet extends GuidedBullet{
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public BigGuidedBullet(game.item.BigGuidedBullet b){
		super(b);
	}
	public void explode(){
		if(exploded)return;
		exploded=true;
		double c=0.4/(hypot(xv,yv)+1e-8);
		for(SingleItem si:((game.item.BigGuidedBullet)bullet).items.toArray()){
			Item it=si.popItem();
			if(it!=null)it.onLaunch(Agent.temp(x+yv*c,y-xv*c,0.3,0.3,xv>0?1:-1,this),yv/xv,mass()*(xv*xv+yv*yv)/2);
			c=-c;
		}
		kill();
	}
	public void update(){
		super.update();
		if(target!=null){
			if(distL2(target)<=2+rnd_exp(1))explode();
		}
	}
}