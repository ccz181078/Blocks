package game.item;

import graphics.Canvas;
import game.entity.Human;
import game.entity.Player;
import util.BmpRes;
import game.entity.DroppedItem;

public abstract class EnergyTool extends Tool implements DefaultEnergyContainer,DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	public static final double E0=0.0125;
	SpecialItem<EnergyCell> ec=new SpecialItem<EnergyCell>(EnergyCell.class);
	public double toolVal(){return 0;}
	public void onDesBlock(game.block.Block b){}
	public double hardness(){return game.entity.NormalAttacker.IRON;}

	public ShowableItemContainer getItems(){return ec;}
	public EnergyContainer getEnergyContainer(){return ec.get();}


	public void drawInfo(Canvas cv){
		super.drawInfo(cv);
		EnergyCell e=ec.get();
		if(e!=null)game.ui.UI.drawProgressBar(cv,0xff00ffff,0xff007f7f,e.getEnergy()*1f/e.maxEnergy(),-0.4f,-0.3f,0.4f,-0.23f);
	}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(1,3));
		super.onBroken(x,y);
	}
	protected boolean findEnergyCell(Human h,int n){
		if(!hasEnergy(n)){
			ec.drop(h.x,h.y);
			for(SingleItem si:h.items.toArray()){
				if(si.get() instanceof EnergyCell){
					if(((EnergyCell)si.get()).getEnergy()>=n)ec.insert(si);
				}
			}
		}
		
		return hasEnergy(n);
	}
}
