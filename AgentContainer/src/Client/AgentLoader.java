/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import common.agent.Agent;
import common.agent.AgentConnector;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruy
 */
public class AgentLoader implements Runnable 
{
    private String path;
    private ClassLoader loader;
    private ListFilter filter;
    private HashMap agents;
    private boolean ready;
    private AgentConnector container;
    private HashMap guisMap;
    private XMLParser parser;

    public AgentLoader(String path,String mask,AgentConnector container,HashMap guisMap,ClassLoader loader)
    {
        ready=false;
        this.path = path;
        filter=new ListFilter(mask);
        agents=new HashMap();
        this.container=container;
        this.guisMap=guisMap;
        this.loader=loader;
        new Thread(this).start();
    }

       
    public void run() 
    {
        
        File[] list;
        Agent agent;
        HashMap listMap=new HashMap();

        if (path==null) throw new Error("Path no set");
        File folder=new File(path);
        if(!folder.isDirectory()) throw new Error("Folder not exist");
        if(!folder.exists()) throw new Error("Folder not exist");
        parser=new XMLParser(loader,guisMap);
        list = folder.listFiles(filter);
        for (int x = 0; x < list.length; x++) 
        {
             listMap.put(list[x].getName(), list[x].lastModified());
             agent=parser.loadXML(path,list[x].getName());
             agents.put(agent.getID(), agent);
             agent.setContainer(container);
             agent.start();
             System.out.println(list[x].getName() );
        }
        ready=true;
        while(true)
        {
                try 
                {
                    Thread.sleep(10000);
                } 
                catch (InterruptedException ex) 
                {
                    System.out.println(ex.getMessage());
                }
                list = folder.listFiles(filter);
                ready=false;
                for (int x = 0; x < list.length; x++) 
                {
                    Long value=(Long)listMap.get(list[x].getName());
                    long modified=value!=null?value.longValue():0;
                    long lm=list[x].lastModified();
                    if(modified==0 || modified!=lm)
                    {    
                        agent=parser.loadXML(path,list[x].getName());
                        agents.put(agent.getID(), agent);
                        agent.setContainer(container);
                        agent.start();
                        System.out.println(list[x].getName() );                    
                    }    
                }
                ready=true;
                listMap=new HashMap();
                for (int x = 0; x < list.length; x++) 
                {
                    listMap.put(list[x].getName(), list[x].lastModified());
                }
                System.gc();
        }    
            
    
    }
   
    class ListFilter implements FileFilter
    {
        private String extesion;

        public ListFilter(String extesion) 
        {
            this.extesion = extesion;
        }

        public boolean accept(File f) 
        {
            String name= f.getName().toLowerCase();
            return name.endsWith(extesion); 
        }

        public String getDescription() 
        {
            return null;
        }

        private void setExtension(String mask) 
        {
            this.extesion=mask;
        }

        public String getExtesion() 
        {
            return extesion;
        }
   }

    public HashMap getAgents() 
    {
        while (!ready)
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(AgentLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        return agents;
    }

}
