/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.agent;

import java.io.Serializable;



/**
 *
 * @author ruy
 */
public class AgentDefinition implements Serializable
{
    private Object id;
    private Object  cert;
    private Object leaseTime;
    

    public AgentDefinition() 
    {
        this.id = null;
        this.cert = null;
        this.leaseTime = null;
    }

    
    public AgentDefinition(Object id, Object cert, Object leaseTime) 
    {
        this.id = id;
        this.cert = cert;
        this.leaseTime = leaseTime;
    }

    
    public Object getCert() 
    {
        return cert;
    }

    public void setCert(Object cert) 
    {
        this.cert = cert;
    }

    public Object getId() 
    {
        return id;
    }

    public void setId(Object id) 
    {
        this.id = id;
    }

    public Object getleaseTime() 
    {
        return leaseTime;
    }

    public void setleaseTime(Object leaseTime) 
    {
        this.leaseTime = leaseTime;
    }    
}
