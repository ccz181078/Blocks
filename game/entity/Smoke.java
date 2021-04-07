package game.entity;
import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.block.Block;
public class Smoke extends NonInteractiveEnt{
	private static final long serialVersionUID=1844677L;

	static BmpRes bmp[]=BmpRes.load("Entity/Smoke_",1);

	@Override
	public double width(){return height();}

	@Override
	public double height(){return min(1,sqrt(hp)*0.15);}
	
	@Override
	public BmpRes getBmp(){
		return bmp[0];
	}
	
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	
	@Override
	public double mass(){return 0.01;}

	public Smoke(){
		hp=5;
	}
	
	@Override
	public void update(){
		super.update();
		f+=0.3;
		hp-=0.6;
	}

	@Override
	public double gA(){
		return 0;
	}
}
