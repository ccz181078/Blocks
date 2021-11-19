package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import util.BmpRes;

public class BoxShield extends BigShield implements ItemContainer{
	static BmpRes bmp=new BmpRes("Item/BoxShield");
	public BmpRes getBmp(){return bmp;}
	public Attack transform(Attack a){
		return a;
	}
	public double height(){return 0.5;}
	public double mass(){return 2.5f;}
	public double maxHp(){return 1000;}
	//public double touchVal(){return 0.05;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	protected ItemList items=ItemList.emptyList(32);
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
