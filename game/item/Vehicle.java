package game.item;

import static game.ui.UI.drawProgressBar;
import game.entity.*;
import game.world.World;

public class Vehicle extends EnergyArmor{
	private static final long serialVersionUID=1844677L;
	
	@Override
	public boolean isClosed(){return true;}
	
	protected float reload=maxReload();
	protected float maxReload(){return 1f;}
	
	@Override
	public void drawLeftInfo(graphics.Canvas cv){
		super.drawLeftInfo(cv);
		float reload=this.reload/maxReload();
		drawProgressBar(cv,0xff7fffff,0xff3f7f7f,reload,2.1f,0.3f,3.9f,0.4f);
	}
	
	public Item clickAt(double x,double y,Agent a){
		return new VehiclePlaceHolder(x,y,this).cadd() ? null : this;
	}
	@Override
	public void onBroken(double x,double y){
		onBrokenTip();
	}
	@Override
	public void onLaunchAtPos(Agent a,int dir,double x,double y,double slope,double mv2){
		Entity e=new VehiclePlaceHolder(x,y,this);
		a.throwEntAtPos(e,dir,x,y,slope,mv2);
	}
	
	protected boolean checkEnergy(int e,Human w){
		if(!hasEnergy(e)){
			damage+=w.xdir*w.xdir+w.ydir*w.ydir;
			if(w.ydep!=0)w.xf+=0.75;
			if(w.xdep!=0)w.yf+=0.75;
			return false;
		}
		return true;
	}
};