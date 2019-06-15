package NDSParser.Sounds.SMDL;

import NDSParser.Cart;
import NDSParser.Utils.ByteUtils;
import NDSParser.Utils.Tuple;

/**
 * Created by Spencer on 6/11/19.
 */
public class SMDLSongChunkFactory implements SMDLChunkFactory {

    @Override
    public int getChunkLabel() {
        return 0x736f6E67;
    }

    @Override
    public Tuple<SMDLChunk, Integer> parseChunk(byte[] data, int base) throws BadSMDLException {
        return new Tuple<>(new SMDLSongChunk(data, base), base + 64);
    }

    public static class SMDLSongChunk implements SMDLChunk{
        private SMDLSongChunk(byte[] data, int base) throws BadSMDLException{
            if(ByteUtils.getInt(data, base, false) != 0x736f6E67){
                throw new BadSMDLException();
            }
        }
    }
}
