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
	private static BmpRes bmp=new BmpRes("Armor/Ball8");
	private static BmpRes bmp1=new BmpRes("Item/Ball8");
	public BmpRes getBmp(){return bmp1;}
	ItemList items=ItemList.emptyNonOverlapList(8);
	public ShowableItemContainer getItems(){return ItemList.create(ec,items);}
	
	public void onUpdate(Human hu){
		super.onUpdate(hu);
		SingleItem is[]=items.toArray();
		for(int i=0;i<8;++i){
			double a=PI*2*(i+0.5)/8;
			double c=cos(a),s=sin(a),r=1.5;
			double x=c*r,y=s*r;
			updateRotatingItem(hu,is[i],x,y);
		}
	}
	
	public Attack transform(Attack atk){
		SingleItem is[]=items.toArray();
		for(int t=0;t<2;++t){
			int i=rndi(0,7);
			Item w=is[i].popItem();
			if(w instanceof Shield){
				atk=((Shield)w).transform(atk);
				if(w.isBroken()){
					double a=PI*2*(i+0.5)/8;
					double c=cos(a),s=sin(a),r=1.5;
					double x=c*r,y=s*r;
					w.onBroken(x,y);
					w=null;
				}
			}
			is[i].insert(w);
			if(atk==null)return null;
		}
		return super.transform(atk);
	}

	public void onBroken(double x,double y){
		super.onBroken(x,y);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		super.draw(cv,hu);
		bmp.draw(cv,0,0,(float)width(),(float)height());
		SingleItem is[]=items.toArray();
		float xys[]=new float[32];
		for(int i=0;i<8;++i){
			double a=PI*2*(i+0.5)/8;
			double c=cos(a),s=sin(a),r=1.5;
			double x=c*r,y=s*r;
			xys[(i*4-2)&31]=xys[i*4]=(float)x;
			xys[(i*4-1)&31]=xys[i*4+1]=(float)y;
		}
		cv.drawLines(xys,0xff00aaaa);
		for(int i=0;i<8;++i){
			double a=PI*2*(i+0.5)/8;
			double c=cos(a),s=sin(a),r=1.5;
			double x=c*r,y=s*r;
			drawRotatingItem(cv,is[i],x,y);
		}
	}
}
