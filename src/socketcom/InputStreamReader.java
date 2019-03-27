/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketcom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author I14746
 */
public class InputStreamReader
  implements Runnable
{
   protected InputStream in;
   protected List<ChangeListener> listeners;

   public InputStreamReader( InputStream in )
   {
     this.in = in;
     this.listeners = new ArrayList<ChangeListener>();
   }

   public void addChangeListener( ChangeListener<byte[]> l )
   {
     this.listeners.add( l );
   }

   public void run()
   {
       try {
           while(in.available()>0){
               byte[] bytes = new byte[in.available()]; //make this whatever you need
               try {
                   in.read( bytes );
               } catch (IOException ex) {
                   Logger.getLogger(InputStreamReader.class.getName()).log(Level.SEVERE, null, ex);
               }
               
               //need some more checking here to make sure 256 bytes was read, etc.
               //Maybe write a subclass of ChangeEvent
//               ChangeListener evt = new ChangeListener( bytes );
               for( ChangeListener l : listeners )
               {
                   l.changed(null, null,  bytes);
               }
           }   } catch (IOException ex) {
           Logger.getLogger(InputStreamReader.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
}
