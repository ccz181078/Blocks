package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import game.block.Block;
import game.block.MineBlock;
import game.block.SmokeBlock;
import game.world.World;

public class Warhead_RPG extends Warhead implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_RPG");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return null;}
	public int maxAmount(){return 1;}
	
	public NonOverlapSpecialItem<RPGItem> rpg=new NonOverlapSpecialItem<>(RPGItem.class);
	public ShowableItemContainer getItems(){return rpg;}
	
	public void explode(double x,double y,double xv,double yv,Source src,Agent a){
		if(rpg.isEmpty())return;
		getBall().initPos(x,y,xv,yv,SourceTool.launch(SourceTool.launch(src,getName()))).add();
	}
	public int getBallCnt(){return rpg.getAmount();}
	public Entity getBall(){return rpg.popItem().toEnt();}
	@Override
	protected boolean explodeDirected(){return false;}
};
