package game.item;

import util.BmpRes;
import static util.MathUtil.*;import game.entity.*;
import game.entity.*;
import game.block.*;
import game.world.*;


public class BlockActivator extends Tool{
	public int maxDamage(){return 1000;}
	protected double toolVal(){return 0;}
	private static BmpRes bmp=new BmpRes("Item/BlockActivator");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	BlockAt ba=null;
	long last_pick_time;
	int picking_time=0;
	/*@Override
	public void drawTip(graphics.Canvas cv,Player pl){
		if(ba!=null){
			double c=picking_time/(ba.block.maxDamage()+30.);
			cv.save();
			cv.translate((float)(ba.x-pl.x),(float)(ba.y-pl.y));
			game.ui.UI.drawProgressBar(cv,0x6000ffff,0x3000ffff,(float)c,0,0,1,1);
			cv.restore();
		}
	}
	@Override
	public void onCarried(Agent a){
		if(last_pick_time+1<World.cur.time)ba=null;
	}
	@Override
	public boolean onLongPress(Agent w,double tx,double ty){
		if(abs(tx-w.x)>4||abs(ty-w.y)>4)return false;
		int x=f2i(tx),y=f2i(ty);
		Block b=World.cur.get(x,y);
		if(ba!=null){
			if(ba.x!=x||ba.y!=y||ba.block!=b)ba=null;
			else{
				last_pick_time=World.cur.time;
				++picking_time;
				if(picking_time>=ba.block.maxDamage()+30){
					World.cur.get(ba.x,ba.y).fall();
				}
			}
		}else if(b.fallable()&&b.isSolid()){
			last_pick_time=World.cur.time;
			picking_time=0;
			ba=new BlockAt(x,y,b);
		}
		return true;
	}*/
}

