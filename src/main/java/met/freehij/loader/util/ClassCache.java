package met.freehij.loader.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ClassCache {
	
	static class MethodSignature{
		public final String name;
		public final Class<?>[] clz;
		public final int hashCode;
		public MethodSignature(String name, Class<?>[] clz) {
			this.name = name;
			this.clz = clz;
			int hc = name.hashCode();
			for(Class<?> c : clz) hc += c.hashCode();
			this.hashCode = hc;
		}
		
		
		@Override
		public int hashCode() {
			return this.hashCode;
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof MethodSignature)) return false;
			MethodSignature me = (MethodSignature) o;
			if(!me.name.equals(this.name)) return false;
			if(me.clz.length != this.clz.length) return false;
			for(int i = 0; i < me.clz.length; ++i) {
				if(me.clz[i] != this.clz[i]) return false;
			}
			return true;
		}
	}
	
    private static final HashMap<Class<?>, ClassCache> classes = new HashMap<>();
    public static ClassCache get(Class<?> clz) {
    	if(classes.containsKey(clz)) {
    		return classes.get(clz);
    	}
    	ClassCache r = new ClassCache(clz);
    	classes.put(clz, r);
    	return r;
    }
	
	public final Class<?> clz;
	private final HashMap<String, Field> fname2field = new HashMap<>();
    private final HashMap<MethodSignature, Method> mname2field = new HashMap<>();
    
	public ClassCache(Class<?> clz) {
		this.clz = clz;
	}
	
	public Field field(String f) {
		return this.fname2field.get(f);
	}
	
	public Method method(String m, Class<?>[] args) {
		return this.mname2field.get(new MethodSignature(m, args));
	}
	
	public void cache(String name, Field f) {
		fname2field.put(name, f);
	}
	public void cache(String name, Method f, Class<?>[] args) {
		mname2field.put(new MethodSignature(name, args), f);
	}
}
