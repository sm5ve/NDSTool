package NDSParser.Sounds.SMDL;

import NDSParser.Cart;
import NDSParser.Utils.ByteUtils;
import NDSParser.Utils.Tuple;

/**
 * Created by Spencer on 6/11/19.
 */
public class SMDLEndChunkFactory implements SMDLChunkFactory {

    @Override
    public int getChunkLabel() {
        return 0x656f6320;
    }

    @Override
    public Tuple<SMDLChunk, Integer> parseChunk(byte[] data, int base) throws BadSMDLException {
        return new Tuple<>(new SMDLEndChunk(data, base), base + 16);
    }

    public static class SMDLEndChunk implements SMDLChunk{
        private SMDLEndChunk(byte[] data, int base) throws BadSMDLException{
            if(ByteUtils.getInt(data, base, false) != 0x656f6320){
                throw new BadSMDLException();
            }
        }
    }
}
