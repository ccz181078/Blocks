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
	public double repairRate(){return 2e-4*maxDamage();}
	
	public int maxAir(){
		return 2;
	}
	
	public int wearEnergyCost(){return 30;}
	
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
	
	static void drawRotatingItem(graphics.Canvas cv,SingleItem si,double x,double y){
		Item w=si.get();
		if(w!=null){
			cv.save();
			cv.translate((float)x,(float)y);
			new ThrowedItem(0,0,w).draw(cv);
			cv.restore();
		}
	}
	static void updateRotatingItem(Human hu,SingleItem si,double x,double y){
		Item w=si.popItem();
		if(w!=null){
			Entity e=new ThrowedItem(0,0,w).initPos(hu.x+x,hu.y+y,hu.xv,hu.yv,hu);
			e.update0();
			e.update();
			e.anti_g=1;
			e.move();
			hu.impulseMerge(e);
			w.onCarried(hu);
			if(w.isBroken()){
				w.onBroken(x,y,hu);
				w=null;
			}
		}
		si.insert(w);
	}
};
