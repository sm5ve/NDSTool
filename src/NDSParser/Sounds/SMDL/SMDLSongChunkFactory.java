package NDSParser.Sounds.SMDL;

import NDSParser.Cart;
import NDSParser.Tuple;

/**
 * Created by Spencer on 6/11/19.
 */
public class SMDLSongChunkFactory implements SMDLChunkFactory {

    @Override
    public int getChunkLabel() {
        return 0x736f6E67;
    }

    @Override
    public Tuple<SMDLChunk, Integer> parseChunk(Cart c, int base) throws BadSMDLException {
        return new Tuple<>(new SMDLSongChunk(c, base), base + 64);
    }

    public static class SMDLSongChunk implements SMDLChunk{
        private SMDLSongChunk(Cart c, int base) throws BadSMDLException{
            if(c.getInt(base, false) != 0x736f6E67){
                throw new BadSMDLException();
            }
        }
    }
}
