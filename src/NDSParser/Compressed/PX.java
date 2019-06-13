package NDSParser.Compressed;

/**
 * Created by Spencer on 6/12/19.
 */
public class PX {
    public static void decodePX(byte[] ctrlFlags, byte[] data, int offset, byte[] out, int compressedLength){
        int ctrlByte = 0;

        int outInd = 0;

        int end = offset + compressedLength;

        while(offset < end){
            ctrlByte = data[offset++] & 0xff;
            int mask = 0x80;
            while(mask > 0){
                int bit = ctrlByte & mask;
                mask >>= 1;
                if(offset < end){
                    if(bit > 0){
                        out[outInd++] = data[offset++];
                        System.out.println("Copying byte");
                    }
                    else{
                        byte byt = data[offset++];
                        int nblow = (byt & 0xf);
                        int matchingIndex = doesByteMatchFlag(byt, ctrlFlags);
                        System.out.printf("Executing command: 0x%x\n", byt);
                        if(matchingIndex != -1){

                            System.out.printf("We got a match!", byt);


                            if(matchingIndex == 0){
                                int byte1 = nblow | (nblow << 4);
                                int byte2 = byte1;
                                System.out.printf("Index = 0", byt);
                                out[outInd++] = (byte)byte1;
                                out[outInd++] = (byte)byte2;
                                //TODO maybe this is wrong?
                            }
                            else{
                                int baseNybble = nblow;
                                if(matchingIndex == 1){
                                    baseNybble++;
                                }
                                if(matchingIndex == 5){
                                    baseNybble--;
                                }

                                int[] nybbles = {baseNybble, baseNybble, baseNybble, baseNybble};

                                if(1 <= matchingIndex && matchingIndex <= 4){
                                    nybbles[matchingIndex - 1] -= 1;
                                }
                                else{
                                    nybbles[matchingIndex - 5] += 1;
                                }

                                int byte1 = ((nybbles[0] & 0xf) << 4) | ((nybbles[1]) & 0xf);
                                int byte2 = ((nybbles[2] & 0xf) << 4) | ((nybbles[3]) & 0xf);

                                out[outInd++] = (byte)byte1;
                                out[outInd++] = (byte)byte2;
                            }

                            //System.err.println("Insert byte pattern");
                            //System.exit(0);
                            //break;
                        }
                        else{
                            System.out.printf("Copy back! 0x%x\n", byt);
                            short lookback = -0x1000;

                            lookback += (nblow << 8);
                            lookback += data[offset++] & 0xff;

                            int copyAmount = (byt >> 4) & 0xf;
                            copyAmount += 3;

                            System.out.println("Copying " + copyAmount + " bytes");

                            for(int i = 0; i < copyAmount; i++){
                                out[outInd + i] = out[outInd + lookback + i];
                            }

                            outInd += copyAmount;
                            //break;
                        }
                    }
                }
                else{
                    break;
                }
            }
        }
    }

    private static int doesByteMatchFlag(byte byt, byte[] flags){
        for(int i = 0; i < flags.length; i++){
            if(((byt >> 4) & 0xf) == (flags[i] & 0xf)){
                return i;
            }
        }
        return -1;
    }
}
