package game.entity;
import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
public class Fragment extends NonInteractiveEnt{
	BmpRes bmp;
	float l,t,r,b;
	double xd,yd;

	@Override
	public double width(){return xd;}

	@Override
	public double height(){return yd;}
	
	public static class Config{
		double x,y,xd,yd;
		int xn=2,yn=2,cnt=4;
		BmpRes bmp;
		int t0=30,t1=4;
		double xv=0,yv=0,v_scale=0.03;
		public Config setPos(double x,double y,double xd,double yd,BmpRes bmp){
			this.x=x;
			this.y=y;
			this.xd=xd;
			this.yd=yd;
			this.bmp=bmp;
			return this;
		}
		public Config setEnt(Entity e){
			this.x=e.x;
			this.y=e.y;
			this.xd=e.width();
			this.yd=e.height();
			this.bmp=e.getBmp();
			this.xv=e.xv/2;
			this.yv=e.yv/2;
			return this;
		}
		public Config setGrid(int xn,int yn,int cnt){
			this.xn=xn;
			this.yn=yn;
			this.cnt=cnt;
			return this;
		}
		public Config setTime(int t){
			t0=0;
			t1=t;
			return this;
		}
		public Config setVel(double c){
			v_scale=c;
			return this;
		}
		public void apply(){
			long flag=0;
			for(int i=0;i<cnt;++i){
				int x0=rndi(0,xn-1),y0=rndi(0,yn-1);
				long bit=1L<<(x0+y0*xn);
				if((flag&bit)!=0){
					--i;
					continue;
				}
				flag|=bit;
				Fragment e=new Fragment();
				e.bmp=bmp;
				double x1=(x0+0.5)/xn*2-1,y1=(y0+0.5)/yn*2-1;
				e.l=x0*1f/xn;
				e.t=y0*1f/yn;
				e.r=(x0+1)*1f/xn;
				e.b=(y0+1)*1f/yn;
				e.x=x+x1*xd;
				e.y=y-y1*yd;
				e.xv=(x1+rnd_gaussion())*v_scale+xv;
				e.yv=(y1+rnd_gaussion())*v_scale+yv;
				e.xd=(float)xd/xn;
				e.yd=(float)yd/yn;
				e.hp=t0+rnd_gaussion()*t1;
				e.add();
			}
		}
	}
	
	public static void gen(double x,double y,double xd,double yd,int xn,int yn,int cnt,BmpRes bmp){
		new Config().setPos(x,y,xd,yd,bmp).setGrid(xn,yn,cnt).apply();
	}
	
	public boolean chkBlock(){return true;}
	public boolean chkRigidBody(){return true;}

	@Override
	public double gA(){
		return super.gA()*0.3;
	}

	@Override
	public void update(){
		super.update();
		hp-=1;
	}

	@Override
	public void draw(Canvas cv){
		cv.drawBitmapRevY(bmp,l,t,r,b,(float)xd,(float)yd);
	}
	
}
