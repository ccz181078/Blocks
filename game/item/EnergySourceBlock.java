package game.block;
import util.BmpRes;
import game.entity.Entity;
import game.world.World;
import game.entity.Agent;
import game.item.EnergySource;
import graphics.Canvas;

public class EnergySourceBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;

	public EnergySourceBlock(Block b){super(b);}
	public int energyValL(){return 32;}
	public int energyValR(){return 32;}
	public int energyValD(){return 32;}
	public int energyValU(){return 32;}

	public void onCircuitDestroy(int x,int y){
		new EnergySource().drop(x,y);
		World.cur.check4(x,y);
	}

	public void draw(Canvas cv){
		super.draw(cv);
		EnergySource.bmp.draw(cv,0,0,.5f,.5f);
	}
}
