package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class APDSBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.16;}
	public double height(){return 0.16;}
	public double mass(){return 0.1;}
	public boolean chkRigidBody(){return false;}
	public boolean rotByVelDir(){return true;}
	
	boolean exploded=false;
	boolean state=false;
	
	public BmpRes getBmp(){return bullet.getBmp();}
	
	public APDSBullet(game.item.APDSBullet b){
		super(b);
		hp=100;
	}
	public void update(){
		super.update();
		hp-=30;
	}
	void touchBlock(int px,int py,Block b){
		if(b.isSolid())
			hp-=50;
	}
	@Override
	void touchEnt(Entity ent){
		if(ent instanceof Spark) return;
		if((ent instanceof Bullet)){
			if(xv*ent.xv+yv*ent.yv>0)return;
		}
		hp-=50;
	}
	public void explode(){
		if(exploded)return;
		exploded=true;
		Source ex=SourceTool.explode(this);
		Entity e=(new game.item.IronNail()).asEnt();
		double k = (sqrt(xv*xv+yv*yv)+1.5)/sqrt(xv*xv+yv*yv);
		e.initPos(x,y,xv*k,yv*k,ex);
		e.add();
		new Fragment.Config().setEnt(this).setGrid(2,2,4).setVel(0.06).apply();
		kill();
	}
	public void onKill(){
		explode();
	}
}
