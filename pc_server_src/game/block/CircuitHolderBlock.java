package game.block;
import game.entity.Entity;

public class CircuitHolderBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	int maxDamage(){return 20;}
	public boolean isSolid(){return false;}
	public boolean circuitCanBePlaced(){return true;}
	public void touchEnt(int x,int y,Entity ent){}
	
}
