package game.entity;

import util.BmpRes;
import game.block.LiquidType;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;

public class HT_FireBall extends FireBall{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/HT_FireBall");
	public BmpRes getBmp(){return bmp;}
	boolean active=false;
	public HT_FireBall(){
		hp=10+rnd_gaussion();
	}
	@Override
	public double radius(){
		return min(1,sqrt(max(1,hp/10))*0.2);
	}
	public boolean chkEnt(){return true;}//是否与Entity接触
	double friction(){return 1;}
	public double mass(){return 0.1;}
	public void update(){
		super.update();
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		b.touchEnt(px,py,this);
		touchBlock(px,py,b);
		xa+=rnd_gaussion()*0.01;
		ya+=rnd_gaussion()*0.01;
		if(hypot(xv,yv)>0.2)f+=0.5;
		{
			double r=width()*(1+rnd_gaussion()*0.5),a=rnd(2*3.1415926),c=cos(a)*r,s=sin(a)*r;
			if(!World.cur.get(x+c*r,y+s*r).isCoverable()){
				xa-=c*0.02;
				ya-=s*0.02;
			}
		}
		if(active){
			active=false;
			HT_FireBall w=new HT_FireBall();
			w.hp=rnd(hp*0.3,hp*0.7);
			hp-=w.hp;
			double d=width()+w.width();
			double c=rnd_gaussion(),s=rnd_gaussion();
			w.initPos(x+c*d,y+s*d,xv+c*0.1,yv+s*0.1,getSrc());
			w.add();
		}
	}
	void touchEnt(Entity a){
		if(hp<=0||a.hp<=0)return;
		if(a instanceof HT_FireBall){
			//System.err.println(hp+"  "+a.hp+"  "+distL2(a)+"  w:"+width()+"  aw:"+a.width());
			if(hp>a.hp&&distL2(a)<(width()+a.width())){
				hp+=a.hp;
				a.hp=0;
			}
			return;
		}
		if(rnd()<0.5)active=true;
		double v=2;
		while(a.hp>0&&hp>0){
			v=min(v*1.5,hp);
			a.onAttackedByFire(v,this);
			hp-=v;
		}
	}
	@Override
	public double RPG_ExplodeProb(){return radius()*0.3;}
	void touchBlock(int px,int py,game.block.Block b){
		if(b.rootBlock() instanceof LiquidType)remove();
		b.onFireUp(px,py);
		b.onBurn(px,py);
		if(!b.isCoverable()){
			if(rnd()<0.1)active=true;
			hp-=0.3;
			b.des(px,py,1,this);
		}
	}
}
