package NDSParser.Files.NARC;

import NDSParser.Files.FileObject;
import NDSParser.Utils.LZ77;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class CARC {
    public static NARC decode(FileObject obj) throws BadNARCException {
        byte[] data = LZ77.decompress(obj.copyFile());
        return new NARC(data, 0, data.length);
    }
}
