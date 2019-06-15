package NDSParser.Utils;

/**
 * Created by Spencer on 6/14/19.
 */
public class LZ77 {

    //TODO there's some LZ77 0x11 business that I haven't implemented yet. I should figure out what that even is.
    public static byte[] decompress(byte[] data){
        int size = (ByteUtils.getInt(data, 0, true) >> 8) & 0x00ffffff;
        byte[] out = new byte[size];

        int dataOff = 4;
        int writePtr = 0;

        while(dataOff < data.length){
            int flag = ByteUtils.getUnsignedByte(data, dataOff++);
            int mask = 0x80;
            for(int i = 0; i < 8; i++){
                if(dataOff >= data.length){
                    break;
                }
                int bitVal = flag & mask;
                mask >>= 1;
                if(bitVal != 0){
                    int val = ByteUtils.getUnsignedShort(data, dataOff, false);
                    dataOff += 2;

                    int copyStart = (writePtr - (val & 0xfff)) - 1;
                    int copyLength = (val >> 12) + 3;

                    for(int j = 0; j < copyLength; j++){
                        out[writePtr++] = out[copyStart + j];
                    }
                }
                else{
                    out[writePtr++] = data[dataOff++];
                }
            }
        }

        return out;
    }
}
