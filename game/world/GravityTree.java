package game.world;

import static java.lang.Math.*;

public class GravityTree{
	int x,y,w,h,m;
	double cgx,cgy;
	GravityTree c1,c2;
	GravityTree(int x0,int y0,int w0,int h0){
		x=x0;
		y=y0;
		w=w0;
		h=h0;
		if(w>h){
			c1=new GravityTree(x,y,w/2,h);
			c2=new GravityTree(x+w/2,y,w-w/2,h);
			up();
		}else if(h>1){
			c1=new GravityTree(x,y,w,h/2);
			c2=new GravityTree(x,y+h/2,w,h-h/2);
			up();
		}else{
			m=0;
			cgx=x+0.5;
			cgy=y+0.5;
			if(!World.cur.get(x,y).isCoverable()){
				m=World.cur.get(x,y).mass();
			}
		}
	}
	void update(int x0,int y0,int a){
		if(a==0)return;
		if(x0<x||x0>=x+w||y0<y||y0>=y+h)return;
		if(w>h){
			(x0-x<w/2?c1:c2).update(x0,y0,a);
			up();
		}else if(h>1){
			(y0-y<h/2?c1:c2).update(x0,y0,a);
			up();
		}else{
			m+=a;
		}
	}
	void up(){
		m=c1.m+c2.m;
		if(m!=0){
			cgx=(c1.cgx*c1.m+c2.cgx*c2.m)/m;
			cgy=(c1.cgy*c1.m+c2.cgy*c2.m)/m;
		}
	}
	public void query(game.entity.Entity e){
		if(m==0)return;
		double xd=cgx-e.x,yd=cgy-e.y,d=hypot(xd,yd)+0.5;
		if(d<max(w,h)*3){
			if(c1!=null){
				c1.query(e);
				c2.query(e);
			}
		}else{
			double G=e.last_g*3e-3;
			/*if(e instanceof game.entity.Player && World.cur.time%300==0){
				System.out.printf("[%d,%d][%d,%d]   cg: %g,%g   d: %g   m: %d   ga: %g,%g   G:%g\n",
				x,x+w,y,y+h,
				cgx,cgy,
				d,
				m,
				xd/d*m/d,
				yd/d*m/d,
				G);
			}*/
			e.impulse(xd/d,yd/d,m/d*e.mass()*G);
		}
	}
}
