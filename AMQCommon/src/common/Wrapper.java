//esta wrapper esta basada en la estructura definida de un mensaje del ACL KQML
package common;

import java.io.Serializable;
import java.util.Random;

public class Wrapper implements Serializable
{
    public static final int SUBSCRIBE=0;
    public static final int INFORM=1;
    public static final int TELL=2;
    public static final int REQUEST=3;
    public static final int COMMAND=4;
    public static final int RESPONSE=5;
    
    private String id,sender,receiver,priority,ontology;
    private int performative;
    private Object obj;

    public Wrapper() 
    {
        id="";
        setId();
    }
    
    public String getPriority() 
    {
        return priority;
    }

    public String getId() 
    {
        return id;
    }

    private void setId() 
    {
        Random rnd = new Random();
        for (int i = 0; i < 32; i++) 
           	this.id+= (int)(rnd.nextDouble() * 10.0);        
    }

    public int getPerformative() 
    {
        return performative;
    }

    public void setPerformative(int performative) 
    {
        this.performative = performative;
    }

    public String getOntology() 
    {
        return ontology;
    }

    public void setOntology(String ontology) 
    {
        this.ontology = ontology;
    }

    public void setPriority(String priority) 
    {
        this.priority = priority;
    }
    
    public Object getObj()
    {
	return obj;
    }

    public void setObj(Object obj)
    {
	this.obj=obj;
    }

    public String getReceiver() 
    {
        return receiver;
    }

    public void setReceiver(String receiver) 
    {
        this.receiver = receiver;
    }

    public String getSender() 
    {
        return sender;
    }

    public void setSender(String sender) 
    {
        this.sender = sender;
    }
}
