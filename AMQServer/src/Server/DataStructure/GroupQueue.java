/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DataStructure;

import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;

/**
 *
 * @author ruy
 */
public class GroupQueue 
{
    private HashMap groupQueue;
    private static final int addNewGroup=0;
    private static final int getMsgQueue=1;
    private static final int delGroup=2;

    
    public GroupQueue() 
    {
        groupQueue = new HashMap();
    }
    

    private synchronized Object QueueAccess(int operation,Object group)
    {
        switch (operation)
        {
            case GroupQueue.addNewGroup:
                return groupQueue.put(((GroupDefinition)group).getId(), group);
            case GroupQueue.getMsgQueue:
                return groupQueue.get(group);
            case GroupQueue.delGroup:
                return groupQueue.remove(group);
            default:    
                return null;        
        }        
    }        

    public Object getQueue(Object group)
    {
        return QueueAccess(getMsgQueue, group);
    }

    public void add(Object group) 
    {
       QueueAccess(addNewGroup, group);

    }
    
  
    private Object remove(Object group)
    {
        return QueueAccess(delGroup, group);
    }        

    public Object  getQueuesFrom(Object agentID)
    {
        Vector lista=new Vector(1,1);
        boolean flag=false;
        Object element;
        Iterator it=groupQueue.values().iterator();
        if(it.hasNext())
        {
            element=it.next(); 
            if(((GroupDefinition) element).isMember(agentID))
            {
                flag=true;
                lista.add(((GroupDefinition)element).getId());
            }
        }    
        
        if(flag)
            return lista;
        else
            return null;
    }
}
    
