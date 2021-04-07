package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class Ball8 extends FastBox{
	private static final long serialVersionUID=1844677L;
	ItemList items=ItemList.emptyNonOverlapList(8);
	public ShowableItemContainer getItems(){return ItemList.create(ec,items);}
	
	public void onUpdate(Human hu){
		super.onUpdate(hu);
		SingleItem is[]=items.toArray();
		for(int i=0;i<8;++i){
			Item w=is[i].popItem();
			if(w!=null){
				double a=PI*2*(i+0.5)/8;
				double c=cos(a),s=sin(a),r=1.5;
				double x=c*r,y=s*r;
				Entity e=new ThrowedItem(0,0,w).initPos(hu.x+x,hu.y+y,hu.xv,hu.yv,hu);
				e.update0();
				e.update();
				e.anti_g=1;
				e.move();
				hu.impulseMerge(e);
				w.onCarried(hu);
				if(w.isBroken()){
					w.onBroken(x,y,hu);
					w=null;
				}
			}
			is[i].insert(w);
		}
	}

	public void onBroken(double x,double y){
		super.onBroken(x,y);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		super.draw(cv,hu);
		SingleItem is[]=items.toArray();
		for(int i=0;i<8;++i){
			Item w=is[i].get();
			if(w!=null){
				double a=PI*2*(i+0.5)/8;
				double c=cos(a),s=sin(a),r=1.5;
				double x=c*r,y=s*r;
				cv.save();
				cv.translate((float)x,(float)y);
				new ThrowedItem(x,y,w).draw(cv);
				cv.restore();
			}
		}
	}
}
