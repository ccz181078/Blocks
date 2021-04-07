package game.item;

import util.BmpRes;
import game.entity.Entity;

public class PathBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/PathBullet");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected Entity toEnt(){return new game.entity.PathBullet(this);}
	public void onKill(double x,double y){}
	public int maxAmount(){return 999;}//最大叠加
	public void touchEnt(game.entity.Bullet self,Entity ent){
		double v2=self.v2rel(ent);
		if(self.hp>0){
			self.hp-=10;
			ent.onAttacked(Math.max(0,v2+1)*20,self);
		}
	}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
};
