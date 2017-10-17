/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.events;

import java.io.Serializable;
import java.util.EventObject;

/**
 *
 * @author ruy
 */

public class QueueEvent extends EventObject implements Serializable
{
    private String msg;

    public QueueEvent(Object source, String msg) 
    {
        super(source);
        this.msg = msg;
    }

    public String getMsg() 
    {
        return msg;
    }

    public void setMsg(String msg) 
    {
        this.msg = msg;
    }    
}
