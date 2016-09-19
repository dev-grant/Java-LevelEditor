package leveleditor;


public class GGColor {
	
	private java.awt.Color awtColor;
	private org.newdawn.slick.Color slickColor;
	
	public GGColor(java.awt.Color c){
		this(c.getRed(), c.getGreen(), c.getBlue());
	}
	
	public GGColor(org.newdawn.slick.Color c){
		this(c.getRed(), c.getGreen(), c.getBlue());
	}
	
	public GGColor(int r, int g, int b){
		awtColor = new java.awt.Color(r,g,b);
		slickColor = new org.newdawn.slick.Color(r,g,b);
	}

	public java.awt.Color getAWTColor(){
		return awtColor;
	}
	
	public org.newdawn.slick.Color getSlickColor(){
		return slickColor;
	}
}
