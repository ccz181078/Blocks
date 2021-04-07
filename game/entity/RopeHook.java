package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;

public class RopeHook extends Rope{
	int res_cnt=80;
	public double mass(){return 1;}
	double nx,ny;
	int bx,by;
	boolean hook=false;
	public void update(){
		super.update();
		if(prev!=null){
			if(res_cnt>0&&!hook)
			if(distL2(prev)>MIN_D){
				Rope r=new Rope();
				r.initPos(x+0.1*(prev.x-x),y+0.1*(prev.y-y),xv,yv,source);
				r.link(prev,px,py);
				link(r,0,0);
				r.add();
				--res_cnt;
			}
		}
		if(hook){
			if(next!=null){
				if(next.isRemoved()){
					hook=false;
					next=null;
				}
			}else{
				if(!World.cur.get(bx,by).isSolid()){
					hook=false;
				}
			}
		}
		if(hook){
			double x1=nx,y1=ny;
			if(next!=null){
				x1+=next.x;
				y1+=next.y;
			}
			if(!restrict(x1,y1,next,MIN_D,MAX_D,0.2)){
				hook=false;
				next=null;
			}
		}
	}
	void touchEnt(Entity ent){
		if(ent instanceof Rope)return;
		if(ent==prev||ent==next)return;
		super.touchEnt(ent);
		if(!hook){
			Rope w=this;
			for(int t=0;t<30&&w.prev instanceof Rope;++t)w=(Rope)w.prev;
			if(w.prev!=ent){
				hook=true;
				next=ent;
				nx=x-ent.x;
				ny=y-ent.y;
			}
		}
	}
	void touchBlock(int x,int y,Block b){
		super.touchBlock(x,y,b);
		if(b.isSolid())
		if(!hook){
			hook=true;
			next=null;
			nx=this.x;
			ny=this.y;
			bx=x;
			by=y;
		}
	}
	public void draw(graphics.Canvas cv){//绘制
		super.draw(cv);
		if(hook){
			double x1=nx,y1=ny;
			if(next!=null){
				x1+=next.x;
				y1+=next.y;
			}
			float ps[]=new float[]{0,0,(float)(x1-x),(float)(y1-y)};
			cv.drawLines(ps,0xff800000);
		}
	}
}

