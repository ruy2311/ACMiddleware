/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DataStructure;

import common.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author ruy
 */
public class MessageQueue 
{
    private boolean flag;
    private Vector shadow;
    private Hashtable messages;
    
    public MessageQueue() 
    {
        flag = false;
        messages = new Hashtable();
        shadow = new Vector(1,1);
    }
    
    synchronized private void setFlag()
    {
        flag=!flag;
    }        

    public void setShadow(Object ID)
    {
        shadow.add(ID);
    }        
    
    synchronized public void commit()
    {
        Iterator i=shadow.iterator();
        while(i.hasNext())
            messages.remove(i.next());
        shadow.clear();
        setFlag();
        notifyAll();
    }
     
    synchronized public void rollback()
    {
        shadow.clear();
    }

    synchronized public void release()
    {
        setFlag();
        notifyAll();
    }

    synchronized public boolean put(Wrapper msg)
    {
        while(flag)
        {    
            try 
            {
                wait();
            } 
            catch (InterruptedException ex) 
            {
                    return false;
            }
        }       
        setFlag();
        if (messages.containsKey(msg.getId()))
        {    
            release();              
            return false;       
        }
        messages.put(msg.getId(),msg);
        release();         
        
        return true;
    }
   
    synchronized public Enumeration messages()
    {
        while(flag)
        {    
            try 
            {
                wait();
            } 
            catch (InterruptedException ex) 
            {
                    return null;
            }
        }       
        setFlag();
        return messages.elements();
    }
}
