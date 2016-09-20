package pa1.utility;

public class Ray {
	
	public Point position;//aka origin
	public Vector direction;
	
	public Ray(Point p, Vector d) {
		this.position= new Point(p);
		this.direction=new Vector(d);
	}
	
	
}
