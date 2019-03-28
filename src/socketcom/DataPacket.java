/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketcom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 *
 * @author I14746
 */
public class DataPacket {
    /*
    BLOCK 1
    */
    public byte[] data_identifiers = new byte[]{(byte)0xaa,(byte)0x99}; 
    public byte[] format_identifier = new byte[]{(byte)0x3,(byte)0x00};    
    /* ****d_length*******
    size = 1 byte if format identifier is not used, 2 if format identifier is used, so it is always 2 bytes in size.
    it is number of bytes in between source MSB and CRC LSB (including these two)*/    
    public byte[] d_length = new byte[]{(byte)0x03,(byte)0xd7};     
    public byte[] d_source = new byte[]{(byte)0xfd,(byte)0x00};
    public byte[] d_destination = new byte[]{(byte)0x03,(byte)0x00};
    public byte packet_serial_number = 0x00;
    
    /*
    END OF BLOCK 1
    */
    
    /*
    BLOCK 2
    */
    
    public byte function_code = (byte) 0x81;
    public byte serial_number = 0;
    public byte continuity = 0;
    public byte nornam_or_default = 0;
    public byte[] data = new byte[972];
    
    /*
    END OF BLOCK 2
    */
    
    /*
    BLOCK 3
    */
    public byte[] crc16 = new byte[2];
    /*
    END OF BLOCK 3
    */
    
    public byte[] getByteData(BufferedImage userSpaceImage) {
    WritableRaster raster = userSpaceImage.getRaster();
    DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
    return buffer.getData();
}
    
    public byte[] getDataAsBytes(){
        int length_of_total_frame = getTotalLength();
        byte[] byte_as_array = new byte[length_of_total_frame];
        System.arraycopy(data_identifiers, 0, byte_as_array, 0, data_identifiers.length);
        System.arraycopy(format_identifier, 0, byte_as_array, data_identifiers.length, format_identifier.length);
        System.arraycopy(d_length, 0, byte_as_array, 4, d_length.length);
        System.arraycopy(d_source, 0, byte_as_array, 6, d_source.length);
        System.arraycopy(d_destination, 0, byte_as_array, 8, d_destination.length);
        byte_as_array[10] = packet_serial_number;
        byte_as_array[11] = function_code;
        byte_as_array[12] = serial_number;
        byte_as_array[13] = continuity;
        byte_as_array[14] = nornam_or_default;
        DisplayDataPacket ddp = new DisplayDataPacket();
        System.arraycopy(ddp.getDataAsBytes(), 0, data, 0, 12);         
        System.arraycopy(data, 0, byte_as_array, 15, data.length);
        byte[] crc_array = new byte[length_of_total_frame-6];
        System.arraycopy(byte_as_array, 4, crc_array, 0, crc_array.length);
        int crc = getCRC16_SBI(crc_array);        
        int crc_standard = getCRC16_standard(crc_array);
        crc16[0] = (byte) ((crc & 0xff00) >> 8);
        crc16[1] = (byte) ((crc & 0xff));
        System.arraycopy(crc16, 0, byte_as_array, byte_as_array.length-2, crc16.length);
        return byte_as_array;
    }
    
    private int getCRC16_standard(byte[] bytes){
        int POLYNOMIAL = 0x1021; //reverse polynomial of 0x1021
        int current_crc_value = 0xffff;
           for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((current_crc_value >> 15    & 1) == 1);
                current_crc_value <<= 1;
                if (c15 ^ bit) current_crc_value ^= POLYNOMIAL;
            }
        }

    return current_crc_value & 0xFFFF;
    }
    
    private int getCRC16_SBI(byte[] bytes){
        int cnt;
	short CRCITTSum = (short) 0xFFFF;
	short byte_value;
	int bit_index;
	for (cnt = 0; cnt < bytes.length ; cnt++)
	{
		byte_value = bytes[cnt];
		byte_value  <<=  8;
		for (bit_index=0; bit_index < 8 ; bit_index++)
		{
			if (((CRCITTSum ^ byte_value) & 0x8000) != 0)
			{
				CRCITTSum = (short) ((CRCITTSum <<1) ^ (short)0x1021);
			}
			else
			{
				CRCITTSum <<= 1;
			}

			byte_value <<= 1;
		}
	}
	return(CRCITTSum);
        
        /*
        public static int crc16(byte[] data) {
    int current_crc_value = PRESET_VALUE;
    for (int i = 0; i < data.length; i++) {
        current_crc_value ^= data[i] & 0xFF;
        for (int j = 0; j < 8; j++) {
            if ((current_crc_value & 1) != 0) {
                current_crc_value = (current_crc_value >>> 1) ^ POLYNOMIAL;
            } else {
                current_crc_value = current_crc_value >>> 1;
            }
        }
    }

    return current_crc_value & 0xFFFF;
}
        */
    }
    
    private int getTotalLength(){
        int l = (d_length[0] << 8) + (d_length[1] & 0xff) ;
        l = l + 6; //Adding 6 because 2 identifiers + 2 format identifiers + 2 crc16
        return l;
    }

    void setTextData(byte[] bData) {
       if(bData.length>960)System.arraycopy(bData, 0, data, 12, 960);
       else {
//           int offset = 960 + 12 - bData.length;
//byte[] b = new byte[] {(byte)00,(byte)00,(byte)0xff,(byte)0xff,(byte)0x40,
//    (byte)0x0,(byte)0x10,0,0x08,00,0x04,00,0x02,0,0x04,00,0x08,
//    00,0x10,00,(byte)0x80,00,(byte)0xff,(byte)0xff};
           System.arraycopy(bData, 0, data, 12, bData.length);
       }
    }
}
