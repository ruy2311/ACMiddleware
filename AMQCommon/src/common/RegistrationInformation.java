/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author ruy
 */
public class RegistrationInformation implements Serializable
{
    public static final int FOREVER=0;
    
    private InetAddress ip;
    private int port,leaseTime;

    public InetAddress getIp() 
    {
        return ip;
    }

    public void setIp(InetAddress ip) 
    {
        this.ip = ip;
    }

    public int getLeaseTime() 
    {
        return leaseTime;
    }

    public void setLeaseTime(int leaseTime) 
    {
        this.leaseTime = leaseTime;
    }

    public int getPort() 
    {
        return port;
    }

    public void setPort(int port) 
    {
        this.port = port;
    }

    public RegistrationInformation() 
    {
            this.port=0;
            this.leaseTime=RegistrationInformation.FOREVER;
    }
    
}
