package pa1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

public class Part1 extends JFrame implements GLEventListener {

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

	public Part1() {
		super("Jia Chen Part 1");
		this.scene = renderScene();

		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(profile);

		GLCanvas canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);

		this.setName("Jia Chen Part 1");
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
				//for()
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

				p1Color(i);
				
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
	
	// pixel setup for part 1
	public void p1Color(int i) {

		if (s1t != -1) {
			data[i + 0] = 1.0f; // red
			data[i + 1] = 1.0f; // green
			data[i + 2] = 1.0f; // blue
		} else if (s2t != -1) {
			data[i + 0] = 1.0f; // red
			data[i + 1] = 1.0f; // green
			data[i + 2] = 1.0f; // blue

		} else if (s3t != -1) {
			data[i + 0] = 1.0f; // red
			data[i + 1] = 1.0f; // green
			data[i + 2] = 1.0f; // blue

		} else if (pt != -1) {
			data[i + 0] = 1.0f; // red
			data[i + 1] = 1.0f; // green
			data[i + 2] = 1.0f; // blue
		} else {
			data[i + 0] = 0.0f; // red
			data[i + 1] = 0.0f; // green
			data[i + 2] = 0.0f; // blue
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
		Part1 game = new Part1();
	}
}