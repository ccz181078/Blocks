package game.block;

import util.BmpRes;
import game.item.*;
import game.world.World;
import game.entity.Entity;
import graphics.Canvas;
import game.entity.DroppedItem;
import game.ui.*;
import static java.lang.Math.*;

public class LauncherBlock extends IronBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/LauncherBlock");
	public BmpRes getBmp(){return bmp;}
	private SpecialItem<EnergyBallLauncher> launcher=null;
	private int dir=1;
	private float slope=0;
	private boolean on=false;
	public UI getUI(BlockAt ba){
		return new UI_Group(-7,0){
			{
				addChild(new UI(){
					protected void onDraw(Canvas cv){
						float px=2+dir*1.5f,py=2-dir*slope*1.5f;
						cv.drawRect(px-0.4f,py-0.4f,px+0.4f,py+0.4f,0xff00aaaa);
					}
					protected boolean onPress(float tx,float ty){
						tx-=2;ty=2-ty;
						if(abs(tx)<=2&&abs(ty)<=2){
							dir=tx>0?1:-1;
							if(tx!=0)slope=max(-1,min(1,ty/tx));
							return true;
						}
						return false;
					}
				});
				addChild(new UI_ItemList(1.5f,1.5f,1,1,launcher,pl.il));
			}
		}.setBlock(ba);
	}
	public void onPlace(int x,int y){
		launcher=new SpecialItem<EnergyBallLauncher>(EnergyBallLauncher.class);
	}
	public boolean onCheck(final int x,final int y){
		if(super.onCheck(x,y))return true;
		boolean on_e=true;
		if(World._.get(x-1,y).energyValX()>0);
		else if(World._.get(x+1,y).energyValX()>0);
		else if(World._.get(x,y+1).energyValY()>0);
		else if(World._.get(x,y-1).energyValY()>0);
		else on_e=false;
		if(on_e&&!on&&!launcher.isEmpty()){
			EnergyBallLauncher ebl=launcher.get();
			if(ebl.shootCond()){
				ebl.shoot(slope,new Launcher(){
					public void launch(Entity e,double slope,double v){
						e.x=x+0.5+(0.51+e.width())*dir;
						e.y=y+0.5;
						e.xv=v*dir;
						e.yv=v*dir*slope;
						e.add();
					}
				});
				if(ebl.isBroken()){
					ebl.onBroken(x+dir,y+0.5);
					launcher.clear();
				}
			}
		}
		on=on_e;
		return false;
	}
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(launcher,x,y);
		new LauncherBlock().asItem().drop(x,y);
	}

	public void draw(Canvas cv){
		super.draw(cv);
		EnergyBallLauncher ebl=launcher.get();
		if(ebl!=null){
			cv.save();
			cv.scale(dir,1);
			cv.rotate((float)(Math.atan(slope*dir)*180/Math.PI),0.4f,0);
			ebl.getBmp().draw(cv,0.2f,0,0.2f,0.2f);
			cv.restore();
		}
	}
};
