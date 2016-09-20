package pa1.objects;

import pa1.Light;
import pa1.utility.Color;
import pa1.utility.Point;
import pa1.utility.Ray;
import pa1.utility.Vector;

public class Plane {
	
	public Vector normal;
	public Color color;
	public Point point;
	 public Color kd,ka,ks;
	 int specular;
	public Plane(Point p, Vector normal){
		this.normal=new Vector(normal);//1,0,0
		this.color=new Color(color);
		this.point=new Point(p);
		
	}
	
	public Plane(Color ka, Color kd, Color ks, int p){
		this.normal=new Vector(0,2,0);//or 0,1,0
		this.color=new Color(1.0F,1.0F,1.0F);
		this.point=new Point(0,-2,0);
		 this.ka=new Color(ka);
		 this.kd=new Color(kd);
		 this.ks=new Color(ks);
		 specular = p;
	}
	
	public double findIntersection(Ray ray){//hit
		/*Vector rayDirection = ray.direction;
		double a = rayDirection.dot(normal);
		double t=distance.sub(ray.position).dot(normal)/a;*/
		
		//py+tdy=y=-2
		//t=-2-py/dy
		//debug by mu
		double t=(-2-ray.position.y)/ray.direction.y;
		if(t>10E-9){
			return t;
		}else{//ray parallel to plane, no intersection;
			return -1;
		}
		
	}
	
	public void shade(Ray ray, Light light,int x, int y,Point pt,Boolean shade) {
		//System.out.println("Plane light");

		if(shade==false){
			
		// ---------point of intersection on sphere----------
		//Point p = new Point(ray.direction.x, ray.direction.y, ray.direction.z);
		Point p = new Point(pt.x, -2, pt.z);

		// ---------eye direction v = -normalize(ray direction)-------
		Vector v = new Vector(ray.direction.sub(ray.position));
		v.normalize().negative();

		//--------------- light direction = normalize(light.pos-point)----------
		Vector l = new Vector(light.origin.x - p.x, light.origin.y - p.y, light.origin.z - p.z);
		l.normalize();
		//System.out.println("Plane light"+l.x);

		//---------normal vector--------------
		normal.normalize();
		
		//------h bisector =v+l/magnitude of(v+1)----------
		Vector h=new Vector();
		h=v.add(l).normalize();
		
		//----for lambertian----------
		double lx = Math.max(0, normal.dot(l));

		//----------phong--------
		 double lp=Math.pow(Math.max(0, normal.dot(h)), specular);

		color.r = (float) ((float) (ka.r*light.color.r)+(kd.r * light.color.r * lx)+(ks.r*light.color.r*lp));
		color.g = (float) ((float) (ka.g*light.color.g)+(kd.g * light.color.g * lx)+(ks.g*light.color.g*lp));
		color.b = (float) ((float) (ka.b*light.color.b)+(kd.b * light.color.b * lx)+(ks.b*light.color.b*lp));
		
		}else{
			color.r = (float) (ka.r*light.color.r);
			color.g = (float) (ka.g*light.color.g);
			color.b = (float) (ka.b*light.color.b);
		}
		color.gammaCorrect();

	}
}
