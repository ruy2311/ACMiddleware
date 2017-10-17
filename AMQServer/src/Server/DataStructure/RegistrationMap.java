/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DataStructure;

import common.RegistrationInformation;
import common.events.RegistrationEvent;
import common.events.RegistrationListener;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author Rodrigo Dominguez Garcia
 */
public class RegistrationMap 
{
    private TreeMap binaryQueue;
    private Vector listeners;


    public RegistrationMap() 
    {
        binaryQueue=new TreeMap();
        listeners=new Vector(1,1);
    }

    public synchronized boolean add( RegistrationInformation ri)
    {
        
        if(binaryQueue.put(ri.getIp().toString(), ri.getIp())==null)
        {
            Iterator it=listeners.iterator();
            RegistrationEvent re=new RegistrationEvent(this,ri);
            while(it.hasNext())
            {    
                ((RegistrationListener) it.next()).registration(re);
            }       
            return true;
        }    
        return false;
    }

     
    public synchronized Object remove(Object ID)
    {
        return binaryQueue.remove(ID);
    }        
    
    public void addRegistrationListener(RegistrationListener rl)
    {
        if(rl!=null)
            listeners.add(rl);
    }

    public void removerRegistrationListener(RegistrationListener rl)
    {
            listeners.remove(rl);
    }
}
