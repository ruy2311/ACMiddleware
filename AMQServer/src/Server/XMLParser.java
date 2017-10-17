/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Server.DataStructure.GroupDefinition;
import common.agent.AgentDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Vector;
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
    static public int AGENTS=0,GROUPS=1;
    private SAXParser sparser;
    private StringBuffer value;
    private Vector agents,groups;
    private AgentDefinition agent;
    private int set=0;
    private int type=0;
    private GroupDefinition group;
    
    
    public Iterator loadXML(int type)
    {
        this.type=type;
        if(type==XMLParser.AGENTS)
        {    
            if ( readXML("config" + File.separator + "SystemAgents.xml") )
                return agents.iterator();
            else
                return null;
        }    
        else if(type==XMLParser.GROUPS)
        {    
            if ( readXML("config" + File.separator + "SystemGroups.xml") )
                return groups.iterator();
            else
                return null;
        }  
        else
            return null;            
    }
            
            
    public boolean readXML(String path)  
    {
        sparser=new SAXParser();
        try 
        {
            
            sparser.setContentHandler(this);
            sparser.setErrorHandler(this);
            sparser.parse(path);
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
        if(type==XMLParser.AGENTS)
        {    
            if(localName.equals("SystemAgents"))
                agents=new Vector(1,1);
            else if(localName.equals("Agent"))
                agent=new AgentDefinition();
            else if(localName.equals("ID"))
                set=1;
            else if(localName.equals("Cert"))
                set=2;
            else if(localName.equals("LeaseTime"))
                set=3;
        }
        else
        {    
            if(localName.equals("SystemGroups"))
                groups=new Vector(1, 1);
            else if(localName.equals("Group"))
                group=new GroupDefinition();
            else if(localName.equals("ID"))
                set=11;
            else if(localName.equals("Members"))
                ;
            else if(localName.equals("Agent"))
                set=12;
        }

    }
   
    @Override
    public void endElement(String uri,String localName,String rawName) throws SAXException
    {
        System.out.println("</"+localName+">");
        if(localName.equals("LeaseTime"))
            agents.add(agent);
        else if(localName.equals("Members"))
            groups.add(group);
        set=0;
    }
    
    @Override
    public void characters (char ch[], int start, int length)	throws SAXException
    {
        value=new StringBuffer("");
        value.append(ch,start,length);
        if(set==1)
        {
            agent.setId(value.toString());
            System.out.print(value.toString());
        }
        else if(set==2)
        {
            agent.setCert(value.toString());
            System.out.print(value.toString());
        }
        else if(set==3)
        {
            agent.setleaseTime(value.toString());
            System.out.print(value.toString());            
        }
        else if(set==11)
        {
            group.setId(value.toString());
            System.out.print(value.toString());            
        }
        else if(set==12)
        {
            group.addMember(value.toString());
            System.out.print(value.toString());            
        }           
    }
    
    public boolean addAgent(AgentDefinition ad)
    {
        String line,definition;
        
        definition="\n<Agent>\n<ID>"+ad.getId()+"</ID>";
        definition+="\n<Cert>"+ad.getCert()+"</Cert>";
        definition+="\n<LeaseTime>"+ad.getleaseTime()+"</LeaseTime>";
        definition+="\n</Agent>\n</SystemAgents>";
        
        try 
        {
            RandomAccessFile xml = new RandomAccessFile("config" + File.separator + "SystemAgents.xml", "rw");
            xml.seek(xml.length()-16);
            xml.writeBytes(definition);
            xml.close();
            return true;
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        catch (IOException ioex)
        {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ioex);
            return false;
        }
    }
    
    public boolean delAgent(AgentDefinition ad)
    {
    
        return true;
    }
    
}
