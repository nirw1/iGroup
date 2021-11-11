package iob.attributes;

import java.util.HashMap;

public class Attributes extends HashMap<String, Object>{
	private static final long serialVersionUID = 1L;

	public Object getAttribute(String name) {
		return this.get(name);
	}
}
