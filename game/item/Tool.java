package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.StatMode.StatResult;

public abstract class Tool extends Item{
private static final long serialVersionUID=1844677L;
	public int damage=0;
	
	abstract public int maxDamage();
	public final int maxAmount(){return 1;}
	/*public boolean cmpType(Item it){
		if(getClass()==it.getClass()){
			return damage==0&&((Tool)it).damage==0;
		}
		return false;
	}*/
	abstract protected double toolVal();
	public void drawInfo(Canvas cv){
		if(damage>0)game.ui.UI.drawProgressBar(cv,0xff00ff00,0xff007f00,1-damage*1f/maxDamage(),-0.4f,-0.4f,0.4f,-0.33f);
	}
	public final boolean isBroken(){return damage>=maxDamage();}
	public void onDesBlock(Block b){
		if(rnd()<b.getWeight(this))++damage;
	}
	public void onAttack(Entity a,Source src){
		++damage;
	}
	public double repairRate(){return 2e-3*maxDamage();}
	public void onVanish(double x,double y,Source src){
		onBroken(x,y);
	}
	public String getAmountString(int cnt){
		int c=max(0,maxDamage()-damage);
		if(c<100)return "("+Integer.toString(c)+")";
		return "";
	}
};

abstract class HammerType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*1.0)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.1)+1;}
	public int axVal(){return rf2i(toolVal()*0.2)+1;}
	public int swordVal(){return rf2i(toolVal()*0.7)+1;}
	public boolean longable(){return true;}
	public boolean dominate(Item it){
		if(it instanceof HammerType){
			HammerType h=(HammerType)it;
			if(h.swordVal()<swordVal())return true;
		}
		return super.dominate(it);
	}
}

abstract class PickaxType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*1.0)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.4)+1;}
	public int axVal(){return rf2i(toolVal()*0.3)+1;}
	public int swordVal(){return rf2i(toolVal()*0.4)+1;}
	public boolean longable(){return true;}
	public boolean dominate(Item it){
		if(it instanceof PickaxType){
			PickaxType h=(PickaxType)it;
			if(h.swordVal()<swordVal())return true;
		}
		return super.dominate(it);
	}
}

abstract class AxType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*0.5)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.4)+1;}
	public int axVal(){return rf2i(toolVal()*1.0)+1;}
	public int swordVal(){return rf2i(toolVal()*0.7)+1;}
	public boolean longable(){return true;}
	public boolean dominate(Item it){
		if(it instanceof AxType){
			AxType h=(AxType)it;
			if(h.swordVal()<swordVal())return true;
		}
		return super.dominate(it);
	}
}

abstract class ShovelType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*0.1)+1;}
	public int shovelVal(){return rf2i(toolVal()*1.0)+1;}
	public int axVal(){return rf2i(toolVal()*0.3)+1;}
	public int swordVal(){return rf2i(toolVal()*0.2)+1;}
	public boolean longable(){return true;}
	public boolean dominate(Item it){
		if(it instanceof ShovelType){
			ShovelType h=(ShovelType)it;
			if(h.swordVal()<swordVal())return true;
		}
		return super.dominate(it);
	}
}

abstract class SwordType extends Tool{
private static final long serialVersionUID=1844677L;
	public int pickaxVal(){return rf2i(toolVal()*0.3)+1;}
	public int shovelVal(){return rf2i(toolVal()*0.3)+1;}
	public int axVal(){return rf2i(toolVal()*0.5)+1;}
	public int swordVal(){return rf2i(toolVal()*1.0)+1;}
	public boolean longable(){return true;}
	public boolean dominate(Item it){
		if(it instanceof SwordType){
			SwordType h=(SwordType)it;
			if(h.swordVal()<swordVal())return true;
		}
		return super.dominate(it);
	}
}

