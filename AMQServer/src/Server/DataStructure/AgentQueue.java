/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DataStructure;

import common.events.QueueListener;
import common.events.QueueEvent;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;


/**
 *
 * @author Rodrigo Dominguez Garcia
 */
public class AgentQueue 
{
    private TreeMap binaryQueue;
    private Vector listeners;
    private static final int addNewAgent=0;
    private static final int getMsgQueue=1;
    

    public AgentQueue() 
    {
        binaryQueue=new TreeMap();
        listeners=new Vector(1,1);
    }

    private synchronized Object binaryQueueAccess(int operation,Object agentID)
    {
        switch (operation)
        {
            case AgentQueue.addNewAgent:
                return binaryQueue.put(agentID, new MessageQueue());
            case AgentQueue.getMsgQueue:
                return binaryQueue.get(agentID);
            default:    
                return null;        
        }        
    }        
    
    //add new Agente to the queue
    public void add( Object agentID)
    {
            binaryQueueAccess(AgentQueue.addNewAgent, agentID);
    }

    public Object getQueue(Object agentID)
    {
        return binaryQueueAccess(AgentQueue.getMsgQueue, agentID);
    }
   
    //remove agent from AgentQueue
    private Object remove(Object agentID)
    {
        return binaryQueue.remove(agentID);
    }        
    
    public void addQueueListener(QueueListener ql)
    {
        if(ql!=null)
            listeners.add(ql);
    }

    public void removeQueueListener(QueueListener ql)
    {
            listeners.remove(ql);
    }
    

    synchronized public void notifyMsg(QueueEvent qe)
    {       
            Iterator it=listeners.iterator();
            //QueueEvent qe=new QueueEvent(this, "new message");
            while(it.hasNext())
            {    
                ((QueueListener) it.next()).newMessages(qe);
            }     

    }
    
    public synchronized  boolean contain(Object ID)
    {
        return binaryQueue.containsKey(ID);
    }    
    
}
