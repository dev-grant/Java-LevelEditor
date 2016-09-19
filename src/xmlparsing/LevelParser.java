package xmlparsing;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import leveleditor.LevelObject;
import leveleditor.Main;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class LevelParser {
	
	public static final int STRING_TYPE = 0;
	public static final int INTEGER_TYPE = 1;
	public static final int BOOLEAN_TYPE = 2;
	
	public static String exportLevelObjects(ArrayList<LevelObject>lvlObjects, Main program,String levelName){
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Exception e) {
			return null;
		}
		
		Document levelDoc = docBuilder.newDocument();
		
		Element plistElement = levelDoc.createElement("plist");
		plistElement.setAttribute("version", "1.0");
		levelDoc.appendChild(plistElement);
		
		Element rootDict = createDict(plistElement, levelDoc);
		
		Element shapesArray = createKeyAndArray("SHAPES",rootDict,levelDoc);
		
		for(LevelObject lvlObject : lvlObjects){
			
			switch(lvlObject.getObjectType()){
			case LevelObject.CIRCLE_SHAPE:
				Circle circle = ((Circle)lvlObject.getSlickShape());
				Element circleDict = createDict(shapesArray, levelDoc);
				createKeyAndValue("SHAPE_TYPE", "Circle", STRING_TYPE, circleDict, levelDoc);
				createKeyAndValue("POSITION", pointFromShape(circle), STRING_TYPE, circleDict, levelDoc);
				createKeyAndValue("RADIUS", "" + circle.radius, STRING_TYPE, circleDict, levelDoc);
				
				Color cCol = lvlObject.getShapeColor().getAWTColor();
				createKeyAndValue("COLOR", cCol.getRed() + "," + cCol.getGreen() + "," + cCol.getBlue(), STRING_TYPE, circleDict, levelDoc);
				break;
			case LevelObject.RECTANGLE_SHAPE:
				Shape rect;
				try{
					rect = ((Rectangle) lvlObject.getSlickShape());
				}catch(Exception e){
					rect = ((Polygon) lvlObject.getSlickShape());
				}
				Element rectDict = createDict(shapesArray, levelDoc);
				createKeyAndValue("SHAPE_TYPE", "Rectangle", STRING_TYPE, rectDict, levelDoc);
				createKeyAndValue("POSITION", pointFromShape(rect), STRING_TYPE, rectDict, levelDoc);
				createKeyAndValue("WIDTH_HEIGHT", sizeFromRect(lvlObject), STRING_TYPE, rectDict, levelDoc);
				createKeyAndValue("ANGLE", "" + lvlObject.getAngle(), STRING_TYPE, rectDict, levelDoc);
				
				Color rCol = lvlObject.getShapeColor().getAWTColor();
				createKeyAndValue("COLOR", rCol.getRed() + "," + rCol.getGreen() + "," + rCol.getRed(), STRING_TYPE, rectDict, levelDoc);
				break;
			case LevelObject.POLYGON_SHAPE:
				
				break;
			}
		}
		
		return writeDocToSystem(levelDoc, program, levelName);
	}
	
	private static String pointFromShape(Shape s){
		return s.getCenterX() + "," + s.getCenterY();
	}
	
	private static String sizeFromRect(LevelObject r){
		return r.getInitialSize().x + "," + r.getInitialSize().y;
	}
	
	private static Element createDict(Node appendTo, Document doc){
		Element dictElement = doc.createElement("dict");
		appendTo.appendChild(dictElement);
		return dictElement;
	}
	
	private static Element createKeyAndArray(String keyName, Node appendTo, Document doc){
		Element keyElement = doc.createElement("key");
		keyElement.appendChild(doc.createTextNode(keyName));
		appendTo.appendChild(keyElement);
		
		Element arrayElement = doc.createElement("array");
		appendTo.appendChild(arrayElement);
		return arrayElement;
	}
	
	private static void createKeyAndValue(String keyName, String keyValue, int valueType, Node lvlDoc, Document doc){
		Element keyElement = doc.createElement("key");
		keyElement.appendChild(doc.createTextNode(keyName));
		lvlDoc.appendChild(keyElement);
		
		switch(valueType){
		case STRING_TYPE:
			Element sKeyValueElement = doc.createElement("string");
			sKeyValueElement.appendChild(doc.createTextNode(keyValue));
			lvlDoc.appendChild(sKeyValueElement);
			break;
		case INTEGER_TYPE:
			Element iKeyValueElement = doc.createElement("integer");
			iKeyValueElement.appendChild(doc.createTextNode(keyValue));
			lvlDoc.appendChild(iKeyValueElement);
			break;
		case BOOLEAN_TYPE:
			if(keyValue.equals("true")){
				Element bKeyValueElement = doc.createElement("true");
				lvlDoc.appendChild(bKeyValueElement);
			}else if(keyValue.equals("false")){
				Element bKeyValueElement = doc.createElement("false");
				lvlDoc.appendChild(bKeyValueElement);
			}
			break;
		}
	}
	
	public static String writeDocToSystem(Document doc, Main program, String levelName){
		System.out.println(program.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		try{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Apple Computer//DTD PLIST 1.0//EN");
		    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "");
			DOMSource source = new DOMSource(doc);
			File f = new File(program.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + levelName + ".plist");
			StreamResult result = new StreamResult(f);
			
			transformer.transform(source, result);
			
			return f.getAbsolutePath();
		}catch(Exception e){
			
		}
		return null;
	}
	
	public static void sendEmail(String name,ArrayList<LevelObject>lvlObjects, Main program,String levelName){
		
		try{
			String host = "smtp.gmail.com";
		    String from = "hamburgerhillsmaps@gmail.com";
		    String pass = "leveleditor";
		    Properties props = System.getProperties();
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.host", host);
		    props.put("mail.smtp.user", from);
		    props.put("mail.smtp.password", pass);
		    props.put("mail.smtp.port", "587");
		    props.put("mail.smtp.auth", "true");
	
		    String[] to = {"grant.golden5@gmail.com"};
	
		    Session session = Session.getDefaultInstance(props, null);
		    MimeMessage message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(from));
	
		    InternetAddress[] toAddress = new InternetAddress[to.length];
	
		    // To get the array of addresses
		    for( int i=0; i < to.length; i++ ) { // changed from a while loop
		        toAddress[i] = new InternetAddress(to[i]);
		    }
		    System.out.println(Message.RecipientType.TO);
	
		    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
		        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		    }
		    message.setSubject(name);
		    
		    MimeBodyPart messagePart = new MimeBodyPart();
		    messagePart.setText("From: " + name);
		    
		    String filePath = exportLevelObjects(lvlObjects, program, levelName);
		    
		    MimeBodyPart attachmentPart = new MimeBodyPart();
		    FileDataSource fileDataSource = new FileDataSource(filePath) {
		    	@Override
		    	public String getContentType() {
		    		return "application/octet-stream";
		    	}
		    };
		    attachmentPart.setDataHandler(new DataHandler(fileDataSource));
		    attachmentPart.setFileName(filePath);
		    
		    MimeMultipart mtprt = new MimeMultipart();
		    
		    mtprt.addBodyPart(messagePart);
		    mtprt.addBodyPart(attachmentPart);
		    
		    message.setContent(mtprt);
		    
		    Transport transport = session.getTransport("smtp");
		    transport.connect(host, from, pass);
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();
		}catch(Exception e){
			System.out.println("failed sending email");
		}
	}
}
