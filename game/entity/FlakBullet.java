package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class FlakBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.12;}
	public boolean chkRigidBody(){return false;}
	public boolean rotByVelDir(){return true;}
	
	boolean exploded=false;
	boolean state=false;
	
	public BmpRes getBmp(){return bullet.getBmp();}
	
	public FlakBullet(game.item.FlakBullet b){
		super(b);
		hp=100;
	}
	public void update(){
		super.update();
		if(!state){
			state=true;
			Fragment.gen(x,y,width(),height(),2,2,4,bullet.getBmp());
		}
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		if(!b.isCoverable()){
			hp-=0.3;
			if(b instanceof game.block.PlantType ) b.des(px,py,30,this);
			else b.des(px,py,4,this);
		}
		if(sqrt(xv*xv+yv*yv)<=0.1)kill();
		hp-=0.2;
	}
	void touchBlock(int px,int py,Block b){
		double v=sqrt(xv*xv+yv*yv);
		if(b.isSolid()){
			if(b instanceof game.block.GlassBlock)b.des(px,py,30,this);
			b.des(px,py,12,this);
			if(b instanceof game.block.PlantType ){
				b.des(px,py,10,this);
				hp -= rnd() * 2;
				f += 0.04;
			}else if( b instanceof game.block.LiquidType ){
				hp -= rnd() * 2;
				f += 0.1;
			}else{
				hp-=rnd() * 9;
				f += 0.2;
			}
		}
	}
	@Override
	void touchEnt(Entity ent){
		if(ent instanceof Spark) return;
		if((ent instanceof Bullet)){
			if(xv*ent.xv+yv*ent.yv>0)return;
		}
		double v=sqrt(xv*xv+yv*yv);
		if(ent instanceof FlakBullet) hp -= 5;
		else{
			ent.onAttacked(v*bullet.attackValue(),this);
			hp -= 70 * rnd();
			exchangeVel(ent,0.3);
		}
		super.touchEnt(ent);
	}
	public void explode(){
		if(exploded)return;
		exploded=true;
		Spark.explode(x,y,xv,yv,8,0,0.5,this,false,0.1,false);
		kill();
	}
	public void onKill(){
		explode();
	}
}
