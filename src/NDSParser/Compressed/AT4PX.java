package NDSParser.Compressed;

/**
 * Created by Spencer on 6/12/19.
 */
public class AT4PX {
    public static byte[] decode(byte[] data) throws BadCompressionException {
        if(data[0] != 0x41 || data[1] != 0x54 || data[2] != 0x34 || data[3] != 0x50 || data[4] != 0x58){
            throw new BadCompressionException();
        }

        //int compressedLength = ((data[5] & 0xff) << 8) | (data[6] & 0xff) - 0x12;
        //int decompressedLength = ((data[0x10] & 0xff) << 8) | (data[0x11] & 0xff);

        int compressedLength = ((data[6] & 0xff) << 8) | (data[5] & 0xff) - 0x12;
        int decompressedLength = ((data[0x11] & 0xff) << 8) | (data[0x10] & 0xff);

        byte[] ctrlFlags = new byte[9];
        for(int i = 0; i < 9; i++){
            ctrlFlags[i] = data[i + 0x7];
            System.out.printf("%d: 0x%x\n", i, ctrlFlags[i]);
        }

        byte[] out = new byte[decompressedLength];

        PX.decodePX(ctrlFlags, data, 0x12, out, compressedLength);

        return out;
    }
}
