/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;


import common.agent.Agent;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author ruy
 */
public class XMLParser extends DefaultHandler
{
    private SAXParser sparser;
    private StringBuffer value;
    private Object agent;
    private int set=0;
    private HashMap guisMap;
    private ClassLoader loader;

    
    public XMLParser(ClassLoader loader,HashMap guisMap)
    {
        this.guisMap = guisMap;
        this.loader=loader;
    }
        
    public Agent loadXML(String path,String file)
    {
        if ( readXML(path + File.separator + file) )
            return (Agent) agent;
        else
            return null;
    }
            
    public boolean readXML(String file)  
    {
        sparser=new SAXParser();
        try 
        { 
            sparser.setContentHandler(this);
            sparser.setErrorHandler(this);
            sparser.parse(file);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            return false;
        }
        return true;
    }
    
    @Override
    public void startDocument() throws SAXException
    {
        System.out.println("Document starting");
    }
    
    @Override
    public void endDocument() throws SAXException
    {
        System.out.println("Document end");
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException 
    {
        System.out.print("<"+localName+">"); 
        if(localName.equals("class"))
            set=1;
        else if(localName.equals("id"))
            set=2;
        else if(localName.equals("gui"))
            set=3;
    }
   
    @Override
    public void endElement(String uri,String localName,String rawName) throws SAXException
    {
        System.out.println("</"+localName+">");
        set=0;
    }
    
    @Override
    public void characters (char ch[], int start, int length)	throws SAXException
    {
        value=new StringBuffer("");
        value.append(ch,start,length);
        if(set==1)
        {
            try 
            {
               System.out.println(value.toString());
               Class c = loader.loadClass(value.toString());
               System.out.println("loading fase 1");
               agent =  c.newInstance();
               System.out.println("loading fase 2");
               //agent = Class.forName(value.toString()).newInstance();
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        else if(set==2)
        {
            ((Agent)agent).setID(value.toString());
            System.out.print(value.toString());
        }
        else if(set==3)
        {
            guisMap.put(value.toString(), agent);
            System.out.print(value.toString());            
        }
    }
}
