package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class EntityPicker extends Tool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EntityPicker");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public void onDesBlock(Block b){}
	public int maxDamage(){return 2000;}
	Agent dst=null;
	long last_pick_time;
	int picking_time=0;
	public void drawTip(graphics.Canvas cv,Player pl){
		if(dst!=null){
			double c=picking_time/(pl.distLinf(dst)*30+60.);
			float w=(float)dst.width();
			float h=(float)dst.height();
			cv.save();
			cv.translate((float)(dst.x-pl.x),(float)(dst.y-pl.y));
			game.ui.UI.drawProgressBar(cv,0x6000ffff,0x3000ffff,(float)c,-w,-h,w,h);
			cv.restore();
		}
	}
	@Override
	public void onCarried(Agent a){
		if(last_pick_time+1<World.cur.time)dst=null;
		if(dst!=null)++damage;
	}
	@Override
	public boolean onLongPress(Agent w,double tx,double ty){
		if(abs(tx-w.x)>4||abs(ty-w.y)>4)return false;
		for(Agent a:World.cur.getNearby(tx,ty,0.1,0.1,false,false,true).agents){
			if(a.pickable()){
				if(last_pick_time+1<World.cur.time||dst!=a){
					picking_time=0;
				}
				last_pick_time=World.cur.time;
				dst=a;
				++picking_time;
			}
		}
		if(dst!=null){
			if(!dst.pickable()){
				dst=null;
			}else if(picking_time>=w.distLinf(dst)*30+60){
				dst.pickedBy(w);
				dst=null;
			}
		}
		return true;
	}
}
