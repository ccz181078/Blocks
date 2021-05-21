package game.item;

import util.BmpRes;
import game.entity.*;

public class DarkBall extends BasicBall{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkBall");
	public BmpRes getBmp(){return bmp;}
	
	public void onExplode(Entity pos,double tx,double ty,int amount,Source src){
		pos.explode(50,()->new game.entity.DarkBall().setHpScale(amount),false);
	}
	@Override
	game.entity.Entity getBall(){return new game.entity.DarkBall().setHpScale(35);}
};
