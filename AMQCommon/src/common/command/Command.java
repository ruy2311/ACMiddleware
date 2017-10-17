/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.command;

import java.io.Serializable;

/**
 *
 * @author ruy
 */
public class Command implements Serializable
{
    public static int ADDAGENT=0;
    public static int DELAGENT=1;
    private int command;
    private String[] params;
    private Object target;

    public Command(int command, String[] params,Object target) 
    {
        this.command = command;
        this.params = params;
        this.target=target;
    }
    
    public Command(int command,Object target) 
    {
        this.command = command;
        this.target=target;
    }

    
    public void setParams(String[] params)
    {
        this.params=params;
    }

    public void setCommand(int command) 
    {
        this.command = command;
    }

    public int getCommand()
    {
        return command;
    }

    public String[] getParams() 
    {
        return params;
    }

    public Object getTarget() 
    {
        return target;
    }

    public void setTarget(Object target) 
    {
        this.target = target;
    }    
}
