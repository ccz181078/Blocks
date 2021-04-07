package game.block;
import util.BmpRes;
import game.world.World;

public class EnergyTorchBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/EnergyTorchBlock");
	public BmpRes getBmp(){return bmp;}
	
	public boolean isCoverable(){return true;}
	public boolean isSolid(){return false;}
	public double transparency(){return 0;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public void touchEnt(int x,int y,game.entity.Entity ent){}
	
	
	@Override
	int maxDamage(){return 1;}

	@Override
	public double light(){
		return 0.8;
	}
	
	public boolean onCheck(int x,int y){
		if(!World.cur.get(x,y-1).isSolid()){
			des(x,y,1);
		}
		return super.onCheck(x,y);
	}
	public boolean onUpdate(int x,int y){
		return super.onUpdate(x,y);
	}
}
