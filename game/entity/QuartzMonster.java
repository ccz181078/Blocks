package game.entity;
import util.BmpRes;

public class QuartzMonster extends StoneMonster{
	private static final long serialVersionUID=1844677L;
	
	static BmpRes bmp=new BmpRes("Entity/QuartzMonster");
	@Override public BmpRes getBmp(){return bmp;}
	public QuartzMonster(double _x,double _y){super(_x,_y);}
	@Override public double maxHp(){return 100;}
	@Override public void onKill(){
		new game.block.QuartzBlock().drop(x,y);
		super.onKill();
	}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
}
