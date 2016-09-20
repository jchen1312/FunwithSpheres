package pa1;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Random;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import pa1.objects.Plane;
import pa1.objects.Sphere;
import pa1.utility.Color;
import pa1.utility.Point;
import pa1.utility.Ray;
import pa1.utility.Vector;

import javax.swing.JFrame;

public class Part3 extends JFrame implements GLEventListener {
	final static String name = "Jia";

	final int width = 512;
	final int height = 512;
	Buffer scene;

	// scene set up
	Sphere s1 = new Sphere(new Point(-4, 0, -7), 1,  new Color(0.2F, 0.0F, 0.0F),
			new Color(1.0F, 0.0F, 0.0F), new Color(0F, 0F, 0F), 0);
	Sphere s2 = new Sphere(new Point(0, 0, -7), 2,  new Color(0.0F, 0.2F, 0.0F),
			new Color(0.0F, 0.5F, 0.0F), new Color(0.5F, 0.5F, 0.5F), 32);
	Sphere s3 = new Sphere(new Point(4, 0, -7), 1,  new Color(0.0F, 0.0F, 0.2F),
			new Color(0.0F, 0.0F, 1.0F), new Color(0F, 0F, 0F), 0);
	Plane p = new Plane(new Color(0.2F, 0.2F, 0.2F), new Color(1.0F, 1.0F, 1.0F), new Color(0.0F, 0.0F, 0.0F), 0);
	Camera camera = new Camera();
	Light light = new Light();
	Ray ray;

	public Part3() {
		super(name + "'s Raytracer");
		this.scene = renderScene();

		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(profile);

		GLCanvas canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);

		this.setName(name + "'s Raytracer");
		this.getContentPane().add(canvas);

		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		canvas.requestFocusInWindow();
	}
	
	Double s1t, s2t, s3t, pt;//t value of intersection
	Point s1p,s2p,s3p,pp;//point of intersection
	float[] data = new float[width * height * 3];
	int s1l,s2l,s3l,pl;
	public Buffer renderScene() {
		long start=System.nanoTime();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int i = (y * height) + x;
				i *= 3;
				Color color = new Color(0.0F, 0.0F, 0.0F);
				for (int dx = 0; dx < 8; dx++) {// 8 steps
					for (int dy = 0; dy < 8; dy++) {// 8 steps
						// viewing ray computation; randomization in camera.calcualte
						ray = new Ray(camera.e,calculate(x, y));

						// intersection computation
						s1t = s1.findIntersection(ray);
						s2t = s2.findIntersection(ray);
						s3t = s3.findIntersection(ray);
						pt = p.findIntersection(ray);

						s1p = intersectPoint(s1t);
						s2p = intersectPoint(s2t);
						s3p = intersectPoint(s3t);
						pp = intersectPoint(pt);
						
						//suming up color
						color.add(colorScene(i, dx, dy, color));
						//System.out.println("before "+color.g);

					}
				}
				color.divide(64);
				data[i + 0] = color.r; // red
				data[i + 1] = color.g; // green
				data[i + 2] = color.b; // blue	
			}
		}
		long end=System.nanoTime();
		System.out.println("Loop Time: "+(end-start)/1000000000.F);
		return FloatBuffer.wrap(data);
	}
	public Color colorScene(int i, int x, int y, Color color) {
		boolean shade = false;// true if ray blocked
		if (s2t != -1) {
			shade = shadeRay(s2p);
			s2.shade(ray, light, x, y, s2p,shade);

			return s2.color;
			//color.add(s2.color);
		} else if (s3t != -1) {
			shade = shadeRay(s3p);
			s3.shade(ray, light,x,y,s3p,shade);

			return s3.color;

			//color.add(s3.color);
		} else if (s1t != -1) {
			shade = shadeRay(s1p);
			s1.shade(ray, light,x,y,s1p,shade);

			return s1.color;

			//color.add(s1.color);
		} else if (pt != -1) {
			shade = planeShade(x, y, pp);
			p.shade(ray,light,x,y,pp,shade);

			return p.color;

			//color.add(p.color);
		} else {
			return new Color(0.0F, 0.0F, 0.0F);

			//color.add(new Color(0.0F, 0.0F, 0.0F));
		}
		//return color;
	}
	
	public Vector calculate(int x, int y){
		Random random = new Random();
		double vx=-0.1+((0.1-(-0.1))*(x-0.5+random.nextFloat())/512.0);
		double vy=-0.1+((0.1-(-0.1))*(y-0.5+random.nextFloat())/512.0);
		
		return new Vector(vx, vy, -0.1);
		
	}
	public Point intersectPoint(Double t){
				Vector aidVector = new Vector();
				aidVector= aidVector.getScaledVector(ray.direction,t);//t*d
				return new Point(ray.position.x+aidVector.x, ray.position.y+aidVector.y, ray.position.z+aidVector.z);//p+t*d
	}
	
	//returns true if shadeRay is blocked
	public boolean shadeRay(Point p){
		Vector shadowDirection=new Vector();
		shadowDirection=shadowDirection.sub(light.origin, p);
		shadowDirection=shadowDirection.normalize();
		
		//shadeRay=(point,light.pos-point)
		Ray shadeRay= new Ray(p,shadowDirection);
		
		if(this.p.findIntersection(shadeRay)!=-1||s1.findIntersection(shadeRay)!=-1||s2.findIntersection(shadeRay)!=-1||s3.findIntersection(shadeRay)!=-1){
		//if(s1.findIntersection(shadeRay)!=-1||s2.findIntersection(shadeRay)!=-1||s3.findIntersection(shadeRay)!=-1){
		return true;//found  intersection
		}else{
			return false;//found no intersection
		}
	}	
	
	//returns true if shadeRay is blocked
	public boolean planeShade(int x, int y,Point point){
		Vector shadowDirection=new Vector();
		shadowDirection=shadowDirection.sub(light.origin, point);
		shadowDirection=shadowDirection.normalize();
		
		//shadeRay=(point,light.pos-point)
		Ray shadeRay= new Ray(point,shadowDirection);
		
		if(s1.findIntersection(shadeRay)!=-1||s2.findIntersection(shadeRay)!=-1||s3.findIntersection(shadeRay)!=-1){
			return true;//found  intersection
		}else{
			return false;//found no intersection
		}
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glDrawPixels(width, height, GL2.GL_RGB, GL2.GL_FLOAT, this.scene);

		gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}

	@Override
	public void init(GLAutoDrawable arg0) {

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {

	}

	public static void main(String[] args) {
		Part3 game = new Part3();
	}
}