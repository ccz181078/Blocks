package game.entity;

import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;
import game.block.BedRockBlock;

public class SpaceBoss extends Agent{
	private static final long serialVersionUID=1844677L;
	static float[] model=new float[]{
		-1,-1,-1, -1,-1,1, -1,-1,1, -1,1,1, -1,1,1, -1,1,-1, -1,1,-1, -1,-1,-1,
		1,-1,-1, 1,-1,1, 1,-1,1, 1,1,1, 1,1,1, 1,1,-1, 1,1,-1, 1,-1,-1,
		-1,-1,-1, 1,-1,-1, -1,-1,1, 1,-1,1, -1,1,-1, 1,1,-1, -1,1,1, 1,1,1,
		1,0,0, 0,1,0, 1,0,0, 0,-1,0, 1,0,0, 0,0,1, 1,0,0, 0,0,-1,
		-1,0,0, 0,1,0, -1,0,0, 0,-1,0, -1,0,0, 0,0,1, -1,0,0, 0,0,-1,
		0,1,0, 0,0,1, 0,0,1, 0,-1,0, 0,-1,0, 0,0,-1, 0,0,-1, 0,1,0,
	};
	float rx,ry,rxv,ryv;
	public double width(){return 1;}
	public double height(){return 1;}
	public double maxHp(){return 3000;}
	double mass(){return 5;}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	double gA(){return 0;}
	public SpaceBoss(double _x,double _y){
		super(_x,_y);
		rx=(float)rnd(0,PI);
		ry=(float)rnd(0,PI);
		rxv=(float)rnd(-0.1,0.1);
		ryv=(float)rnd(-0.1,0.1);
	}

	

	public void update(){
		super.update();
		if(hp>0)hp=min(hp+0.4,maxHp());
	}
	void touchAgent(Agent ent){
		ent.onAttacked(rnd(5),this);
		ent.onAttackedByDark(rnd(10),this);
		super.touchAgent(ent);
	}

	public void action(){
		double md=1e10;
		Player tg=null;
		for(Player p:World.cur.getPlayers()){
			double d=abs(x-p.x)+abs(y-p.y);
			if(d<md){
				md=d;
				tg=p;
			}
		}
		if(tg!=null){
			double xd=tg.x-x,yd=tg.y-y,d=0.01/(abs(xd)+abs(yd)+1);
			xa+=xd*d;
			ya+=yd*d;
			if(rnd()<0.02){
				throwEntFromCenter(new DarkCube(),tg.x,tg.y,0.3);
			}
			if(md<10){
				xv*=0.98;
				yv*=0.98;
				int x=f2i(tg.x+rnd(-6,6));
				int y=f2i(tg.y+rnd(-6,6));
				int x1=x,y1=y;
				if(rnd()<0.5)x1+=rnd()<0.5?-1:1;
				else y1+=rnd()<0.5?-1:1;
				Block b=World.cur.get(x,y);
				if(!b.isCoverable()&&!(b.rootBlock() instanceof BedRockBlock||b.isDeep())&&World.cur.get(x1,y1).isCoverable()){
					World.cur.setAir(x,y);
					Entity e=new FallingBlock(x,y,b);
					e.xv=(x1-x)*0.3+rnd(-0.1,0.1);
					e.yv=(y1-y)*0.3+rnd(-0.1,0.1);
					e.add();
				}
			}
		}
		rx+=rxv;
		ry+=ryv;
		rxv+=rnd(-0.01,0.01);
		ryv+=rnd(-0.01,0.01);
		if(abs(rxv)+abs(ryv)>0.2){
			rxv*=0.9;
			ryv*=0.9;
		}
		if(sqrt(xv*xv+yv*yv)>0.1){
			xv*=0.9;
			yv*=0.9;
		}
	}

	public AttackFilter getAttackFilter(){
		return dark_filter;
	}
	
	

	public boolean chkRemove(long t){return false;}
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
			xys[i*2]=cy*x+sy*z;
			xys[i*2+1]=-sx*y+cx*zn;
		}
		cv.drawLines(xys,0xff800080);
		
		super.draw(cv);
	}
	void onKill(){
		for(int t=6;t>0;--t)new game.item.Cube().drop(x,y);
	}
}

