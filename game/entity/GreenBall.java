package game.entity;

import util.BmpRes;
import game.block.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class GreenBall extends Ball{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return bmp;}
	static BmpRes bmp=new BmpRes("Entity/GreenBall");
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	public boolean harmless(){return true;}

	public boolean chkRigidBody(){return true;}

	public GreenBall(){
		hp=rnd_exp(10);
	}
	@Override
	double friction(){return 1;}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public void update(){
		super.update();
		f+=0.15;
		if(rnd()<(hp>2?0.04:0.01)){
			double m=mass();
			
			GreenBall ball=new GreenBall();
			ball.initPos(x,y,0,0,source);
			ball.hp=rnd(hp*0.3,hp*0.7);
			hp-=ball.hp;
			ball.add();
			
			double xi=rnd_gaussion()*0.1,yi=rnd_gaussion()*0.1,c=m/ball.mass();
			impulse(xi,yi,m);
			
			ball.xv=xv-xi*c;
			ball.yv=yv-yi*c;
		}
	}
	public double mass(){return max(1,hp)*0.02;}
	void touchAgent(Agent a){
		a.addHp(hp);
		kill();
	}
	void touchEnt(Entity ent,boolean flag){
		if(ent.getClass()!=GreenBall.class){
			ent.hp-=2;
			hp-=0.1;
			ent.f+=0.5;
		}else if(rnd()<0.02&&hp>0&&ent.hp>0){
			impulse(ent,1);
			hp+=ent.hp;
			ent.hp=0;
		}
	}
	void touchBlock(int px,int py,Block b){
		if(hp>0 && b instanceof PlantType){
			((PlantType)b).light_v+=hp;
			((PlantType)b).dirt_v+=hp;
			hp=0;
		}
		if(b.rootBlock().transparency()<0.7)return;
		super.touchBlock(px,py,b);
	}
}
