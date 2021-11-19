package game.entity;
import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.block.Block;
public class Spark extends Entity{
	private static final long serialVersionUID=1844677L;

	static BmpRes bmp[]=BmpRes.load("Entity/Spark_",8);
	
	public boolean isPureEnergy(){return true;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	
	double _fc(){return 1e-4;}

	@Override
	public double buoyancyForce(){return 2;}
	@Override
	public double fluidResistance(){return 0.2;}
	
	@Override
	public double width(){return 0.4;}

	@Override
	public double height(){return 0.4;}	
	
	public boolean adhesive=false;

	@Override
	public BmpRes getBmp(){
		return bmp[(int)(World.cur.time&7)];
	}
	
	@Override
	public double touchVal(){return 0;}
	
	@Override
	public double RPG_ExplodeProb(){return adhesive?0.02:0;}
	
	@Override
	double friction(){return 8;}
	
	@Override
	public double mass(){return 0.003;}
	
	int t;

	public Spark(double _x,double _y){
		x=_x;y=_y;
		xv=0;
		yv=0;
		hp=16;
		t=rndi(0,10000);
		shadowed=true;
	}
	public Spark(double _x,double _y,boolean adhesive){
		this(_x,_y);
		this.adhesive=adhesive;
		t=rndi(0,10000);
		shadowed=true;
	}

	public static void gen(double x,double y){
		Spark a=new Spark(x,y);
		a.add();
	}
	@Override
	public void update0(){
		super.update0();
		shadowed=false;
		touched=false;
	}
	@Override
	public void move(){
		boolean s=shadowed;
		super.move();
		shadowed=s;
	}
	@Override
	void touchEnt(Entity ent,boolean chk_ent){
		if(ent instanceof ShockWave)return;
		if(ent instanceof Spark){
			if(distL2(ent)<0.4)if(((Spark)ent).t>t){
				shadowed=true;
				ent.hp+=hp/2;
				hp-=hp/2;
				this.chk_ent=false;
			}
			return;
		}
		if(chk_ent)touchEnt(ent);
	}

	@Override
	public void touchBlock(int px,int py,Block b){
		b.onFireUp(px,py);
		b.onBurn(px,py);
		//if(!b.isCoverable()&&b.fuelVal()==0)hp-=5;
	}

	public boolean chkEnt(){return chk_ent;}
	boolean chk_ent=false;
	private transient boolean touched=false;
	
	@Override
	public void touchAgent(Agent a){
		a.onAttackedByFire(2,this);
		if(adhesive){
			exchangeVel(a,0.5);
			touched=true;
		}
		hp-=1;
	}

	@Override
	public double light(){
		double v=min(1.,max(0.,hp/80));
		return Math.sqrt(v);
	}

	@Override
	public boolean chkRigidBody(){return true;}

	@Override
	public void update(){
		super.update();
		if(rnd()<0.1)chk_ent=true;
		hp-=1;
	}

	@Override
	public double gA(){
		return touched?0.001:0.01;
	}
	@Override
	public AttackFilter getAttackFilter(){return all_filter;}//攻击过滤器
	@Override
	protected boolean useRandomWalk(){return false;}
	public static void explode(double x0,double y0,double xv0,double yv0,int num,double v,double hp_scale,Source src,boolean adhesive,double r0){
		explode(x0,y0,xv0,yv0,num,v,hp_scale,src,adhesive,r0,adhesive);
	}
	public static void explode(double x0,double y0,double xv0,double yv0,int num,double v,double hp_scale,Source src,boolean adhesive,double r0,boolean exp_hp){
		if(num>=5)src=SourceTool.explode(src);
		else{
			if(src==null)debug.Log.printStackTrace();
			src=SourceTool.gen(src);
		}
		for(int i=0;i<num;++i){
			double x1=rnd_gaussion()*v,y1=rnd_gaussion()*v;
			Spark s=new Spark(0,0,adhesive);
			s.initPos(x0+rnd_gaussion()*r0,y0+rnd_gaussion()*r0,xv0+x1,yv0+y1,src);
			s.setHpScale(hp_scale);
			if(exp_hp)s.setHpScale(rnd_exp(1));
			s.add();
		}
	}
	public static void explode(double x0,double y0,double xv0,double yv0,int num,double v,double hp_scale,Source src){
		explode(x0,y0,xv0,yv0,num,v,hp_scale,src,false,0.2);
	}
	public static void explode_adhesive(double x0,double y0,double xv0,double yv0,int num,double v,double hp_scale,Source src){
		explode(x0,y0,xv0,yv0,num,v,hp_scale,src,true,0.2);
	}

}
