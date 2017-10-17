package Server;

import common.agent.AgentDefinition;
import common.RegistrationInformation;
import common.events.QueueEvent;

public interface AMQSInterface 
{
	public void removeClient(AgentConnection client);
        public boolean addAgent2XML(AgentDefinition ad);
        public void notifyMsg(QueueEvent qe);
        public Object getMsgQueue(Object agentID);
        public Object getGroup(Object group);
        public Object getGroupsFrom(Object agentID);
        public boolean registration(RegistrationInformation ri);
}
