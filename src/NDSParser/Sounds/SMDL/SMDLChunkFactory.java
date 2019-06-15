package NDSParser.Sounds.SMDL;

import NDSParser.Cart;
import NDSParser.Tuple;

/**
 * Created by Spencer on 6/11/19.
 */
public interface SMDLChunkFactory {
    int getChunkLabel();
    Tuple<SMDLChunk, Integer> parseChunk(Cart c, int base) throws BadSMDLException;
}
