/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.GUI;

import Client.ContainerInterface;
import common.Wrapper;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruy
 */
public class Plugin implements Runnable
{
    private ContainerInterface container;
    private Socket connection;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private ClassLoader loader;
    
    public  Plugin( ContainerInterface container,ClassLoader loader)
    {
        this.container=container;
        this.loader=loader;
        new  Thread(this).start();
    }		
        
    public void run() 
    {
        try 
        {
            
            ServerSocket serverSocket = new ServerSocket( 2111);
            while ( true )
            {      
                setConnection(serverSocket.accept());
            } // fin del while     
         }
         catch ( IOException ioE ) 
         {
            ioE.printStackTrace();
         }
    }       
        
    public void setConnection(Socket clientSocket)
    {
	connection=clientSocket; //referencia al socket cliente
	try
	{
		connection.setSoTimeout(5000);
		salida=new ObjectOutputStream(connection.getOutputStream());
                salida.flush();
		entrada=new ObjectInputStream(connection.getInputStream());
	}
	catch (IOException ioE)
	{
		ioE.printStackTrace();
	}
        processConnection();
    }
    
    public void processConnection()        
    {
            Wrapper msg=null;
            while( true )
            {
                try
		{	
                    msg=(Wrapper)entrada.readObject();
                    procesar(msg);
		}
                catch (ClassNotFoundException e)
		{
                    e.getStackTrace();
                    break;			
		}
		catch( SocketException sE)
		{
                    sE.getStackTrace();
                    break;			
		}
		catch(InterruptedIOException e)
		{
			continue;
		}
		catch (IOException ioE)
		{
			ioE.getStackTrace();
			break;
		}
            }
            close();
    }
    
    public void procesar(Wrapper msg)
    {
        switch(msg.getPerformative())
        {
            case Wrapper.REQUEST:
                sendGUIS(msg);
                break;
            case Wrapper.COMMAND:
                loadGUI(msg);
                break;
        }
        
    }
    
    public void send(Wrapper msg) 
    {
        try 
        {
            salida.reset();
            salida.writeObject(msg);
            salida.flush();
        } 
        catch (IOException ex) 
        {
            ex.getStackTrace();
        }     
    }

    synchronized private void loadGUI(Wrapper msg)
    {
        try 
        {
            System.out.println("loading gui: " + msg.getObj() + "....");
            Class c = loader.loadClass((String) msg.getObj());
            final AgentFrame frame=(AgentFrame)  c.newInstance();
//            final AgentFrame frame=(AgentFrame) Class.forName((String) msg.getObj()).newInstance();
            Object agent= container.getGuisMap().get(msg.getObj());
            if(agent!=null)
            {
                frame.setAgent(agent);
                new Thread() 
                {
                        @Override
                        public void run() 
                        {
                             frame.setVisible(true);
                        }
                }.start();
                System.out.println("loading gui: " + msg.getObj() + " ok");   
            } 
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(Plugin.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }

    private void sendGUIS(Wrapper msg) 
    {
        Iterator  it=container.getGuisMap().keySet().iterator();
        if(it!=null)
        {
            Vector vector=new Vector(1, 1);
            while(it.hasNext())
                vector.add(it.next());
            msg.setPerformative(Wrapper.RESPONSE);
            msg.setObj(vector);
            send(msg);
        }    
    }

    public void close()
    {
    	try
	{
            entrada.close();
            salida.close();
            connection.close();
	}
	catch(NullPointerException nPE)
	{
            nPE.getStackTrace();
	}
	catch( IOException ioE )
	{
            ioE.printStackTrace();
        }
    }

}
