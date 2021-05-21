package game.item;

import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.*;

public class ShootTool{
	public static boolean auto(final Human h,final Agent a,final ShootableTool tool){
		final double x=(a.x-h.x)*(1+rnd_gaussion()*0.5),y=(a.y-h.y)*(1+rnd_gaussion()*0.5);
		NearbyInfo ni[]=new NearbyInfo[1];
		OneDimFunc f=new OneDimFunc(){public double get(double z){
			double c=cos(z),s=sin(z);
			int dir0=h.dir;
			h.dir=(x*c+y*s>0?1:-1);
			Entity e=tool.test_shoot(h,h.x+(x*c+y*s),h.y+(-x*s+y*c));
			h.dir=dir0;
			//Line.gen(h.x+(x*c+y*s),h.y+(-x*s+y*c),h.x,h.y);
			//System.err.println("test_shoot:"+e);
			return Entity.predictHit(e,a,ni[0]);
		}};
		
		Pos p=Optimizer.optimize(f,0,1,0.01);
		if(p.y<0){
			ni[0]=World.cur.getNearby(h.x,h.y,World.cur.setting.BW,World.cur.setting.BW/2,false,true,true);
			if(f.get(p.x)>=0)return true;
			double c=cos(p.x),s=sin(p.x);
			h.clickAt(h.x+(x*c+y*s),h.y+(-x*s+y*c));
			return true;
		}
		return false;
	}
}
