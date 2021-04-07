package game.entity;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class CactusBoss extends CactusMonster{
	private static final long serialVersionUID=1844677L;
	public double maxHp(){return max_hp;}
	int max_hp;
	double target_dist=1e6;
	CactusBoss parent=null;
	
	@Override
	public void ai(){
		if(gA()!=0&&rnd()<0.95){
			super.ai();
			return;
		}
		xdir=ydir=0;
		f+=0.1;
		if(target!=null&&target.isRemoved())target=null;
		if(target!=null){
			double xd=target.x-x;
			double yd=target.y-y;
			double d=hypot(xd,yd)+1e-8;
			xa+=xd/d*0.02;
			ya+=yd/d*0.02;
			target_dist=d-sqrt(target.maxHp());
			if(max_hp>=5000&&rnd()<0.05&&!in_wall){
				CactusBoss child=new CactusBoss(x,y,this);
				double x1=rnd_gaussion()*0.001;
				double y1=rnd_gaussion()*0.001;
				child.thrownFrom(this,x,y,x1,y1);
				child.source=source;
				child.x=x+x1;
				child.y=y+y1;
				child.target=target;
			}
			if(parent!=null&&!parent.isRemoved()&&rnd()<0.05){
				if(parent.target_dist>target_dist)parent.target=target;
				else if(parent.target_dist<target_dist&&parent.target!=null)target=parent.target;
			}
		}else{
			target_dist=1e6;
		}
		if(parent!=null&&parent.isRemoved())parent=parent.parent;
		if(parent!=null&&!parent.isRemoved()){
			double xd=parent.x-x;
			double yd=parent.y-y;
			double d=hypot(xd,yd)+1e-8;
			xd/=d;
			yd/=d;
			if(d>5)d=5;
			double s=0.005*d;
			if(target!=null){
				if(distL2(target)>4)s*=2;
			}
			if((parent.hp+hp)*2<(max_hp+parent.max_hp)||target==null&&parent.target==null){
				s*=2;
			}else if(d<5&&(target==null||parent.target!=target))s*=d*2./5.-1;
			xa+=xd*s;
			ya+=yd*s;
		}
	}
	@Override
	public void showHpChange(){}
	@Override
	void touchAgent(Agent a){
		if(isRemoved()||a.isRemoved())return;
		if(parent==a){
			if((parent.hp+hp)*2<(max_hp+parent.max_hp)&&rnd()<0.1||(target==null||distL2(target)>4)){
				removed=true;
				parent.hp+=hp;
				parent.max_hp+=max_hp;
			}
		}
		super.touchAgent(a);
	}
	@Override
	void touchBlock(int x,int y,Block b){
		if(rnd()<0.3&&target!=null)b.des(x,y,3,this);
		super.touchBlock(x,y,b);
	}
	@Override
	public double gA(){return target!=null?0:0.03;}
	@Override
	public boolean chkRemove(long t){return false;}
	public CactusBoss(double _x,double _y){
		super(_x,_y);
		hp=max_hp=100000;
		parent=null;
	}
	public CactusBoss(double _x,double _y,CactusBoss parent){
		super(_x,_y);
		this.parent=parent;
		hp=(parent.hp/=2);
		max_hp=(parent.max_hp>>=1);
	}
	@Override
	public void draw(graphics.Canvas cv){
		if(parent!=null&&!parent.isRemoved()){
			double xd=parent.x-x;
			double yd=parent.y-y;
			double d=hypot(xd,yd)+1e-8;
			if(d>0.5&&d<5){
				double x1=xd/d*0.25,y1=yd/y*0.25;
				cv.drawLines(new float[]{(float)x1,(float)y1,(float)(xd-x1),(float)(yd-y1)},0xff00cc00);
			}
		}
		super.draw(cv);
	}
	protected double attackVal(){return 2;}
	protected double targetNullProb(){return 0;}
}
