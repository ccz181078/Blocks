package game.item;
import graphics.Canvas;
import game.entity.Player;

public class SelectableItemList extends ItemList{
	private static final long serialVersionUID=1844677L;
	SingleItem selected;
	public SelectableItemList(int n){
		super(n);
		select();
	}
	public void select(SingleItem si){selected=si;}
	public void select(){
		if(si.length>0)selected=si[0];
		else selected=new SingleItem();
	}
	public SingleItem getSelected(){return selected;}
	public void draw(Canvas cv,SingleItem si,int flag){
		if(si==selected)flag|=SingleItem.SELECTED_FLAG;
		super.draw(cv,si,flag);
	}
	public Item popItem(){
		return selected.popItem();
	}

	public void insert(SingleItem it){
		if(si.length>0)super.insert(it);
	}
	
	public Item get(){
		return selected.get();
	}
	public void onClick(Player pl,SingleItem s,ItemContainer ic){
		select(s);
		super.onClick(pl,s,ic);
	}
}
