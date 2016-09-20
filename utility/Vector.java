package pa1.utility;

public class Vector {
	
  public double x,y,z;
  
 public Vector(double x, double y,double z){
	 this.x=x;
	 this.y=y;
	 this.z=z;
 }
 
 public Vector(Vector v){
	 this.x=v.x;
	 this.y=v.y;
	 this.z=v.z;
 }
 
 public Vector(){
	 this.x=0;
	 this.y=0;
	 this.z=0;
 }
 
 public Vector add(Vector v){
	 return new Vector(x+v.x,y+v.y,z+v.z);
 }
 
 public Vector sub(Vector v){
	 return new Vector(x-v.x,y-v.y,z-v.z);
 }
 
 public Vector sub(Point p){
	 return new Vector(x-p.x,y-p.y,z-p.z);
 }
 public Vector sub(Point p1, Point p2){
	 return new Vector(p1.x-p2.x, p1.y-p2.y, p1.z-p2.z);
 }
 public double dot(Vector v){
	 return x*v.x + y*v.y + z*v.z;
 }
 
 public double dot(Point p){
	 return x*p.x + y*p.y + z*p.z;
 }

 public Vector cross(Vector v){
	 return new Vector((y*v.z)-(z*v.y),(z*v.x)-(x*v.z),(x*v.y)-(y*v.x));
 }
 
 public Vector multi(double b){
	 return new Vector(x*b,y*b,z*b);
 }
 
 public Vector negative(){
	 x=-x;
	 y=-y;
	 z=-z;
	 return new Vector(x,y,z);
 }
 
 public double magnitude(){
	 return Math.sqrt(x*x + y*y + z*z);
 }
 
 public Vector normalize(){
	double magnitude = magnitude();
	return new Vector(x/=magnitude,y/=magnitude,z/=magnitude);	 
 }
 
 public Vector getScaledVector(Vector unitVector, double scale) {
	 	
	    return unitVector.multi(scale);
}

}
