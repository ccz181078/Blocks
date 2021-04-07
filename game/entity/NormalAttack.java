package game.entity;

public class NormalAttack extends Attack{
	private static final long serialVersionUID=1844677L;
	public NormalAttacker z;
	public NormalAttack(Source s,double v,NormalAttacker z){
		super(s,v);
		this.z=z;
	}
	public double getWeight(NormalAttacker w){
		return z.getWeight(w);
	}
}
