/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;


import Client.GUI.Plugin;
import common.agent.AgentDefinition;
import common.Wrapper;
import common.agent.Agent;
import common.agent.AgentConnector;
import common.events.QueueEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruy
 */
public class Container implements ContainerInterface,Runnable,AgentConnector
{
    private Vector listeners;
    private ContainerConnection cc;
    private EventAdapter adapter;
    private AgentLoader loader;
    private HashMap agents,guisMap;
    private Plugin plugin;
    private int ctime;
    
    public Container(String server, int port,String remotePath,String ctime)
    {
        ClassLoader cloader=createLoader(remotePath);
        this.ctime=Integer.parseInt(ctime);
        guisMap=new HashMap();

        loader=new AgentLoader("publish" + File.separator , "xml",this,guisMap,cloader);
        cc=new ContainerConnection(server, port);
        agents=loader.getAgents();
        adapter=new EventAdapter(this);
        Iterator it=agents.values().iterator();
        while(it.hasNext())
            agentRegistration((Agent)it.next());
        plugin=new Plugin(this,cloader);
        new Thread(this).start();
    }     

    private ClassLoader createLoader(String remotePath)
    {
        //remotePath="file:Classes"+File.separator+","+remotePath;
        System.out.println(remotePath);
        String[] path=remotePath.split(",");
        URL[] urls=new URL[(path.length)+1];
        try
        {
            for(int x=0;x<path.length;x++)
            {
                System.out.println(path[x]);
                urls[x]=(new URL(path[x]));
            }
            return new URLClassLoader(urls);
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }       
    }
    synchronized  public void newMsg(QueueEvent qe) 
    {
        if(agents.containsKey(qe.getMsg()))
            cc.requestMsg((Agent)(agents.get(qe.getMsg())));
    }
    
    public void agentRegistration(Agent agent)
    {
        AgentDefinition ad=new AgentDefinition(agent.getID(),null,"0");
        cc.addAgent(ad, "0");
    }

    public void run() 
    {
        while(true)
            try 
            {
                cc.requestMsg(agents.values().iterator());
                Thread.sleep(ctime);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void send(Wrapper msg) 
    {
        cc.sendMsg(msg);
    }

    public HashMap getGuisMap() 
    {
        return guisMap;
    }

    public static void main(String args[])
    {
        String server="localhost";
        String remotePath="file:Classes"+File.separator;
        String ctime="60000";
        for (int x=0;x<args.length;x++)
        {
            if(args[x].equals("-AMQServer"))
                server=args[++x];
            else if(args[x].equals("-RemotePath"))
                remotePath=args[++x];
            else if(args[x].equals("-CTime"))
                ctime=args[++x];
        }
        Container container=new Container(server, 2311,remotePath,ctime);


    }
}

