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
	public static void init(){
		for(Item items[]:new Item[][]{
			_normal_item,
			_throwable_item,
			_agent,
			_normal_tool,
			_energy_tool,
			_circuit,
			_normal_block,
			_functional_block,
		}){
			for(Item it:items){
				String default_name=it.getClass().getCanonicalName()+".name";
				String name=it.getName();
				if(name.equals(default_name)||name.equals("")){
					System.err.println("[Warning] "+default_name+" is empty, please modify assets/doc.txt");
				}
				String default_doc=it.getClass().getCanonicalName()+".doc";
				String doc=it.getDoc();
				if(doc.equals(default_doc)||doc.equals("")){
					System.err.println("[Warning] "+default_doc+" is empty, please modify assets/doc.txt");
				}
			}
		}
	}
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
		for(SingleItem si:out){
			if(si.isEmpty()){
				//System.err.println("[Warning] empty craft output");
			}else{
				Item it=si.get();
				String default_name=it.getClass().getCanonicalName()+".name";
				if(it.getName().equals(default_name)||it.getName().equals("")){
					//System.err.println("[Warning] "+default_name+" is empty, please modify assets/doc.txt");
				}
				if(it instanceof AgentMaker)continue;
				String default_doc=it.getClass().getCanonicalName()+".doc";
				if(it.getDoc().equals(default_doc)||it.getDoc().equals("")){
					//System.err.println("[Warning] "+default_doc+" is empty, please modify assets/doc.txt");
				}
			}
		}
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
				if(j.get() instanceof Tool){
					if(((Tool)j.get()).damage>0)continue;
				}
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
	
	
	private static <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	
	//创造模式物品列表
	public static Item[] _physics_tool={
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
	},_energy_sword={
		new BloodSword(),
		new BlueCrystalSword(),
		new EnergyStoneSword(),
		new FireSword(),
		new DarkSword(),
	},_bow={
		new Slingshot(),
		new Bow(),
		new CrossBow(),
		new EnergyCrossBow(),
		new FireCrossBow(),
		new DarkCrossBow(),
		new BloodCrossBow(),
	},_others_tool={
		new Flint(),
		new Scissors(),
		new Bucket(),
		new Spanner(),
		new BlockFillTool(),
		//new HookRope(),
		
		new EnergyPickax(),
		new EnergySword(),
		
		new IronPatch(),
		new EnergyPatch(),
		new ItemSlot(),
		new ItemSlotLock(),
		new LevelEnd(),
	},_normal_armor={
		new WoodenArmor(),
		new CactusArmor(),
		new GlassArmor(),
		new IronArmor(),
		new IronNailArmor(),
		new QuartzArmor(),
		new DiamondArmor(),
		new GreenArmor(),
		new BloodArmor(),
		new BloodNailArmor(),
		new DarkArmor(),
		new ExplosiveArmor(),
		new WaterArmor(),
		new FireArmor(),
		new EnergyStoneArmor(),
		//new FlankArmor(),
	},_normal_shield={
		new WoodenShield(),
		new CactusShield(),
		new IronShield(),
		new IronNailShield(),
		new BloodNailShield(),
		new QuartzShield(),
		new DiamondShield(),
		new EnergyStoneShield(),
		
		new BigWoodenShield(),
		new BigIronShield(),
		new BigIronNailShield(),
		new BigHDShield(),
		new BoxShield(),
	},_normal_shoes={
		new Boat(),
		new IronBoat(),
		new SkateBoard(),
		new SpringShoes(),
		new SpringNailShoes(),
		new EscapeDevice(),
		new GlueShoes(),
		new ClimberShoes(),
	};
	public static Item[] 
	_normal_tool=concatAll(
		_physics_tool,
		_energy_sword,
		_bow,
		_others_tool,
		_normal_armor,
		_normal_shield,
		_normal_shoes
	),_functional_block={
		new WoodenWorkBenchBlock(),
		new IronWorkBenchBlock(),
		new WoodenBoxBlock(),
		new IronBoxBlock(),
		new DarkBoxBlock(),
		new FurnaceBlock(),
		new BlockMakerBlock(),
		
		new TorchBlock(),
		new EnergyTorchBlock(),
		
		new LadderBlock(),
		new IronLadderBlock(),
		new WoodenScaffoldBlock(),
		new IronScaffoldBlock(),
		new SoftIronScaffoldBlock(),
		new IronMeshBlock(),
		new SpringBlock(),
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
		new LightCollectorBlock(),
		new AgentEnergyCollectorBlock(),
		
		new ExplosiveBlock(),
		new Mine(),
		new HDEnergyStoneBlock(),
		
		new TeleportationBlock(),
		
	},_normal_block={

		new StoneBlock(),
		new QuartzBlock(),
		new CrystalBlock(),
		new DiamondBlock(),
		new WoodenBlock(),
		new IronBlock(),
		new GoldBlock(),
		new CoalBlock(),
		new IronOreBlock(3),
		new BlueCrystalBlock(),
		
		new ImprovedStoneBlock(),
		new ImprovedQuartzBlock(),
		
		new EnergyStoneBlock(),
		new HeatBlock(),
		new DarkBlock(),
		new GreenBlock(),
		new BloodBlock(),
		
		new StonePowderBlock(),
		new GlassBlock(),
		
		new BedRockBlock(),
		new ReactionBlock(),
		new SmokeBlock(),
		new GlueBlock(),
		
		new TrunkBlock(),
		new LeafBlock(0),
		new LeafBlock(1),
		new LeafBlock(2),
		new LeafBlock(3),
		new CactusBlock(),
		new DarkVineBlock(0),
		new DarkVineBlock(1),
		new BallPlantBlock(),
		new RedFlowerBlock(),
		new PurpleFlowerBlock(),
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
		//new EnergySandBlock(),
		new DarkSandBlock(),
		new SemilavaBlock(),
		new ZombieSandBlock(),
		
		
		WaterBlock.getInstance(),
		MutWaterBlock.getInstance(),
		LavaBlock.getInstance(),
	},_balls={
		new StoneBall(),
		new IronBall(),
		new ExplosiveBall(),
		new ExplosiveIronNailBall(),
		new EnergyStoneRing(),
		new HeatRing(),
		new EnergyIronBall(),
		new HeatBall(),
		new DarkIronBall(),
		new BloodIronBall(),
		new IronNailBall(),
		new SmallIronNailBall(),
		new BigIronBall(),
		new BlocksBall(),
		new BoundaryBall(),
		new IronBall_HD(),
		new game.item.BlackHole(),
		new game.item.HandGrenade(),
	},_explosive_block={
		new ExplosiveBlock(),
		new HDEnergyStoneBlock(),
	},_essence={
		new BloodEssence(),
		new PlantEssence(),
		new EnergyStone(),
		new FireBall(),
		new DarkBall(),
		new Cube(),
	},_arrow={
		new Arrow(),
		new IronArrow(),
	},_bullet={
		new WoodenBullet(),
		new StoneBullet(),
		new QuartzBullet(),
		new Bullet(),
		new DiamondBullet(),
		new IronNail(),
		new SpecialBullet(1),
		new SpecialBullet(2),
		new SpecialBullet(3),
		new SpecialBullet(4),
		new SpecialBullet(5),
		new Bullet_HD(),
		new DefendBullet(),
		new FlakBullet(),
		new FireBullet(),
		new APDSBullet(),
		new BigGuidedBullet(),
		new GuidedBullet(),
		new DarkGuidedBullet(),
		new GreenGuidedBullet(),
		new BloodGuidedBullet(),
		new PathBullet(),
	},_rpg_normal={
		new game.item.RPG_Empty(),
		new game.item.RPG_Energy(),
		new game.item.RPG_Energy_HE(),
		new game.item.RPG_HE(),
		new game.item.RPG_HEAT(),
		new game.item.RPG_Spark(),
		new game.item.RPG_WideRangeSpark(),
		new game.item.RPG_Dark(),
		new game.item.RPG_Dark_HE(),
		new game.item.RPG_Blood(),
		new game.item.RPG_Smoke(),
		
		new game.item.RPG_Bullet(),
	},_rpg_guided={
		new game.item.RPG_Guided(),
		new game.item.RPG_EnergyBall(),
		new game.item.RPG_EnergyBarrier(),
		new game.item.RPG_FireBarrier(),
		new game.item.RPG_HD(),
		new game.item.RPG_Block(),
		
		new game.item.RPG_Small_HE(),
		new game.item.RPG_Item(),
	},_tank_shell={
		new game.item.Tank_HEATFS(),
		new game.item.Tank_APFSDS(),
	},_warhead={
		new Warhead_Energy(),
		new Warhead_Energy_HE(),
		new Warhead_HE(),
		new Warhead_HEAT(),
		new Warhead_Spark(),
		new Warhead_WideRangeSpark(),
		new Warhead_Dark(),
		new Warhead_Dark_HE(),
		new Warhead_Blood(),
		new Warhead_Smoke(),
		new EnergyBarrier(),
		new FireBarrier(),
		new GreenBarrier(),
	},_bottle={
		new Bottle(),
		
		new EnergyStoneBottle(),
		new HeatBottle(),
		new DarkBottle(),
		new BloodBottle(),
		new GreenBottle(),
		
		new WaterBottle(),
		new GlueBottle(),
		new FireBottle(),
		new AirBottle(),
	},_throwable_item=concatAll(
		_balls,
		_explosive_block,
		_essence,
		_arrow,
		_bullet,
		_rpg_normal,
		_rpg_guided,
		_tank_shell,
		_warhead,
		_bottle
	),_agent={
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
		new AgentMaker(Slime.class),
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
		new AgentMaker(CactusBoss.class),
		new AgentMaker(FloatingDetector.class),
		new AgentMaker(FloatingDetector_5.class),
		new AgentMaker(Robot.class),
		new AgentMaker(IronBall_HDX.class),
	},_energy_cell={
		EnergyCell.full(),
		HeatEnergyCell.full(),
		HeatEnergyCellGroup.full(),
		FuelEnergyCell.full(),
		DarkEnergyCell.full(),
		BlueCrystalEnergyCell.full(),
		BlueCrystalEnergyCellGroup.full(),
		InfiniteEnergyCell.full(),
	},_field={
		new BloodField(),
		new GreenField(),
		new EnergyField(),
		new FireField(),
		new DarkField(),
		new ProtectionZone(),
	},_others_energy_tool={
		new FieldMaintainer(),
		
		new TeleportationStick(),
		new TeleportationSquare(),
		new Flashlight(),
		new MineDetector(),
		new EnergyFocuser(),
		new BlockRepairer(),
		new ItemAbsorber(),
		new AgentRef(),
		new AgentRefReadonly(),
		
		new EntityPicker(),
		new EnergyLifter(),
		
		new EnergyDrill(),
		new DiamondEnergyDrill(),
		
		new EnergyBoxingGlove(),
	},_energy_launcher={
		new DetectorBallLauncher(),
		new EnergyBallLauncher(),
		new EnergyBallLauncher_3(),
		new FireBallLauncher(),
		new FireBallLauncher_3(),
		new DarkBallLauncher(),
		new DarkBallLauncher_3(),
		new ShockWaveLauncher(),
		
		new HighEnergyLauncher(),
		new HighEnergyFireLauncher(),
		new HighEnergyDarkLauncher(),
		new HighEnergyShockWaveLauncher(),
	},_launcher={
		new WarheadLauncher(),
		new RPGLauncherDisposable(),
		new SimpleLauncher(),
		new RPGLauncher(),
		new RecoillessGun(),
		new BallLauncher(),
		new EnergyGun(),
		new EnergyShotgun(),
		new EnergySubmachineGun(),
		new EnergyMachineGun(),
		new EnergyAntiMaterialGun(),
	},_energy_armor={
		new JetPack(),
		new FloatingPack(),
		new EnergySkateBoard(),
		//new ClimberStick(),
		new EnergyShield(),
	},_vehicle={
		new Tank(),
		new IronAirship(),
		new WoodenAirship(),
		new GlassAirship(),
		new IronAirshipFlank(),
		new FastAirshipFlank(),
		new GlassAirshipFlank(),
		new WoodenAirshipFlank(),
		new EnergyStoneAirshipFlank(),
		new IronNailBox(),
		new CEBox(),
		new FastBox(),
		new ExplosiveBox(),
		new JumpBox(),
		new Shilka(),
		new Trebuchet(),
		new FastBall(),
		//new Ball8(),
		
		new FlakGun(),
		new BowPipeline(),
		new EnergyPipeline(),
	},_energy_tool=concatAll(
		_energy_cell,
		_field,
		_others_energy_tool,
		_energy_launcher,
		_launcher,
		_energy_armor,
		_vehicle
	),_circuit={
		new CircuitHolderBlock(),
		new EnergyStoneBlock(),
		
		new Button(),
		new Switch(),
		new ContactInductor(),
		new EnergySource(),
		new EnergyStone(),
		new Wire(),
		new OneWayWire(),
		new AndGate(),
		new EnergyBlocker(),
		new OneWayControler(),
		
		new BlockFillTool(),
		new BlockCopyTool(),
		new CircuitPainter(),
		new Spanner(),
		new EnergyPickax(),
		new LauncherBlock(),
		
		/*new LambdaNode.Lambda(),
		new LambdaNode.Apply(),
		new LambdaNode.Variable(),*/
		new Copy(),
		
		new RangeSelector(),
		
		//new EnergyPowerContainerBlock(),
		//new EnergyPowerPipelineBlock(),
	},_normal_item={
		new Apple(),
		new RedFlower(),
		new PurpleFlower(),
		new PurpleFruit(),
		new ZombieCrystal(),
		
		new BloodEssence(),
		new PlantEssence(),
		new EnergyStone(),
		new FireBall(),
		new DarkBall(),
		
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
		new FireStick(),
		new Spring(),
		new Stick(),
		new EnergyMotor(),
		new StringItem(),
		
		new CarbonPowder(),
		new StonePowder(),
		new EnergyStoneOrePowder(0),
		new EnergyStoneOrePowder(1),
		
		new DarkSquare(),
		
		new Book(),
		new Paper(),
	};
	
	//物品合成方案总表
	public static Object[][] 
	_craft_normal_tool={{
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
			new IronStick(),
			new EnergyStone().setAmount(2),
			new BloodEssence().setAmount(4),
			new CraftInfo(400,20,_energy),
			new BloodSword(),
		},{
			new IronStick(),
			new EnergyStone().setAmount(4),
			new BlueCrystal().setAmount(2),
			new CraftInfo(400,20,_energy),
			new BlueCrystalSword(),
		},{
			new IronStick(),
			new EnergyStone().setAmount(2),
			new FireBall().setAmount(4),
			new CraftInfo(400,20,_energy),
			new FireSword(),
		},{
			new IronStick(),
			new EnergyStone().setAmount(2),
			new DarkBall().setAmount(4),
			new CraftInfo(400,20,_energy),
			new DarkSword(),
		},{
			new Stick(),
			new StringItem(),
			new CraftInfo(50,5,_complex),
			new Slingshot(),
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
			new EnergyStone().setAmount(2),
			new CraftInfo(100,5,_complex),
			new EnergyCrossBow(),
		},{
			new CrossBow(),
			new Iron().setAmount(2),
			new FireBall().setAmount(2),
			new CraftInfo(100,5,_complex),
			new FireCrossBow(),
		},{
			new CrossBow(),
			new Iron().setAmount(2),
			new DarkBall().setAmount(2),
			new CraftInfo(100,5,_complex),
			new DarkCrossBow(),
		},{
			new CrossBow(),
			new Iron().setAmount(2),
			new BloodEssence().setAmount(2),
			new CraftInfo(100,5,_complex),
			new BloodCrossBow(),
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
		},{
			new IronStick().setAmount(4),
			new CraftInfo(100,20,0),
			new BlockFillTool()
		},{
			new WoodenBlock(),
			new CraftInfo(300,15,_cut),
			new game.item.Boat()
		},{
			new Iron().setAmount(8),
			new CraftInfo(300,15,_heat),
			new IronBoat()
		},{
			new Iron().setAmount(4),
			new IronNail().setAmount(4),
			new CraftInfo(300,15,_complex),
			new SkateBoard()
		},{
			new SkateBoard(),
			new EnergyMotor().setAmount(2),
			new CraftInfo(300,15,_energy),
			new EnergySkateBoard()
		},{
			new Iron().setAmount(2),
			new Spring().setAmount(2),
			new IronNail().setAmount(4),
			new CraftInfo(300,15,_complex),
			new SpringShoes()
		},{
			new SpringShoes(),
			new IronNail().setAmount(8),
			new CraftInfo(300,15,_complex),
			new SpringNailShoes()
		},{
			new Iron().setAmount(2),
			new EnergyStone().setAmount(4),
			new Spring().setAmount(2),
			new IronNail().setAmount(4),
			new CraftInfo(300,15,_complex),
			new EscapeDevice()
		},{
			new StonePowder().setAmount(2),
			new EnergyStone().setAmount(4),
			new CraftInfo(300,15,_complex),
			new GlueShoes()
		},{
			new Iron().setAmount(4),
			new EnergyStone(),
			new CraftInfo(300,15,_complex),
			new ClimberShoes()
		},{
			new Iron().setAmount(4),
			new IronNail().setAmount(8),
			new CraftInfo(100,10,_complex),
			new IronPatch().setAmount(4)
		},{
			new IronPatch().setAmount(4),
			new EnergyStone(),
			new CraftInfo(100,10,_complex),
			new EnergyPatch().setAmount(2)
		},{
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
		},{
			new Stick().setAmount(11),
			new CraftInfo(100,10,_cut),
			new WoodenArmor(),
		},{
			new WoodenArmor(),
			new CactusBlock(),
			new CraftInfo(100,10,_cut),
			new CactusArmor(),
		},{
			new GlassBlock(),
			new CraftInfo(100,10,_heat),
			new GlassArmor(),
		},{
			new Iron().setAmount(11),
			new CraftInfo(100,10,_heat),
			new IronArmor(),
		},{
			new IronArmor(),
			new IronNail().setAmount(11),
			new CraftInfo(200,10,_heat),
			new IronNailArmor(),
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
		},{
			new IronArmor(),
			new PlantEssence().setAmount(11),
			new CraftInfo(100,10,_complex),
			new GreenArmor()
		},{
			new IronArmor(),
			new BloodEssence().setAmount(11),
			new CraftInfo(100,10,_complex),
			new BloodArmor()
		},{
			new IronNailArmor(),
			new BloodEssence().setAmount(11),
			new CraftInfo(200,10,_complex),
			new BloodNailArmor()
		},{
			new IronArmor(),
			new DarkBall().setAmount(11),
			new CraftInfo(100,10,_diamond),
			new DarkArmor()
		},{
			new IronArmor(),
			new CarbonPowder().setAmount(8),
			new FireBall(),
			new CraftInfo(100,10,_complex),
			new ExplosiveArmor()
		},{
			new IronArmor(),
			new WaterBottle().setAmount(11),
			new EnergyStone().setAmount(4),
			new CraftInfo(100,10,_energy),
			new WaterArmor(),
		},{
			new IronArmor(),
			new FireBall().setAmount(11),
			new CraftInfo(100,10,_energy),
			new FireArmor(),
		},{
			new EnergyStone().setAmount(11),
			new CraftInfo(100,10,_energy),
			new EnergyStoneArmor(),
		},{
			new Stick().setAmount(7),
			new CraftInfo(100,10,_cut),
			new WoodenShield(),
		},{
			new WoodenShield(),
			new CactusBlock(),
			new CraftInfo(100,10,_cut),
			new CactusShield(),
		},{
			new Iron().setAmount(7),
			new CraftInfo(100,10,_heat),
			new IronShield(),
		},{
			new IronShield(),
			new IronNail().setAmount(16),
			new CraftInfo(100,10,_cut),
			new IronNailShield(),
		},{
			new IronNailShield(),
			new BloodEssence().setAmount(4),
			new CraftInfo(100,10,_cut),
			new BloodNailShield(),
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
		},{
			new IronShield(),
			new EnergyStone().setAmount(7),
			new CraftInfo(100,1000,_compress),
			new EnergyStoneShield(),
		},{
			new WoodenBlock().setAmount(2),
			new CraftInfo(100,10,_cut),
			new BigWoodenShield(),
		},{
			new Iron().setAmount(32),
			new CraftInfo(100,10,_heat),
			new BigIronShield(),
		},{
			new Iron().setAmount(32),
			new IronNail().setAmount(8),
			new CraftInfo(100,10,_heat),
			new BigIronNailShield(),
		},{
			new IronBall_HD().setAmount(2),
			new CraftInfo(200,4000,_compress),
			new BigHDShield(),
		}
	},_craft_energy_tool={{
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
			new Iron(),
			new EnergyStone(),
			new FireBall(),
			new CarbonPowder().setAmount(4),
			new CraftInfo(150,10,_complex),
			FuelEnergyCell.full(),
		},{
			new FuelEnergyCell(),
			new CarbonPowder().setAmount(4),
			new CraftInfo(150,10,_complex),
			FuelEnergyCell.full(),
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
		},{
			new BlueCrystalEnergyCell().setAmount(8),
			new CraftInfo(100,20,_energy),
			new BlueCrystalEnergyCellGroup(),
		},{
			new Bucket(),
			new Spring(),
			new IronStick(),
			new EnergyStone().setAmount(6),
			new CraftInfo(300,20,_complex),
			new EnergyMotor(),
		},{
			new EnergyBallLauncher(),
			new Iron().setAmount(4),
			new EnergyStone().setAmount(4),
			new CraftInfo(100,10,_energy),
			new DetectorBallLauncher(),
		},{
			new EnergyMotor(),
			new Bucket(),
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
		},{
			new EnergyBallLauncher(),
			new EnergyStone().setAmount(8),
			new CraftInfo(100,20,_energy),
			new ShockWaveLauncher(),
		},{
			new EnergyBallLauncher(),
			new EnergyMotor().setAmount(2),
			new EnergyStone().setAmount(16),
			new Iron().setAmount(8),
			new CraftInfo(300,20,_energy),
			new HighEnergyLauncher(),
		},{
			new HighEnergyLauncher(),
			new FireBall().setAmount(16),
			new CraftInfo(300,20,_energy),
			new HighEnergyFireLauncher(),
		},{
			new HighEnergyLauncher(),
			new DarkBall().setAmount(16),
			new CraftInfo(300,20,_energy),
			new HighEnergyDarkLauncher(),
		},{
			new HighEnergyLauncher(),
			new EnergyMotor().setAmount(2),
			new EnergyStone().setAmount(16),
			new CraftInfo(300,20,_energy),
			new HighEnergyShockWaveLauncher(),
		},{
			new EnergyBallLauncher(),
			new FireBall(),
			new CraftInfo(100,20,_energy),
			new WarheadLauncher(),
		},{
			new EnergyStone(),
			new CarbonPowder(),
			new Spring(),
			new Bucket(),
			new CraftInfo(100,20,_energy),
			new RPGLauncherDisposable(),
		},{
			new FireBall(),
			new Bucket(),
			new CraftInfo(50,10,_energy),
			new SimpleLauncher(),
		},{
			new EnergyBallLauncher(),
			new FireBallLauncher(),
			new Spring(),
			new Bucket(),
			new CraftInfo(600,20,_energy),
			new RPGLauncher(),
		},{
			new RPGLauncher(),
			new EnergyMotor().setAmount(6),
			new Bucket(),
			new CraftInfo(600,20,_energy),
			new RecoillessGun(),
		},{
			new EnergyBallLauncher(),
			new EnergyMotor().setAmount(4),
			new Bucket(),
			new CraftInfo(600,20,_energy),
			new BallLauncher(),
		},{
			new EnergyBallLauncher(),
			new Iron().setAmount(4),
			new CraftInfo(100,20,_energy),
			new EnergyGun(),
		},{
			new EnergyGun(),
			new Bucket(),
			new CraftInfo(300,20,_energy),
			new EnergyShotgun(),
		},{
			new EnergyMotor(),
			new Iron().setAmount(2),
			new CraftInfo(300,20,_energy),
			new EnergySubmachineGun(),
		},{
			new EnergyGun(),
			new EnergyMotor().setAmount(2),
			new Iron().setAmount(8),
			new CraftInfo(300,20,_energy),
			new EnergyMachineGun(),
		},{
			new EnergyGun(),
			new EnergyMotor().setAmount(4),
			new CraftInfo(300,20,_energy),
			new EnergyAntiMaterialGun(),
		},{
			new EnergyBallLauncher(),
			new Iron().setAmount(2),
			new EnergyStone().setAmount(2),
			new CraftInfo(200,20,_energy),
			new AgentRef(),
		},{
			new EnergyBallLauncher(),
			new Iron().setAmount(2),
			new EnergyStone().setAmount(2),
			new CraftInfo(200,20,_energy),
			new AgentRefReadonly(),
		},{
			new IronStick(),
			new Iron().setAmount(2),
			new EnergyStone().setAmount(2),
			new CraftInfo(100,5,_energy),
			new EntityPicker(),
		},{
			new EnergyMotor(),
			new Iron().setAmount(4),
			new EnergyStone().setAmount(4),
			new CraftInfo(100,5,_energy),
			new EnergyLifter(),
		},{
			new EnergyMotor(),
			new IronDrill(),
			new CraftInfo(100,5,_energy),
			new EnergyDrill(),
		},{
			new EnergyMotor(),
			new DiamondDrill(),
			new CraftInfo(100,5,_energy),
			new DiamondEnergyDrill(),
		},{
			new EnergyMotor(),
			new Spring(),
			new CraftInfo(200,10,_energy),
			new EnergyBoxingGlove(),
		},{
			new EnergyStone().setAmount(16),
			new CraftInfo(200,20,_energy),
			new EnergyField(),
		},{
			new FireBall().setAmount(16),
			new CraftInfo(200,20,_energy),
			new FireField(),
		},{
			new DarkBall().setAmount(16),
			new CraftInfo(200,20,_energy),
			new DarkField(),
		},{
			new BloodEssence().setAmount(16),
			new CraftInfo(200,20,_energy),
			new BloodField(),
		},{
			new PlantEssence().setAmount(16),
			new CraftInfo(200,20,_energy),
			new GreenField(),
		},{
			new Iron().setAmount(4),
			new EnergyMotor().setAmount(4),
			new CraftInfo(200,20,_energy),
			new FieldMaintainer(),
		},{
			new DarkBall().setAmount(8),
			new EnergyStone().setAmount(2),
			new IronStick(),
			new CraftInfo(100,20,_energy),
			new TeleportationStick(),
		},{
			new DarkBall().setAmount(4),
			new EnergyStone().setAmount(4),
			new CraftInfo(100,20,_energy),
			new TeleportationSquare(),
		},{
			new IronStick(),
			new EnergyStone().setAmount(8),
			new CraftInfo(100,20,_energy),
			new Flashlight(),
		},{
			new IronStick().setAmount(2),
			new EnergyStone().setAmount(4),
			new CraftInfo(100,20,_energy),
			new MineDetector(),
		},{
			new Iron().setAmount(4),
			new EnergyMotor().setAmount(4),
			new EnergyStone().setAmount(32),
			new CraftInfo(200,20,_energy),
			new EnergyFocuser(),
		},{
			new Spanner(),
			new EnergyStone().setAmount(4),
			new CraftInfo(100,10,_energy),
			new BlockRepairer(),
		},{
			new IronStick(),
			new EnergyMotor(),
			new CraftInfo(100,10,_energy),
			new ItemAbsorber(),
		},{
			new IronArmor(),
			new EnergyMotor().setAmount(2),
			new CraftInfo(200,20,_energy),
			new JetPack(),
		},{
			new IronArmor(),
			new EnergyMotor().setAmount(3),
			new CraftInfo(200,20,_energy),
			new FloatingPack(),
		},{
			new IronShield(),
			new EnergyMotor(),
			new CraftInfo(150,15,_energy),
			new EnergyShield(),
		},{
			new Iron().setAmount(64),
			new EnergyStone().setAmount(32),
			new EnergyMotor().setAmount(8),
			new RPGLauncher(),
			new CraftInfo(1000,30,_energy),
			new Tank(),
		},{
			new IronBlock().setAmount(4),
			new EnergyStone().setAmount(32),
			new EnergyMotor().setAmount(8),
			new RPGLauncher(),
			new CraftInfo(1000,30,_energy),
			new IronAirship(),
		},{
			new WoodenBlock().setAmount(4),
			new EnergyStone().setAmount(8),
			new EnergyMotor().setAmount(2),
			new RPGLauncher(),
			new CraftInfo(200,30,_energy),
			new WoodenAirship(),
		},{
			new GlassBlock().setAmount(4),
			new EnergyStone().setAmount(16),
			new EnergyMotor().setAmount(2),
			new RPGLauncher(),
			new CraftInfo(200,30,_energy),
			new GlassAirship(),
		},{
			new IronBlock().setAmount(4),
			new EnergyStone().setAmount(16),
			new EnergyMotor().setAmount(2),
			new CraftInfo(1000,30,_energy),
			new IronAirshipFlank(),
		},{
			new IronBlock().setAmount(4),
			new EnergyStone().setAmount(64),
			new EnergyMotor().setAmount(6),
			new CraftInfo(1000,30,_energy),
			new FastAirshipFlank(),
		},{
			new GlassBlock().setAmount(4),
			new EnergyStone().setAmount(8),
			new EnergyMotor().setAmount(1),
			new CraftInfo(200,30,_energy),
			new GlassAirshipFlank(),
		},{
			new WoodenBlock().setAmount(4),
			new EnergyMotor().setAmount(1),
			new CraftInfo(200,30,_energy),
			new WoodenAirshipFlank(),
		},{
			new EnergyStoneBlock().setAmount(4),
			new EnergyMotor().setAmount(2),
			new CraftInfo(1000,30,_energy),
			new EnergyStoneAirshipFlank(),
		},{
			new Iron().setAmount(32),
			new IronNail().setAmount(256),
			new EnergyMotor().setAmount(4),
			new CraftInfo(1000,30,_energy),
			new IronNailBox(),
		},{
			new Iron().setAmount(32),
			new EnergyMotor().setAmount(4),
			new DarkBall().setAmount(8),
			new EnergyDrill().setAmount(4),
			new CraftInfo(1000,30,_energy),
			new CEBox(),
		},{
			new Iron().setAmount(32),
			new EnergyStone().setAmount(16),
			new EnergyMotor().setAmount(8),
			new CraftInfo(1000,30,_energy),
			new FastBox(),
		},{
			new Iron().setAmount(32),
			new EnergyStone().setAmount(16),
			new Spring().setAmount(4),
			new EnergyMotor().setAmount(4),
			new CraftInfo(1000,30,_energy),
			new JumpBox(),
		},{
			new Iron().setAmount(64),
			new EnergyStone().setAmount(32),
			new EnergyMotor().setAmount(8),
			new CraftInfo(1000,30,_energy),
			new Shilka(),
		},{
			new Iron().setAmount(32),
			new EnergyStone().setAmount(16),
			new EnergyMotor().setAmount(4),
			new CraftInfo(1000,30,_energy),
			new Trebuchet(),
		},{
			new Iron().setAmount(32),
			new EnergyStone().setAmount(16),
			new EnergyMotor().setAmount(8),
			new RPGLauncher(),
			new CraftInfo(1000,30,_energy),
			new FastBall(),
		},{
			new RPGLauncher().setAmount(5),
			new CraftInfo(1000,30,_energy),
			new FlakGun(),
		},{
			new Iron().setAmount(20),
			new EnergyStone().setAmount(10),
			new EnergyMotor().setAmount(5),
			new CraftInfo(1000,30,_energy),
			new BowPipeline(),
		},{
			new Iron().setAmount(20),
			new Spring().setAmount(5),
			new EnergyMotor().setAmount(5),
			new CraftInfo(1000,30,_energy),
			new EnergyPipeline(),
		},
	},_craft_circuit={{
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
			new Iron(),
			new EnergyStone(),
			new CraftInfo(50,5,_energy),
			new EnergySource(),
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
	},_craft_functional_block={{
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
			new CraftInfo(100,5,_energy),
			new DarkBoxBlock().setAmount(2),
		},{
			new DarkSquare().setAmount(2),
			new CraftInfo(100,5,_energy),
			new TeleportationBlock().setAmount(2),
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
			new IronStick(),
			new CraftInfo(20,5,_cut),
			new SoftIronScaffoldBlock().setAmount(8),
		},{
			new IronStick(),
			new CraftInfo(20,5,_cut),
			new IronMeshBlock().setAmount(4),
		},{
			new IronStick().setAmount(5),
			new Spring(),
			new CraftInfo(40,10,_cut),
			new SpringBlock(),
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
			new ItemAbsorber(),
			new IronBlock(),
			new CraftInfo(200,20,_energy),
			new ItemAbsorberBlock(),
		},{
			new Iron().setAmount(8),
			new EnergyStone().setAmount(2),
			new CraftInfo(100,10,_cut),
			new ItemExporterBlock(),
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
			new Diamond().setAmount(4),
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
		},{
			new IronBlock(),
			new EnergyStone().setAmount(8),
			new PlantEssence().setAmount(8),
			new CraftInfo(300,20,_energy),
			new LightCollectorBlock(),
		},{
			new IronBlock(),
			new EnergyStone().setAmount(4),
			new CraftInfo(300,20,_energy),
			new AgentEnergyCollectorBlock(),
		},{
			new WoodenBlock(),
			new Stick().setAmount(3),
			new CraftInfo(50,10,_cut),
			new BookshelfBlock(),
		},{
			new StoneBlock(),
			new CarbonPowder().setAmount(32),
			new FireBall().setAmount(24),
			new CraftInfo(10,5,_complex),
			new ExplosiveBlock(),
		},{
			new EnergyStoneBlock().setAmount(16),
			new CraftInfo(1000,5000,_compress),
			new HDEnergyStoneBlock(),
		},{
			new Stick(),
			new CraftInfo(10,5,0),
			new TorchBlock(),
		},{
			new IronStick(),
			new EnergyStone(),
			new CraftInfo(10,5,_complex),
			new EnergyTorchBlock(),
		}
	},_craft_normal_block={{
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
		},{
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
			new Stone().setAmount(8*4),
			new Iron(),
			new CraftInfo(100,20,_block),
			new ImprovedStoneBlock().setAmount(4),
		},{
			new Quartz().setAmount(16*4),
			new Iron(),
			new CraftInfo(100,20,_block),
			new ImprovedQuartzBlock().setAmount(4),
		},{
			new FireBall().setAmount(16),
			new CraftInfo(200,20,_block),
			new HeatBlock(),
		},{
			new DarkBall().setAmount(16),
			new CraftInfo(200,20,_block),
			new DarkBlock(),
		},{
			new BloodEssence().setAmount(16),
			new CraftInfo(200,20,_block),
			new BloodBlock(),
		},{
			new PlantEssence().setAmount(16),
			new CraftInfo(200,20,_block),
			new GreenBlock(),
		},{
			new StonePowder().setAmount(8),
			new CraftInfo(50,10,_block),
			new StonePowderBlock()
		},{
			WaterBlock.getInstance(),
			new DirtBlock(),
			new CraftInfo(50,10,_block),
			MutWaterBlock.getInstance()
		},{
			new StonePowderBlock(),
			new EnergyStone().setAmount(2),
			new CraftInfo(50,10,_block),
			new GlueBlock()
		},{
			new EnergyStone(),
			new CraftInfo(20,3000,_compress),
			new ReactionBlock()
		}
	},_craft_normal_item={{
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
			new FireBall(),
			new CraftInfo(50,10,_energy),
			new FireStick(),
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
		},{
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
		},{
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
		},{
			new game.item.Grass().setAmount(11),
			new CraftInfo(10,5,_cut),
			new Paper(),
		},{
			new LeafBlock(0).setAmount(11),
			new CraftInfo(10,5,_cut),
			new Paper(),
		},{
			new Paper().setAmount(6),
			new CraftInfo(20,10,_cut),
			new Book(),
		}
	},_craft_throwable_item={{
			new StoneBlock(),
			new CraftInfo(100,10,_cut),
			new StoneBall(),
		},{
			new IronBlock(),
			new CraftInfo(100,10,_heat),
			new IronBall(),
		},{
			new Iron().setAmount(8),
			new FireBall().setAmount(3),
			new CarbonPowder().setAmount(6),
			new CraftInfo(100,10,_energy),
			new ExplosiveBall(),
		},{
			new IronNail().setAmount(100),
			new FireBall().setAmount(3),
			new CarbonPowder(),
			new CraftInfo(100,10,_energy),
			new ExplosiveIronNailBall(),
		},{
			new Iron().setAmount(4),
			new FireBall().setAmount(8),
			new CarbonPowder().setAmount(16),
			new CraftInfo(100,10,_energy),
			new HeatRing(),
		},{
			new IronBall(),
			new EnergyStone().setAmount(16),
			new CraftInfo(10,5,_energy),
			new EnergyIronBall(),
		},{
			new Iron().setAmount(8),
			new CarbonPowder().setAmount(40),
			new FireBall().setAmount(32),
			new CraftInfo(10,5,_energy),
			new HeatBall(),
		},{
			new IronBall(),
			new DarkBall().setAmount(16),
			new CraftInfo(10,5,_energy),
			new DarkIronBall(),
		},{
			new IronBall(),
			new BloodEssence().setAmount(16),
			new CraftInfo(10,5,_energy),
			new BloodIronBall(),
		},{
			new Iron().setAmount(4),
			new EnergyStone().setAmount(8),
			new CraftInfo(100,10,_energy),
			new EnergyStoneRing(),
		},{
			new IronNail().setAmount(12),
			new CraftInfo(100,10,_heat),
			new IronNailBall(),
		},{
			new IronNail().setAmount(12),
			new CraftInfo(100,10,_heat),
			new SmallIronNailBall(),
		},{
			new IronBall().setAmount(4),
			new CraftInfo(400,20,_heat),
			new BigIronBall(),
		},{
			new BigIronBall(),
			new EnergyStone().setAmount(8),
			new CraftInfo(400,20,_energy),
			new BlocksBall(),
		},{
			new IronBall(),
			new EnergyStone().setAmount(2),
			new CraftInfo(100,10,_energy),
			new BoundaryBall(),
		},{
			new Bullet_HD().setAmount(10),
			new CraftInfo(200,1000,_compress),
			new IronBall_HD(),
		},{
			new Cube().setAmount(64),
			new CraftInfo(1000,5000,_compress),
			new BlackHole(),
		},{
			new Iron(),
			new EnergyStone(),
			new FireBall(),
			new CraftInfo(50,10,_energy),
			new game.item.HandGrenade().setAmount(4),
		},{
			new Stick(),
			new IronNail(),
			new CraftInfo(25,5,0),
			new Arrow(),
		},{
			new IronStick(),
			new IronNail(),
			new CraftInfo(25,5,_heat),
			new IronArrow(),
		},{
			new Stick(),
			new CraftInfo(50,5,_cut),
			new WoodenBullet().setAmount(4),
		},{
			new Stone(),
			new CraftInfo(15,5,_cut),
			new StoneBullet(),
		},{
			new Quartz(),
			new CraftInfo(25,5,_cut),
			new QuartzBullet(),
		},{
			new IronNail(),
			new CraftInfo(25,5,_heat),
			new Bullet(),
		},{
			new Diamond(),
			new Bullet().setAmount(4),
			new CraftInfo(50,5,_cut),
			new DiamondBullet().setAmount(4),
		},{
			new Bullet().setAmount(3),
			new EnergyStone(),
			new CraftInfo(25,5,_energy),
			new SpecialBullet(1).setAmount(3),
		},{
			new Bullet().setAmount(3),
			new FireBall(),
			new CraftInfo(25,5,_energy),
			new SpecialBullet(2).setAmount(3),
		},{
			new Bullet().setAmount(3),
			new DarkBall(),
			new CraftInfo(25,5,_energy),
			new SpecialBullet(3).setAmount(3),
		},{
			new Bullet().setAmount(3),
			new BloodEssence(),
			new CraftInfo(25,5,_energy),
			new SpecialBullet(4).setAmount(3),
		},{
			new Bullet().setAmount(3),
			new PlantEssence(),
			new CraftInfo(25,5,_energy),
			new SpecialBullet(5).setAmount(3),
		},{
			new Iron().setAmount(32),
			new EnergyStone(),
			new CraftInfo(25,400,_compress),
			new Bullet_HD().setAmount(8),
		},{
			new IronNail(),
			new CraftInfo(25,5,_heat),
			new DefendBullet().setAmount(8),
		},{
			new Iron().setAmount(2),
			new EnergyStone(),
			new FireBall().setAmount(2),
			new CraftInfo(200,10,_energy),
			new Mine().setAmount(4),
		},{
			new Iron().setAmount(6),
			new EnergyStone(),
			new FireBall(),
			new CarbonPowder().setAmount(3),
			new CraftInfo(200,10,_energy),
			new FlakBullet().setAmount(25),
		},{
			new Iron().setAmount(2),
			new EnergyStone(),
			new CarbonPowder().setAmount(8),
			new CraftInfo(200,5,_energy),
			new FireBullet().setAmount(25),
		},{
			new IronNail().setAmount(8),
			new CarbonPowder(),
			new Iron().setAmount(2),
			new FireBall(),
			new CraftInfo(50,10,_energy),
			new APDSBullet().setAmount(8),
		},{
			new Iron().setAmount(6),
			new EnergyStone().setAmount(4),
			new CraftInfo(200,5,_energy),
			new BigGuidedBullet(),
		},{
			new Iron().setAmount(10),
			new FireBall().setAmount(2),
			new CarbonPowder().setAmount(16),
			new CraftInfo(200,5,_energy),
			new GuidedBullet().setAmount(5),
		},{
			new Iron().setAmount(2),
			new DarkBall().setAmount(1),
			new CraftInfo(200,5,_energy),
			new DarkGuidedBullet().setAmount(1),
		},{
			new Iron().setAmount(2),
			new PlantEssence().setAmount(4),
			new CraftInfo(200,5,_energy),
			new GreenGuidedBullet().setAmount(1),
		},{
			new Iron().setAmount(2),
			new BloodEssence().setAmount(4),
			new CraftInfo(200,5,_energy),
			new BloodGuidedBullet().setAmount(1),
		},{
			new Stone().setAmount(1),
			new EnergyStone().setAmount(1),
			new CraftInfo(1000,20,_energy),
			new PathBullet().setAmount(1),
		},{
			new Bucket(),
			new Iron().setAmount(2),
			new CarbonPowder().setAmount(4),
			new CraftInfo(200,10,_energy),
			new RPG_Empty(),
		},{
			new RPG_Empty(),
			new Warhead_Energy(),
			new CraftInfo(200,10,_energy),
			new RPG_Energy(),
		},{
			new RPG_Empty(),
			new Warhead_Energy_HE(),
			new CraftInfo(200,10,_energy),
			new RPG_Energy_HE(),
		},{
			new RPG_Empty(),
			new Warhead_HE(),
			new CraftInfo(200,10,_energy),
			new RPG_HE(),
		},{
			new RPG_Empty(),
			new Warhead_HEAT(),
			new CraftInfo(200,10,_energy),
			new RPG_HEAT(),
		},{
			new RPG_Empty(),
			new Warhead_Spark(),
			new CraftInfo(200,10,_energy),
			new RPG_Spark(),
		},{
			new RPG_Empty(),
			new Warhead_WideRangeSpark(),
			new CraftInfo(200,10,_energy),
			new RPG_WideRangeSpark(),
		},{
			new RPG_Empty(),
			new Warhead_Dark(),
			new CraftInfo(200,10,_energy),
			new RPG_Dark(),
		},{
			new RPG_Empty(),
			new Warhead_Dark_HE(),
			new CraftInfo(200,10,_energy),
			new RPG_Dark_HE(),
		},{
			new RPG_Empty(),
			new Warhead_Blood(),
			new CraftInfo(200,10,_energy),
			new RPG_Blood(),
		},{
			new RPG_Empty(),
			new Warhead_Smoke(),
			new CraftInfo(200,10,_energy),
			new RPG_Smoke(),
		},{
			new RPG_Guided().setAmount(2),
			new Warhead_HE(),
			new CraftInfo(200,10,_energy),
			new RPG_Small_HE().setAmount(2),
		},{
			new RPG_Empty(),
			new Iron().setAmount(4),
			new EnergyStone().setAmount(12),
			new CraftInfo(200,10,_energy),
			new RPG_Bullet(),
		},{
			new Bucket(),
			new Iron().setAmount(4),
			new EnergyStone().setAmount(4),
			new CarbonPowder().setAmount(16),
			new CraftInfo(200,10,_energy),
			new RPG_Guided(),
		},{
			new RPG_Guided(),
			new EnergyStone().setAmount(12),
			new CraftInfo(200,10,_energy),
			new RPG_EnergyBall(),
		},{
			new RPG_Guided(),
			new EnergyBarrier(),
			new CraftInfo(200,10,_energy),
			new RPG_EnergyBarrier(),
		},{
			new RPG_Guided(),
			new FireBarrier(),
			new CraftInfo(200,10,_energy),
			new RPG_FireBarrier(),
		},{
			new RPG_Guided(),
			new Bullet_HD().setAmount(4),
			new CraftInfo(200,10,_energy),
			new RPG_HD(),
		},{
			new RPG_Guided(),
			new EnergyStone().setAmount(2),
			new Iron().setAmount(4),
			new CraftInfo(200,10,_energy),
			new RPG_Block(),
		},{
			new Bucket(),
			new Iron().setAmount(4),
			new FireBall().setAmount(6),
			new CarbonPowder().setAmount(1),
			new CraftInfo(200,10,_energy),
			new Tank_HEATFS(),
		},{
			new Bucket(),
			new Iron().setAmount(6),
			new FireBall().setAmount(4),
			new CarbonPowder().setAmount(2),
			new CraftInfo(200,10,_energy),
			new Tank_APFSDS(),
		},{
			new Iron(),
			new EnergyStone().setAmount(1+2),
			new CraftInfo(200,20,_energy),
			new Warhead_Energy(),
		},{
			new Iron(),
			new EnergyStone().setAmount(1+4),
			new CraftInfo(200,20,_energy),
			new Warhead_Energy_HE(),
		},{
			new Iron(),
			new EnergyStone().setAmount(1),
			new FireBall().setAmount(2),
			new CraftInfo(200,20,_energy),
			new Warhead_HE(),
		},{
			new Iron(),
			new EnergyStone().setAmount(1),
			new FireBall().setAmount(4),
			new CraftInfo(200,20,_energy),
			new Warhead_HEAT(),
		},{
			new Iron(),
			new EnergyStone(),
			new FireBall(),
			new CarbonPowder().setAmount(12),
			new CraftInfo(200,20,_energy),
			new Warhead_Spark(),
		},{
			new Iron(),
			new FireBall(),
			new CarbonPowder().setAmount(12),
			new CraftInfo(200,20,_energy),
			new Warhead_WideRangeSpark(),
		},{
			new Iron(),
			new EnergyStone().setAmount(1),
			new DarkBall().setAmount(2),
			new CraftInfo(200,20,_energy),
			new Warhead_Dark(),
		},{
			new Iron(),
			new EnergyStone().setAmount(1),
			new DarkBall().setAmount(4),
			new CraftInfo(200,20,_energy),
			new Warhead_Dark_HE(),
		},{
			new Iron(),
			new EnergyStone(),
			new BloodEssence(),
			new CraftInfo(200,20,_energy),
			new Warhead_Blood(),
		},{
			new Iron(),
			new StonePowder(),
			new CraftInfo(200,20,_energy),
			new Warhead_Smoke(),
		},{
			new Iron().setAmount(4),
			new EnergyStone().setAmount(8),
			new CraftInfo(200,20,_energy),
			new EnergyBarrier(),
		},{
			new Iron().setAmount(4),
			new FireBall().setAmount(12),
			new CraftInfo(200,20,_energy),
			new FireBarrier(),
		},{
			new Iron().setAmount(4),
			new PlantEssence().setAmount(8),
			new CraftInfo(200,20,_energy),
			new GreenBarrier(),
		},{
			new GlassBlock(),
			new CraftInfo(200,10,_heat),
			new Bottle().setAmount(4),
		},{
			new Bottle(),
			new EnergyStone(),
			new CraftInfo(25,5,0),
			new EnergyStoneBottle(),
		},{
			new Bottle(),
			new FireBall(),
			new CraftInfo(25,5,0),
			new HeatBottle(),
		},{
			new Bottle(),
			new DarkBall(),
			new CraftInfo(25,5,0),
			new DarkBottle(),
		},{
			new Bottle(),
			new BloodEssence(),
			new CraftInfo(25,5,0),
			new BloodBottle(),
		},{
			new Bottle(),
			new PlantEssence(),
			new CraftInfo(25,5,0),
			new GreenBottle(),
		},{
			new Bottle().setAmount(4),
			WaterBlock.getInstance(),
			new CraftInfo(25,5,0),
			new WaterBottle().setAmount(4),
		},{
			new Bottle().setAmount(4),
			new GlueBlock(),
			new CraftInfo(25,5,0),
			new GlueBottle().setAmount(4),
		},{
			new Bottle().setAmount(4),
			new CarbonPowder().setAmount(16),
			new FireBall(),
			new CraftInfo(25,5,0),
			new FireBottle().setAmount(4),
		},{
			new Bottle().setAmount(3),
			new CraftInfo(25,5,0),
			new AirBottle(),
		},{
			new EnergyStone().setAmount(2),
			new Iron().setAmount(4),
			new CraftInfo(50,10,_energy),
			new AgentMaker(FloatingDetector.class),
		},{
			new AgentMaker(FloatingDetector.class).setAmount(5),
			new EnergyStone().setAmount(4),
			new Iron().setAmount(8),
			new CraftInfo(200,20,_energy),
			new AgentMaker(FloatingDetector_5.class),
		},{
			new Iron().setAmount(16),
			new EnergyMotor().setAmount(2),
			new CraftInfo(200,30,_energy),
			new AgentMaker(Robot.class),
		},{
			new ZombieCrystal().setAmount(4),
			new EnergyStone().setAmount(4),
			new PlantEssence(),
			new BloodEssence(),
			new CraftInfo(100,30,0),
			new AgentMaker(Villager.class),
		},{
			new DarkBall().setAmount(256),
			new CraftInfo(1000,30,0),
			new AgentMaker(SpaceBoss.class),
		},{
			new AgentMaker(SpaceBoss.class),
			new CraftInfo(1000,30,0xffffffff),
			new Cube().setAmount(6),
		},{
			new CactusBlock().setAmount(1024),
			new CraftInfo(1000,30,0),
			new AgentMaker(CactusBoss.class),
		},{
			new Iron().setAmount(96),
			new EnergyMotor().setAmount(12),
			new EnergyStone().setAmount(32),
			new FireBall().setAmount(32),
			new CraftInfo(1000,30,0),
			new AgentMaker(ZombieBase.class),
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
	
	public static Craft[] get(Item it){
		ArrayList<Craft>cs=new ArrayList<>();
		for(Craft[] ws:craft_all)for(Craft w:ws)if(w.out[0].get().cmpType(it))cs.add(w);
		Craft[] c=new Craft[cs.size()];
		return cs.toArray(c);
	}
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
