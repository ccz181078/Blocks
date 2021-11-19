package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;

public class Rope extends Entity{
	public double radius(){return 0.15;}
	public double width(){return radius();}
	public double height(){return radius();}
	//public boolean chkRigidBody(){return false;}
	public double mass(){return 0.1;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	Entity prev,next;
	double px,py;
	static BmpRes bmp=new BmpRes("Entity/Rope");
	public BmpRes getBmp(){return bmp;}
	public Rope(){
		hp=100;
	}
	static final double MAX_D=2,MIN_D=0.5;
	public void link(Entity e,double px,double py){
		this.prev=e;
		this.px=px;
		this.py=py;
		if(e instanceof Rope)((Rope)e).next=this;
	}
	boolean restrict(double x1,double y1,Entity ent){
		return restrict(x1,y1,ent,MIN_D,MAX_D,0.05);
	}
	boolean restrict(double x1,double y1,Entity ent,double L,double R,double k){
		double xd=x-x1,yd=y-y1,d=hypot(xd,yd)+1e-8;
		if(d>R)return false;
		if(d<L)return true;
		double c=(d-L)/(R-L);
		c=c*2*k;
		double xF=xd/d*c,yF=yd/d*c;
		impulse(xF,yF,-1);
		if(ent!=null)ent.impulse(x1,y1,xF,yF,1);
		return true;
	}
	void touchEnt(Entity ent){
		if(ent instanceof Rope)return;
		if(ent==prev||ent==next)return;
		super.touchEnt(ent);
	}
	public double gA(){return 0.001;}
	public void update(){
		super.update();
		hp-=0.01;
		if(prev instanceof Agent){
			Agent a=(Agent)prev;
			SingleItem si=a.getCarriedItem();
			if(si!=null){
				Item it=si.get();
				if(it instanceof HookRope){
					HookRope hr=(HookRope)it;
					if(hr.using){
						if(hr.climbing){
							if(next instanceof Rope){
								Rope nx=(Rope)next;
								nx.restrict(0,0,this,-MAX_D,MAX_D,0.2);
								nx.restrict(0,0,a,-MAX_D*2,MAX_D*2,0.1);
								if(nx.distL2(a)<MAX_D/2){
									nx.link(prev,px,py);
									prev=null;
									kill();
								}
							}else if(distL2(a)<MAX_D/2){
								hr.using=false;
								prev=null;
								kill();
							}
						}
					}else prev=null;
				}else prev=null;
			}else prev=null;
		}
		if(prev!=null){
			if(!restrict(prev.x+px,prev.y+py,prev)){
				prev=null;
			}
		}
	}
	public void draw(graphics.Canvas cv){//绘制
		/*if(prev!=null){
			float ps[]=new float[]{0,0,(float)(prev.x+px-x),(float)(prev.y+py-y)};
			cv.drawLines(ps,0xff808080);
		}*/
		super.draw(cv);
	}
}

