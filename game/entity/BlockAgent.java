package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;
import game.item.Shield;

public class BlockAgent extends SimpleAgent{
	public Block block;
	public BlockAgent(Block b){
		super(0,0);
		this.block=b;
		hp=b.maxHp();
		removed=true;
	}
	//public SpecialItem<Shield> shield=new SpecialItem<>();
	public boolean hasBlood(){return false;}
	
	@Override
	public double maxHp(){return block==null?1:block.maxHp();}
	public double hardness(){return block.hardness();}
	@Override
	Group group(){return Group.STONE;}
	
	@Override
	public String getName(){
		return block.getName();
	}
	
	public double width(){
		return 0.5;
	}
	public double height(){
		return 0.5;
	}
	public double mass(){
		return block.mass();
	}
	
	@Override
	public void update(){
		super.update();
	}
	void touchAgent(Agent ent){
		boolean flag=true;
		if(ent instanceof BlockAgent){
			BlockAgent a=(BlockAgent)ent;
			if(a.block.getClass()==block.getClass())flag=false;
		}
		if(flag){
			ent.onAttacked(0.01*maxHp(),this);
			target=ent;
		}
		super.touchAgent(ent);
	}
	
	@Override
	public boolean drawRev(){return false;}
	@Override
	public boolean chkBlock(){return true;}
	public boolean chkEnt(){return true;}
	@Override
	public boolean chkRigidBody(){return true;}
	@Override
	public BmpRes getBmp(){return block.getBmp();}
	@Override
	void onKill(){
		block.onDestroy(x,y);
		super.onKill();
	}
	
	@Override
	public double onImpact(double v){return v*1e-3;}
}
