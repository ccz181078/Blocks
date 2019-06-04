package game.ui;

import game.entity.Player;
import game.item.Craft;
import graphics.Canvas;
import game.world.World;
import game.item.SingleItem;
import game.item.ItemList;
import game.item.SelectableItemList;
import util.BmpRes;

public class UI_Craft extends UI_ItemList{
	private static final long serialVersionUID=1844677L;
	static BmpRes craft_bmp=new BmpRes("UI/craft_arrow");
	public UI_Craft(Craft[] _cls,float _x){
		super(_x,0,4,5,ItemList.craftList(_cls),null);
	}
	public UI_Craft(Craft[] _cls){
		this(_cls,0);
	}
	private Craft getCraft(){
		return ((Craft.CraftItem)(((SelectableItemList)il).getSelected())).getCraft();
	}
	protected void onDraw(Canvas cv){
		cv.drawRect(0,4,4,8,0xffaaaaaa);
		super.onDraw(cv);
		
		Craft c=getCraft();
		
		cv.gridBegin(.5f,5.5f);
		SingleItem[] si=pl.getItems().toArray();
		for(int i=0;i<c.in.length;++i){
			int flag=0;
			if(Craft.count(si,c.in[i].get())<c.in[i].getAmount())flag|=SingleItem.ALPHA_FLAG;
			c.in[i].draw(cv,c.in[i],flag);
			cv.gridNext();
		}
		cv.end();

		
		boolean flag=c.check(pl);
		
		if(flag)craft_bmp.drawInRect(cv,0,6,1,7);

		cv.gridBegin(.5f,7.5f);
		for(int i=0;i<c.out.length;++i){
			c.out[i].draw(cv,c.out[i],flag?0:SingleItem.ALPHA_FLAG);
			cv.gridNext();
		}
		cv.end();
		
		//if(pl.cur_craft!=null){
		//float v=1-(pl.craft_ft-World._.time)*1f/pl.cur_craft.cost.time;
		float v=pl.getCraftProcess();
		if(v>=0)drawProgressBar(cv,0xff00ff00,0xff007700,v,0.1f,6.7f,3.9f,6.8f);
		//}
	}
	protected boolean onPress(float tx,float ty){
		if(super.onPress(tx,ty))return true;
		if(tx>=0&&tx<width&&ty>=5&&ty<8){
			getCraft().start(pl);
			return true;
		}
		return false;
	}
	public void close(){
		pl.stopCraft();
		super.close();
	}
}

