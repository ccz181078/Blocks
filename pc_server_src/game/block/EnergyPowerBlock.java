package game.block;
import util.*;
import game.world.*;
import game.entity.*;
import graphics.*;
import game.item.*;
import static util.MathUtil.*;

public abstract class EnergyPowerBlock extends Block{
	private static final long serialVersionUID=1844677L;
	
	public int energy;//能量
	public int outEnergy(){return 0;}//输出能量
	public int inEnergy(){return 0;}//输入能量
	public int sEnergy(){return 0;}//损耗能量最大值
	public int tp(){return 0;}//0关闭 1输入 2输出 3其他
	
	public int maxEnergy(){return 0;}
	
	//在方块上当显示能量值进度条，不需要就注释掉
	public void draw(Canvas cv){
		super.draw(cv);
		game.ui.UI.drawProgressBar(cv,0xff00ffff,0xff007f7f,(float)energy/maxEnergy(),-0.4f,0.3f,0.4f,0.4f);
	}
	
	//点击方块输出能量值，调试用，不需要就注释掉
	public boolean onClick(int x,int y,Agent agent){
		super.onClick(x,y,agent);
		World.showText("能量"+new Integer(energy).toString());
		return true;
	}
	
	public void onPress(int x,int y,Item item){
		des(x,y,1);
		item.onDesBlock(this);
	}
	
	//检测方块
	public EnergyPowerBlock[] getNearEnergyBlock(int x,int y){
		Block[] nbl=new Block[]{
			World.cur.get(x-1,y),
			World.cur.get(x,y+1),
			World.cur.get(x+1,y),
			World.cur.get(x,y-1)
		};
		EnergyPowerBlock[] ebl=new EnergyPowerBlock[nbl.length];
		for(int i=0;i<nbl.length;i++){
			if(EnergyPowerBlock.class.isAssignableFrom(nbl[i].getClass())){
				ebl[i]=(EnergyPowerBlock)nbl[i];
			}else{
				ebl[i]=null;
			}
		}
		return ebl;
	}
	
	//自由运输
	public void a(EnergyPowerBlock[] ebl){
		for(int i=0;i<ebl.length;i++){
			if(ebl[i]!=null){
				if(ebl[i].energy<energy&&energy>0&&tp()==3){
					energy-=outEnergy();
					ebl[i].energy+=outEnergy();
				}
				if(rnd()<0.0002&&energy-sEnergy()>=0&&sEnergy()>0){energy-=rndi(1,sEnergy());}
			}
		}
	}
	
	//强制供能
	public void b(EnergyPowerBlock[] ebl){
		for(int i=0;i<ebl.length;i++){
			if(ebl[i]!=null){
				if(ebl[i].energy<ebl[i].maxEnergy()&&energy>0&&(tp()==2)){
					if(energy-outEnergy()>=0&&ebl[i].energy+outEnergy()<=ebl[i].maxEnergy()){
						energy-=outEnergy();
						ebl[i].energy+=outEnergy();
					}
					if(rnd()<0.004&&energy-sEnergy()>=0&&sEnergy()>0){energy-=rndi(1,sEnergy());}
				}
			}
		}
	}
	
	//强制接收
	public void d(EnergyPowerBlock[] ebl){
		for(int i=0;i<ebl.length;i++){
			if(ebl[i]!=null){
				if(energy<maxEnergy()&&ebl[i].energy>0&&tp()==1){
					if(ebl[i].energy-ebl[i].outEnergy()>=0&&energy+ebl[i].outEnergy()<=maxEnergy()){
						ebl[i].energy-=ebl[i].outEnergy();
						energy+=ebl[i].outEnergy();
					}
					if(rnd()<0.004&&ebl[i].energy-sEnergy()>=0&&sEnergy()>0){ebl[i].energy-=rndi(1,ebl[i].sEnergy());}
				}
			}
		}
	}
}
