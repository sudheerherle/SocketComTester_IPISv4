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
public class DisplayDataPacket {
    public byte[] window_left_column = new byte[]{(byte) 0x00, (byte)0x00};
    public byte[] window_right_column = new byte[]{(byte) 0x03, (byte)0xc0};
    public byte[] window_top_row = new byte[]{(byte) 0x00, (byte)0x00};
    public byte[] window_bottom_row = new byte[]{(byte) 0x00, (byte)0x0f};
    public byte reverse_and_speed = 0x03;
    public byte effect_code = 0x05;
    public byte size_and_gap = (byte) 0xff;
    public byte delay = 0x0a;
    
    public byte[] getDataAsBytes(){
        byte[] retval = new byte[12];
        System.arraycopy(window_left_column, 0, retval, 0, 2);
        System.arraycopy(window_right_column, 0, retval, 2, 2);
        System.arraycopy(window_top_row, 0, retval, 4, 2);
        System.arraycopy(window_bottom_row, 0, retval, 6, 2);
        retval[8] = reverse_and_speed;
        retval[9] = effect_code;
        retval[10] = size_and_gap;
        retval[11] = delay;        
        return retval;        
    }
    
}
