package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.*;
import game.entity.*;
import game.item.*;
import java.io.*;
import graphics.Canvas;

public abstract class CircuitBlock extends Block{
	private static final long serialVersionUID=1844677L;
	Block block;
	CircuitBlock(Block _block){
		block=_block;
	}
	
	public Block rootBlock(){return block;}
	//basic info and event
	int maxDamage(){return block.maxDamage();}
	public double transparency(){return block.transparency();}
	public boolean isCoverable(){return block.isCoverable();}//return whether you can place a block on the position of "this"
	public boolean isSolid(){return block.isSolid();}
	public void onPlace(int x,int y){block.onPlace(x,y);}
	public void onPress(int x,int y,Item item){block.onPress(x,y,item);}
	public boolean onClick(int x,int y,Agent agent){return block.onClick(x,y,agent);}
	public boolean onCheck(int x,int y){
		if(block.onCheck(x,y)){
			onCircuitDestroy(x,y);
			return true;
		}
		return false;
	}
	public boolean onUpdate(int x,int y){
		if(block.onUpdate(x,y)){
			onCircuitDestroy(x,y);
			return true;
		}
		return false;
	}
	public void des(int x,int y,int v){block.des(x,y,v);}
	public void onDestroy(int x,int y){
		block.onDestroy(x,y);
		onCircuitDestroy(x,y);
	}
	abstract public void onCircuitDestroy(int x,int y);

	//fire
	public int fuelVal(){return block.fuelVal();}
	public int heatingTime(boolean in_furnace){return block.heatingTime(in_furnace);}
	public Item heatingProduct(boolean in_furnace){return block.heatingProduct(in_furnace);}
	public void onFireUp(int x,int y){block.onFireUp(x,y);}
	public void onBurn(int x,int y){block.onBurn(x,y);}
	public void onDestroyByFire(int x,int y){block.onDestroyByFire(x,y);}

	//energy
	public int energyValX(){return block.energyValX();}
	public int energyValY(){return block.energyValY();}
	public final boolean circuitCanBePlaced(){return false;}

	//physics
	double friction(){return block.friction();}
	public void touchEnt(int x,int y,Entity ent){block.touchEnt(x,y,ent);}

	//draw
	public BmpRes getBmp(){return block.getBmp();}
	public void draw(Canvas cv){block.draw(cv);}
};

