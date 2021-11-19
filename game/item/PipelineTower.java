package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class PipelineTower extends Shilka{
	static BmpRes bmp[]=BmpRes.load("Armor/PipelineTower_",4);
	public BmpRes getBmp(){
		if(xdir==0&&ydir==1)return bmp[0];
		if(xdir==1&&ydir==0)return bmp[1];
		if(xdir==0&&ydir==-1)return bmp[2];
		if(xdir==-1&&ydir==0)return bmp[3];
		return null;
	}
	int xdir=0,ydir=1;
	float H=0.75f;
	public double width(){return xdir==0?0.45:H;}
	public double height(){return ydir==0?0.45:H;}
	@Override
	public void onUse(Human a){
		a.getCarriedItem().insert(this);
		int x0=xdir,y0=ydir;
		xdir=y0;ydir=-x0;
	}
	@Override
	public BmpRes getUseBmp(){
		return rotate_btn;
	}
	public double maxvr(){return 0.15;}
	public double getJumpAcc(Human h,double v){return 0;}
	public void onUpdate(Human w){
		{
			Pipeline_5 p=gun.get();
			if(p!=null)p.onUpdate(w,this);
		}
		if(!checkEnergy(5,w))return;
		double H0=H;
		if(xdir!=0){
			if(w.xdir==+xdir)H=min(H+0.01f,0.95f);
			if(w.xdir==-xdir)H=max(H-0.01f,0.45f);
		}
		if(ydir!=0){
			if(w.ydir==+ydir)H=min(H+0.01f,0.95f);
			if(w.ydir==-ydir)H=max(H-0.01f,0.45f);
		}
		if(H0!=H){
			new SetRelPos(w,w,xdir*(H-H0),ydir*(H-H0));
		}
		{
			Agent a=getAgent();
			if(a==null||a.isRemoved())agentref=0;
			else{
				double wx=w.x-xdir*width();
				double wy=w.y-ydir*height();
				double ax=a.x+xdir*a.width();
				double ay=a.y+ydir*a.height();
				double xd=wx-ax,yd=wy-ay;
				if(max(abs(xd),abs(yd))<0.6){
					double m=min(w.mass(),a.mass())*0.2;
					w.impulse(xd,yd,-m);
					a.impulse(xd,yd,m);
				}else{
					agentref=0;
				}
			}
		}
	}
	public long agentref=0;
	protected Agent getAgent(){
		return World.getAgentRef(agentref);
	}
	protected void setAgent(Agent a){
		agentref=World.getAgentRef(a);
	}
	@Override
	public void touchAgent(Human w,Agent a){
		double wx=w.x-xdir*width();
		double wy=w.y-ydir*height();
		double ax=a.x+xdir*a.width();
		double ay=a.y+ydir*a.height();
		if(max(abs(wx-ax),abs(wy-ay))<0.4){
			Agent agent=getAgent();
			if(agent==null){
				setAgent(a);
			}
		}
		super.touchAgent(w,a);
	}
	@Override
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		walk=40;
		if(!hasEnergy(5))return true;
		double xd=tx-(hu.x+xdir*(H-0.2f)),yd=ty-(hu.y+ydir*(H-0.2f));
		if(rnd()<0.01){loseEnergy(1);damage+=1;}
		if(abs(yd)+abs(xd)>0.001){
			double b = atan2(yd,xd),a0=a;
			double d=(b-a)%(PI*2);
			if(d<-PI)d+=PI*2;
			if(d>PI)d-=PI*2;
			
			if(abs(d)<0.2)a=b;
			else if(d<0)a-=0.2;
			else a+=0.2;
			a%=PI*2;
			if(a<-PI)a+=PI*2;
			if(a>PI)a-=PI*2;
			
			if(abs(d)<0.2)shoot(hu,a,tan(a));
		}
		return true;
	}
	@Override
	public boolean onArmorClick(Human hu,double tx,double ty){
		shoot(hu,a,tan(a));
		return true;
	}
	@Override
	public double getShootX(Human hu,double a,int cnt){
		return hu.x+1.6*cos(a)-0.15*(cnt-3)*sin(a)+xdir*(H-0.2f);
	}
	@Override
	public double getShootY(Human hu,double a,int cnt){
		return hu.y+0.15*(cnt-3)*cos(a)+1.6*sin(a)+ydir*(H-0.2f);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		cv.save();{
			cv.translate(xdir*(H-0.2f),ydir*(H-0.2f));
			cv.rotate((float)(a*180/PI));
			BmpRes bmp=getGunBmp();
			if(bmp!=null)bmp.draw(cv,0.45f,0,0.9f,0.37f);
		}cv.restore();
		getBmp().draw(cv,0,0,(float)width(),(float)height());
	}
}
