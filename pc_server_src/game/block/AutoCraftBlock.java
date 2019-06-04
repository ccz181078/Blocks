package game.block;

import util.BmpRes;
import game.entity.Agent;
import game.item.*;
import game.world.World;
import game.entity.DroppedItem;
import game.entity.Player;
import game.ui.*;
import static util.MathUtil.*;
import java.util.ArrayList;

public class AutoCraftBlock extends CraftHelperBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/AutoCraftBlock_",2);
	public BmpRes getBmp(){
		if(ch==null)return bmp[0];
		//if(task!=null&&!ch.free())return bmp[(int)World.cur.time/8&1];
		if(ch.free())return bmp[0];
		return bmp[1];
	}
	int maxDamage(){return 200;}
	Craft task;
	public void onPlace(int x,int y){
		super.onPlace(x,y);
		task=null;
	}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		task=null;
	}
	public static void check(BlockAt ba,Craft c){
		if(ba==null||c==null)return;
		Block b=World.cur.get(ba.x,ba.y+1);
		if(b instanceof AutoCraftBlock)((AutoCraftBlock)b).task=c;
	}
	public UI getUI(BlockAt ba){
		UI_MultiPage ui=new UI_MultiPage();
		ui.addPage(new EnergyCell(),new UI_ItemList(ec,UI.pl.il));
		return ui.setBlock(ba);
	}
	public boolean onUpdate(final int x,final int y){
		if(super.onUpdate(x,y))return true;
		if(ch.free()){
			if(task!=null&&World.cur.get(x,y-1).getCraftType()!=0){
				task.start(new Crafter(){
					public ItemContainer getItems(){
						return new ItemContainer(){
							public void insert(SingleItem it){gain(it);}
							public SingleItem[] toArray(){
								ArrayList<SingleItem>si=new ArrayList<>();
								for(int t=-1;t<=1;t+=2){
									Block b=World.cur.get(x+t,y);
									if(b instanceof IronBoxBlock)
										for(SingleItem s:((IronBoxBlock)b).toArray())
											if(!s.isEmpty())si.add(s);
								}
								SingleItem ss[]=new SingleItem[si.size()];
								return si.toArray(ss);
							}
						};
					}
					public int getCraftType(){return World.cur.get(x,y-1).getCraftType();}
					public boolean startCraft(Craft craft){return ch.startCraft(craft);}
					public float getCraftProcess(){return ch.getCraftProcess();}
					public int getEnergy(){return ch.getEnergy();}
					public void loseEnergy(int v){ch.loseEnergy(v);}
					public void gain(SingleItem si){ch.gain(si);}
				});
			}
		}
		return false;
	}
	
};
