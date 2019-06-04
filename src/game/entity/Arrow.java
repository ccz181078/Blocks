package game.entity;

import static java.lang.Math.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class Arrow extends Entity{
private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.2;}
	public boolean chkRigidBody(){return false;}
	game.item.Arrow arrow;
	boolean on_fire=false;
	public BmpRes getBmp(){return arrow.getBmp(on_fire);}
	public Arrow(game.item.Arrow a,game.item.Bow b){
		hp=10;
		arrow=a;
		if(b instanceof game.item.FireCrossBow)on_fire=true;
	}
	public void update(){
		super.update();
		if(!World._.get(x,y).isCoverable())hp-=0.2;
		hp-=0.05;
		if(on_fire)hp-=0.05;
	}
	
	void touchBlock(int px,int py,Block b){
		if(on_fire)b.onFireUp(px,py);
		if(b.isSolid())kill();
	}
	void touchAgent(Agent ent){
		double v=sqrt(xv*xv+yv*yv);
		if(on_fire){
			hp-=3;
			ent.onAttackedByFire(3,src);
		}
		if(v>0.1){
			ent.onAttacked((v-0.1)*25,src);
			kill();
		}
		super.touchAgent(ent);
	}

	public void onAttacked(Attack att){
		super.onAttacked(att);
		if(att instanceof FireAttack)on_fire=true;
	}
	
	public void onKill(){
		if(!on_fire)arrow.drop(x,y);
		else new game.item.IronNail().drop(x,y);
	}
	double gA(){return 0.003;}
	public boolean rotByVelDir(){return true;}
}
