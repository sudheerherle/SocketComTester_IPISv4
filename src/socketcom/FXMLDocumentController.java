/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketcom;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
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
        DataPacket dp = new DataPacket();
        byte[] data = dp.getDataAsBytes();
        String msg = SharedData.bytesToHex(data);           
        System.out.println(msg);
        if(this.socket!=null){
            socket.write(data);
        }
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
