package game.item;

import util.*;
import game.block.Block;
import game.entity.*;
import game.world.World;

public class BlockFillTool extends Tool implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/BlockFillTool");
	@Override
	public BmpRes getBmp(){return bmp;}
	
	SingleItem block=new SingleItem();
	@Override
	public ShowableItemContainer getItems(){
		return ItemList.create(block);
	}
	
	@Override
	public double toolVal(){return 1;}
	@Override
	public int maxDamage(){return 1000;}
	@Override
	public BmpRes getUseBmp(){return use_btn;}
	@Override
	public void onUse(Human a){
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			((Player)a).openDialog(new game.ui.UI_Item(this,block));
		}
	}
	@Override
	public boolean onLongPress(Agent a,double tx,double ty){
		Item b=block.popItem();
		if(b!=null){
			if(b instanceof BlockItem)b=b.clickAt(tx,ty,a);
			if(b==null)++damage;
			else block.insert(b);
		}
		return true;
	}
	
	public String getAmountString(int cnt){
		if(!block.isEmpty()){
			return ""+block.getAmount();
		}
		return "";
	}
	@Override
	public void drawInfo(graphics.Canvas cv){
		if(!block.isEmpty()){
			cv.save();
			cv.scale(0.6f,0.6f);
			Item it=block.get();
			cv.drawItem(it.getBmp(),true);
			it.drawInfo(cv);
			cv.restore();
			/*int c=block.getAmount();
			float sz=game.GlobalSetting.getGameSetting().text_size;
			cv.drawText(Integer.toString(c),0.35f,0.35f,Math.min(sz,0.7f),1);*/
		}
		super.drawInfo(cv);
	}
	
	@Override
	public void insert(SingleItem it){block.insert(it);}
	@Override
	public SingleItem[] toArray(){return block.toArray();}
}
