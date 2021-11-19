package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;

public class DeadHumanBoxShield extends BoxShield{
	public void update(){
		if(rnd()<0.01){
			int cnt=0;
			for(SingleItem si:items.toArray()){
				if(!si.isEmpty())++cnt;
			}
			if(cnt==0||cnt<=3&&rnd()<0.1)ent.kill();
		}
	}
};


