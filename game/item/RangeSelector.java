package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;
import game.world.World;
import game.block.*;
import java.util.ArrayList;
import game.ui.*;

public class RangeSelector extends Item{
	static BmpRes bmp=new BmpRes("Item/RangeSelector");
	public BmpRes getBmp(){return bmp;}
	
	Entity.FriendGroup friend_group=new Entity.FriendGroup();
	
	@Override
	public int maxAmount(){
		return 1;
	}
	
	long last_press_time=0;
	boolean pressing=false;
	double tx1,ty1,tx2,ty2;
	
	ArrayList<Agent> selected=null;
	
	public void drawTip(graphics.Canvas cv,Player pl){
		if(pressing){
			double l=min(tx1,tx2),r=max(tx1,tx2);
			double t=min(ty1,ty2),b=max(ty1,ty2);
			cv.drawRect((float)(l-pl.x),(float)(t-pl.y),(float)(r-pl.x),(float)(b-pl.y),0x40ffffff);
			double x=(l+r)/2,y=(t+b)/2,xd=(r-l)/2,yd=(b-t)/2;
			for(Agent a:World.cur.getNearby(x,y,xd,yd,false,false,true).agents){
				cv.drawRect((float)(a.left-pl.x),(float)(a.bottom-pl.y),(float)(a.right-pl.x),(float)(a.top-pl.y),0x40ffffff);
			}
		}else if(selected!=null){
			for(Agent a:selected){
				if(!a.isRemoved()){
					cv.drawRect((float)(a.left-pl.x),(float)(a.bottom-pl.y),(float)(a.right-pl.x),(float)(a.top-pl.y),a.friend_group==friend_group?0x40ffffff:0x40ff0000);
				}
			}
		}
	}
	@Override
	public void onCarried(Agent a){
		if(last_press_time+1<World.cur.time)pressing=false;
	}
	@Override
	public boolean onLongPress(Agent w,double tx,double ty){
		return true;
	}
	
	public void setCursorState(Agent w,boolean on,double tx,double ty,long press_time){
		if(!(w instanceof Player))return;
		Player pl=(Player)w;
		if(on){
			last_press_time=World.cur.time;
			if(!pressing){
				pressing=true;
				tx1=tx;ty1=ty;
			}
			tx2=tx;ty2=ty;
			//selected=null;
		}else if(pressing){
			double l=min(tx1,tx2),r=max(tx1,tx2);
			double t=min(ty1,ty2),b=max(ty1,ty2);
			double x=(l+r)/2,y=(t+b)/2,xd=(r-l)/2,yd=(b-t)/2;
			if(xd<0.1&&yd<0.1&&press_time<=10&&selected!=null){
				for(Agent a:selected){
					if(!a.isRemoved()&&a.friend_group==friend_group){
						a.clickAt(x,y);
					}
				}
			}else{
				selected=World.cur.getNearby(x,y,xd,yd,false,false,true).agents;
				for(Agent a:selected){
					if(!a.isRemoved()&&a.friend_group==null){
						a.friend_group=friend_group;
					}
				}
			}
			pressing=false;
		}
	}
	
	public void onUse(Human hu){
		hu.items.getSelected().insert(this);
		if(!(hu instanceof Player))return;
		Player pl=(Player)hu;
		if(selected==null)return;
		if(selected.size()<1)return;
		Agent a0=selected.get(0);
		UI_MultiPage ui=new UI_MultiPage(){
			public boolean exists(){
				if(pl.getCarriedItem().get()==RangeSelector.this&&selected!=null&&!a0.isRemoved())return true;
				return false;
			}
		};
		a0.initUI(ui);
		UI ui_stgy=new UI_Group(0,0){
			{
				for(int i=-1;i<=1;++i){
					for(int j=-1;j<=1;++j){
						final int i0=i,j0=j;
						addChild(new UI_Button(i+1.5f,j+1.5f){
							protected BmpRes getBmp(){return Player.work_btn;}
							protected void onPress(){
								for(Agent a:selected){
									if(!a.isRemoved()&&a.friend_group==friend_group){
										a.xdir=i0;
										a.ydir=-j0;
									}
								}
							}
						});
					}
				}
			}
		};
		ui.addPage(new BlueCrystalEnergyCell(),ui_stgy);
		pl.openDialog(ui);
	}
	
	public String getAmountString(int cnt){
		return "";
	}

	@Override
	public boolean isCreative(){return true;}

}
