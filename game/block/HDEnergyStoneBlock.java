package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;

public class HDEnergyStoneBlock extends ExplosiveBlock{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/HDEnergyStoneBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 1000;}
	
	public boolean circuitCanBePlaced(){return false;}
	@Override
	public void onFireUp(int x,int y){
		if(World.cur.get(x,y)==this){
			World.cur.setAir(x,y);
			onDestroy(x+0.5,y+0.5);
		}
	}
	@Override
	public void onDestroy(double x,double y){
		EnergyExplodeBall.gen(x,y,SourceTool.make(SourceTool.block(f2i(x),f2i(y),this),"产生的"));
	}
	@Override
	protected int crackType(){return 0;}
	public double hardness(){return game.entity.NormalAttacker.HD;}

};
