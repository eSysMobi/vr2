package pkg.ns.regapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.util.Pair;
import android.util.Xml;
import android.widget.Toast;

public class Utils {
	
	static void ShowMessage(String message)
	{
		Toast.makeText(LoadingActivity.GetContext(), message, Toast.LENGTH_LONG).show();
	}
	static String CreateXml(String root, List<Pair<String, String>> params, String encoding){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try
		{
			serializer.setOutput(writer);
			serializer.startDocument(encoding, true);
			serializer.startTag(Notify.STRING_EMPTY, root);
			for(int i = 0; i < params.size(); ++i)
			{
				createElement(serializer, params.get(i).first, params.get(i).second);
			}
			serializer.endTag(Notify.STRING_EMPTY, root);
			serializer.endDocument();
			return writer.toString();
		}
		catch(Exception e)
		{
			Utils.ShowMessage(e.toString());
		}
		return Notify.STRING_EMPTY;
	}
	static boolean GetParamsFromXmlFile(File file)
	{
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	try 
    	{
    		DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(file);
			document.getDocumentElement().normalize();
		
			NodeList params = document.getDocumentElement().getChildNodes();
			if (params.getLength() > 0)
				Notify.Settings.clear();
			
			for(int i = 0; i < params.getLength(); ++i)
			{
				Notify.Settings.add(new Pair<String, String>(params.item(i).getNodeName(), params.item(i).getTextContent()));
			}
			
			return true;
    	}
    	catch(Exception e)
    	{
    		ShowMessage(e.getMessage());
    	}
		return false;
	}
	private static void createElement(XmlSerializer serializer, String nameElement, String value) 
				throws IllegalArgumentException, IllegalStateException, IOException{
		serializer.startTag("", nameElement.trim());
		serializer.text(value.trim());
		serializer.endTag("", nameElement.trim());
	}
	static void saveDataToFile(String path, String file, String content){
		File fileName = null;
    	String sdState = android.os.Environment.getExternalStorageState();
    	if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) 
    	{
    	    File sdDir = android.os.Environment.getExternalStorageDirectory();
    	    File filePath = new File(sdDir.getAbsolutePath() + "/" + path);
    	    filePath.mkdirs();
    	    fileName = new File(filePath, file);
    	}    	
    	try {
    	    final FileWriter f = new FileWriter(fileName);
    	    try 
    	    {
				f.write(content);
	    	    f.flush();
	    	    f.close();
			} 
    	    catch (IOException e) {
				ShowMessage(e.getMessage());
			}  	        	    
    	} catch (Exception e) {

    	}
    }
	static String GetParam(String param)
	{
		try
		{
			String value = Notify.STRING_EMPTY;
			for(int i = 0; i < Notify.Settings.size(); ++i)
			{
				if (Notify.Settings.get(i).first.trim().equalsIgnoreCase(param))
					return Notify.Settings.get(i).second.trim();
			}
			return value; 
		}
		catch(Exception e)
		{
			Utils.ShowMessage(e.getMessage());
			return Notify.STRING_EMPTY;
		}
	}
}
