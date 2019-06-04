package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import java.util.Collections;
import java.util.Arrays;
import game.block.Block;
import game.block.WaterBlock;

public class LavaMonster extends NormalAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/LavaMonster");
	public BmpRes getBmp(){return bmp;}
	public double maxHp(){return 30;}
	public LavaMonster(double _x,double _y){super(_x,_y);}
	protected Entity getBall(){return new FireBall();}

	public AttackFilter getAttackFilter(){return fire_filter;}

	void touchBlock(int px,int py,Block b){
		if(!removed&&(b.rootBlock() instanceof WaterBlock)){
			explode(hp+5);
			remove();
		}else super.touchBlock(px,py,b);
	}
	
	void onKill(){
		new game.item.FireBall().drop(x,y,3);
	}
}


