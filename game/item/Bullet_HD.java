package game.item;

import util.BmpRes;
import game.entity.Entity;

public class Bullet_HD extends Bullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Bullet_HD");
	public BmpRes getBmp(){return bmp;}
	@Override
	public double attackValue(){return 120;}
	@Override
	public double mass(){return 4;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
};
