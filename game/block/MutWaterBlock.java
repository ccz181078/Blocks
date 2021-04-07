package game.block;
import util.BmpRes;

public class MutWaterBlock extends WaterBlock{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/MutWaterBlock");
	public BmpRes getBmp(){return bmp;}
	public double transparency(){return 0.8;}
	double frictionIn2(){return 200;}
	int maxDamage(){return 15;}
	
	/*private static MutWaterBlock instance=new MutWaterBlock();
	public static MutWaterBlock getInstance(){return instance;}
	private Object readResolve(){return instance;}
	private Object writeReplace(){return instance;}
	public MutWaterBlock clone(){return this;}
	protected MutWaterBlock(){}*/
	
	public static MutWaterBlock getInstance(){return new MutWaterBlock();}
	protected MutWaterBlock(){}

}
