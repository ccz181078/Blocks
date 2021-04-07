package game.item;

import game.block.Block;
import game.entity.Entity;
import game.entity.Agent;
import game.entity.DroppedItem;
import game.world.World;
import java.util.ArrayList;
import static util.MathUtil.*;

public class ItemAbsorber extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 5000;}
	public void onDesBlock(Block b){}
	public void onCarried(Agent a){
		if(hasEnergy(1)&&rnd()<0.1){
			ArrayList<Entity>ents=World.cur.getNearby(a.x,a.y,4,4,false,true,false).ents;
			if(ents.size()>0){
				Entity e=ents.get(rndi(0,ents.size()-1));
				if(e instanceof DroppedItem){
					DroppedItem d=(DroppedItem)e;
					loseEnergy(1);
					d.x=a.x;
					d.y=a.y;
				}
			}
		}
	}
}
