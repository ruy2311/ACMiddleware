package Server;
import Server.DataStructure.GroupQueue;
import Server.DataStructure.RegistrationMap;
import Server.DataStructure.AgentQueue;
import common.agent.AgentDefinition;
import common.RegistrationInformation;
import common.events.QueueEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;


public class AMQServer implements AMQSInterface
{
    private Vector agents;
    private AgentQueue queue;
    private RegistrationMap registrationMap;
    private EventAdapter eventAdpater;
    private XMLParser parser;
    private GroupQueue groups;
    
   	
    public AMQServer() 
    {
        agents=new Vector(1,1);
        queue=new AgentQueue();
        groups=new GroupQueue();
        registrationMap=new RegistrationMap();
        eventAdpater=new EventAdapter();
        parser=new XMLParser();
        Iterator it=(Iterator)parser.loadXML(XMLParser.AGENTS);
        while(it.hasNext())
            queue.add(((AgentDefinition)it.next()).getId());
        
        it=(Iterator)parser.loadXML(XMLParser.GROUPS);
        while(it.hasNext())
            groups.add(it.next());
        
        queue.addQueueListener(eventAdpater);
        registrationMap.addRegistrationListener(eventAdpater);
    } // end Servidor
   	
    public void start() 
    {      
        AgentConnection ac;
        
        try 
	{
            ServerSocket serverSocket = new ServerSocket( 2311, 100 );
            System.out.println( "Servidor escuchando por el puerto 2311 ..." );
            //ecucha en espera de clientes
            while ( true )
            {
                ac=new AgentConnection(this);
                agents.add(ac);
                ac.setConnection(serverSocket.accept());
                System.out.println("Servidor escuchando por el puerto 2311 ..." );
            } // fin del while     
        }
      	catch ( IOException ioE ) 
	{
            ioE.printStackTrace();
      	}
    } // fin iniciarServidor()
	
    synchronized  public void  removeClient(AgentConnection client)
    {
	agents.remove(client);
        agents.trimToSize();      
        System.gc();
        System.out.println(agents.size()-1);
    }

    synchronized public Object getMsgQueue(Object agentID) 
    {
        return queue.getQueue(agentID);    
    }
    
    synchronized public Object getGroup(Object group)
    {
        return groups.getQueue(group);
    }
    
    synchronized public Object getGroupsFrom(Object agentID)
    { 
        return groups.getQueuesFrom(agentID);
    }

    synchronized public boolean registration(RegistrationInformation ri) 
    {
        return registrationMap.add(ri);
    }


    synchronized  public void notifyMsg(QueueEvent qe) 
    {
        queue.notifyMsg(qe);
    }
    
    synchronized public boolean addAgent2XML(AgentDefinition ad)
    {
        
        if(queue.contain(ad.getId()))
            return false;
        queue.add(ad.getId());
        return parser.addAgent(ad);
    }    
    
    public static void main(String[] args)
    {
        AMQServer amqs=new AMQServer();
        amqs.start();
    }
            
}
