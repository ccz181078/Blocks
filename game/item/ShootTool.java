package game.item;

import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class ShootTool{
	public static boolean auto(final Human h,final Agent a,final ShootableTool tool){
		final double x=(a.x-h.x)*(1+rnd_gaussion()*0.5),y=(a.y-h.y)*(1+rnd_gaussion()*0.5);
		OneDimFunc f=new OneDimFunc(){public double get(double z){
			double c=cos(z),s=sin(z);
			Entity e=tool.test_shoot(h,h.x+(x*c+y*s),h.y+(-x*s+y*c));
			Line.gen(h.x+(x*c+y*s),h.y+(-x*s+y*c),h.x,h.y);
			//System.err.println("test_shoot:"+e);
			return Entity.predictHit(e,a);
		}};
		
		Pos p=Optimizer.optimize(f,0,1,0.01);
		if(p.y<0){
			double c=cos(p.x),s=sin(p.x);
			h.clickAt(h.x+(x*c+y*s),h.y+(-x*s+y*c));
			return true;
		}
		return false;
	}
}
interface ShootableTool{
	Entity test_shoot(Human h,double x,double y);
}
