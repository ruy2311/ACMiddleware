/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

//import Client.ContainerInterface;
import common.events.QueueEvent;
import common.events.QueueListener;
import common.events.RegistrationEvent;
import common.events.RegistrationListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author ruy
 */

/*Pendientes
 *   implementar un esquema de monitoreo de los container registrados, y darlos de baja cuando no se encuentren
 * 
 */
public class EventAdapter implements QueueListener,RegistrationListener 
{
    private Vector queueListener;

    public EventAdapter() 
    {
        queueListener=new Vector(1,1);
    }
    

    
    public void newMessages(QueueEvent qe) 
    {
        
        try 
        {

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket dato = new DatagramPacket(new byte[256], 256);
            dato.setPort(2311);
            dato.setData(toByteArray(qe));
            Iterator it=queueListener.iterator();
            while(it.hasNext())
            {
                dato.setAddress((InetAddress) it.next());
                socket.send(dato);            
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte [] toByteArray(Object evt)
    {
        try
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream (bytes);
            os.writeObject(evt);
            os.close();
            return bytes.toByteArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();            
            return null;

        }
    }

    public static Object fromByteArray (byte [] bytes)
    {
        try
        {
            // Se realiza la conversi�n usando un ByteArrayInputStream y un
            // ObjectInputStream
            ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
            ObjectInputStream is = new ObjectInputStream(byteArray);
            Object aux = is.readObject();
            is.close();
            return aux;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void registration(RegistrationEvent re) 
    {
        System.out.println("registration: "+re.getRegistrationInformation().getIp()+ " from port: "+re.getRegistrationInformation().getPort());
        queueListener.add(re.getRegistrationInformation().getIp());
    }
}
