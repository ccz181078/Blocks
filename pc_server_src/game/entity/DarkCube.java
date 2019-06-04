package game.entity;

import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;


public class DarkCube extends DarkBall{
	private static final long serialVersionUID=1844677L;
	static float[] model=new float[]{
		-1,-1,-1, -1,-1,1, -1,-1,1, -1,1,1, -1,1,1, -1,1,-1, -1,1,-1, -1,-1,-1,
		1,-1,-1, 1,-1,1, 1,-1,1, 1,1,1, 1,1,1, 1,1,-1, 1,1,-1, 1,-1,-1,
		-1,-1,-1, 1,-1,-1, -1,-1,1, 1,-1,1, -1,1,-1, 1,1,-1, -1,1,1, 1,1,1,
	};
	float rx,ry,rxv,ryv;
	public double width(){return 0.5;}
	public double height(){return 0.5;}	
	public DarkCube(){
		hp=30;
		rx=(float)rnd(0,PI);
		ry=(float)rnd(0,PI);
		rxv=(float)rnd(-0.2,0.2);
		ryv=(float)rnd(-0.2,0.2);
	}
	void touchAgent(Agent a){
		if(a==src)return;
		a.onAttacked(2,src);
		a.onAttackedByDark(2,src);
		hp-=2;
		xv+=0.5*(a.xv-xv);
		yv+=0.5*(a.yv-yv);
	}

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
			xys[i*2]=(cy*x+sy*z)/4;
			xys[i*2+1]=(-sx*y+cx*zn)/4;
		}
		cv.drawLines(xys,0xff800080);
	}
}
