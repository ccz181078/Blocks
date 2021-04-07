package game.item;

import util.BmpRes;
import game.entity.*;

public class Bottle extends ThrowableItem{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/Bottle");
	public BmpRes getBmp(){return bmp;}
	public void onBroken(double x,double y,game.entity.Bottle ent){}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
	
	@Override
	protected Entity toEnt(){
		return new game.entity.Bottle(this);
	}
	int energyCost(){return 5;}
	public void touchBlock(game.entity.Bottle _this,int px,int py,game.block.Block block){
		if(!block.isCoverable())_this.onBroken();
	}
	public final void touchEnt(game.entity.Bottle _this,game.entity.Entity ent){
		_this.onBroken();
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		return false;
	}
}
