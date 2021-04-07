package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class FlakGun extends Pipeline_5{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Armor/FlakGun_",6);
	public BmpRes getBmp(){
		if(fire>=2)return bmp[0];
		return bmp[6-abs(cnt)];
	}
	
	SingleItem bullet=new SingleItem();
	int cd=0;//,xcd=0;
	SingleItem bullet_src=null;
	@Override
	public ShowableItemContainer getItems(){
		return ItemList.create(bullet);
	}
	
	public double fireReloadCost( Item ammo ){
		if( ammo instanceof game.item.Warhead ) return 0.25f;
		if( ammo instanceof game.block.Block ) return 0.4f;
		if( ammo instanceof RPGItem ) return 0.72f;
		return 0.085f;
	}
	
	public int getCd( double v , Item ammo ){
		int t = 1;
		if( v > 0.01 ) t = 2;
		if( ammo instanceof game.item.Bullet ) return 1 * t;
		if( ammo instanceof game.item.RPGItem ) return 4 * t;
		if( ammo instanceof game.item.Bottle ) return 4 * t;
		if( ammo instanceof game.item.StoneBall ) return 8 * t;
		if( ammo instanceof game.block.Block ) return 8 * t;
		return 2 * t;
	}
	
	private boolean setBulletSrc(SingleItem si){
		if(!si.isEmpty()){
			if(bullet_src!=si&&si==bullet)cd=max(cd,10);
			bullet_src=si;
			return true;
		}
		bullet_src = bullet;
		return false;
	}
	public int energyCost(){return 5;}
	@Override
	public void shoot(Human hu,double a,double b,Shilka ec){
		if(!ec.hasEnergy(energyCost()))return;
		if(bullet_src==null)return;
		//System.out.println(a+","+cd+",");
		if(cd>0||bullet_src.isEmpty())return;
		//System.out.println("1");
		double reload_cost=fireReloadCost( bullet_src.get() );
		if(reload<reload_cost)return;
		//System.out.println("2");
		reload -= reload_cost;
		nextGun();
		
		Item ammo = bullet_src.popItem();
		cd=getCd(hu.xv*hu.xv+hu.yv*hu.yv,ammo);
		double x=hu.x+1.6*cos(a)-0.15*(cnt-3)*sin(a),y=hu.y+0.15*(cnt-3)*cos(a)+1.6*sin(a)+0.23 , t = 0;
		if( ammo instanceof Warhead ) t = 0.4;
		
		/*if( !( ammo instanceof PathBullet ) )
		{
			Spark s=new Spark(0,0);
			s.initPos(x+0.25*cos(a),y+0.25*sin(a),cos(a)*0.5+hu.xv,sin(a)*0.5+hu.yv,hu);
			s.hp*=0.3;
			s.add();
		}*/
		
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x+t*cos(a),y+t*sin(a),b,mv2());
		ec.loseEnergy(energyCost());
	}
	@Override
	public void test_shoot(Human hu,double a,double b,Shilka ec,Item ammo){
		if(cd>0)return;
		double x=hu.x+1.6*cos(a)-0.15*(next[cnt]-3)*sin(a),y=hu.y+0.15*(cnt-3)*cos(a)+1.6*sin(a)+0.23 , t = 0;
		if( ammo instanceof Warhead ) t = 0.4;
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x+t*cos(a),y+t*sin(a),b,mv2());
	}
		
	float reload=0,q=1;
	
	@Override
	public void onUpdate(Human w,Shilka ec){
		if(cd>0)--cd;
		if(!setBulletSrc(w.items.getSelected())){
			setBulletSrc(bullet);
		}
		/*double t = 1.1;
		if( w.items.getSelected().get() instanceof RPGItem ) t = 0.03;
		q -= t;
		if( q <= 0 && w instanceof game.entity.Player){
			SingleItem si=w.items.getSelected().pop();
			bullet.insert(si);
			w.items.getSelected().insert(si);
			q = 1;
		}*/
		fire++;
		reload+=0.011f*(1+((ec.walk!=0)?7:5)*reload/ec.maxReload());
		if(reload>ec.maxReload()-(float)abs(w.xv)*2.5f)reload=ec.maxReload()-(float)abs(w.xv)*2.5f;
		if(reload<0)reload=0;
		ec.reload=reload;
	}
}

