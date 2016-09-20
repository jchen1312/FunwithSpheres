package pa1.utility;

public class Color {
	
	public float r,g,b;
	
	public Color(){
		r=0.0F;
		g=0.0F;
		b=0.0F;
	}
	
	public Color(Color c){
		this.r=c.r;
		this.g=c.g;
		this.b=c.b;

	}
	
	public Color(float r, float g, float b){
		this.r=r;
		this.g=g;
		this.b=b;

	}

	public void add(Color color){
		r+=color.r;
		b+=color.b;
		g+=color.g;
		
	}
	
	public void divide(int scalar){
		r/=scalar;
		g/=scalar;
		b/=scalar;
	}

	public void gammaCorrect(){
		float inverseG = (float) (1.0/2.2);
		this.r=(float) Math.pow(r, inverseG);
		this.g = (float)Math.pow(g, inverseG);
		this.b = (float)Math.pow(b, inverseG);
	}

	/**
	 * This function returns an int which represents this color.  The
	 * standard RGB style bit packing is used and is compatible with
	 * java.awt.BufferedImage.TYPE_INT_RGB. (ie - the low 8 bits, 0-7
	 * are for the blue channel, the next 8 are for the green channel,
	 * and the next 8 are for the red channel).  The highest 8 bits
	 * are left untouched.
	 * @return An integer representing this color.
	 */
	public int toInt() {
		
		int iR, iG, iB;
		
		//Here we do the dumb thing and simply clamp then scale.
		//The "+ 0.5" is to achieve a "round to nearest" effect
		//since Java float to int casting simply truncates.
		iR = (int) (255.0 * Math.max(Math.min(r, 1.0), 0.0) + 0.5);
		iG = (int) (255.0 * Math.max(Math.min(g, 1.0), 0.0) + 0.5);
		iB = (int) (255.0 * Math.max(Math.min(b, 1.0), 0.0) + 0.5);
		
		//Bit packing at its finest
		return (iR << 16) | (iG << 8) | (iB << 0);
	}
	
		
}
