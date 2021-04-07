package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;

public class HookRope extends Tool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/StringItem");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public int maxDamage(){return 1000;}
	public boolean using=false,climbing;
	public Item clickAt(double x,double y,Agent a){
		if(!using){
			double xd=x-a.x,yd=y-a.y,d=hypot(xd,yd)+1e-8;
			RopeHook w=new RopeHook();
			w.initPos(a.x,a.y,a.xv+xd/d*0.6,a.yv+yd/d*0.6,a);
			w.link(a,0,0);
			w.add();
			using=true;
			climbing=false;
		}else{
			climbing=!climbing;
		}
		return this;
	}
};

