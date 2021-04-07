package util;

import java.io.*;

public class UncheckedObjectInputStream extends ObjectInputStream{
	public UncheckedObjectInputStream(InputStream in) throws IOException {
		super(in);
	}
	public UncheckedObjectInputStream() throws SecurityException, IOException{
		super();
	}
	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException{
		ObjectStreamClass objInputStream = super.readClassDescriptor();
		Class localClass = Class.forName(objInputStream.getName());
		ObjectStreamClass localInputStream = ObjectStreamClass.lookup(localClass);
		if(localInputStream != null){
			final long localUID = localInputStream.getSerialVersionUID();
			final long objUID = objInputStream.getSerialVersionUID();
			if(localUID != objUID){
				return localInputStream;
			}
		}
		return objInputStream;
	}
}