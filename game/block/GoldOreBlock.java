package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.Coal;
import game.item.GoldParticle;

public class GoldOreBlock extends StoneBlock{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/GoldOreBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 90;}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		new GoldParticle().drop(x,y,rndi(2,6));
	}
};
