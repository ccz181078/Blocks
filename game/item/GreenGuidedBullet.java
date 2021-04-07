package game.item;

import util.BmpRes;
import game.entity.Entity;

public class GreenGuidedBullet extends GuidedBullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/GreenGuidedBullet");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected Entity toEnt(){return new game.entity.GreenGuidedBullet(this);}
	public void onKill(double x,double y){}
	public double attackValue(){return 50;}
	public int maxAmount(){return 99;}//最大叠加
};
