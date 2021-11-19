package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.World;
import game.block.Block;
import game.block.MineBlock;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class Mine extends Item implements BlockItem{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/Mine");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public Item clickAt(double x,double y,Agent a){
		Block b=World.cur.get(x,y);
		int px=f2i(x),py=f2i(y);
		if(b.circuitCanBePlaced()){
			MineBlock w=new MineBlock(b,this);
			World.cur.setCircuit(px,py,w);
			return null;
		}
		return super.clickAt(x,y,a);
	}
	public void explode(double x,double y,Entity e,Warhead wh,Source src){
		double xd=e.x-x,yd=e.y-y;
		double d=sqrt(xd*xd+yd*yd+1e-8);
		xd/=d;yd/=d;
		if(wh!=null)wh.explode(x,y,xd,yd,src,null);
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		double D=h.distLinf(a);
		if(D>2&&D<4){
			if(h.selectItem(Warhead.class,true)){
				double x=a.x+a.width()*a.xdir,y=a.bottom;
				if(World.cur.get(x,y).circuitCanBePlaced()){
					h.selectItem(Mine.class,false);
					h.clickAt(x,y);
					h.selectItem(Warhead.class,true);
					h.clickAt(x,y);
					return true;
				}
			}
		}
		return super.autoUse(h,a);
	}
};
