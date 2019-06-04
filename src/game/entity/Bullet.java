package game.entity;

import static java.lang.Math.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class Bullet extends Entity{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.2;}
	public boolean chkRigidBody(){return false;}
	public boolean rotByVelDir(){return true;}
	game.item.Bullet bullet;
	
	public BmpRes getBmp(){return bullet.getBmp();}
	
	public Bullet(game.item.Bullet b){
		hp=30;
		bullet=b;
	}
	public void update(){
		super.update();
		if(!World._.get(x,y).isCoverable())hp-=0.2;
		if(sqrt(xv*xv+yv*yv)<=0.1)kill();
		hp-=0.05;
	}
	void touchBlock(int px,int py,Block b){
		if(b.isSolid()){
			b.des(px,py,1);
			kill();
		}
	}
	void touchAgent(Agent ent){
		double v=sqrt(xv*xv+yv*yv);
		if(v>0.1){
			ent.onAttacked((v-0.1)*40,src);
			kill();
		}
		super.touchAgent(ent);
	}
	public void onKill(){
		bullet.onKill(x,y);
	}
	double gA(){return 0.001;}
}
