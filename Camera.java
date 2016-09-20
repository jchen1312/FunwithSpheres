package pa1;

import pa1.utility.Point;
import pa1.utility.Vector;

public class Camera {
	//e=campos
	//-w=camdir
	//u=camright
	//v=camup
	Vector  u, v, w;
	double l, r, b, t, d;
	Vector camdir;//direction
	Point e;//position
	public Camera() {
		e = new Point(0, 0, 0);
		u = new Vector(1, 0, 0);
		v = new Vector(0, 1, 0);
		w = new Vector(0, 0, 1);
		l=-0.1;
		r=0.1;
		b=-0.1;
		t=0.1;
		d=0.1;
		camdir=new Vector(0,0,-1);

	}

	public Vector  calculate(int x, int y) {
		double vx, vy;
		//vx = ((x / 512) * 0.2) + (-0.1);
		//vy = ((y / 512) * 0.2 + (-0.1));

		vx=l+((r-l)*(x+0.5)/512.0);//left + ((right - left)*(x+0.5)/512.0) 
		vy=b+((t-b)*(y+0.5)/512.0);//bottom + ((top - bottom)*(x+0.5)/512.0)
		
		return new Vector(vx, vy, -d);
	}
}
