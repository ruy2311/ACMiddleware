package common.agent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import common.*;
import java.util.Stack;

/**
 *
 * @author ruy
 */

public abstract class Agent implements Runnable 
{
    private AgentConnector container;
    private Object ID;
    private Stack  queue;
    private boolean set;
    
    public Agent() 
    {
        set=false;
        queue=new Stack();
    }

    final public void setID(Object ID) 
    {
        if(!set)
            this.ID = ID;
        set=true;
    }
    
    final public Object getID() 
    {
        return ID;
    }

    public void setContainer(AgentConnector container) 
    {
        this.container = container;
    }

    
    final public  void  giveMsg(Wrapper msg)
    {
        queue.push(msg);
    }

    public Stack getQueue() 
    {
        return queue;
        
    }

    final public void send(String receiver,Object contain)
    {
        Wrapper msg=new Wrapper();
        msg.setSender((String)ID);
        msg.setReceiver(receiver);
        msg.setPerformative(Wrapper.TELL);
        msg.setObj(contain);
        container.send(msg);
        
    }
    
    final public void publish(String group,Object contain)
    {
        Wrapper msg=new Wrapper();
        msg.setSender((String)ID);
        msg.setReceiver(group);
        msg.setPerformative(Wrapper.INFORM);
        msg.setObj(contain);
        container.send(msg);
    }
   
    final public void run()
    {
        behaviour();
    }        
    
    final public void  start()
    {
        new Thread(this).start();
    }
    
    public abstract void behaviour();
    public abstract void newMessages();

    
    
}
