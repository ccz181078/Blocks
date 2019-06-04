package game.item;

import static util.MathUtil.*;
import graphics.Canvas;
import game.block.Block;

public abstract class Tool extends Item{
private static final long serialVersionUID=1844677L;
	public int damage=0;
	abstract public int maxDamage();
	public final int maxAmount(){return 1;}
	boolean cmpType(Item it){
		if(getClass()==it.getClass())return damage==0&&((Tool)it).damage==0;
		return false;
	}
	abstract protected double toolVal();
	public void drawInfo(Canvas cv){
		if(damage>0)game.ui.UI.drawProgressBar(cv,0xff00ff00,0xff007f00,1-damage*1f/maxDamage(),-0.4f,-0.4f,0.4f,-0.33f);
	}
	abstract public void onBroken(double x,double y);
	public final boolean isBroken(){return damage>=maxDamage();}
	public void onDesBlock(Block b){
		++damage;
	}
	public void onAttack(game.entity.Agent Agent){
		++damage;
	}
	public double repairRate(){return 1;}
};

abstract class HammerType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*1.0)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.1)+1;}
	public int axVal()    {return rf2i(toolVal()*0.2)+1;}
	public int swordVal() {return rf2i(toolVal()*0.7)+1;}
}

abstract class PickaxType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*1.0)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.4)+1;}
	public int axVal()    {return rf2i(toolVal()*0.3)+1;}
	public int swordVal() {return rf2i(toolVal()*0.4)+1;}
}

abstract class AxType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*0.5)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.4)+1;}
	public int axVal()    {return rf2i(toolVal()*1.0)+1;}
	public int swordVal() {return rf2i(toolVal()*0.7)+1;}
}

abstract class ShovelType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*0.1)+1;}
	public int shovelVal(){return rf2i(toolVal()*1.0)+1;}
	public int axVal()    {return rf2i(toolVal()*0.3)+1;}
	public int swordVal() {return rf2i(toolVal()*0.2)+1;}
}

abstract class SwordType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*0.3)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.3)+1;}
	public int axVal()    {return rf2i(toolVal()*0.5)+1;}
	public int swordVal() {return rf2i(toolVal()*1.0)+1;}
}

