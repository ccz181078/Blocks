package game.entity;


import util.BmpRes;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import game.world.World;
import game.block.Block;
import game.block.ReactionBlock;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class ZombieBase extends Agent implements DroppedItem.Picker{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/ZombieBase");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.9;}
	public double height(){return 0.9;}
	public double maxHp(){return 7500;}
	public double maxXp(){return 5000;}
	public double mass(){return 16;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	int ball_cnt=0,zombie_cnt=0;
	
	Group group(){return Group.ZOMBIE;}

	@Override
	public double light(){
		return 1;
	}

	@Override
	public boolean chkRemove(long t){
		return false;
	}	
	
	public ZombieBase(double _x,double _y){
		super(_x,_y);
		items=ItemList.emptyList(64);
		es=new Enemy[8];
	}

	protected ItemList items;
	protected Enemy es[];
	
	Agent getMainTarget(){
		return Enemy.weightedSelect(es);
	}
	@Override
	public void update0(){
		super.update0();
		ball_cnt=zombie_cnt=0;
	}
	boolean full(){
		SingleItem si[]=items.toArray();
		int empty_cnt=0;
		for(SingleItem s:si)if(s.isEmpty())++empty_cnt;
		return empty_cnt<=8;
	}
	
	void work(){

		if(rnd()<0.5&&ball_cnt<32){
			FloatingBall fb=new FloatingBall(x+rnd(-0.5,0.5),y+rnd(-0.5,0.5));
			fb.target=getMainTarget();
			fb.base=this;
			fb.add();
		}
		if(rnd()<0.1&&zombie_cnt<16){
			boolean free=false;
			if(rnd()<0.1&&full())free=true;
			
			Zombie z;
			if(free&&rnd()<0.7){
				z=new ZombieRobot(x+rnd(-0.2,0.2),y+0.1);
				z.items.insert(new BloodEssence().setAmount(2));
			}else z=new FloatingZombie(x+rnd(-0.2,0.2),y+0.1);
			SingleItem si[]=items.toArray();
			boolean empty=true;
			for(int i=0;i<5;++i){
				SingleItem w=si[rndi(0,si.length-1)];
				if(w.getAmount()==0)continue;
				empty=false;
				int k=rndi(1,(w.getAmount()+1)/2);
				if(rnd()<0.1)k=w.getAmount();
				for(int j=0;j<k;++j)z.items.insert(w.pop());
			}
			if(!free){
				z.protectBase();
				if(empty||rnd()<0.2)z.pickUpItem();
				z.base=this;
			}
			z.camp=camp;
			z.add();
		}
		if(rnd()<0.1){
			switch(rndi(0,5)){
				case 0:
				items.insert(new BloodSword().setAmount(1));
				break;
				case 1:
				items.insert(new QuartzArmor().setAmount(1));
				break;
				case 2:
				if(rnd()<0.6)items.insert(new game.block.StoneBlock().setAmount(1));
				else items.insert(new game.item.StoneBall().setAmount(1));
				break;
				case 3:
				items.insert(new DiamondSword().setAmount(1));
				break;
				case 4:
				items.insert(new DiamondHammer().setAmount(1));
				break;
				case 5:
				items.insert(new BloodEssence().setAmount(1));
				break;
			}
		}
	}

	@Override
	public void ai(){
		Enemy.check(es);
		for(int i=0;i<es.length;++i){
			if(es[i]!=null&&es[i].w.group()==group())es[i]=null;
		}
		if(rnd()<0.1&&ball_cnt<32){
			ArrayList<Entity> ents=World.cur.getNearby(x,y,6,6,false,true,false).ents;
			Collections.shuffle(ents);
			for(Entity e:ents){
				if(e.harmless()|| e instanceof Ball)continue;
				int t=0;
				boolean flag=false;
				for(;t<10;++t){
					double d=hypot(e.x+e.xv*t-x,e.y+e.yv*t-y);
					if(d<0.3*t){
						flag=true;
						break;
					}
				}
				if(flag){
					FloatingBall b=new FloatingBall(x,y);
					b.is_tmp=true;
					throwEntFromCenter(b,e.x+e.xv*t,e.y+e.yv*t,0.5);
				}
				if(rnd()<0.1)break;
			}
		}
		if(rnd()<0.3){
			int rx=f2i(x+rnd_gaussion()*3);
			int ry=f2i(y+rnd_gaussion()*3);
			Block b=World.cur.get(rx,ry);
			Class bc=b.getClass();
			if(bc==ReactionBlock.class){
				
			}else if((bc!=game.block.AirBlock.class||rnd()<0.1)&&
			   bc!=game.block.BedRockBlock.class&&
			   World.cur.destroyable(rx,ry)){
				World.cur.set(rx,ry,new ReactionBlock());
				World.cur.check4(rx,ry);
			}
		}
		double v=Math.max(0,maxHp()-hp)/maxHp();
		if(rnd()<v*0.3+0.05)work();
		if(rnd()<0.01){
			if(World.cur.getGroundY(f2i(x))<y-20){
				yv-=0.01;
			}
		}
		climbable=true;
		f=0.5;
	}

	@Override
	public double gA(){
		return 0;
	}

	@Override
	public void onAttacked(Attack a){
		if(a.src!=null){
			Enemy.ins(es,new Enemy(a.val,a.src,this));
		}
		super.onAttacked(a);
	}
	
	

	@Override
	void touchAgent(Agent ent){
		if(ent instanceof Zombie){
			//Zombie z=(Zombie)ent;
			return;
		}
		super.touchAgent(ent);
	}
	
	@Override
	void onKill(){
		DroppedItem.dropItems(items,x,y);
		super.onKill();
	}

	@Override
	public void pick(DroppedItem item){
		items.insert(item.item);
		if(item.item.isEmpty())item.remove();
	}
	

	@Override
	public void onKilled(Source src){
		String s=getName();
		World.showText(">>> "+getName()+"被"+src+"破坏了");
		super.onKilled(src);
	}

}
/*
		new StoneHammer(),
		new QuartzHammer(),
		new CrystalHammer(),
		new CactusHammer(),
		new IronPickax(),
		new IronAx(),
		new IronShovel(),
		new IronSword(),
		new IronHammer(),
		new DiamondAx(),
		new DiamondShovel(),
		new DiamondSword(),
		new DiamondHammer(),
		new Mace(),
		new Bow(),
		new CrossBow(),
		new FireCrossBow(),
		new Flint(),
		new Scissors(),
		new Bucket(),
		new Spanner(),
		new BlockFillTool(),
		
		new WoodenArmor(),
		new CactusArmor(),
		new IronArmor(),
		new QuartzArmor(),
		new DiamondArmor(),
		new BloodArmor(),
		new DarkArmor(),
		
		new WoodenShield(),
		new IronShield(),
		new QuartzShield(),
		new DiamondShield(),
	},_functional_block={
		new WoodenWorkBenchBlock(),
		new IronWorkBenchBlock(),
		new WoodenBoxBlock(),
		new IronBoxBlock(),
		new DarkBoxBlock(),
		new FurnaceBlock(),
		new BlockMakerBlock(),
		
		new LadderBlock(),
		new IronLadderBlock(),
		new WoodenScaffoldBlock(),
		new IronScaffoldBlock(),
		new IronNailBlock(),
		new BookshelfBlock(),
		
		new WoodenDoorBlock(),
		new IronDoorBlock(),
		
		new ConveyorBeltBlock(),
		new ItemAbsorberBlock(),
		new ItemExporterBlock(),
		
		new EnergyFurnaceBlock(),
		new CompresserBlock(),
		new PulverizerBlock(),
		new RepairerBlock(),
		new LauncherBlock(),
		new RespawnBlock(),
		new AutoCraftBlock(),
		
		new TeleportationBlock(),
	},_normal_block={
		
		new StoneBlock(),
		new QuartzBlock(),
		new CrystalBlock(),
		new DiamondBlock(),
		new WoodenBlock(),
		new IronBlock(),
		new GoldBlock(),
		new GreenBlock(),
		new BloodBlock(),
		new StonePowderBlock(),
		new GlassBlock(),
		new CoalBlock(),
		new IronOreBlock(3),
		new BlueCrystalBlock(),
		
		new TrunkBlock(),
		new LeafBlock(0),
		new LeafBlock(1),
		new LeafBlock(2),
		new LeafBlock(3),
		new CactusBlock(),
		new DarkVineBlock(0),
		new DarkVineBlock(1),
		new Grass(),
		new Algae(),
		new AquaticGrass(),
		new EnergyPlantBlock(),
		
		new CoalOreBlock(),
		new QuartzOreBlock(),
		new IronOreBlock(0),
		new IronOreBlock(1),
		new IronOreBlock(2),
		new EnergyStoneOreBlock(0),
		new EnergyStoneOreBlock(1),
		new EnergyStoneOreBlock(2),
		new GoldOreBlock(),
		new DiamondOreBlock(),
		new BloodStoneBlock(),
		new BlueCrystalOreBlock(),
		
		new DirtBlock(),
		new SandBlock(),
		new GravelBlock(),
		new DarkSandBlock(),
		
		
		WaterBlock.getInstance(),
		MutWaterBlock.getInstance(),
		LavaBlock.getInstance(),
	},_throwable_item={
		new StoneBall(),
		new IronBall(),
		new Arrow(),
		new Bullet(),
		new game.item.RPG_HE(),
		new game.item.RPG_HEAT(),
		new Bottle(),
		new BloodBottle(),
		new GreenBottle(),
		new WaterBottle(),
		//new game.item.Boat(),
		new AgentMaker(GreenMonster.class),
		new AgentMaker(BloodMonster.class),
		new AgentMaker(CactusMonster.class),
		new AgentMaker(LavaMonster.class),
		new AgentMaker(EnergyMonster.class),
		new AgentMaker(DarkMonster.class),
		new AgentMaker(DarkWorm.class),
		new AgentMaker(BigDarkWorm.class),
		new AgentMaker(StoneMonster.class),
		new AgentMaker(QuartzMonster.class),
		new AgentMaker(LavaBallMonster.class),
		new AgentMaker(AbsorberMonster.class),
		new AgentMaker(RotatorMonster.class),
		new AgentMaker(Villager.class),
		new AgentMaker(Zombie.class),
		new AgentMaker(ZombieRobot.class),
		new AgentMaker(FloatingZombie.class),
		new AgentMaker(ZombieBase.class),
		new AgentMaker(FloatingBall.class),
		new AgentMaker(FireBoss.class),
		new AgentMaker(SpaceBoss.class),
	},_energy_tool={
		EnergyCell.full(),
		HeatEnergyCell.full(),
		HeatEnergyCellGroup.full(),
		DarkEnergyCell.full(),
		BlueCrystalEnergyCell.full(),
		new TeleportationStick(),
		new BloodSword(),
		new BlockRepairer(),
		new ItemAbsorber(),
		new EnergyDrill(),
		new DiamondEnergyDrill(),
		new EnergyBallLauncher(),
		new EnergyBallLauncher_3(),
		new FireBallLauncher(),
		new FireBallLauncher_3(),
		new DarkBallLauncher(),
		new DarkBallLauncher_3(),
		new EnergyGun(),
		new EnergyShotgun(),
		new HighEnergyLauncher(),
		new HighEnergyFireLauncher(),
		new HighEnergyDarkLauncher(),
		new JetPack(),
		new EnergyShield(),
		new RPGLauncher(),
	},_circuit={
		new CircuitHolderBlock(),
		new EnergyStoneBlock(),
		
		new Button(),
		new Switch(),
		new ContactInductor(),
		new EnergyStone(),
		new Wire(),
		new OneWayWire(),
		new AndGate(),
		new EnergyBlocker(),
		new OneWayControler(),
		
		//new EnergyPowerContainerBlock(),
		//new EnergyPowerPipelineBlock(),
	},_normal_item={
		new Apple(),
		new BloodEssence(),
		new PlantEssence(),
		
		new Stone(),
		new Coal(),
		new IronOre(),
		new Quartz(),
		new Crystal(),
		new EnergyStoneOre(0),
		new EnergyStoneOre(1),
		new BlueCrystal(),
		new GoldParticle(),
		new Diamond(),
		
		new Gold(),
		new Iron(),
		new IronNail(),
		new IronStick(),
		new Spring(),
		new Stick(),
		new StringItem(),
		
		new CarbonPowder(),
		new StonePowder(),
		new EnergyStoneOrePowder(0),
		new EnergyStoneOrePowder(1),
		
		new EnergyStone(),
		new FireBall(),
		new DarkBall(),
		new DarkSquare(),
		new Cube(),
		
		new Book(),
		new Paper(),
	};


*/
