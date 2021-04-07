package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.*;

import util.BmpRes;

public class Tank_APFSDS extends RPG{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/Tank_APFSDS");
	game.item.Tank_APFSDS rpg;
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.075;}
	double lifetime;
	boolean state=false;
	double _fc(){return 0.0005;}
	@Override
	public double friction(){return 0.1;}
	public Tank_APFSDS(game.item.Tank_APFSDS a){
		super();
		rpg=a;
		fuel=5;
		lifetime=4+rnd(1,8);
	}
	protected void drop(){}
	public void update(){
		super.update();
		if(!state){
			state=true;
			Fragment.gen(x,y,width(),height(),2,2,4,rpg.getBmp());
		}
		

		double v=sqrt(xv*xv+yv*yv);
		if(v<1e-8){
			try_explode();
			return;
		}
	}
	@Override
	void upd_a(){
		double ax_=ax,ay_=ay;
		if(ax*xv+ay*yv<0){ax_*=-1;ay_*=-1;}
		aa+=(ax_*yv-ay_*xv)*0.08-av*0.1;
	}
	public double gA(){return !World.cur.get(x,y).isCoverable()?0.003:0.02;}
	void touchBlock(int px,int py,Block b){
		//这个产生了有趣的效果，不要删去这里的注释
		/*
		Block now_Block = World.cur.get(x,y).rootBlock();
		if(now_Block instanceof AirBlock)return;
		if(now_Block instanceof WaterBlock)return;
		if(now_Block instanceof GlueBlock)return;
		if(now_Block instanceof GrassBlock)return;
		if(now_Block instanceof WoodenBlock || now_Block instanceof CactusBlock || now_Block instanceof DarkVineBlock || now_Block instanceof LeafBlock){
			lifetime -= 0.2;
			now_Block.damage+=20;
		}else{
			lifetime--;
			now_Block.damage+=15;
		}
		if(lifetime<0)explode();
		*/
		if(!b.isSolid()&&b.isCoverable())return;
		if(b instanceof PlantType){
			lifetime -= 0.15;
			b.des(px,py,10,this);
		}else{
			lifetime -= 0.45;
			b.des(px,py,5,this);
		}
		if(lifetime<0)try_explode();
	}
	public void touchEnt(Entity a){
		double difx=xv-a.xv,dify=yv-a.yv;
		a.onAttacked(250*(difx*difx+dify*dify),this);
		lifetime -= 2.5;
		if(a instanceof Human)lifetime -= 3.2;
		if(lifetime<=0)try_explode();
	}
	public void explode(){
		Spark.explode(x,y,0,0,125,0.02,1,this,false,0.1,false);
		kill();
	}
}
