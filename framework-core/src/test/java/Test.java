import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;


public class Test {
	public static void main(String[] args){
		MultiValueMap map=new MultiValueMap();	
		map.put("A", "11");
		map.put("A", "2");
		map.put("A", "11");
		map.put("S", "cc");
		List<String> a=(List<String>)map.get("A");
		System.out.println(a);
		System.out.println(map.get("S"));
	}
	
}
