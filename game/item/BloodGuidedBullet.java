package game.item;

import util.BmpRes;
import game.entity.Entity;

public class BloodGuidedBullet extends GuidedBullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BloodGuidedBullet");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected Entity toEnt(){return new game.entity.BloodGuidedBullet(this);}
	public void onKill(double x,double y){}
};
