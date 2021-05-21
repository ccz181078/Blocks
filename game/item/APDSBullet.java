package game.item;

import util.BmpRes;
import game.entity.Entity;

public class APDSBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/APDSBullet");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected Entity toEnt(){return new game.entity.APDSBullet(this);}
	public void onKill(double x,double y){}
	public double attackValue(){return 40;}
};
