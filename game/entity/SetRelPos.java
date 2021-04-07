package game.entity;

import game.world.Event;

public class SetRelPos extends Event{
	private static final long serialVersionUID=1844677L;
	protected Entity e,target;
	protected double x,y;
	public SetRelPos(Entity e,Entity target,double x,double y){
		this.e=e;
		this.target=target;
		this.x=x;
		this.y=y;
		addAfterMove();
	}
	public void run(){
		if(target==null){
			e.x=x;
			e.y=y;
		}else{
			e.x=target.x+x;
			e.y=target.y+y;
		}
	}
}
class SyncVel extends Event{
	private static final long serialVersionUID=1844677L;
	protected Entity e1,e2;
	protected double x,y;
	public SyncVel(Entity e1,Entity e2){
		this.e1=e1;
		this.e2=e2;
		addAfterMove();
	}
	public void run(){
		double m1=e1.mass();
		double m2=e2.mass();
		double m=m1+m2+1e-300;
		double xv=(m1*e1.xv+m2*e2.xv)/m;
		double yv=(m1*e1.yv+m2*e2.yv)/m;
		//System.out.printf("(%g,%g)  (%g,%g)  (%g,%g)\n",e1.xv,e1.yv,e2.xv,e2.yv,xv,yv);
		e1.xv=e2.xv=xv;
		e1.yv=e2.yv=yv;
	}
}