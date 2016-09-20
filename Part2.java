package pa1;

import java.nio.Buffer;
import java.nio.FloatBuffer;

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

public class Part2 extends JFrame implements GLEventListener {
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

	public Part2() {
		super("Jia Chen Part 1");
		this.scene = renderScene();

		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(profile);

		GLCanvas canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);

		this.setName("Jia Chen Part 1 ");
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

	public Buffer renderScene() {
		long start=System.nanoTime();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// viewing ray & intersection computation
				ray = new Ray(camera.e, camera.calculate(x, y));
				s1t = s1.findIntersection(ray);
				s2t = s2.findIntersection(ray);
				s3t = s3.findIntersection(ray);
				pt = p.findIntersection(ray);
				
				s1p=intersectPoint(s1t);
				s2p=intersectPoint(s2t);
				s3p=intersectPoint(s3t);
				pp=intersectPoint(pt);
				
				int i = (y * height) + x;
				i *= 3;

				p2Color(i,x,y);
				
			}
		}
		long end=System.nanoTime();
		System.out.println("Loop Time: "+(end-start)/1000000000.F);
		return FloatBuffer.wrap(data);
	}
	
	public Point intersectPoint(Double t){
				Vector aidVector = new Vector();
				aidVector= aidVector.getScaledVector(ray.direction,t);//t*d
				return new Point(ray.position.x+aidVector.x, ray.position.y+aidVector.y, ray.position.z+aidVector.z);//p+t*d
	}
	
	// pixel set up for part 2
	public void p2Color(int i,int x, int y) {
		boolean shade=false;//true if ray blocked
		if (s2t != -1) {
			shade=shadeRay(s2p);
			s2.shade(ray, light, x, y, s2p,shade);
			data[i + 0] = s2.color.r; // red
			data[i + 1] = s2.color.g; // green
			data[i + 2] = s2.color.b; // blue

		} else if (s3t != -1) {
			shade=shadeRay(s3p);
			s3.shade(ray, light,x,y,s3p,shade);
			data[i + 0] = s3.color.r; // red
			data[i + 1] = s3.color.g; // green
			data[i + 2] = s3.color.b; // blue

		} else if (s1t != -1) {
			shade=shadeRay(s1p);
			s1.shade(ray, light,x,y,s1p,shade);
			data[i + 0] = s1.color.r; // red
			data[i + 1] = s1.color.g; // green
			data[i + 2] = s1.color.b; // blue
		}  else if (pt != -1) {
			shade=planeShade(x,y,pp);
			p.shade(ray,light,x,y,pp,shade);
			data[i + 0] = p.color.r; // red
			data[i + 1] = p.color.g; // green
			data[i + 2] = p.color.b; // blue
		} else {
			data[i + 0] = 0.0f; // red
			data[i + 1] = 0.0f; // green
			data[i + 2] = 0.0f; // blue
		}
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
		Part2 game = new Part2();
	}
}