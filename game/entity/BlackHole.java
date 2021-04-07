package game.entity;

import util.BmpRes;
import game.world.World;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class BlackHole extends Entity{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/BlackHole");
	public BmpRes getBmp(){return bmp;}
	@Override
	public double light(){
		return 2;
	}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	double radius0(){return min(1,sqrt(hp)*0.01);}
	double radius(){return min(1,sqrt(hp)*0.01);}
	public double width(){return min(1,radius());}
	public double height(){return min(1,radius());}
	public double gA(){return 0;}
	public boolean chkAgent(){return false;}
	public boolean chkEnt(){return false;}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return true;}
	public double mass(){return max(10,hp);}
	public BlackHole(){
		xv=yv=0;
		hp=1e2;
	}
	private void checkEnt(Entity e){
		if(e.hp<=0||e.isRemoved())return;
		double d=distL2(e),xd=x-e.x,yd=y-e.y;
		if(d>20*radius0())return;
		if(abs(e.x-x)<e.width()+radius0()&&abs(e.y-y)<e.height()+radius0()&&rnd()<V/(e.V+1e-8)){
			impulse(e,1);
			f+=e.mass()/(hp+e.mass());
			hp+=e.mass();
			if(e instanceof Agent)e.kill(this);
			else e.remove();
			return;
		}
		double xF=xd/(d*d),yF=yd/(d*d),M=hp*e.mass()*1e-4;
		e.impulse(xF,yF,M);
		impulse(-xF,-yF,M);
	}
	@Override
	public void update(){
		super.update();
		if(y<0||y>128)hp-=1;
		hp-=0.01;
		double R=20*radius0();
		for(Entity e:game.world.World.cur.getNearby(x,y,R,R,false,true,false).ents){
			if(e!=this)checkEnt(e);
		}
		for(Agent e:game.world.World.cur.getNearby(x,y,R,R,false,false,true).agents){
			checkEnt(e);
		}
		for(int i=0;i<3;++i){
			double c=(rnd()<0.2?2:8)*radius0();
			int px=f2i(x+rnd_gaussion()*c);
			int py=f2i(y+rnd_gaussion()*c);
			Block b=World.cur.get(px,py);
			if(b.fallable()){
				World.cur.setAir(px,py);
				Entity e=new FallingBlock(px,py,b);
				e.xv=0;
				e.yv=0;
				checkEnt(e);
				e.add();				
			}else{
				b.des(px,py,3,this);
			}
		}
	}
	public void onKill(){
		ShockWave.explode(x+.5,y+.5,xv,yv,72,0.1,5,this);
	}
}
