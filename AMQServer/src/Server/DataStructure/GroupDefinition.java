/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DataStructure;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author ruy
 */
public class GroupDefinition 
{
    
    private Object id;
    private Vector members;

    public GroupDefinition() 
    {
        members=new Vector(1,1);
    }

    
    public Object getId() 
    {
        return id;
    }

    public void setId(Object id) 
    {
        this.id = id;
    }

    public Iterator getMembers() 
    {
        return members.iterator();
    }

    public void addMember(Object member) 
    {
        members.add(member);
    }
    
    public void delMember(Object member)
    {
        members.remove(member);
    }

    public  boolean isMember(Object member)
    {
        Iterator it=getMembers();
        while(it.hasNext())
            if (it.next().equals(member)) return true;
        return false;
                
    } 
    
}
