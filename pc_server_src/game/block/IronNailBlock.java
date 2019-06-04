package game.block;
import game.entity.Entity;

public class IronNailBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	int maxDamage(){return 40;}
	public void touchEnt(int x,int y,Entity ent){
		ent.onAttacked(Math.min(intersection(x,y,ent)*2,0.2),null);
		super.touchEnt(x,y,ent);
	}
}
