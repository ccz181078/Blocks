package debug;
import java.lang.reflect.*;
import util.BmpRes;
import java.util.ArrayList;

public class ObjectInfo{
	private static final long serialVersionUID=1844677L;
	//将对象转为字符串
	public static String obj2str(Object obj){
		StringBuffer sb=new StringBuffer();
		Class c=obj.getClass();
		while(c!=Object.class){
			if(c!=obj.getClass())sb.append("extends ");
			sb.append(c.getName());
			sb.append('\n');
			for(Field f:c.getDeclaredFields()){
				try{
					f.setAccessible(true);
					if(Modifier.isStatic(f.getModifiers()))continue;
					Object o=f.get(obj);
					if(o instanceof BmpRes)continue;
					sb.append(f.getName());
					sb.append('=');
					try{
						sb.append("["+o+"]");
					}catch(Exception e){}
					sb.append("   ");
				}catch(Exception e){}
			}
			c=c.getSuperclass();
		}
		return sb.toString();
	}
	public static String obj2str_deep(Object obj){
		StringBuffer sb=new StringBuffer();
		obj2str_deep(sb,obj,0);
		return sb.toString();
	}
	/*public static Class[] tps={
		byte.class,
		short.class,
		int.class,
		long.class,
		float.class,
		double.class,
		boolean.class,
		char.class,
	};*/
	private static void indent(StringBuffer sb,int depth){
		for(int i=0;i<depth*2;++i)sb.append(' ');
	}
	private static void obj2str_deep(StringBuffer sb,Object obj,int depth){
		if(obj==null){
			sb.append("null");
			return;
		}
		if(depth>16){
			sb.append("...");
			return;
		}
		sb.append("{\n");
		Class c=obj.getClass();
		if(c.getComponentType()!=null){
			int n=Array.getLength(obj);
			for(int i=0;i<n;++i){
				indent(sb,depth);
				sb.append("["+i+"]=");
				obj2str_deep(sb,Array.get(obj,i),depth+1);
				sb.append('\n');
			}
		}else while(c!=Object.class){
			indent(sb,depth);
			if(c!=obj.getClass())sb.append("extends ");
			sb.append(c.getSimpleName());
			sb.append('\n');
			for(Field f:c.getDeclaredFields()){
				try{
					f.setAccessible(true);
					if(Modifier.isStatic(f.getModifiers()))continue;
					Object o=f.get(obj);
					indent(sb,depth);
					Class tp=f.getType();
					if(!Modifier.isFinal(tp.getModifiers())&&obj!=null)tp=obj.getClass();
					sb.append(tp.getSimpleName()+" "+f.getName()+"=");
					if(Modifier.isAbstract(tp.getModifiers()))sb.append(obj+"");
					else obj2str_deep(sb,o,depth+1);
					sb.append('\n');
				}catch(Exception e){}
			}
			c=c.getSuperclass();
		}
		indent(sb,depth);
		sb.append("}");
	}
}

