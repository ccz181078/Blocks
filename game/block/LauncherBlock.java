package game.block;

import util.BmpRes;
import game.item.*;
import game.world.World;
import game.entity.*;
import graphics.Canvas;
import game.entity.DroppedItem;
import game.ui.*;
import static java.lang.Math.*;

public class LauncherBlock extends IronBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/LauncherBlock");
	public BmpRes getBmp(){return bmp;}
	private SpecialItem<EnergyLauncher> launcher=null;
	private int dir=1;
	private float slope=0;
	private boolean on=false;

	public boolean isDeep(){return true;}
	
	public UI getUI(BlockAt ba){
		return new UI_Group(-7,0){{
				addChild(new UI(){
					@Override
					protected void onDraw(Canvas cv){
						float xd=dir,yd=dir*slope,d=max(1,abs(yd));
						xd/=d;yd/=d;
						float px=2+xd*1.5f,py=2-yd*1.5f;
						cv.drawRect(px-0.4f,py-0.4f,px+0.4f,py+0.4f,0xff00aaaa);
					}
					@Override
					protected boolean onTouch(float tx,float ty,int tp){
						tx-=2;ty=2-ty;
						if(abs(tx)<=2&&abs(ty)<=2){
							dir=tx>0?1:-1;
							if(tx!=0)slope=max(-1e3f,min(1e3f,ty/tx));
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
		launcher=new SpecialItem<EnergyLauncher>(EnergyLauncher.class);
	}
	public boolean onCheck(final int x,final int y){
		if(super.onCheck(x,y))return true;
		boolean on_e=true;
		if(World.cur.get(x-1,y).energyValR()>0);
		else if(World.cur.get(x+1,y).energyValL()>0);
		else if(World.cur.get(x,y-1).energyValU()>0);
		else if(World.cur.get(x,y+1).energyValD()>0);
		else on_e=false;
		if(on_e&&!on&&!launcher.isEmpty()){
			EnergyLauncher ebl=launcher.get();
			if(ebl.shootCond()){
				ebl.clickAt(x+0.5+dir*1.5,y+0.5+dir*slope,Agent.temp(x+0.5,y+0.5,0.5,0.5,dir,SourceTool.block(x,y,this)));
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
		new LauncherBlock().drop(x,y);
	}

	public void draw(Canvas cv){
		super.draw(cv);
		EnergyLauncher ebl=launcher.get();
		if(ebl!=null){
			cv.save();
			cv.scale(dir,1);
			cv.rotate((float)(Math.atan(slope*dir)*180/Math.PI),0.4f,0);
			ebl.getBmp().draw(cv,0.2f,0,0.2f,0.2f);
			cv.restore();
		}
	}
};
