package pa1.objects;

import pa1.Light;
import pa1.utility.Color;
import pa1.utility.Point;
import pa1.utility.Ray;
import pa1.utility.Vector;

public class Sphere {
	public Point center;
	public double radius;
	public Color color;
	public Color kd, ka, ks;
	public int specular;

	public Sphere(Point c, double r, Color ka, Color kd, Color ks, int p) {
		this.center = new Point(c);
		this.radius = r;
		this.ka = new Color(ka);
		this.kd = new Color(kd);
		this.ks = new Color(ks);
		color=new Color(0.0F,0.0F,0.0F);
		specular = p;

	}

	public double findIntersection(Ray ray) {
		double a = ray.direction.dot(ray.direction);
		double b = 2 * ray.position.sub(center).dot(ray.direction);
		double c = ray.position.sub(center).dot(ray.position.sub(center)) - radius * radius;
		double discriminant = b * b - 4 * a * c;

		if (discriminant < 0.0) {
			return -1;
		} else {
			double t = (-b - Math.sqrt(discriminant)) / (2 * a);
			double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);
			if(t>t2){
				t=t2;
			}
			if (t > 10E-9) {
				return t;
			} else {
				return -1;
			}
		}
	}

	public void shade(Ray ray, Light light, int x, int y, Point p, Boolean shade) {
		if (shade == false) {
			//------eye direction v = -normalize(ray direction)--------
			//Vector v = new Vector(p.x-ray.position.x, p.y-ray.position.y,p.z-ray.position.z);
			//v.normalize();

			Vector v = new Vector(ray.direction.sub(ray.position));
			v.normalize().negative();

			//------light direction = normalize(light.pos-point)-----------
			Vector l = new Vector(light.origin.x - p.x, light.origin.y - p.y, light.origin.z - p.z);
			//Vector l = new Vector(p.x-light.origin.x , p.y-light.origin.y, p.z-light.origin.z);
			l.normalize();
			// System.out.println("Sphere light"+l.x);

			//-------normal vector-----------
			Vector vNormal = new Vector(p.x - center.x, p.y - center.y, p.z - center.z);
			vNormal.normalize();
			// System.out.println(vNormal.x+" "+vNormal.y);

			//------h bisector =v+l/magnitude of(v+1)----------
			Vector h=new Vector();
			h = v.add(l.negative()).normalize();
			h.negative();
			l.negative();
			//System.out.println("h "+h.x+"y "+h.y+"z "+h.z);

			//----for lambertian----------
			double lx = Math.max(0, vNormal.dot(l));

			//----------phong=max(0,n*h)to the power p--------
			 double lp=Math.pow(Math.max(0, vNormal.dot(h)), specular);
			 Float la=(float) (ks.g*light.color.g*lp);
			//System.out.println("lp "+lp+" la: "+la);
			
			color.r = (float) ((float) (ka.r*light.color.r)+(kd.r * light.color.r * lx)+(ks.r*light.color.r*lp));
			color.g = (float) ((float) (ka.g*light.color.g)+(kd.g * light.color.g * lx)+(ks.g*light.color.g*lp));
			color.b = (float) ((float) (ka.b*light.color.b)+(kd.b * light.color.b * lx)+(ks.b*light.color.b*lp));
			
		} else {
			color.r = (float) (ka.r*light.color.r);
			color.g = (float) (ka.g*light.color.g);
			color.b = (float) (ka.b*light.color.b);
		}
		color.gammaCorrect();

	}
}
