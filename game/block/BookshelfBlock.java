package game.block;
import game.ui.*;
import static game.ui.UI.*;
import game.item.*;
import static game.item.ItemList.*;
import util.*;
import graphics.*;
import game.entity.*;

public class BookshelfBlock extends WoodenType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	
	private static BmpRes[] bmps=BmpRes.load("Block/BookshelfBlock_",2);
	int maxDamage(){return 50;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	public boolean isempty=true;
	public BmpRes getBmp(){return bmps[isempty?0:1];}
	private ShowableItemContainer books;
	@Override
	public boolean isDeep(){return true;}
	
	@Override
	public void onPlace(int x,int y){
		books=ItemList.emptyNonOverlapList(6);
	}
	
	@Override
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(books,x+0.5f,y+0.5f);
		books=null;
		isempty=true;
		super.onDestroy(x,y);
	}
	
	@Override
	public UI getUI(BlockAt ba){
		UI_Group uig=new UI_Group(-7,0);
		uig.addChild(new UI_ItemList(0.5f,1,3,2,books,pl.il));
		uig.setBlock(ba);
		return uig;
	}
	
	/*@Override
	public boolean onUpdate(int x,int y){
		for(SingleItem si : books.toArray()){
			if(si.get() instanceof Book){
				isempty=false;
				return true;
			}
		}
		isempty=true;
		return false;
	}*/
	
	@Override
	public void draw(Canvas cv){
		for(SingleItem si : books.toArray()){
			if(si.get() instanceof Book){
				isempty=false;
				super.draw(cv);
				return;
			}
		}
		isempty=true;
		super.draw(cv);
	}
}
