package game.block;
import util.*;
import game.entity.*;
import game.world.World;
import graphics.*;
import static util.MathUtil.*;
import game.ui.*;

public class EnergyPowerContainerBlock extends EnergyPowerBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	
	static BmpRes bmp=new BmpRes("Block/EnergyPowerContainerBlock");
	public BmpRes getBmp(){return bmp;}
	
	int maxDamage(){return 50;}
	
	public int outEnergy(){return 5;}
	public int inEnergy(){return 5;}
	public int sEnergy(){return 5;}
	public int tp(){return 3;}
	public int[] connect;
	public int maxEnergy(){return 10000;}
	
	//按接口传递
	public void e(EnergyPowerBlock[] ebl){
		for(int i=0;i<ebl.length;i++){
			if(ebl[i]!=null){
				if(ebl[i].energy<ebl[i].maxEnergy()&&energy>0&&connect[i]==2){
					if(energy-outEnergy()>=0&&ebl[i].energy+outEnergy()<=ebl[i].maxEnergy()){
						energy-=outEnergy();
						ebl[i].energy+=outEnergy();
					}
					if(rnd()<0.004&&energy-sEnergy()>=0&&ebl[i].sEnergy()>0){energy-=rndi(1,sEnergy());}
				}else if(energy<maxEnergy()&&ebl[i].energy>0&&connect[i]==1){
					if(ebl[i].energy-ebl[i].outEnergy()>=0&&energy+ebl[i].outEnergy()<=maxEnergy()){
						ebl[i].energy-=ebl[i].outEnergy();
						energy+=ebl[i].outEnergy();
					}
					if(rnd()<0.004&&ebl[i].energy-ebl[i].sEnergy()>=0&&ebl[i].sEnergy()>0){ebl[i].energy-=rndi(1,ebl[i].sEnergy());}
				}
			}
		}
	}
	
	public void onPlace(int x,int y){
		connect=new int[]{2,1,1,2};
	}
	@Override
	public boolean isDeep(){return true;}
	
	
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		connect=null;
	}
	
	public boolean onCheck(int x,int y){
		super.onCheck(x,y);
		EnergyPowerBlock[] ebl=getNearEnergyBlock(x,y);
		e(ebl);
		return true;
	}
	
	public UI getUI(BlockAt ba){
		return new UI_Group(-7,0){{
				addChild(new UI_Button(0,0){
					protected BmpRes getBmp(){
						if(connect[0]==0){
							return new BmpRes("UI/CloessButton");
						}else if(connect[0]==1){
							return new BmpRes("UI/InButton");
						}else{return new BmpRes("UI/OutButton");}
					}
					protected void onPress(){
						if(connect[0]==0){
							connect[0]=1;
						}else if(connect[0]==1){
							connect[0]=2;
						}else{connect[0]=0;}
					}
				});
				addChild(new UI_Button(1,0){
					protected BmpRes getBmp(){
						if(connect[1]==0){
							return new BmpRes("UI/CloessButton");
						}else if(connect[1]==1){
								return new BmpRes("UI/InButton");
						}else{return new BmpRes("UI/OutButton");}
					}
					protected void onPress(){
						if(connect[1]==0){
							connect[1]=1;
						}else if(connect[1]==1){
							connect[1]=2;
						}else{connect[1]=0;}
					}
				});
				addChild(new UI_Button(2,0){
					protected BmpRes getBmp(){
						if(connect[2]==0){
							return new BmpRes("UI/CloessButton");
						}else if(connect[2]==1){
							return new BmpRes("UI/InButton");
						}else{return new BmpRes("UI/OutButton");}
					}
					protected void onPress(){
						if(connect[2]==0){
							connect[2]=1;
						}else if(connect[2]==1){
							connect[2]=2;
						}else{connect[2]=0;}
					}
				});
				addChild(new UI_Button(3,0){
					protected BmpRes getBmp(){
						if(connect[3]==0){
							return new BmpRes("UI/CloessButton");
						}else if(connect[3]==1){
							return new BmpRes("UI/InButton");
						}else{return new BmpRes("UI/OutButton");}
					}
					protected void onPress(){
						if(connect[3]==0){
							connect[3]=1;
						}else if(connect[3]==1){
							connect[3]=2;
						}else{connect[3]=0;}
					}
				});
				addChild(new UI_Button(1.5f,1.5f){
					protected BmpRes getBmp(){
						return new BmpRes("UI/maxe");
					}
					protected void onPress(){
						energy=maxEnergy();
					}
				});
			}
			protected void onDraw(Canvas cv){
				super.onDraw(cv);
				drawProgressBar(cv,0xff00ffff,0xff007f7f,(float)energy/maxEnergy(),0.25f,3.5f,3.75f,3.75f);
			}
		}.setBlock(ba);
	}
}
