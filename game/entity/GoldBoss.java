package game.entity;

import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;
import game.block.BedRockBlock;

public class GoldBoss extends SpaceBoss{
	public GoldBoss(double _x,double _y){
		super(_x,_y);
	}
	public double maxHp(){return 1e8;}
	public Entity getBall(){
		if(rnd()<0.5)return new GoldCube();
		return new DarkCube();
	}

	public void update(){
		super.update();
		if(hp>0)hp=min(hp*1.001+1,maxHp());
	}
	protected int getColor(){return 0xffffdd00;}
}

