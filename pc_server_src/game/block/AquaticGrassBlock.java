package game.block;

import util.BmpRes;
import game.world.World;
import static java.lang.Math.*;
import game.item.AquaticGrass;
import game.item.Item;
import game.item.Scissors;

public class AquaticGrassBlock extends AquaticPlantBlock{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return AquaticGrass.bmp;}
	
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(World.cur.get(x-1,y).isCoverable()||World.cur.get(x+1,y).isCoverable()){
			++damage;
		}
		Block d=World.cur.get(x,y-1).rootBlock();
		dirt_v=max(0,dirt_v-0.01f);
		light_v=max(0,light_v-0.1f);
		if(d.getClass()==AquaticGrassBlock.class)spread((PlantType)d,0.2f);
		else if(d instanceof DirtType){
			if(light_v>2){
				light_v-=2;
				dirt_v=min(5f,dirt_v+1);
			}
		}else{
			++damage;
		}
		if(dirt_v>3&&World.cur.get(x,y+1).rootBlock().getClass()==WaterBlock.class){
			AquaticGrassBlock w=new AquaticGrassBlock();
			spread(w,0.2f);
			World.cur.place(x,y+1,w);
		}
		return false;
	}

	public void onDestroy(int x,int y){
		new AquaticGrass().drop(x,y);
		super.onDestroy(x,y);
	}
	
}
