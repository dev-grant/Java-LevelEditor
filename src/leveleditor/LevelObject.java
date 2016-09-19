package leveleditor;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class LevelObject {
	private Shape slickShape;
	private GGColor shapeColor;
	private int objectType;
	private float angle;
	private Vector2f initialSize;
	
	public static final int CIRCLE_SHAPE = 0;
	public static final int RECTANGLE_SHAPE = 1;
	public static final int POLYGON_SHAPE = 2;
	
	public LevelObject(Shape s){
		slickShape = s;
		initialSize = new Vector2f(s.getWidth(), s.getHeight());
		if(s instanceof Circle){
			objectType = CIRCLE_SHAPE;
		}else if(s instanceof Rectangle){
			objectType = RECTANGLE_SHAPE;
		}else if(s instanceof Polygon){
			objectType = POLYGON_SHAPE;
		}else{
			objectType = -1;
		}
		
		shapeColor = new GGColor(new java.awt.Color(51,255,102));
	}
	
	public Vector2f getInitialSize(){
		return initialSize;
	}
	
	public void setAngle(float a){
		angle = a;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public int getObjectType(){
		return objectType;
	}
	
	public Shape getSlickShape() {
		return slickShape;
	}
	public void setSlickShape(Shape slickShape) {
		this.slickShape = slickShape;
	}
	public GGColor getShapeColor() {
		return shapeColor;
	}
	public void setShapeColor(GGColor shapeColor) {
		this.shapeColor = shapeColor;
	}
}
