/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.events;

import common.RegistrationInformation;
import java.io.Serializable;
import java.util.EventObject;


/**
 *
 * @author ruy
 */
public class RegistrationEvent extends EventObject implements Serializable
{
    RegistrationInformation registrationInformation;

    public RegistrationEvent(Object source, RegistrationInformation registrationInformation) 
    {
        super(source);
        this.registrationInformation = registrationInformation;
    }

    public RegistrationInformation getRegistrationInformation() 
    {
        return registrationInformation;
    }

    public void setRegistrationInformation(RegistrationInformation registrationInformation) 
    {
        this.registrationInformation = registrationInformation;
    }
    
    
    
    
}
