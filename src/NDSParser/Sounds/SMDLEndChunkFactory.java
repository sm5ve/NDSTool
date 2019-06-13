package NDSParser.Sounds;

import NDSParser.Cart;
import NDSParser.Tuple;

/**
 * Created by Spencer on 6/11/19.
 */
public class SMDLEndChunkFactory implements SMDLChunkFactory {

    @Override
    public int getChunkLabel() {
        return 0x656f6320;
    }

    @Override
    public Tuple<SMDLChunk, Integer> parseChunk(Cart c, int base) throws BadSMDLException {
        return new Tuple<>(new SMDLEndChunk(c, base), base + 16);
    }

    public static class SMDLEndChunk implements SMDLChunk{
        private SMDLEndChunk(Cart c, int base) throws BadSMDLException{
            if(c.getInt(base, false) != 0x656f6320){
                throw new BadSMDLException();
            }
        }
    }
}
