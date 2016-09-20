package pa1;

import pa1.utility.Color;
import pa1.utility.Point;

public class Light {
	public Point origin;
	public Color color;
	
	public Light(){
		origin = new Point(-4,4,-3);
		color= new Color(1.0F,1.0F,1.0F);
	}
}
