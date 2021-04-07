package game.entity;
import util.BmpRes;
import static util.MathUtil.*;
import game.block.StoneBlock;

public class BigDarkWorm extends DarkWorm{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/BigDarkWorm");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public double maxHp(){return 15;}
	public double gA(){return 0.02;}
	
	@Override
	public double mass(){return 0.7;}
	
	public BigDarkWorm(double _x,double _y){super(_x,_y);}
}
