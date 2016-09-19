package leveleditor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import java.awt.GridLayout;

public class MainInterface extends JFrame implements InterfaceCallBack{

	private static final long serialVersionUID = 3029009256047910250L;
	private JPanel contentPane;
	private JPanel gamePanel;
	private GameCallBack delegate;
	private JTabbedPane tabbedPane;
	
	private JTextField radiusField;
	private JTextField rectWidthField;
	private JTextField rectHeightField;
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
	
	public void setDelegate(GameCallBack d){
		delegate = d;
	}
	
	public JPanel getGamePanel(){
		return gamePanel;
	}
	/**
	 * Create the frame.
	 */
	public MainInterface() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
				System.exit(0);
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		JMenuItem mi_newLevel = new JMenuItem("New Level");
		mi_newLevel.setMnemonic('N');
		fileMenu.add(mi_newLevel);
		JMenuItem mi_loadLevel = new JMenuItem("Load Level");
		mi_loadLevel.setMnemonic('L');
		fileMenu.add(mi_loadLevel);
		JMenuItem mi_exportLevel = new JMenuItem("Export Level");
		mi_exportLevel.setMnemonic('E');
		fileMenu.add(mi_exportLevel);
		JMenuItem mi_emailLevel = new JMenuItem("Email level to Grant");
		fileMenu.add(mi_emailLevel);
		JMenuItem mi_clearLevel = new JMenuItem("Clear Level");
		mi_clearLevel.setMnemonic('C');
		fileMenu.add(mi_clearLevel);
		
		mi_newLevel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(delegate != null){
					
				}
			}
		});
		
		mi_loadLevel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		
		mi_exportLevel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(delegate.getShapeCount() < 1){
					JOptionPane.showMessageDialog(contentPane, "There is no level, y u export?", "Error", 0);
				}else{
					delegate.handleExportEvent(arg0);
				}
			}
		});
		
		mi_emailLevel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(delegate.getShapeCount() < 1){
					JOptionPane.showMessageDialog(contentPane, "There is no level, y u export?", "Error", 0);
				}else{
					delegate.handleEmailEvent(arg0, JOptionPane.showInputDialog("Name please"));
				}
			}
			
		});
		
		mi_clearLevel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				delegate.clearShapes();
			}
		});
		
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		
		//END MENU
		
		
		contentPane.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.WEST);
		
		JPanel selectionTab = new JPanel();
		BoxLayout bl_stab = new BoxLayout(selectionTab, BoxLayout.Y_AXIS);
		selectionTab.setLayout(bl_stab);
		
		JPanel geometryTab = new JPanel();
		
		JPanel generalPanel = new JPanel();
		BoxLayout bl_genPan = new BoxLayout(generalPanel, BoxLayout.Y_AXIS);
		generalPanel.setLayout(bl_genPan);
		
		JPanel colorPanel = new JPanel(new FlowLayout());
		
		final JTextField selectedColor = new JTextField();
		selectedColor.setColumns(5);
		selectedColor.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				GGColor c = null;
				try{
					c = new GGColor(JColorChooser.showDialog(contentPane, "Choose a color", null));
				}catch(Exception e){
					
				}
				if(c != null){
					selectedColor.setBackground(c.getAWTColor());
					delegate.setSelectedColor(c);
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		geometryTab.setLayout(new GridLayout(0, 1, 0, 0));
		selectedColor.setMaximumSize(new Dimension(40, selectedColor.getPreferredSize().height));
		selectedColor.setBackground(new java.awt.Color(51,255,102));
		selectedColor.setEnabled(false);
		colorPanel.add(selectedColor);
		
		JLabel colorLabel = new JLabel("Color");
		colorPanel.add(colorLabel);
		
		generalPanel.add(colorPanel);
		geometryTab.add(generalPanel);
		
		JPanel circlePanel = new JPanel();
		//circlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//circlePanel.setBorder(BorderFactory.createEtchedBorder());
		BoxLayout bl_circPanel = new BoxLayout(circlePanel, BoxLayout.Y_AXIS);
		circlePanel.setLayout(bl_circPanel);
		
		final JButton circleButton = new JButton("Circle");
		circleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		circlePanel.add(circleButton);
		circleButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				if(delegate != null){
					
					delegate.setPolygonMode(false);
					delegate.setShapeToDraw(new LevelObject(new Circle(0,0,20)));
				}
			}
			
		});
		
		JPanel radiusPanel = new JPanel(new FlowLayout());
		
		radiusField = new JTextField();
		radiusField.setText("20.0");
		radiusField.setColumns(4);
		radiusField.addMouseListener(new TextFieldListener(ObjectGeometry.CIRCLE_SHAPE_RADIUS, radiusField));
		radiusField.setMaximumSize(new Dimension(60, radiusField.getPreferredSize().height));
		radiusField.setToolTipText("radius");
		radiusPanel.add(radiusField);
		
		JLabel radiusLabel = new JLabel("Radius");
		radiusPanel.add(radiusLabel);
		
		circlePanel.add(radiusPanel);
		
		JPanel rectPanel = new JPanel();
		rectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		BoxLayout bl_rectPanel = new BoxLayout(rectPanel, BoxLayout.Y_AXIS);
		rectPanel.setLayout(bl_rectPanel);
		
		JButton rectangleButton = new JButton("Rectangle");
		rectangleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rectPanel.add(rectangleButton);
		rectangleButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				delegate.setPolygonMode(false);
				delegate.setShapeToDraw(new LevelObject(new Rectangle(0,0,50,20)));
			}
			
		});
		
		JPanel rectWidthPanel = new JPanel(new FlowLayout());
	
		rectWidthField = new JTextField();
		rectWidthField.setText("50.0");
		rectWidthField.addMouseListener(new TextFieldListener(ObjectGeometry.RECTANGLE_SHAPE_WIDTH, rectWidthField));
		rectWidthField.setMaximumSize(new Dimension(60, rectWidthField.getPreferredSize().height));
		rectWidthField.setToolTipText("width");
		rectWidthPanel.add(rectWidthField);
		
		JLabel rectWidthLabel = new JLabel("Width");
		rectWidthPanel.add(rectWidthLabel);
		
		rectWidthPanel.setPreferredSize(rectWidthLabel.getPreferredSize());
		
		rectPanel.add(rectWidthPanel);
		
		JPanel rectHeightPanel = new JPanel(new FlowLayout());
		
		rectHeightField = new JTextField();
		rectHeightField.setText("20.0");
		rectHeightField.addMouseListener(new TextFieldListener(ObjectGeometry.RECTANGLE_SHAPE_HEIGHT, rectHeightField));
		rectHeightField.setMaximumSize(new Dimension(60, rectHeightField.getPreferredSize().height));
		rectHeightField.setToolTipText("height");
		rectHeightPanel.add(rectHeightField);
		
		JLabel rectHeightLabel = new JLabel("Height");
		rectHeightPanel.add(rectHeightLabel);
		
		rectWidthPanel.setPreferredSize(rectHeightLabel.getPreferredSize());
		
		rectPanel.add(rectHeightPanel);
		
		JPanel polyGonPanel = new JPanel();
		polyGonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		BoxLayout bl_polyPanel = new BoxLayout(polyGonPanel, BoxLayout.Y_AXIS);
		polyGonPanel.setLayout(bl_polyPanel);
		
		JButton polyGonButton = new JButton("Polygon");
		polyGonButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		polyGonButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				delegate.handleActionEvent(arg0);
			}
		});
		polyGonPanel.add(polyGonButton);
		
		geometryTab.add(circlePanel);
		geometryTab.add(rectPanel);
		geometryTab.add(polyGonPanel);
		
		JScrollPane geometryScroll = new JScrollPane(geometryTab);
		JScrollPane selectionScroll = new JScrollPane(selectionTab);
		
		tabbedPane.addTab("Geometry", geometryScroll);
		tabbedPane.add("Select", selectionScroll);
		
		gamePanel = new JPanel();
		gamePanel.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				delegate.setScreenBounds(gamePanel.getWidth(), gamePanel.getHeight());
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		contentPane.add(gamePanel, BorderLayout.CENTER);
	}
	
	private class TextFieldListener implements MouseListener{
		
		int textFieldId;
		JTextField textField;

		public TextFieldListener(int textFieldId, JTextField textField){
			this.textFieldId = textFieldId;
			this.textField = textField;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
			switch(textFieldId){
			case ObjectGeometry.CIRCLE_SHAPE_RADIUS:
				float radius;
				
				try{
					radius = Float.parseFloat((String)JOptionPane.showInputDialog(contentPane, "enter value", "enter value", JOptionPane.DEFAULT_OPTION));
					delegate.setShapeToDraw(new LevelObject(new Circle(0,0,radius)));
				}catch(Exception ex){
					JOptionPane.showMessageDialog(contentPane, "Enter a number!");
					radius = 10.0f;
				}
				textField.setText("" + radius);
				break;
			case ObjectGeometry.RECTANGLE_SHAPE_WIDTH:
				float width = 10.0f;
				
				try{
					width = Float.parseFloat((String)JOptionPane.showInputDialog(contentPane, "enter value", "enter value", JOptionPane.DEFAULT_OPTION));
					delegate.setShapeToDraw(new LevelObject(new Rectangle(0,0,width,parseTextField(rectHeightField))));
				}catch(Exception ex){
					JOptionPane.showMessageDialog(contentPane, "Enter a number!");
					width = 10.0f;
				}
				textField.setText("" + width);
				break;
			case ObjectGeometry.RECTANGLE_SHAPE_HEIGHT:
				float height = 10.0f;
				
				try{
					height = Float.parseFloat((String)JOptionPane.showInputDialog(contentPane, "enter value", "enter value", JOptionPane.DEFAULT_OPTION));
					delegate.setShapeToDraw(new LevelObject(new Rectangle(0,0,parseTextField(rectWidthField),height)));
				}catch(Exception ex){
					JOptionPane.showMessageDialog(contentPane, "Enter a number!");
					height = 10.0f;
				}
				textField.setText("" + height);
				break;
			}
			delegate.returnFocus();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public float parseTextField(JTextField field){
		return Float.parseFloat(field.getText());
	}

	@Override
	public float getCircleRadius() {
		return Float.parseFloat(radiusField.getText());
	}

	@Override
	public float getRectWidth() {
		return Float.parseFloat(rectWidthField.getText());
	}

	@Override
	public float getRectHeight() {
		return Float.parseFloat(rectHeightField.getText());
	}
}
