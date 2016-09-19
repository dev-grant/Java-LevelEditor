package leveleditor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import xmlparsing.LevelParser;

import com.rits.cloning.Cloner;

public class Main extends BasicGame implements GameCallBack{
	
	private ArrayList<LevelObject>shapes;
	private static CanvasGameContainer gameContainer;
	
	private LevelObject shapeToDraw;
	
	private Cloner objCloner;
	
	private Color greenColor;
	
	private Color selectedColor;

	private static MainInterface ui;
	
	private ArrayList<LevelObject>selectedShapes;
	
	private int rotateKeyPressed;
	
	@SuppressWarnings("unused")
	private InterfaceCallBack delegate;
	
	private Vector2f rMousePanPoint;
	private Point lMouseSelPoint;
	
	@SuppressWarnings("unused")
	private Point mouseDifference;
	
	private Vector2f camera;
	
	private boolean polygonMode;
	
	private ArrayList<Point> polygonPoints;
	
	private GameContainer mainContainer;
	
	private Rectangle marqueeSelection;
	@SuppressWarnings("unused")
	private Shape mainSelectedShape;
	
	private ArrayList<Point> mouseDifferences;
	
	private float scaleFactor;
	
	private Vector2f cameraMouseDifference;
	
	private Rectangle iphoneScreenSize;
	
	public void setInterfaceCallBack(InterfaceCallBack i){
		delegate = i;
	}
	
	public Main(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		container.setAlwaysRender(true);
		container.setShowFPS(false);
		container.getGraphics().setBackground(Color.white);
		
		mainContainer = container;

		shapes = new ArrayList<LevelObject>();
		objCloner = new Cloner();
		
		greenColor = new Color(51,255,102);
		
		selectedColor = greenColor;
		
		rotateKeyPressed = -1;
		
		rMousePanPoint = null;
		lMouseSelPoint = null;
		camera = new Vector2f(0,0);
		
		polygonMode = false;
		polygonPoints = new ArrayList<Point>();
		
		selectedShapes = new ArrayList<LevelObject>();
		
		mouseDifferences = new ArrayList<Point>();
		
		iphoneScreenSize = new Rectangle(0, 0, 480, 320);
		
		scaleFactor = 1.0f;
	}

	@Override
	public void render(GameContainer container, Graphics graphic) throws SlickException {
		// TODO Auto-generated method stub

		
		//graphic.translate(camera.x - .x, camera.y - cameraMouseDifference.y);
		
		graphic.translate(camera.x, camera.y);
		graphic.scale(scaleFactor, scaleFactor);
		container.getInput().setScale(1.0f/scaleFactor, 1.0f/scaleFactor);
		container.getInput().setOffset(-camera.x/scaleFactor, -camera.y/scaleFactor);
		
		if(rMousePanPoint != null){
			camera.x = rMousePanPoint.x*scaleFactor - cameraMouseDifference.x*scaleFactor;
			camera.y = rMousePanPoint.y*scaleFactor - cameraMouseDifference.y*scaleFactor;
		}
		
		graphic.setColor(Color.white);
		graphic.draw(iphoneScreenSize);
		
		for(LevelObject s : shapes){
			graphic.setColor(s.getShapeColor().getSlickColor());
			graphic.fill(s.getSlickShape());
		}
		
		if(shapeToDraw != null && ui.getTabbedPane().getSelectedIndex() == 0 && !polygonMode){
			shapeToDraw.getSlickShape().setCenterX(container.getInput().getMouseX());
			shapeToDraw.getSlickShape().setCenterY(container.getInput().getMouseY());
			
			graphic.setColor(new Color(255, 255, 255, 100));
			graphic.fill(shapeToDraw.getSlickShape());
		}
		
		/*
		if(selectedShapes.size() > 0){
			for(GGShape selectedShape : selectedShapes){
				if(selectedShape != null && isSelectionTabOpen()){

				}
			}
		}
		*/
		
		if(selectedShapes.size() > 0){
			for(LevelObject selectedShape : selectedShapes){
				if(selectedShape != null && ui.getTabbedPane().getSelectedIndex() == 1){
					graphic.setLineWidth(2);
					graphic.setColor(new Color(255,255,255));
					graphic.draw(selectedShape.getSlickShape());
					graphic.setLineWidth(1);
				}
			}
		}
		
		if(polygonMode && isGameTabOpen()){
			graphic.setColor(Color.white);
			int mouseX = container.getInput().getMouseX();
			int mouseY = container.getInput().getMouseY();
			
			graphic.drawLine(mouseX - 10, mouseY, mouseX + 10, mouseY);
			graphic.drawLine(mouseX, mouseY - 10, mouseX, mouseY + 10);
			
			if(polygonPoints.size() > 0){
				for(Point p : polygonPoints){
					graphic.fillOval(p.x, p.y, 3, 3);
				}
			}
			
			if(polygonPoints.size() > 1){
				for(int i = 1; i < polygonPoints.size(); i++){
					graphic.drawLine(polygonPoints.get(i).x, polygonPoints.get(i).y, polygonPoints.get(i-1).x, polygonPoints.get(i-1).y);
				}
				
				if(pointsShouldSnap(polygonPoints.get(0), new Point(container.getInput().getMouseX(), container.getInput().getMouseY()))){
					graphic.drawRect(polygonPoints.get(0).x - 5, polygonPoints.get(0).y - 5, 10, 10);
				}
			}
		}
		
		if(isSelectionTabOpen() && marqueeSelection != null){
			graphic.setColor(Color.white);
			graphic.draw(marqueeSelection);
		}
	}
	
	public Vector2f getScreenCenter(){
		return new Vector2f(mainContainer.getWidth()/2.0f, mainContainer.getHeight()/2.0f);
	}

	public void clearPolygonPoints(){
		polygonPoints.clear();
	}
	
	public boolean pointsShouldSnap(Point p1, Point p2){
		if(Math.sqrt((p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)*(p2.y-p1.y)) < 10){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	@Override
	public void update(GameContainer arg0, int delta) throws SlickException {

		
		
		
		// TODO Auto-generated method stub
		if((arg0.getInput().isKeyDown(Input.KEY_DELETE) || arg0.getInput().isKeyDown(Input.KEY_BACK)) && selectedShapes.size() > 0 && ui.getTabbedPane().getSelectedIndex() == 1){
			if(selectedShapes.size() > 0){
				for(int i = selectedShapes.size() - 1; i >= 0 ; i--){
					if(selectedShapes.get(i) != null && isSelectionTabOpen()){
						shapes.remove(selectedShapes.get(i));
						LevelObject s = selectedShapes.get(i);
						s = null;
						selectedShapes.remove(i);
					}
				}
				for(LevelObject selectedShape : selectedShapes){
					if(selectedShape != null && isSelectionTabOpen()){
						shapes.remove(selectedShape);
						selectedShapes.remove(selectedShape);
						selectedShape = null;
					}
				}
			}
		}
		
		if(lMouseSelPoint != null && selectedShapes.size() > 0){
			for(int i = 0; i < selectedShapes.size(); i++){
				LevelObject selectedShape = selectedShapes.get(i);
				
				if(selectedShape != null && isSelectionTabOpen()){
					selectedShape.getSlickShape().setCenterX(lMouseSelPoint.x - mouseDifferences.get(i).x);
					selectedShape.getSlickShape().setCenterY(lMouseSelPoint.y - mouseDifferences.get(i).y);
				}
			}
			
		}
		
		if(polygonPoints.size() > 2 && polygonMode && pointsShouldSnap(polygonPoints.get(0), polygonPoints.get(polygonPoints.size() - 1))){
			polygonPoints.remove(polygonPoints.size() - 1);
			closePolygon();
		}
		
		if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			if(shapeToDraw != null && ui.getTabbedPane().getSelectedIndex() == 0 && !polygonMode){
				LevelObject newShape = objCloner.deepClone(shapeToDraw);
				newShape.setShapeColor(new GGColor(selectedColor));
				shapes.add(newShape);
			}
			
		};
		
		switch(rotateKeyPressed){
		case Input.KEY_EQUALS:
			if(ui.getTabbedPane().getSelectedIndex() == 0 && shapeToDraw != null){
				rotateShape(shapeToDraw, delta, 5);
			}
			if(isSelectionTabOpen() && selectedShapes.size() > 0){
				for(LevelObject selectedShape : selectedShapes){
					if(selectedShape != null && isSelectionTabOpen()){
						rotateShape(selectedShape, delta, 5);
					}
				}
			}
			break;
		case Input.KEY_MINUS:
			if(ui.getTabbedPane().getSelectedIndex() == 0 && shapeToDraw != null){
				rotateShape(shapeToDraw, delta, -5);
			}
			if(isSelectionTabOpen() && selectedShapes.size() > 0){
				for(LevelObject selectedShape : selectedShapes){
					if(selectedShape != null && isSelectionTabOpen()){
						rotateShape(selectedShape, delta, -5);
					}
				}
			}
			break;
		}
		
		
	}
	
	public void rotateShape(LevelObject shape, int delta, int angle){
		if(!(shape.getSlickShape() instanceof Circle)){
			shape.setAngle(shape.getAngle() + degreesToRadians((float) (angle/60.0*delta)));
			shape.setSlickShape(shape.getSlickShape().transform(Transform.createRotateTransform(degreesToRadians((float) (angle/60.0*delta)),
																								shape.getSlickShape().getCenterX(),
																								shape.getSlickShape().getCenterY())));
		}
	}
	
	public static float calculateAngleToMouse(float x, float y, float x1, float y1) {
		double angle = Math.atan2(y - y1, x - x1);
		return (float) (angle - (Math.PI/2.0));
	}
	
	public float degreesToRadians(float degrees){
		return (float) (degrees/180.0 * Math.PI);
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		switch(key){
		case Input.KEY_EQUALS:
			rotateKeyPressed = Input.KEY_EQUALS;
			break;
		case Input.KEY_MINUS:
			rotateKeyPressed = Input.KEY_MINUS;
			break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		
		switch(key){
		case Input.KEY_EQUALS:
			rotateKeyPressed = -1;
			break;
		case Input.KEY_MINUS:
			rotateKeyPressed = -1;
			break;
		}
	}	
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub
		super.mouseDragged(oldx, oldy, newx, newy);
		
		if(rMousePanPoint != null){
			rMousePanPoint.x = newx;
			rMousePanPoint.y = newy;
		}
		
		if(lMouseSelPoint != null){
			lMouseSelPoint.x = newx;
			lMouseSelPoint.y = newy;
		}
	
		if(marqueeSelection != null){
			marqueeSelection.setWidth(newx - marqueeSelection.getX());
			marqueeSelection.setHeight(newy - marqueeSelection.getY());
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		
		if(button == Input.MOUSE_RIGHT_BUTTON){
			rMousePanPoint = new Vector2f(x,y);
			cameraMouseDifference = new Vector2f(x - camera.x, y - camera.y);
			selectedShapes.clear();
		}
		
		if(isSelectionTabOpen() && button == Input.MOUSE_LEFT_BUTTON){
			int count = 0;
			
			for(LevelObject s : shapes){
				if(s.getSlickShape().contains(x, y)){
					if(selectedShapes.size() == 0){
						selectedShapes.add(s);
						count++;
						break;
					}
					if(selectedShapes.contains(s)){
						count++;
					}
				}
			}
			if(count == 0){

				marqueeSelection = new Rectangle(x, y, 1, 1);
				
				selectedShapes.clear();
			}
			
			lMouseSelPoint = new Point(x,y);
			
			if(selectedShapes.size() > 0){
				for(LevelObject s : selectedShapes){
					mouseDifferences.add(new Point((int)(x - s.getSlickShape().getCenterX()), (int)(y - s.getSlickShape().getCenterY())));
				}
			}
		}
		
		if(button == Input.MOUSE_LEFT_BUTTON && polygonMode && isGameTabOpen()){
			float mx, my;
			mx = mainContainer.getInput().getMouseX();
			my = mainContainer.getInput().getMouseY();
			polygonPoints.add(new Point((int)mx, (int)my));
		}
	}

	
	@Override
	public void mouseReleased(int button, int x, int y) {
		super.mouseReleased(button, x, y);
		
		if(button == Input.MOUSE_RIGHT_BUTTON){
			rMousePanPoint = null;
			cameraMouseDifference = null;
		}
		
		if(mouseDifferences.size() > 0){
			mouseDifferences.clear();
		}
		
		if(button == Input.MOUSE_LEFT_BUTTON && isSelectionTabOpen()){
			
			if(lMouseSelPoint != null){
				lMouseSelPoint = null;
			}
			
			if(marqueeSelection != null){
				for(LevelObject shape : shapes){
					if(marqueeSelection.intersects(shape.getSlickShape()) || marqueeSelection.contains(shape.getSlickShape()) && isSelectionTabOpen()){
						System.out.println("heero");
						selectedShapes.add(shape);
					}
				}
				marqueeSelection = null;
			}
		}
	}
	

	public LevelObject getChosenSelectedShape(){
		if(selectedShapes.size() > 0){
			for(LevelObject s : selectedShapes){
				if(s.getSlickShape().contains(mainContainer.getInput().getMouseX(), mainContainer.getInput().getMouseY())){
					return s;
				}
			}
		}
		return  null;
	}
	
	
	public void closePolygon(){
		shapeToDraw = new LevelObject(new Polygon());
		shapeToDraw.setShapeColor(new GGColor(selectedColor));
		((Polygon)(shapeToDraw.getSlickShape())).setAllowDuplicatePoints(false);
		
		for(Point p : polygonPoints){
			((Polygon)(shapeToDraw.getSlickShape())).addPoint(p.x, p.y);
		}
		
		((Polygon)(shapeToDraw.getSlickShape())).setClosed(true);
		
		shapes.add(shapeToDraw);
		shapeToDraw = null;
		polygonPoints.clear();
		
		
	}
	
	public boolean isGameTabOpen(){
		return ui.getTabbedPane().getSelectedIndex() == 0;
	}
	
	public boolean isSelectionTabOpen(){
		return ui.getTabbedPane().getSelectedIndex() == 1;
	}



	
	
	@Override
	public void mouseWheelMoved(int change) {
		super.mouseWheelMoved(change);
		
		scaleFactor += change/1000.0f;
		if(scaleFactor < 0){
			scaleFactor = .04f;
		}
	}

	public static void main(String[]args){
		try{
			ui = new MainInterface();
			Main mainGame = new Main("Editor");
			mainGame.setInterfaceCallBack(ui);
			gameContainer = new CanvasGameContainer(mainGame);
			
			ui.setDelegate(mainGame);
			ui.setLocationRelativeTo(null);
			ui.getGamePanel().add(gameContainer);
			ui.setVisible(true);
			gameContainer.start();
		}catch(Exception e){
			
		}
	}

	@Override
	public void setScreenBounds(int x, int y) {
		// TODO Auto-generated method stub
		gameContainer.setBounds(new java.awt.Rectangle(x,y));
	}

	@Override
	public void setShapeToDraw(LevelObject s) {
		// TODO Auto-generated method stub
		shapeToDraw = s;
	}

	@Override
	public void setSelectedColor(GGColor c) {
		// TODO Auto-generated method stub
		selectedColor = c.getSlickColor();
	}

	@Override
	public void returnFocus() {
		gameContainer.requestFocus();
	}

	@Override
	public void clearShapes() {
		shapes.clear();
		selectedShapes.clear();
	}

	@Override
	public void handleActionEvent(ActionEvent event) {
		polygonMode = !polygonMode;
		if(polygonMode == false){
			shapeToDraw = null;
			polygonPoints.clear();
		}else{
			
		}
	}

	@Override
	public void setPolygonMode(boolean mode) {
		// TODO Auto-generated method stub
		polygonMode = mode;
	}

	@Override
	public void handleExportEvent(ActionEvent event) {
		LevelParser.exportLevelObjects(shapes, this, "leveltest");
	}

	@Override
	public int getShapeCount() {
		// TODO Auto-generated method stub
		return shapes.size();
	}

	@Override
	public void handleEmailEvent(ActionEvent event, String name) {
		LevelParser.sendEmail(name, shapes, this, "leveltest");
	}

}
