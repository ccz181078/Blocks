package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.block.FireBlock;
import util.BmpRes;

public class FireBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.05;}
	
	boolean exploded=false;
	
	public BmpRes getBmp(){return bullet.getBmp();}
	
	public FireBullet(game.item.FireBullet b){
		super(b);
		hp=60 * (0.2*rnd_gaussion()+1);
	}
	public void update(){
		super.update();
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		if(!b.isCoverable()){
			hp-=0.1;
			b.des(px,py,3,this);
		}
		if(sqrt(xv*xv+yv*yv)<=0.1)kill();
		hp-=0.3;
	}
	void touchBlock(int px,int py,Block b){
		double v=sqrt(xv*xv+yv*yv);
		if(b.isSolid()){
			if(b instanceof game.block.GlassBlock)b.des(px,py,10,this);
			b.des(px,py,1,this);
			if(b instanceof game.block.PlantType || b instanceof game.block.LiquidType ){
				hp -= rnd() * 2;
				f += 0.1;
			}else{
				hp -= rnd() * 9;
				f += 0.2;
			}
		}
		if( hp < 0 || b instanceof FireBlock)
			explode();
	}
	void touchEnt(Entity ent){
		if((ent instanceof Bullet)|| (ent instanceof Spark))return;
		double v=sqrt(xv*xv+yv*yv);
		if(ent instanceof FireBullet);
		else{
			ent.onAttacked(v*bullet.attackValue(),this);
			hp -= 100;
		}
		super.touchEnt(ent);
		if( hp < 0 )
			explode();
	}
	public void explode(){
		if(exploded)return;
		exploded=true;
		Spark.explode(x,y,xv/10,yv/10,2,0.05,8,this,true,0.3);
		Spark.explode(x,y,xv/10,yv/10,1,0.05,3,this,false,0.3);
		kill();
	}
	public void onKill(){
		explode();
		bullet.onKill(x,y);
	}
}
