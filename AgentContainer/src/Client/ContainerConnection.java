package Client;
import common.agent.AgentDefinition;
import common.RegistrationInformation;
import common.Wrapper;
import common.agent.Agent;
import common.command.Command;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContainerConnection 
{
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;
	private Socket coneccion;	
	private int port,count;
	private String server;
    private boolean connected;
    private Agent agent;
    
	//contructor de Hilocliente
	public ContainerConnection(String server, int port)
	{
		this.server=server;
		this.port=port;
                connected=false;
                register();
    }		
	
    public void register()
    {
            Wrapper msg=new Wrapper();
            msg.setPerformative(Wrapper.SUBSCRIBE);
            RegistrationInformation ri=new RegistrationInformation();
            ri.setPort(2311);
            msg.setObj(ri);
            send(msg);
        }

 	private void conectarse()
	{
                if(!connected)
                {    
                    try
                    {
			//System.out.println("Conectando a: " +  server + "["+port+"]" );
			coneccion=new Socket( InetAddress.getByName(server), port );
			coneccion.setSoTimeout(5000);
			salida=new ObjectOutputStream(coneccion.getOutputStream());
                        entrada= new ObjectInputStream(coneccion.getInputStream());
                    }
                    catch (IOException ioE)
                    {
			ioE.printStackTrace();
                    }
                    connected=true;
                }
            
	}//fin del run()

	public void run()
	{
		Wrapper msg=null;
		while( connected )
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



	private void procesar (Wrapper msg)
	{
            switch(msg.getPerformative())
            {
                case Wrapper.INFORM:
                    giveMsg(msg);
                    break;
                case Wrapper.TELL:
                    giveMsg(msg);
                    break;
                case Wrapper.COMMAND:
                    processCommand(msg);
                    break;
                case Wrapper.RESPONSE:
                    processResponse(msg);
                    break;
            }
	}
        
        private void giveMsg(Wrapper msg)
        {
            count++;
            agent.giveMsg(msg);
        }
        
        synchronized public void addAgent(AgentDefinition ad,String leaseTime)
        {
            String[] params={"-t",leaseTime};
            Command command=new Command(Command.ADDAGENT,params,ad);
            Wrapper msg=new Wrapper();
            msg.setPerformative(Wrapper.COMMAND);
            msg.setSender("Container");
            msg.setReceiver("AMQS");
            msg.setObj(command);
            send(msg);
        }
        
        synchronized public void requestMsg(Agent agent)
        {
            count=0;
            this.agent=agent;
            Wrapper msg=new Wrapper();
            msg.setSender((String) agent.getID());
            msg.setReceiver("AMQS");
            msg.setPerformative(Wrapper.REQUEST);
            msg.setObj("request");
            send(msg);
            if(count>0) 
                agent.newMessages();
            this.agent=null;
        }

        public void requestMsg(Iterator receivers)
        {
            while(receivers.hasNext())
                requestMsg((Agent)receivers.next());
        } 
        
        private void processResponse(Wrapper msg) 
        {
          if(((String)msg.getObj()).contains("Response:"))
               connected=false;
            //System.out.println(msg.getReceiver()+">>"+ msg.getObj().toString().substring(9));
        }

        private void processCommand(Wrapper msg) 
        {
            //Command cmd=(Command) msg.getObj();
            //System.out.print(cmd);
        }
        
        synchronized private void send(Wrapper msg) 
        {
            conectarse();
            try 
            {
                salida.reset();
                salida.writeObject(msg);
                salida.flush();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ContainerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }     
            run();
        }
        
        public void sendMsg(Wrapper msg)
        {
            send(msg);
        }
	
	//metodo utilitario para cerrar toda la comunicacion exitente con la aplicacion cliente
	private void close()
	{
 		try 
		{	
			entrada.close();
			salida.close();
			coneccion.close();
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

