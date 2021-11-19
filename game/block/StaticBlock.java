package game.block;

import game.world.World;
import game.entity.*;
import game.item.Item;

public class StaticBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	public StaticBlock(Block _block){super(_block);}
	@Override
	public Block deStatic(int x,int y){
		Block b=(Block)block.clone();
		World.cur.set(x,y,b);
		return b;
	}

	@Override
	public double light(){
		return block.light();
	}

	@Override
	public double transparency(){
		return block.transparency();
	}
	@Override
	public void onOverlap(int x,int y,Entity ent,double k){
		deStatic(x,y).onOverlap(x,y,ent,k);
	}
	@Override
	double friction(){return block.friction();}//表面
	@Override
	double frictionIn1(){return block.frictionIn1();}//内部
	@Override
	double frictionIn2(){return block.frictionIn2();}//内部
	@Override
	public double fallValue(){return block.fallValue();}
	@Override
	double minEnterVel(){return block.minEnterVel();}

	public boolean onSpannerClick(int x,int y){return false;}
	
	public void onPress(int x,int y,Item item){
		deStatic(x,y).onPress(x,y,item);
	}
	public void onLight(int x,int y,double v){
		deStatic(x,y).onLight(x,y,v);
	}
	public boolean onClick(int x,int y,Agent agent){
		return deStatic(x,y).onClick(x,y,agent);
	}
	public boolean onCheck(int x,int y){
		return deStatic(x,y).onCheck(x,y);
	}
	public boolean onUpdate(int x,int y){
		if(block.updateCond(x,y))return deStatic(x,y).onUpdate(x,y);
		return false;
	}
	public int getDamage(){return block.damage;}
	protected void des(int x,int y,int v){
		deStatic(x,y).des(x,y,v);
	}
	public void onCircuitDestroy(int x,int y){}
	public void onFireUp(int x,int y){
		deStatic(x,y).onFireUp(x,y);
	}
	public void onBurn(int x,int y){
		deStatic(x,y).onBurn(x,y);
	}
	@Override
	public boolean fallable(){return block.fallable();}
	@Override
	public void fall(int x,int y,double xmv,double ymv){deStatic(x,y).fall(x,y,xmv,ymv);}
};

