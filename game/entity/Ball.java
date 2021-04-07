package game.entity;

public abstract class Ball extends Entity implements Cloneable{
	private static final long serialVersionUID=1844677L;
	public double radius(){return 0.2;}
	public double width(){return radius();}
	public double height(){return radius();}
	public boolean chkRigidBody(){return false;}
	public double mass(){return 0.01;}
	@Override
	double friction(){return 1e-15;}
	public void update(){
		super.update();
		hp-=0.03;
	}
	public double gA(){return 0;}
	@Override
	public Entity getBall(){
		return clone();
	}
	@Override
	public Ball clone(){
		Ball i=null;
		try{
			i=(Ball)super.clone();
		}catch(Exception e){e.printStackTrace();}
		return i;
	}
}
