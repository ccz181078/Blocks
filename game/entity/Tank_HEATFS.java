package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;
import game.world.World;

public class Tank_HEATFS extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.Tank_HEATFS rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public double width(){return 0.18;}
	public double height(){return 0.18;}
	public double mass(){return 0.28;}
	public Tank_HEATFS(game.item.Tank_HEATFS a){
		super();
		rpg=a;
		fuel=20;
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	public void update(){
		super.update();
		double v=sqrt(xv*xv+yv*yv);
		if(v<1e-8){
			try_explode();
			return;
		}
	}
	public double gA(){return !World.cur.get(x,y).isCoverable()?0.003:0.02;}
	
	public void explode_targeted(double v){
		Source ex=SourceTool.explode(this);
		int c=min(300,rf2i(v*2));
		Spark.gen(x,y);
		for(int i=0;i<c;++i){
			double xv=rnd(-1,1),yv=rnd(-1,1),v2=xv*xv+yv*yv;
			if(v2>0.1&&v2<1){
				FireBall f=new FireBall();
				f.initPos(x+xv/3,y+yv/3,this.xv/rnd(30,55)+xv/7,this.yv/rnd(30,55)+yv/7,ex);
				f.hp*=0.6;
				f.add();
			}
		}
	}
	public void explode(){
		explodeDirected(new HE_FireBall(),32,0.5,1,0.05,0.8);
		explode_targeted(64);
		Spark.explode(x,y,0,0,20,0.1,1,this);
		ShockWave.explode(x,y,0,0,32,0.1,0.4,this);
		kill();
	}
}
