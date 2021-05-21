package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import game.block.Block;
import game.block.MineBlock;
import game.block.SmokeBlock;
import game.world.World;

public abstract class Warhead extends Item implements BallProvider{
	private static final long serialVersionUID=1844677L;
	public int maxAmount(){return 16;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		double v0=sqrt(1+slope*slope),xv=dir/v0,yv=dir*slope/v0;
		explode(x+xv/2,y+yv/2,xv,yv,a.getLaunchSrc(),a);
	}
	public void onExplode(Entity pos,double tx,double ty,Source src){
		explode(pos.x,pos.y,pos.xv,pos.yv,src,null);
	}
	public void explode(double x,double y,double xv,double yv,Source src,Agent a){
		game.entity.RPG e=((game.entity.RPG)getRPG().initPos(x,y,xv,yv,SourceTool.make(src,"引爆的")));
		e.is_warhead=true;
		if(!(this instanceof Warhead_Spark || this instanceof Warhead_WideRangeSpark))e.fuel=0;
		else e.fuel=e.fuel*3/4;
		if(a!=null)Entity.beginCalcImpulse(a);
		e.explode();
		if(a!=null)Entity.endCalcImpulse(a,-1);
	}
	public abstract int getBallCnt();
	public abstract Entity getBall();
	protected abstract game.entity.RPG getRPG();
	public Item clickAt(double x,double y,Agent a){
		Block b=World.cur.get(x,y);
		int px=f2i(x),py=f2i(y);
		if(b instanceof MineBlock){
			return ((MineBlock)b).setWarhead(this,a);
		}
		return super.clickAt(x,y,a);
	}
	protected boolean explodeDirected(){return true;}
};
class Warhead_Energy_HE extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_Energy_HE");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_Energy_HE().toEnt();}
	public int getBallCnt(){return 30;}
	public Entity getBall(){return new HE_EnergyBall();}
	@Override
	protected boolean explodeDirected(){return false;}
};
class Warhead_Dark_HE extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_Dark_HE");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_Dark_HE().toEnt();}
	public int getBallCnt(){return 30;}
	public Entity getBall(){return new HE_DarkBall();}
	@Override
	protected boolean explodeDirected(){return false;}
};


class Warhead_Energy extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_Energy");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_Energy().toEnt();}
	public int getBallCnt(){return 210;}
	public Entity getBall(){return new game.entity.EnergyBall();}
};
class Warhead_Dark extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_Dark");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_Dark().toEnt();}
	public int getBallCnt(){return 105;}
	public Entity getBall(){return new game.entity.DarkBall();}
};

class Warhead_Blood extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_Blood");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_Blood().toEnt();}
	public int getBallCnt(){return 30;}
	public Entity getBall(){return new BloodBall(1);}
};
class Warhead_Smoke extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_Smoke");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_Smoke().toEnt();}
	public int getBallCnt(){return 50;}
	public Entity getBall(){return new FallingBlock(0,0,new SmokeBlock());}
};

class Warhead_Spark extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_Spark");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_Spark().toEnt();}
	public int getBallCnt(){return 25;}
	public Entity getBall(){return new Spark(0,0,true).setHpScale(12);}
};

