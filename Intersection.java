package pa1;

import pa1.utility.Point;
import pa1.utility.Vector;

public class Intersection {
	public Point location;
	public Vector normal;
	public String obj;
	
	public Intersection(Point l,Vector n,String ob){
		this.location = new Point(l);
		this.normal = new Vector(n);
		obj=ob;
	}
}
