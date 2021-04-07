package game.item;

import game.entity.*;

public class EnergySwordType extends SwordType{
	private static final long serialVersionUID=1844677L;
	@Override
	protected double toolVal(){return 4;}
	public int maxDamage(){return 200;}
	public double repairRate(){return 10;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public void onDesBlock(game.block.Block b){}
	public void onBroken(double x,double y){
		new IronStick().drop(x,y);
		super.onBroken(x,y);
	}
	public boolean longable(){return true;}
}