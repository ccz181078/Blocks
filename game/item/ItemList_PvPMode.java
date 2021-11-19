package game.item;
import util.SerializeUtil;
import graphics.Canvas;
import game.entity.Player;
import game.world.*;
import game.world.StatMode.StatResult;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.GlobalSetting;

public class ItemList_PvPMode extends ItemList{
	private static final long serialVersionUID=1844677L;
	public ItemList_PvPMode(Item it[]){
		super(it.length);
		for(int i=0;i<it.length;++i)si[i]=new SingleItemWithPrice(it[i]);
	}
	public static class SingleItemWithPrice extends SingleItem{
		private static final long serialVersionUID=1844677L;
		public SingleItemWithPrice(Item item){
			super(item,1);
		}
		@Override
		public void draw(Canvas cv,SingleItem si,int flag){
			StatResult res=((PvPMode)World.cur.getMode()).getStat();
			cv.drawItemFrame((flag&SELECTED_FLAG)!=0);
			if(si.isEmpty())return;
			Item it=si.get();
			double cost=res.getPrice(it,true);
			cv.drawItem(it.getBmp(),(cost<=game.ui.UI.pl.money));
			it.drawInfo(cv);
			float sz=GlobalSetting.getGameSetting().text_size;
			cv.drawText(res.getPriceString(cost),0.35f,0.35f,Math.min(sz,0.7f),1);
		}
	}
	public void onClick(Player pl,SingleItem s,ItemContainer ic){
		if(ic==null)return;
		StatResult res=((PvPMode)World.cur.getMode()).getStat();
		Item it=(Item)SerializeUtil.deepCopy(s.get());
		double cost=res.getPrice(it,true);
		int cnt=min((pl.batch_op?it.maxAmount():1),f2i(pl.money/cost));
		SingleItem si=it.setAmount(cnt);
		if(cnt>0)ic.insert(si);
		cnt-=si.getAmount();
		pl.money-=cost*cnt;
	}

	public boolean dragable(){return false;}
	
	public void insert(SingleItem it){
		StatResult res=((PvPMode)World.cur.getMode()).getStat();
		double c=res.getPrice(it.get(),false)*it.getAmount();
		if(c>1e10)return;
		game.ui.UI.pl.money+=c;
		it.clear();
	}
}

