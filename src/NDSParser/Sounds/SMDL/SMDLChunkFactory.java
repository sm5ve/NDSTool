package NDSParser.Sounds.SMDL;

import NDSParser.Cart;
import NDSParser.Utils.Tuple;

/**
 * Created by Spencer on 6/11/19.
 */
public interface SMDLChunkFactory {
    int getChunkLabel();
    Tuple<SMDLChunk, Integer> parseChunk(byte[] data, int base) throws BadSMDLException;
}
