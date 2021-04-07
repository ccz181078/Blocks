package game.item;

import util.BmpRes;
import game.entity.Entity;

public class DarkGuidedBullet extends GuidedBullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkGuidedBullet");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected Entity toEnt(){return new game.entity.DarkGuidedBullet(this);}
	public void onKill(double x,double y){}
	public boolean chkBlock(){return false;}
};
