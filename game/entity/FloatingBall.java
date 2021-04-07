package game.entity;
import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.Iron;
import game.item.EnergyStone;
import game.world.World;
import game.block.Block;

public class FloatingBall extends Agent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/FloatingBall");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double maxHp(){return 20;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}

	@Override
	public double mass(){return 0.5;}
	
	Group group(){return Group.ZOMBIE;}
	
	public FloatingBall(double _x,double _y){super(_x,_y);}
	Agent target;
	boolean is_tmp=false;
	ZombieBase base=null;

	@Override
	public void update(){
		super.update();
		if(base!=null)base.ball_cnt+=1;

		climbable=true;
		if(f<0.06)f=0.06;
		if(xv*xv+yv*yv>0.04&&f<0.1)f=0.1;
		if(ydep<0)ydir=1;
		if(ydep>0)ydir=-1;
		if(xdep<0)xdir=1;
		if(xdep>0)xdir=-1;


		if(rnd()<0.0002)hp=0;
		addHp(0.01);
	}
	
	@Override
	public void ai(){
		if(target!=null&&target.isRemoved())target=null;
		if(ydep<0&&ydir<0)ydir=0;
		if(target!=null&&rnd()<0.3){
			xdir=x+xv*3<target.x?1:-1;
			ydir=y+yv*3<target.y?1:-1;
			if(target.getClass()==getClass()){
				Agent tmp=((FloatingBall)target).target;
				if(tmp!=null)target=tmp;
			}else if(hp<maxHp()*0.3){
				xdir*=-1;
				ydir*=-1;
			}
		}
		
		if(is_tmp&&target==null&&rnd()<0.01)target=base;
		
		if(!is_tmp&&rnd()<(xdir==0?0.05:0.01)){
			xdir=rndi(-1,1);
			ydir=rndi(-1,1);
			if(rnd()<0.1)ydir=-1;
		}
		
		if(!is_tmp&&target==null&&rnd()<0.05){
			if(target==null&&base!=null){
				target=base.getMainTarget();
				if(target==null)target=base;
			}
			if(target==null){
				game.world.NearbyInfo ni=World.cur.getNearby(x,y,4,4,false,false,true);
				java.util.Collections.shuffle(ni.agents);
				for(Agent a:ni.agents){
					if(a.getClass()==getClass()){
						target=((FloatingBall)a).target;
						if(target==null)target=a;
						break;
					}
					if(a.group()!=group()){
						target=a;
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean chkRemove(long t){
		return false;
	}

	@Override
	public double gA(){return 0;}

	@Override
	void touchBlock(int x,int y,Block b){
		if(rnd()<0.3)b.des(x,y,1,this);
		super.touchBlock(x,y,b);
	}

	void touchAgent(Agent ent){
		if(ent instanceof ZombieBase){
			if(target==ent){
				ent.addHp(hp);
				remove();				
			}
			return;
		}
		if(ent.group()!=group()){
			ent.onAttacked(0.4,this);
			target=ent;
		}
		super.touchAgent(ent);
	}

	void onKill(){
		if(base!=null){
			Spark.explode(x,y,xv,yv,4,0.1,0.2,this);
		}else{
			if(rnd()<0.05)new Iron().drop(x,y);
			if(rnd()<0.05)new EnergyStone().drop(x,y);
		}
		super.onKill();
	}
}
