package met.freehij.kareliq.injection;

public class ByteClassLoader extends ClassLoader{
	private final byte[] bytes;
	public ByteClassLoader(byte[] bytes) {
		super();
		this.bytes = bytes;
	}
	
	@Override
	public Class<?> findClass(String name) {
		return this.defineClass(name, bytes, 0, bytes.length);
	}

}
