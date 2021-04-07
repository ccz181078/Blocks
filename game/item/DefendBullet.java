package game.item;

import util.BmpRes;
import game.entity.Entity;

public class DefendBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DefendBullet");
	public BmpRes getBmp(){return bmp;}
	public int maxAmount(){return 999;}
	@Override
	public double attackValue(){return 2;}
	@Override
	public double mass(){return 0.05;}
	@Override
	public double RPG_ExplodeProb(){return 0.1;}
};
