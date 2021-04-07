package game.block;

import graphics.Canvas;
import game.entity.Entity;
import game.item.Item;
import game.world.World;

public abstract class AquaticPlantBlock extends PlantType{
	private static final long serialVersionUID=1844677L;
	int maxDamage(){return 1;}
	public double transparency(){return 0.35;}
	public int fuelVal(){return 0;}
	
	@Override
	public Block rootBlock(){return WaterBlock.getInstance();}

	public void draw(Canvas cv){
		WaterBlock.bmp.draw(cv,0,0,0.5f,0.5f);
		super.draw(cv);
	}
	public void touchEnt(int x,int y,Entity ent){
		WaterBlock.getInstance().touchEnt(x,y,ent);
	}
	public void onDestroy(int x,int y){
		if(World.cur.get(x,y).rootBlock().getClass()==AirBlock.class){
			World.cur.set(x,y,WaterBlock.getInstance());
		}
	}
}
