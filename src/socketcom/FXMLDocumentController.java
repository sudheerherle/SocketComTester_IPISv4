/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketcom;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author I14746
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private TextField txtFieldIPAddress;
    
    @FXML
    private TextField txtFieldPortNumber;
    @FXML
    private TextField textFieldText;
    
    @FXML
    private TextArea textAreaDataRecieved;
    @FXML
    private TextArea textAreaDataToSend;
    
    @FXML 
    private TextArea textAreaDataSent;
    @FXML 
    private TextArea textAreaStatus;
    
    private SocketClient socket;
    
    
    @FXML
    private void btnSendDisplayDataAction(ActionEvent event) {
        ArrayList<Byte> list = getTextasByteList();
        byte[] bData = new byte[list.size()];
        for(int b=0;b<list.size();b++){
            bData[b] = list.get(b);
        }
        DataPacket dp = new DataPacket();
        dp.setTextData(bData);
        byte[] data = dp.getDataAsBytes();
        String msg = SharedData.bytesToHex(data);           
        System.out.println(msg);
        if(this.socket!=null){
            socket.write(data);
        }
    }
    
    private ArrayList<Byte> getTextasByteList(){
        UnicodeFont uf = new UnicodeFont();
        String t = textFieldText.getText();
        BufferedImage bi= uf.stringToBufferedImage(t);
        int psize = bi.getColorModel().getPixelSize();
        int pi_index=0;
        byte m_byte = 0;
        int p_data = 0;
//        DataBuffer f = bi.getData().getDataBuffer();
        int w = bi.getWidth();
        int h = bi.getHeight();
        int s = 0;
        if((w * h)%8>0){
            s = (w*h)/8 + 1;
        }else s = (w*h)/8;
        byte[] n_data = new byte[s];
        ArrayList<Byte> list = new ArrayList<Byte>();
        for(int iw=0; iw<w;iw++){
            for(int ih=0;ih<h;){               
               for(int k=0;k<8;k++,ih++){                
                if(k>=h || ih>=h){
                    ih=0;iw = iw+1;
                }
                if(ih>=h){
                 ih=0;   
                }
                if(iw>=w){
                   break; 
                }
                p_data = bi.getRGB(iw, ih);  
                p_data = (p_data >> 24 ) & 0xff;
                if(p_data >0) p_data = 1;
                int temp  = p_data << k;
                m_byte += temp;
               }
               list.add(m_byte);
               if(iw>=w){
                   break; 
               }
        }
        }
        return list;
    }
    @FXML
    private void btnDataToSendAction(ActionEvent event){
        byte[] bytes = textAreaDataToSend.getText().getBytes();
        if(this.socket!=null){
            socket.write(bytes);
        }
    }
    @FXML
    private void btnConnectAction(ActionEvent event) {
        Output output = new Output() {
            @Override
            public void PrintDataSent(String msg) {
                textAreaDataSent.appendText(msg);
            }

            @Override
            public void PrintDataRecieved(String msg) {
               textAreaDataRecieved.appendText(msg);
            }

            @Override
            public void PrintStatusMessage(String msg) {
                textAreaStatus.appendText(msg);
            }
        };
        
        Platform.runLater(() -> {
            socket = new SocketClient(txtFieldIPAddress.getText(), Integer.parseInt(txtFieldPortNumber.getText()), output);
        });
        
//        Service<Void> service = new Service<Void>() {
//        @Override
//        protected Task<Void> createTask() {
//            return new Task<Void>() {           
//                @Override
//                protected Void call() throws Exception {
//                    //Background work                       
//                    final CountDownLatch latch = new CountDownLatch(1);
//                    Platform.runLater(new Runnable() {                          
//                        @Override
//                        public void run() {
//                            try{
//                                //FX Stuff done here
//                                socket = new SocketClient(txtFieldIPAddress.getText(), Integer.parseInt(txtFieldPortNumber.getText()), output);
//                            }finally{
//                                latch.countDown();
//                            }
//                        }
//                    });
//                    latch.await();                      
//                    //Keep with the background work
//                    return null;
//                }
//            };
//        }
//    };
//    service.start();
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
