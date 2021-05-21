package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class Cursor extends Item{
	static BmpRes bmp=new BmpRes("Item/IronStick");
	public BmpRes getBmp(){return bmp;}
	UIEnt dst=null;
	public void drawTip(graphics.Canvas cv,Player pl){
		/*if(dst!=null){
			double c=picking_time/(pl.distLinf(dst)*30+60.);
			float w=(float)dst.width();
			float h=(float)dst.height();
			cv.save();
			cv.translate((float)(dst.x-pl.x),(float)(dst.y-pl.y));
			game.ui.UI.drawProgressBar(cv,0x6000ffff,0x3000ffff,(float)c,-w,-h,w,h);
			cv.restore();
		}*/
	}
	@Override
	public void onCarried(Agent a){}
	@Override
	public Item clickAt(double tx,double ty,Agent w){
		dst=null;
		for(Entity a:World.cur.getNearby(tx,ty,0.1,0.1,false,true,false).ents){
			if(a instanceof UIEnt){
				dst=(UIEnt)a;
				break;
			}
		}
		if(dst!=null)dst.onClick();
		return this;
	}
}
