package debug;

import android.app.AlertDialog;
import com.ccz.blocks.MainActivity;
import android.widget.ListView;
import android.content.DialogInterface;
import java.util.*;
import java.lang.reflect.*;

public class ObjectNode{
	final Object obj;
	final Class tp;
	ObjectNode childs[],fa;
	String items[];
	String getTitle(){
		return tp.getSimpleName();
	}
	public ObjectNode(Object o,Class c){
		obj=o;
		tp=c;
	}
	void init(){
		ArrayList<String>is=new ArrayList<>();
		ArrayList<ObjectNode>ls=new ArrayList<>();
		Class c=tp;
		if(obj==null);
		else if(c.getComponentType()!=null){
			int n=Array.getLength(obj);
			for(int i=0;i<n;++i)if(i<100||i>=n-100){
				Class t=c.getComponentType();
				Object val=Array.get(obj,i);
				if(!Modifier.isFinal(t.getModifiers())&&val!=null)t=val.getClass();
				String s="["+i+"]:"+t.getSimpleName()+"="+val;
				is.add(s);
				ls.add(new ObjectNode(val,t));
			}
		}else{
			for(Field f:c.getDeclaredFields()){
				try{
					f.setAccessible(true);
					if(Modifier.isStatic(f.getModifiers()))continue;
					Object val=f.get(obj);
					Class t=f.getType();
					if(!Modifier.isFinal(t.getModifiers())&&val!=null)t=val.getClass();
					String s=f.getName()+":"+t.getSimpleName()+"="+val;
					is.add(s);
					ls.add(new ObjectNode(val,t));
				}catch(Exception e){}
			}
			c=c.getSuperclass();
			if(c!=null){
				is.add("super="+c.getSimpleName());
				ls.add(new ObjectNode(obj,c));
			}
		}
		items=new String[is.size()];
		childs=new ObjectNode[ls.size()];
		is.toArray(items);
		ls.toArray(childs);
		for(ObjectNode o:childs)o.fa=this;
	}
	public void show(){
		init();
		new AlertDialog.Builder(MainActivity._this)
		.setTitle(getTitle())
		.setItems(items,
		new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			childs[w].show();
		}})
		.setPositiveButton("close",null)
		.setNegativeButton("back",new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			if(fa!=null)fa.show();
		}})
		.setNeutralButton("reflesh",new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			ObjectNode.this.show();
		}})
		.show();
	}
}
