package game.entity;
import game.world.World;

public class NonInteractiveEnt extends Entity{
	private static final long serialVersionUID=1844677L;

	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	@Override
	public void add(){
		if(is_test){
			last_launched_ent=this;
			return;
		}
		World.cur.newNonInteractiveEnt(this);
	}
	@Override
	public boolean harmless(){return true;}
	
	@Override
	public void update(){
		shadowed=false;
	}
	
	@Override
	public boolean chkAgent(){
		return false;
	}
	@Override
	public boolean chkEnt(){
		return false;
	}
	
}
