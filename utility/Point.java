package pa1.utility;

public class Point {
	
	public double x, y,z;
	
	public Point(double x, double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Point(Point p){
		this.x=p.x;
		this.y=p.y;
		this.z=p.z;
	}
	
	public Point add(Point p){
		return new Point(x+p.x,y+p.y,z+p.z);
	}
	
	public Point sub(Point p){
		return new Point(x-p.x,y-p.y,z-p.z);
	}
	
	 public double dot(Vector v){
		 return x*v.x + y*v.y + z*v.z;
	 }
	 
	 public double dot(Point p){
		 return x*p.x + y*p.y + z*p.z;
	 }
}
