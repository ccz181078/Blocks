package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;

public class BoxShield extends BigShield implements ItemContainer{
	public Attack transform(Attack a){
		return a;
	}
	public double height(){return 0.5;}
	public double mass(){return 2.5f;}
	public double maxHp(){return 2000;}
	public double touchVal(){return 0.05;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	private ItemList items=ItemList.emptyList(32);
	public boolean onDragTo(SingleItem src,SingleItem dst,boolean batch){return false;}
	public boolean onClickEnt(double x,double y,Agent a){
		if(a instanceof Player && a.distLinf(a)<4){
			Player pl=(Player)a;
			pl.openDialog(new game.ui.UI_ItemList(-7,0,4,8,items,pl.il){
				public boolean exist(){
					return pl.distLinf(ent)<4 && !ent.isRemoved();
				}
			});
		}
		return false;
	}
	public void update(){
		if(rnd()<0.01){
			int cnt=0;
			for(SingleItem si:items.toArray()){
				if(!si.isEmpty())++cnt;
			}
			if(cnt==0||cnt<=3&&rnd()<0.1)ent.kill();
		}
	}
	public void onKill(){
		DroppedItem.dropItems(items,ent.x,ent.y);
	}
	public void pick(DroppedItem item){
		items.insert(item.item);
	}
	public SingleItem[] toArray(){
		return items.toArray();
	}
	public void insert(SingleItem si){
		items.insert(si);
	}
}
