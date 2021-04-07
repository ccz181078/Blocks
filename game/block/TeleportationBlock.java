package game.block;

import util.BmpRes;
import game.item.ItemList;
import game.entity.DroppedItem;
import game.ui.*;
import game.item.Item;
import static util.MathUtil.*;

public class TeleportationBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Block/TeleportationBlock");
	public BmpRes getBmp(){return bmp;}
	int x1=-1,y1=-1,x2=-1,y2=-1;
	int maxDamage(){return 1;}
	protected void des(int x,int y,int v){if(rnd()*20<v)super.des(x,y,1);}
	private ItemList items=null;
	public boolean forceCheckEnt(){return true;}
	public boolean chkNonRigidEnt(){return true;}
	public void onPlace(int x,int y){
		super.onPlace(x,y);
		if(x1==-1&&y1==-1){
			x1=x;
			y1=y;
		}else{
			x2=x;
			y2=y;
		}
	}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		x1=y1=x2=y2=-1;
	}
	public boolean isSolid(){return false;}
	public void touchEnt(int x,int y,game.entity.Entity ent){
		if(x1!=x||y1!=y||x2==-1||y2==-1)return;
		if(rnd()<intersection(x,y,ent)){
			new game.entity.SetRelPos(ent,ent,x2-x1,y2-y1);
		}
	}
	public int maxAmount(){return 2;}
	public boolean cmpType(Item b){return this==b;}
	public Block clone(){return this;}
	public boolean isDeep(){return true;}
};
