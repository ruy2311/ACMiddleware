/*
 * AplicationLoader.java
 *
 * Created on March 26, 2009, 12:06 AM
 */

package Client.GUI;


import common.Wrapper;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author  ruy
 */
public class AplicationLoader extends javax.swing.JFrame implements Runnable 
{
    private Socket connection;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private boolean connected;

    /** Creates new form AplicationLoader */
    public AplicationLoader() 
    {
        initComponents();
        connected=false;
    }
    
    public void connect()
    {
        try 
        {
            connection = new Socket(InetAddress.getByName("localhost"),2111);
            connection.setSoTimeout(5000);
            salida=new ObjectOutputStream(connection.getOutputStream());
            salida.flush();
            entrada= new ObjectInputStream(connection.getInputStream());
            connected=true;

        } 
        catch (IOException ex) 
        {
            Logger.getLogger(AplicationLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane2.setViewportView(jList2);

        jLabel1.setText("Aplicaciones Disponibles:");

        jButton1.setText("Load");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    Wrapper msg=new Wrapper();
    msg.setPerformative(Wrapper.COMMAND);
    msg.setObj(jList2.getSelectedValue());  
    send(msg);
}//GEN-LAST:event_jButton1ActionPerformed
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

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
		}//while( continuar )
		close();
    }


    
    synchronized public void send(Wrapper msg) 
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

    private void procesar(Wrapper msg) 
    {
            switch(msg.getPerformative())
            {
                case Wrapper.RESPONSE:
                    processResponse(msg);
                    break;
            }
    }

    private void processResponse(Wrapper msg) 
    {
        DefaultListModel modelo = new DefaultListModel();  
        Iterator it=((Vector)msg.getObj()).iterator();
        while(it.hasNext())
            modelo.addElement(it.next());
        jList2.setModel(modelo);

    }

    public void close()
    {
    	try
        {
            connected=false;
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

    public static void main(String args[])
    {
        final AplicationLoader app=new AplicationLoader();
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run() {
                app.setVisible(true);
            }
        });
        app.connect();
        new Thread(app).start();
        Wrapper msg=new Wrapper();
        msg.setPerformative(Wrapper.REQUEST);
        app.send(msg);
    }
}
