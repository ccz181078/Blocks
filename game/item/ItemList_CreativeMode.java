package game.item;
import util.SerializeUtil;
import graphics.Canvas;
import game.entity.Player;

public class ItemList_CreativeMode extends ItemList{
	private static final long serialVersionUID=1844677L;
	public ItemList_CreativeMode(Item it[]){
		super(it.length);
		for(int i=0;i<it.length;++i)si[i]=it[i].setAmount(1);
	}
	public void onClick(Player pl,SingleItem s,ItemContainer ic){
		if(ic==null)return;
		Item it=(Item)SerializeUtil.deepCopy(s.get());
		ic.insert(it.setAmount(pl.batch_op?it.maxAmount():1));
	}

	public boolean dragable(){return false;}
	
	public void insert(SingleItem it){it.clear();}
}

