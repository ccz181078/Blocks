package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class HEAT_Bullet extends WeakBullet implements BallProvider{
	static BmpRes bmp=new BmpRes("Item/HEAT_Bullet");
	public BmpRes getBmp(){return bmp;}
	public void onKill(double x,double y,game.entity.Bullet ent){
		ent.explodeDirected(this,3,0.3,1,0.05,1.5);
	}
	public Entity getBall(){
		return new HE_FireBall();
	}
}
