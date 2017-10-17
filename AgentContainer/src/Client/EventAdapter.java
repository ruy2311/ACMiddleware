/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import common.events.QueueEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author ruy
 */
public class EventAdapter implements Runnable
{
    private ContainerInterface container;
    
    public EventAdapter(ContainerInterface container) 
    {
        this.container=container;
        new Thread(this).start();
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

    public void run() 
    {
        QueueEvent qe;
        try 
        {

            //DatagramSocket socket = new DatagramSocket(2311, InetAddress.getByName(InetAddress.getLocalHost().getHostName()));
            DatagramSocket socket = new DatagramSocket(2311);
            DatagramPacket dato = new DatagramPacket(new byte[256], 256);
            while(true)
            {    
                socket.receive(dato);
                qe=(QueueEvent)fromByteArray(dato.getData());
                //System.out.println(qe.getMsg()+": ");
                //aqui se debe implementar el monitor del los contenedores
                container.newMsg(qe);
                
            }
           
                       
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
