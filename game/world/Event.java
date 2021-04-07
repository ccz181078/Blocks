package game.world;

import java.util.*;
import java.io.*;

public abstract class Event implements Serializable{
	private static final long serialVersionUID=1844677L;
	public final void add(){
		add(0);
	}
	public final void add(int delay){
		World.cur.addEvent(this,delay);
	}
	public final void addAfterMove(){
		World.cur.addMoveEvent(this);
	}
	public abstract void run();
}