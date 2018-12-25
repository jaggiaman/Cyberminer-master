package KWIC;
public class Line {
	private int id;
	private String descriptor;
	private String url;
	
	public Line(String descriptor, String url) {
		this.descriptor = descriptor;
		this.url = url;
	}
	
	public Line(int id, String descriptor, String url) {
		this.id = id;
		this.descriptor = descriptor;
		this.url = url;
	}
	
	public int getId( ) {
		return id;
	}
	
	public String getDescriptor( ) {
		return descriptor;
	}
	
	public String getUrl( ) {
		return url;
	}
}