package game.item;

import static util.MathUtil.*;
import game.block.Block;
import game.entity.Agent;
import game.world.World;
import util.BmpRes;
import game.block.AirBlock;
import game.entity.Player;

public final class BlockItem extends Item{
private static final long serialVersionUID=1844677L;
	Block block;
	public BlockItem(Block _block){block=_block;}
	boolean cmpType(Item b){return b instanceof BlockItem&&block.cmpType(((BlockItem)b).block);}
	public boolean isAir(){return block.getClass()==AirBlock.class;}
	public BmpRes getBmp(){return block.getBmp();}
	public int fuelVal(){return block.fuelVal();}
	public int heatingTime(boolean in_furnace){return block.heatingTime(in_furnace);}
	public Item heatingProduct(boolean in_furnace){return block.heatingProduct(in_furnace);}
	public BlockItem clone(){
		BlockItem bi=null;
		try{
			bi=(BlockItem)super.clone();
			bi.block=block.clone();
		}catch(Exception e){e.printStackTrace();}
		return bi;
	}
	public String getName(){return util.AssetLoader.loadString(block.getClass(),"name");}
	public String getDoc(){return util.AssetLoader.loadString(block.getClass(),"doc");}
	public Item clickAt(double x,double y,Agent a){
		if(!isAir()){
			boolean placeable=World._.placeable(f2i(x),f2i(y));
			if(a instanceof Player){
				Player pl=(Player)a;
				if(pl.creative_mode&&pl.suspend_mode)placeable=World._.get(x,y).isCoverable();
			}
			if(placeable){
				World._.place(f2i(x),f2i(y),block);
				return null;
			}
			if(a instanceof Player){
				((Player)a).fail_to_place_block=10;
			}
		}
		return super.clickAt(x,y,a);
	}

};
