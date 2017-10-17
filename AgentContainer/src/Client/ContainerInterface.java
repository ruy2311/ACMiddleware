/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import common.events.QueueEvent;
import java.util.HashMap;

/**
 *
 * @author ruy
 */
public interface ContainerInterface 
{
    public void newMsg(QueueEvent qe);
    public HashMap getGuisMap();
}
