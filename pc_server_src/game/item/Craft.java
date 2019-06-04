package game.item;

import static game.item.CraftInfo.*;
import game.block.*;
import game.item.*;
import game.entity.*;
import game.world.World;
import java.util.*;
import graphics.Canvas;
import util.SerializeUtil;

public class Craft implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	//描述传统的合成
	
	public SingleItem[] in,out;
	public CraftInfo cost;
	private Craft(Object[] src){
		int n=0;
		for(;!(src[n] instanceof CraftInfo);++n);
		in=new SingleItem[n];
		for(int i=0;i<n;++i){
			in[i]=SingleItem.cast(src[i]);
		}
		cost=(CraftInfo)src[n];
		out=new SingleItem[src.length-1-n];
		for(int i=n+1;i<src.length;++i)out[i-1-n]=SingleItem.cast(src[i]);
	}
	public class CraftItem extends SingleItem{
		public CraftItem(){super(out[0].get(),1);}
		public void draw(Canvas cv,SingleItem si,int flag){
			flag|=NO_AMOUNT_FLAG;
			if(!check(game.ui.UI.pl))flag|=ALPHA_FLAG;
			super.draw(cv,si,flag);
		}
		public Craft getCraft(){
			return Craft.this;
		}
	}
	public SingleItem toItem(){
		return new CraftItem();
	}
	public static int count(SingleItem[] si,Item it){
		int c=0;
		for(SingleItem j:si)if(!j.isEmpty()){
			if(it.cmpType(j.get())){
				c+=j.getAmount();
			}
		}
		return c;
	}
	public boolean check(Crafter p){
		if(p.getEnergy()<cost.energy)return false;
		if((p.getCraftType()&cost.type)!=cost.type)return false;
		SingleItem si[]=p.getItems().toArray();
		for(SingleItem i:in)if(count(si,i.get())<i.getAmount())return false;
		return true;
	}
	
	//以p为启动者，p提供物品，开始一次Craft
	public void start(Crafter p){//start this task on p
		if(!check(p)){
			return;
		}
		SingleItem si[]=p.getItems().toArray();
		if(p.startCraft(this)){
			p.loseEnergy(cost.energy);
			for(SingleItem i:in){
				int c=i.getAmount();
				for(SingleItem j:si)if(!j.isEmpty()){
					if(i.get().cmpType(j.get())){
						int a=Math.min(c,j.getAmount());
						c-=a;
						j.dec(a);
					}
				}
			}
		}
	}
	
	//中断Craft
	public void interrupt(ItemReceiver p){
		for(SingleItem it:in)p.gain((SingleItem)SerializeUtil.deepCopy(it));
	}
	
	//结束Craft
	public void finish(ItemReceiver p){
		for(SingleItem it:out)p.gain((SingleItem)SerializeUtil.deepCopy(it));
	}
	
	//从Object[][]创建Craft[]
	public static Craft[] create(Object[][] src){
		Craft[] cs=new Craft[src.length];
		for(int i=0;i<cs.length;++i)cs[i]=new Craft(src[i]);
		return cs;
	}
	
	//创造模式物品列表
	public static Item[] _normal_tool={
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
		
		new WoodenArmor(),
		new IronArmor(),
		new QuartzArmor(),
		new DiamondArmor(),
		
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
		
		new WoodenDoorBlock(),
		new IronDoorBlock(),
		
		new ConveyorBeltBlock(),
		
		new EnergyFurnaceBlock(),
		new CompresserBlock(),
		new PulverizerBlock(),
		new RepairerBlock(),
		new LauncherBlock(),
		new RespawnBlock(),
		new AutoCraftBlock(),
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
		
		
		new WaterBlock(),
		new LavaBlock(),
	},_throwable_item={
		new StoneBall(),
		new IronBall(),
		new Arrow(),
		new Bullet(),
		//new game.item.Boat(),
		new AgentMaker(GreenMonster.class),
		new AgentMaker(BloodMonster.class),
		new AgentMaker(CactusMonster.class),
		new AgentMaker(LavaMonster.class),
		new AgentMaker(EnergyMonster.class),
		new AgentMaker(DarkMonster.class),
		new AgentMaker(Zombie.class),
	},_energy_tool={
		EnergyCell.full(),
		HeatEnergyCell.full(),
		HeatEnergyCellGroup.full(),
		DarkEnergyCell.full(),
		BlueCrystalEnergyCell.full(),
		new TeleportationStick(),
		new BloodSword(),
		new BlockRepairer(),
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
	};
	
	//物品合成方案总表
	public static Object[][] 
	_craft_normal_tool={
		{
			new Stick(),
			new StoneBlock(),
			new CraftInfo(25,5,0),
			new StoneHammer(),
		},{
			new Stick(),
			new QuartzBlock(),
			new CraftInfo(50,5,0),
			new QuartzHammer(),
		},{
			new Stick(),
			new CrystalBlock(),
			new CraftInfo(50,5,0),
			new CrystalHammer(),
		},{
			new Stick(),
			new CactusBlock(),
			new CraftInfo(50,5,0),
			new CactusHammer(),
		},{
			new Stick(),
			new Iron().setAmount(3),
			new CraftInfo(200,10,_heat),
			new IronPickax(),
		},{
			new Stick(),
			new Iron().setAmount(3),
			new CraftInfo(200,10,_heat),
			new IronAx(),
		},{
			new Stick(),
			new Iron().setAmount(2),
			new CraftInfo(200,10,_heat),
			new IronShovel(),
		},{
			new Stick(),
			new Iron().setAmount(3),
			new CraftInfo(200,10,_heat),
			new IronSword(),
		},{
			new IronStick(),
			new IronBlock(),
			new CraftInfo(200,10,_heat),
			new IronHammer(),
		},{
			new IronStick(),
			new IronNail().setAmount(8),
			new CraftInfo(100,10,_heat),
			new Mace(),
		},{
			new Stick(),
			new StringItem(),
			new CraftInfo(50,5,_complex),
			new Bow(),
		},{
			new Bow(),
			new Stick().setAmount(2),
			new IronNail().setAmount(2),
			new CraftInfo(100,10,_complex),
			new CrossBow(),
		},{
			new CrossBow(),
			new Iron().setAmount(2),
			new FireBall().setAmount(2),
			new CraftInfo(100,5,_complex),
			new FireCrossBow(),
		},{
			new Iron(),
			new Quartz(),
			new CraftInfo(10,5,0),
			new Flint(),
		},{
			new Iron().setAmount(2),
			new Spring(),
			new IronNail(),
			new CraftInfo(100,5,_heat),
			new Scissors(),
		},{
			new Iron().setAmount(6),
			new CraftInfo(100,5,_heat),
			new Bucket(),
		},{
			new IronStick(),
			new Iron().setAmount(2),
			new CraftInfo(100,5,_heat),
			new Spanner()
		},/*{
			new WoodenBlock(),
			new CraftInfo(150,15,_cut),
			new game.item.Boat()
		},*/
		
		{
			new IronAx(),
			new Diamond().setAmount(3),
			new CraftInfo(100,10,_complex),
			new DiamondAx(),
		},{
			new IronShovel(),
			new Diamond().setAmount(2),
			new CraftInfo(100,10,_complex),
			new DiamondShovel(),
		},{
			new IronSword(),
			new Diamond().setAmount(3),
			new CraftInfo(100,10,_complex),
			new DiamondSword(),
		},{
			new IronHammer(),
			new Diamond().setAmount(8),
			new CraftInfo(100,10,_complex),
			new DiamondHammer(),
		},
		
		{
			new Stick().setAmount(11),
			new CraftInfo(100,10,_cut),
			new WoodenArmor(),
		},{
			new Iron().setAmount(11),
			new CraftInfo(100,10,_heat),
			new IronArmor(),
		},{
			new IronArmor(),
			new Quartz().setAmount(11),
			new CraftInfo(100,10,_diamond),
			new QuartzArmor(),
		},{
			new IronArmor(),
			new Diamond().setAmount(11),
			new CraftInfo(100,10,_diamond),
			new DiamondArmor(),
		},
		
		{
			new Stick().setAmount(7),
			new CraftInfo(100,10,_cut),
			new WoodenShield(),
		},{
			new Iron().setAmount(7),
			new CraftInfo(100,10,_heat),
			new IronShield(),
		},{
			new IronShield(),
			new Quartz().setAmount(7),
			new CraftInfo(100,10,_diamond),
			new QuartzShield(),
		},{
			new IronShield(),
			new Diamond().setAmount(7),
			new CraftInfo(100,10,_diamond),
			new DiamondShield(),
		}
	},_craft_energy_tool={
		{
			new Iron(),
			new EnergyStone(),
			new CraftInfo(50,5,_complex),
			EnergyCell.full(),
		},{
			new EnergyCell(),
			new FireBall(),
			new CraftInfo(50,5,_complex),
			new HeatEnergyCell(),
		},{
			new HeatEnergyCell().setAmount(8),
			new CraftInfo(150,10,_complex),
			new HeatEnergyCellGroup(),
		},{
			new EnergyCell(),
			new DarkSquare().setAmount(2),
			new CraftInfo(100,10,_complex),
			new DarkEnergyCell().setAmount(2),
		},{
			new EnergyCell(),
			new BlueCrystal().setAmount(4),
			new CraftInfo(100,20,_energy),
			new BlueCrystalEnergyCell(),
		},
		
		{
			new Bucket(),
			new Spring(),
			new IronStick(),
			new EnergyStone().setAmount(6),
			new CraftInfo(300,20,_complex),
			new EnergyMotor(),
		},
		
		{
			new EnergyMotor(),
			new Bucket(),
			new EnergyStone().setAmount(6),
			new CraftInfo(150,10,_energy),
			new EnergyBallLauncher(),
		},{
			new EnergyBallLauncher(),
			new Bucket().setAmount(3),
			new EnergyStone().setAmount(9),
			new CraftInfo(300,20,_energy),
			new EnergyBallLauncher_3(),
		},{
			new EnergyBallLauncher(),
			new FireBall().setAmount(6),
			new CraftInfo(100,10,_energy),
			new FireBallLauncher(),
		},{
			new EnergyBallLauncher_3(),
			new FireBall().setAmount(9),
			new CraftInfo(300,20,_energy),
			new FireBallLauncher_3(),
		},{
			new EnergyBallLauncher(),
			new DarkBall().setAmount(6),
			new CraftInfo(100,10,_energy),
			new DarkBallLauncher(),
		},{
			new EnergyBallLauncher_3(),
			new DarkBall().setAmount(9),
			new CraftInfo(300,20,_energy),
			new DarkBallLauncher_3(),
		},
		
		{
			new EnergyBallLauncher(),
			new EnergyMotor().setAmount(4),
			new EnergyStone().setAmount(32),
			new Iron().setAmount(8),
			new CraftInfo(300,20,_energy),
			new HighEnergyLauncher(),
		},{
			new HighEnergyLauncher(),
			new FireBall().setAmount(32),
			new CraftInfo(300,20,_energy),
			new HighEnergyFireLauncher(),
		},{
			new HighEnergyLauncher(),
			new DarkBall().setAmount(32),
			new CraftInfo(300,20,_energy),
			new HighEnergyDarkLauncher(),
		},
		
		{
			new EnergyBallLauncher(),
			new Iron().setAmount(4),
			new CraftInfo(100,20,_energy),
			new EnergyGun(),
		},{
			new EnergyGun(),
			new Bucket(),
			new CraftInfo(300,20,_energy),
			new EnergyShotgun(),
		},
		
		{
			new EnergyMotor(),
			new IronDrill(),
			new CraftInfo(100,5,_energy),
			new EnergyDrill(),
		},{
			new EnergyMotor(),
			new DiamondDrill(),
			new CraftInfo(100,5,_energy),
			new DiamondEnergyDrill(),
		},
		
		{
			new DarkBall().setAmount(8),
			new EnergyStone().setAmount(2),
			new IronStick(),
			new CraftInfo(100,20,_energy),
			new TeleportationStick(),
		},{
			new IronStick(),
			new EnergyStone().setAmount(2),
			new BloodEssence().setAmount(4),
			new CraftInfo(400,20,_energy),
			new BloodSword(),
		},{
			new Spanner(),
			new EnergyStone().setAmount(4),
			new CraftInfo(100,10,_energy),
			new BlockRepairer(),
		},
		
		{
			new IronArmor(),
			new EnergyMotor().setAmount(2),
			new CraftInfo(200,20,_energy),
			new JetPack(),
		},{
			new IronShield(),
			new EnergyMotor(),
			new CraftInfo(150,15,_energy),
			new EnergyShield(),
		}
	},_craft_circuit={
		{
			new IronStick(),
			new EnergyStone().setAmount(2),
			new CraftInfo(50,5,_energy),
			new Wire().setAmount(2),
		},{
			new Wire(),
			new IronNail(),
			new EnergyStone(),
			new CraftInfo(50,5,_energy),
			new OneWayWire(),
		},{
			new Iron(),
			new EnergyStone(),
			new CraftInfo(50,5,_energy),
			new Button(),
		},{
			new Iron(),
			new EnergyStone(),
			new CraftInfo(50,5,_energy),
			new Switch(),
		},{
			new Iron().setAmount(2),
			new EnergyStone().setAmount(2),
			new CraftInfo(50,5,_energy),
			new ContactInductor(),
		},{
			new EnergyStone().setAmount(16),
			new CraftInfo(50,5,_energy),
			new EnergyStoneBlock(),
		},{
			new EnergyStone().setAmount(2),
			new CraftInfo(50,5,_energy),
			new AndGate(),
		},{
			new GoldParticle().setAmount(2),
			new AndGate(),
			new CraftInfo(50,5,_energy),
			new EnergyBlocker(),
		},{
			new Iron(),
			new AndGate(),
			new CraftInfo(50,5,_energy),
			new OneWayControler(),
		}
	},_craft_functional_block={
		{
			new WoodenBlock(),
			new CraftInfo(100,5,0),
			new WoodenWorkBenchBlock(),
		},{
			new WoodenBlock(),
			new CraftInfo(100,5,_cut),
			new WoodenBoxBlock(),
		},{
			new Iron().setAmount(8),
			new CraftInfo(100,5,_heat),
			new IronBoxBlock(),
		},{
			new DarkSquare().setAmount(3),
			new CraftInfo(100,5,_diamond),
			new DarkBoxBlock().setAmount(2),
		},{
			new StoneBlock(),
			new CraftInfo(150,10,_cut),
			new FurnaceBlock(),
		},{
			new IronBlock(),
			new EnergyStone().setAmount(6),
			new CraftInfo(300,15,_complex),
			new IronWorkBenchBlock(),
		},{
			new StoneBlock(),
			new Iron().setAmount(4),
			new CraftInfo(100,10,_complex),
			new BlockMakerBlock(),
		},{
			new Stick().setAmount(6),
			new CraftInfo(50,10,_cut),
			new LadderBlock(),
		},{
			new IronStick().setAmount(6),
			new CraftInfo(50,10,_cut),
			new IronLadderBlock(),
		},{
			new Stick().setAmount(6),
			new CraftInfo(50,10,_cut),
			new WoodenScaffoldBlock(),
		},{
			new IronStick().setAmount(6),
			new CraftInfo(50,10,_cut),
			new IronScaffoldBlock(),
		},{
			new IronNail().setAmount(12),
			new CraftInfo(50,10,_heat),
			new IronNailBlock(),
		},{
			new IronStick().setAmount(2),
			new CraftInfo(25,5,_cut),
			new CircuitHolderBlock(),
		},{
			new WoodenBlock(),
			new Stick().setAmount(4),
			new CraftInfo(50,10,_cut),
			new WoodenDoorBlock(),
		},{
			new IronBlock(),
			new IronStick().setAmount(4),
			new CraftInfo(100,20,_cut),
			new IronDoorBlock(),
		},{
			new Iron().setAmount(3),
			new CraftInfo(50,10,_cut),
			new ConveyorBeltBlock(),
		},{
			new IronBlock(),
			new EnergyStone().setAmount(4),
			new FireBallLauncher(),
			new CraftInfo(100,10,_energy),
			new EnergyFurnaceBlock(),
		},{
			new IronBlock(),
			new EnergyMotor().setAmount(4),
			new Spring().setAmount(4),
			new CraftInfo(100,10,_energy),
			new CompresserBlock(),
		},{
			new IronBlock(),
			new EnergyMotor(),
			new Quartz().setAmount(4),
			new CraftInfo(100,10,_energy),
			new PulverizerBlock(),
		},{
			new IronBlock(),
			new BlockRepairer().setAmount(4),
			new CraftInfo(100,10,_energy),
			new RepairerBlock(),
		},{
			new IronBlock(),
			new EnergyStone().setAmount(8),
			new CraftInfo(300,20,_energy),
			new LauncherBlock(),
		},{
			new IronBlock(),
			new EnergyStone().setAmount(4),
			new BloodEssence().setAmount(8),
			new CraftInfo(300,30,_energy),
			new RespawnBlock(),
		},{
			new IronBlock(),
			new EnergyMotor().setAmount(2),
			new EnergyStone().setAmount(4),
			new CraftInfo(300,20,_energy),
			new AutoCraftBlock(),
		}
	},_craft_normal_block={
		{
			new Stone().setAmount(8),
			new CraftInfo(50,10,0),
			new StoneBlock(),
		},{
			new TrunkBlock(),
			new CraftInfo(50,5,0),
			new WoodenBlock(),
		},{
			new Iron().setAmount(16),
			new CraftInfo(400,20,_heat),
			new IronBlock(),
		},{
			new Gold().setAmount(16),
			new CraftInfo(400,20,_heat),
			new GoldBlock(),
		},
		
		{
			new Quartz().setAmount(16),
			new CraftInfo(50,10,_block),
			new QuartzBlock(),
		},{
			new Crystal().setAmount(16),
			new CraftInfo(50,10,_block),
			new CrystalBlock(),
		},{
			new Diamond().setAmount(16),
			new CraftInfo(50,10,_block),
			new DiamondBlock(),
		},{
			new Coal().setAmount(16),
			new CraftInfo(50,10,_block),
			new CoalBlock(),
		},{
			new IronOre().setAmount(16),
			new CraftInfo(50,10,_block),
			new IronOreBlock(3),
		},{
			new BlueCrystal().setAmount(16),
			new CraftInfo(50,10,_block),
			new BlueCrystalBlock(),
		},{
			new PlantEssence().setAmount(16),
			new CraftInfo(200,20,_block),
			new GreenBlock(),
		},{
			new BloodEssence().setAmount(16),
			new CraftInfo(200,20,_block),
			new BloodBlock(),
		},{
			new StonePowder().setAmount(8),
			new CraftInfo(50,10,_block),
			new StonePowderBlock()
		}
	},_craft_normal_item={
		{
			new WoodenBlock(),
			new CraftInfo(50,5,_cut),
			new Stick().setAmount(8),
		},{
			new Grass(),
			new CraftInfo(150,5,0),
			new StringItem(),
		},{
			new Iron(),
			new CraftInfo(50,10,_heat),
			new IronNail().setAmount(4),
		},{
			new Iron().setAmount(2),
			new CraftInfo(50,10,_heat),
			new IronStick(),
		},{
			new IronStick(),
			new CraftInfo(150,10,0),
			new Spring(),
		},{
			new Gold(),
			new CraftInfo(50,5,_cut),
			new GoldParticle().setAmount(8),
		},{
			new GoldParticle().setAmount(8),
			new CraftInfo(50,5,_heat),
			new Gold(),
		},{
			new Iron().setAmount(6),
			new CraftInfo(200,10,_heat),
			new IronDrill(),
		},{
			new IronDrill(),
			new Diamond().setAmount(6),
			new CraftInfo(200,10,_diamond),
			new DiamondDrill(),
		},{
			new Cube(),
			new CraftInfo(100,20,_diamond),
			new DarkSquare().setAmount(6),
		},
		
		{
			new Coal().setAmount(4),
			new CraftInfo(100,2000,_compress),
			new Diamond(),
		},{
			new EnergyStoneOrePowder(0).setAmount(4),
			new CraftInfo(100,200,_compress),
			new EnergyStone(),
			new StonePowder().setAmount(3),
		},{
			new EnergyStoneOrePowder(1).setAmount(2),
			new CraftInfo(100,100,_compress),
			new EnergyStone(),
			new StonePowder(),
		},
		
		{
			new Coal(),
			new CraftInfo(40,10,_pulverize),
			new CarbonPowder(),
		},{
			new Stone(),
			new CraftInfo(80,20,_pulverize),
			new StonePowder(),
		},{
			new EnergyStoneOre(0),
			new CraftInfo(80,20,_pulverize),
			new EnergyStoneOrePowder(0),
		},{
			new EnergyStoneOre(1),
			new CraftInfo(80,20,_pulverize),
			new EnergyStoneOrePowder(1),
		}
	},_craft_throwable_item={
		{
			new StoneBlock(),
			new CraftInfo(100,10,_cut),
			new StoneBall(),
		},{
			new IronBlock(),
			new CraftInfo(100,10,_heat),
			new IronBall(),
		},{
			new Stick(),
			new IronNail(),
			new CraftInfo(25,5,0),
			new Arrow(),
		},{
			new IronNail(),
			new CraftInfo(25,5,_heat),
			new Bullet(),
		},
	};
	
	public static Craft[]
	craft_normal_item=create(_craft_normal_item),
	craft_throwable_item=create(_craft_throwable_item),
	craft_normal_block=create(_craft_normal_block),
	craft_functional_block=create(_craft_functional_block),
	craft_normal_tool=create(_craft_normal_tool),
	craft_energy_tool=create(_craft_energy_tool),
	craft_circuit=create(_craft_circuit);
	public static Craft[][] craft_all=new Craft[][]{
		craft_normal_item,
		craft_throwable_item,
		craft_normal_block,
		craft_functional_block,
		craft_normal_tool,
		craft_energy_tool,
		craft_circuit,
	};
	
	//type条件，能进行的Craft列表
	public static Craft[] getAll(int type){
		ArrayList<Craft>cs=new ArrayList<>();
		for(Craft[] ws:craft_all)for(Craft w:ws)if((w.cost.type|type)==type)cs.add(w);
		Craft[] c=new Craft[cs.size()];
		return cs.toArray(c);
	}

	//type条件，ls中能进行的Craft
	public static Craft[] get(Craft[]ls,int type){
		ArrayList<Craft>cs=new ArrayList<>();
		for(Craft w:ls)if((w.cost.type|type)==type)cs.add(w);
		Craft[] c=new Craft[cs.size()];
		return cs.toArray(c);
	}
	
	//条件为type，能进行的Craft列表
	public static Craft[] getAllEq(int type){
		ArrayList<Craft>cs=new ArrayList<>();
		for(Craft[]ws:craft_all)for(Craft w:ws)if(w.cost.type==type)cs.add(w);
		Craft[] c=new Craft[cs.size()];
		return cs.toArray(c);
	}
}
