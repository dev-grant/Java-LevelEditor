package leveleditor;
import java.awt.event.ActionEvent;

public interface GameCallBack {
	public void setShapeToDraw(LevelObject s);
	public void setScreenBounds(int x, int y);
	public void setSelectedColor(GGColor c);
	public void returnFocus();
	public void clearShapes();
	public void setPolygonMode(boolean mode);
	public void handleActionEvent(ActionEvent event);
	public void handleExportEvent(ActionEvent event);
	public void handleEmailEvent(ActionEvent event, String name);
	public int getShapeCount();
}
