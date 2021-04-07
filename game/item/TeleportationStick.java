package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class TeleportationStick extends Tool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/TeleportationStick");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 0;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	int cd=0;
	public Item clickAt(double x,double y,Agent a){
		if(cd>0)return this;
		int c=rf2i(hypot(x-a.x,y-a.y)*0.2+a.mass()*0.5);
		new SetRelPos(a,null,x,y);
		damage+=c;
		cd+=c*4+8;
		return this;
	}
	@Override
	public void onCarried(game.entity.Agent a){
		if(cd>0)--cd;
	}
	@Override
	public boolean useInArmor(){return true;}
	public void onDesBlock(Block b){}
	public int maxDamage(){return 10;}
	public void onBroken(double x,double y){
		new IronStick().drop(x,y);
		super.onBroken(x,y);
	}
	public double repairRate(){return 0.1;}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(cd>0)return false;
		if(a!=null){
			double d=h.distLinf(a);
			if(d<8){
				double x1=h.x+rnd(-4,4),y1=h.y+rnd(-4,4);
				if(max(abs(x1-a.x),abs(y1-a.y))<d)
				if(game.world.World.cur.noBlock(x1,y1,h.width(),h.height())){
					h.clickAt(x1,y1);
					return true;
				}
			}
		}else if(h.xdir!=0){
			for(int t=0;t<10;++t){
				double x1=h.xdir*rnd(0,4),y1=rnd(-4,4);
				if(game.world.World.cur.noBlock(h.x+x1,h.y+y1,h.width(),h.height())){
					h.clickAt(h.x+x1,h.y+y1);
					return true;
				}
			}
		}
		return false;
	}
}
