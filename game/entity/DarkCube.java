package game.entity;

import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;


public class DarkCube extends DarkBall implements AttackFilter{
	private static final long serialVersionUID=1844677L;
	static float[] model=new float[]{
		-1,-1,-1, -1,-1,1, -1,-1,1, -1,1,1, -1,1,1, -1,1,-1, -1,1,-1, -1,-1,-1,
		1,-1,-1, 1,-1,1, 1,-1,1, 1,1,1, 1,1,1, 1,1,-1, 1,1,-1, 1,-1,-1,
		-1,-1,-1, 1,-1,-1, -1,-1,1, 1,-1,1, -1,1,-1, 1,1,-1, -1,1,1, 1,1,1,
	};
	public double mass(){return 0.2;}
	float rx,ry,rxv,ryv;
	public double width(){return min(1,0.5*cbrt(cnt));}
	public double height(){return min(1,0.5*cbrt(cnt));}
	int cnt;
	public DarkCube(){
		hp=100;
		rx=(float)rnd(0,PI);
		ry=(float)rnd(0,PI);
		rxv=(float)rnd(-0.2,0.2);
		ryv=(float)rnd(-0.2,0.2);
		cnt=1;
	}
	protected void checkSlow(){}
	@Override
	public AttackFilter getAttackFilter(){return this;}//攻击过滤器
	public Attack transform(Attack a){
		if(a!=null)a.val*=0.005;
		return a;
	}
	@Override
	void touchAgent(Agent a){
		if(a.group()==Agent.Group.DARK)return;
		a.onAttacked(2*cnt,this);
		a.onAttackedByDark(2*cnt,this);
		xa+=0.5*(a.xv-xv)+0.1*(a.x-x);
		ya+=0.5*(a.yv-yv)+0.1*(a.y-y);
	}

	@Override
	public void update(){
		super.update();
		rx+=rxv;
		ry+=ryv;
		rxv+=rnd(-0.01,0.01);
		ryv+=rnd(-0.01,0.01);
		if(abs(rxv)+abs(ryv)>0.4){
			rxv*=0.9;
			ryv*=0.9;
		}
		if(rnd()<0.04){
			for(Entity e:game.world.World.cur.getNearby(x,y,width(),height(),false,true,false).ents){
				if(e!=this&&e.getClass()==DarkCube.class){
					hp=max(hp,e.hp);
					cnt+=((DarkCube)e).cnt;
					e.kill();
				}
			}
		}
	}
	
	public void draw(Canvas cv){
		int n=model.length/3;
		float[] xys=new float[n*2];
		float x=0,y=0,z=0,zn;
		float cx=(float)cos(rx),sx=(float)sin(rx);
		float cy=(float)cos(ry),sy=(float)sin(ry);
		for(int i=0;i<n;i++){
			x=model[i*3];
			y=model[i*3+1];
			z=model[i*3+2];
			zn=-sy*x+cy*z;
			xys[i*2]=(cy*x+sy*z)/2*(float)width();
			xys[i*2+1]=(-sx*y+cx*zn)/2*(float)height();
		}
		cv.drawLines(xys,0xff800080);
	}
}
