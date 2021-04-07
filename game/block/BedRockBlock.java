package game.block;

import util.BmpRes;

public class BedRockBlock extends Block{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/BedRockBlock");
	public BmpRes getBmp(){return bmp;}
	public boolean circuitCanBePlaced(){return false;}
	public void des(int x,int y,int v){} 
	@Override
	double frictionIn1(){return 0.5;}
	@Override
	double frictionIn2(){return 10000;}
	@Override
	public boolean fallable(){return false;}
	@Override
	public void fall(int x,int y,double xmv,double ymv){}
}
