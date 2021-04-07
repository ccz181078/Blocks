package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.Agent;
import game.entity.Human;
import game.entity.Entity;
import game.entity.EnergyBall;
import game.entity.DroppedItem;

public class DetectorBallLauncher extends EnergyBallLauncher implements CamaraController{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DetectorBallLauncher");
	public BmpRes getBmp(){return bmp;}
	EnergyBall ball=null;
	public int energyCost(){return 20;}
	public Entity getBall(){
		ball=new EnergyBall();
		ball.setHpScale(4);
		return ball;
	}
	@Override
	public double[] getCamaraPos(Agent a){
		if(ball!=null&&ball.removed)ball=null;
		if(ball==null)return new double[]{a.x,a.y};
		return new double[]{ball.x,ball.y};
	}
}

