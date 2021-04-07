package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.block.FireBlock;
import util.BmpRes;

public abstract class RPG extends Entity{
private static final long serialVersionUID=1844677L;
	static BmpRes bmps[]=BmpRes.load("Entity/JetFire_",4);
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.2;}
	public boolean chkRigidBody(){return false;}
	public double fluidResistance(){return 0.2;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public String getName(){
		String s=util.AssetLoader.loadString(getClass(),"name");
		if(is_warhead){
			s=s.replaceAll("火箭弹","弹头");
		}
		return s;
	}
	
	public boolean is_warhead=false;

	@Override
	double friction(){
		return 1;
	}
	
	public int fuel;
	public RPG(){
		hp=200;
		fuel=100;
	}
	double _fc(){return 0.01;}
	public void update(){
		super.update();
		hp-=0.01;
		Line.gen(this);
		if(fuel>0){
			if(genSmoke())new Smoke().initPos(x-ax*0.2,y-ay*0.2,-ax*0.5,-ay*0.5,null).setHpScale(this instanceof RPG_Guided?1.5:1).add();
			--fuel;
			xa+=ax*0.03;
			ya+=ay*0.03;
		}
	}
	protected boolean genSmoke(){return rnd()<hypot(xv,yv);}
	@Override
	public void test_update(){
		super.test_update();
		if(fuel>0){
			--fuel;
			xa+=ax*0.03;
			ya+=ay*0.03;
		}
	}
	@Override
	void upd_a(){
		double ax_=ax,ay_=ay;
		if(ax*xv+ay*yv<0){ax_*=-1;ay_*=-1;}
		aa+=(ax_*yv-ay_*xv)*0.02-av*0.1;
	}
	public void explode(){
		Spark.explode_adhesive(x,y,xv/2,yv/2,fuel/4,0.1,4,this);
		kill();
	}
	void touchBlock(int px,int py,Block b){
		if(fuel>0)b.onFireUp(px,py);
		if(b.isSolid())try_explode();
	}
	protected void try_explode(){
		if(!removed){
			new Fragment.Config().setEnt(this).setGrid(2,2,4).setVel(0.06).apply();
			remove();
			explode();
		}
	}
	public void onKill(){
		if(rnd()<0.7)try_explode();
		new Fragment.Config().setEnt(this).setGrid(2,2,4).apply();
	}
	void touchEnt(Entity ent,boolean chk_ent){
		if(rnd()<ent.RPG_ExplodeProb())touchEnt(ent);
	}
	void touchEnt(Entity ent){
		try_explode();
	}
	/*public void onAttacked(Attack att){
		try_explode();
	}*/
	public double gA(){return !World.cur.get(x,y).isCoverable()?0.005:fuel>0?0.003:0.01;}
	public boolean rotByVelDir(){return true;}
	@Override
	public void draw(graphics.Canvas cv){
		BmpRes bmp=getBmp();
		float a=getRotation();
		cv.save();
		cv.rotate(a);
		bmp.draw(cv,0,0,(float)width(),(float)height());
		if(fuel>0)bmps[rndi(0,3)].draw(cv,-0.3f,0,0.1f,0.1f);
		cv.restore();
	}
}
