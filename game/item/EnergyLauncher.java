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
import game.world.World;

public abstract class EnergyLauncher extends EnergyTool implements ShootableTool{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 1000;}
	public void onDesBlock(Block b){}
	public void onAttack(Agent a){}
	private long next_time=0;
	private int last_cd=1;
	int getCd(){return 30;}
	boolean ready(){return next_time<=World.cur.time;}
	public void copyCd(EnergyLauncher el){
		last_cd=el.last_cd;
		next_time=el.next_time;
	}
	void resetCd(){
		resetCd(getCd());
	}
	void resetCd(int cd){
		last_cd=cd;
		next_time=World.cur.time+cd;
	}
	public Item click(double x,double y,Agent a){
		return clickAt(x,y,a);
	}
	public Item clickAt(double x,double y,Agent w){
		w.dir=(x-w.x>0?1:-1);
		if(!ready())return this;
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(x*w.dir>0&&shootCond())shoot(y/x,w);
		return this;
	}
	@Override
	public boolean onLongPress(Agent a,double x,double y){
		clickAt(x,y,a);
		return true;
	}
	
	public void drawInfo(graphics.Canvas cv){
		super.drawInfo(cv);
		if(!ready())game.ui.UI.drawProgressBar(cv,0xcccccccc,0,(next_time-World.cur.time)*1f/last_cd,-0.4f,-0.3f,0.4f,-0.23f);
	}
	
	public boolean shootCond(){
		return hasEnergy(energyCost());
	}
	protected void shoot(double s,Agent w){
		if(!Entity.is_test){
			resetCd();
			loseEnergy(energyCost());
			++damage;
		}
		Entity ball=getBall();
		Agent.temp(w,game.entity.SourceTool.use(w,getName())).throwEnt(ball,s,mv2());
	}
	public Entity test_shoot(Human w,double x,double y){
		if(!ready())return null;
		
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(x*w.dir<=0)return null;
		
		Entity.beginTest();
		Entity ball=getBall();
		if(ball!=null)w.throwEnt(ball,y/x,mv2());
		return Entity.endTest();
	}
	
	public boolean autoShoot(Human h,Agent a){
		return ShootTool.auto(h,a,this);
	}

	@Override
	public boolean autoUse(Human h,Agent a){
		if(!ready())return false;
		if(!findEnergyCell(h,energyCost()))return false;
		return autoShoot(h,a);
	}
	public abstract Entity getBall();
}

