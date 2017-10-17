/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.events;

import java.util.EventListener;

/**
 *
 * @author ruy
 */
public interface QueueListener extends EventListener 
{
    public void newMessages(QueueEvent qe);
}
