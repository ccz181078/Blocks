package game.item;
import util.*;
import game.entity.*;

public class IronNailArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/IronNailArmor");
	@Override public BmpRes getBmp(){return bmp;}
	@Override public int maxDamage(){return super.maxDamage()/2;}
	@Override
	public void touchAgent(Human w,Agent a){
		double difx=w.xv-a.xv,dify=w.yv-a.yv;
		a.onAttacked((difx*difx+dify*dify)*12+0.25,SourceTool.armor(w,this),this);
	}
}
