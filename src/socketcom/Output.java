/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketcom;

/**
 *
 * @author I14746
 */
public interface Output{
    public void PrintDataSent(String msg);
    public void PrintDataRecieved(String msg);
    public void PrintStatusMessage(String msg);
}