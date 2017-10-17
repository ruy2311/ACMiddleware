package Server;
import Server.DataStructure.GroupDefinition;
import Server.DataStructure.MessageQueue;
import common.agent.AgentDefinition;
import common.RegistrationInformation;
import common.Wrapper;
import common.command.Command;
import common.events.QueueEvent;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;

public class AgentConnection implements Runnable
{
		
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;
	private AMQSInterface server;
	private Socket coneccion;	
        private Wrapper response;
        private MessageQueue queue;
        
	//contructor de Hilocliente
	public AgentConnection( AMQSInterface server)
	{
            this.server=server;
	}		
        
	//fin del contructor Hilocliente
    public void setConnection(Socket clientSocket)
    {
		coneccion=clientSocket; //referencia al socket cliente
		try
		{
			coneccion.setSoTimeout(5000);
			System.out.println("Coneccion recivida de: " +  coneccion.getInetAddress() );
			salida=new ObjectOutputStream(coneccion.getOutputStream());
                        salida.flush();
			entrada=new ObjectInputStream(coneccion.getInputStream());
			
		}
		catch (IOException ioE)
		{
			ioE.printStackTrace();
		}
		new Thread(this).start();
        }
        
	//ejecucion del objeto como un hilo independiente
	public void run()
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
                        System.gc();
		}//while( continuar )
		cerrar();
	}//fin del run()

	private void procesar (Wrapper msg)
	{
            switch(msg.getPerformative())
            {
                case Wrapper.SUBSCRIBE: //registra el contenedor en el sistema
                    subscribe(msg);                   
                    break;
                case Wrapper.INFORM: //enviar mensaje para todos
                    publish(msg);
                    break;
                case Wrapper.TELL: // entrega el mensaje al pool del agente
                    giveMsg(msg);
                    break;
                case Wrapper.REQUEST: //solicitud de optencion mensajes
                    requestMsg(msg);
                    break;
                case Wrapper.COMMAND: //comando
                    processCommand(msg);
                    break;                    
                case Wrapper.RESPONSE: //respuesta de accion
                    processResponse(msg);
                    break;                                                
            }
        }
        
    public void requestMsg(Wrapper msg)
    {
            queue=(MessageQueue) server.getMsgQueue(msg.getSender());
            if(queue!=null)
            {    
                Enumeration list=queue.messages();
                while(list.hasMoreElements())
                {
                    response=(Wrapper) list.nextElement();
                    if (!sendMsg(response)) queue.rollback();
                    queue.setShadow(response.getId());
                }
                queue.commit();
                sendResponse("AMQS","Container", "EOQ");
            }
            else
                sendResponse("AMQS","Container", "Agent not Found");
        }

        public boolean sendMsg(Wrapper msg)
        {
            try 
            {
                salida.reset();
                salida.writeObject(msg);
                salida.flush();
            } 
            catch (IOException ex) 
            {
                return false;
            }            
            return true;
        }        

        private void sendResponse(String sender,String receiver,Object msg)
        {
            response=new Wrapper();
            response.setSender(sender);
            response.setReceiver(receiver);
            response.setPerformative(Wrapper.RESPONSE);            
            response.setObj("Response:"+msg);
            sendMsg(response);
        }
        
        public void giveMsg(Wrapper msg)
        {
            queue=(MessageQueue) server.getMsgQueue(msg.getReceiver());
            if(queue!=null && queue.put(msg))
            {
                sendResponse("AMQS", msg.getReceiver(), "messages ok");
                server.notifyMsg(new QueueEvent(this, (String)msg.getReceiver()));
            }
            else
                sendResponse("AMQS", msg.getReceiver(), "messages error");
        }
        


        
        private void processCommand(Wrapper msg) 
        {
            boolean flag=false;
            Command cmd=(Command) msg.getObj();
            if(cmd.getCommand()==Command.ADDAGENT)
                flag=server.addAgent2XML((AgentDefinition) cmd.getTarget());
            if(flag)
                sendResponse("AMQS","Container", "command successful");
            else
                sendResponse("AMQS","Container", "command error");
        }

        private void processResponse(Wrapper msg) 
        {
           
        }
 
        
        private void subscribe(Wrapper msg) 
        {
            RegistrationInformation ri=(RegistrationInformation)msg.getObj();
            InetAddress ip=coneccion.getInetAddress();
            ri.setIp(ip);
            if (server.registration(ri))
                sendResponse("AMQS","Container", "Registration OK");
            else
                sendResponse("AMQS","Container", "Registration Failed");
        }
        
        private void publish(Wrapper msg)
        {
           Iterator it; 
           GroupDefinition group=(GroupDefinition)server.getGroup(msg.getReceiver());
           if(group!=null)
           {    
               it=group.getMembers();
               while(it.hasNext())
               {
                    Object receiver=it.next();
                    queue=(MessageQueue) server.getMsgQueue(receiver);
                    if(queue!=null && queue.put(msg))
                    {
                        sendResponse("AMQS",msg.getReceiver(), "message ok");
                        server.notifyMsg(new QueueEvent(this, (String)receiver));
                    }
                    else
                        sendResponse("AMQS",msg.getReceiver(), "message error");
               }  
           }
           else
                sendResponse("AMQS",msg.getReceiver(), "group not found");
        }

    //metodo utilitario para cerrar toda la comunicacion exitente con la aplicacion cliente
	public void cerrar()
	{
		try
		{
                    entrada.close();
                    salida.close();
                    coneccion.close();
                    server.removeClient(this);
                    System.gc();
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

}//fin de la clase HiloCliente

