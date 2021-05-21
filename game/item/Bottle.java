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
		if(block.isSolid())_this.onBroken();
	}
	public final void touchEnt(game.entity.Bottle _this,game.entity.Entity ent){
		_this.onBroken();
	}
	protected boolean noUse(){return getClass()==Bottle.class;}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(h.distLinf(a)<3)return false;
		if(noUse())return false;
		return super.autoUse(h,a);
	}
}
