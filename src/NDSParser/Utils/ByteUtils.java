package NDSParser.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by Spencer on 6/14/19.
 */
public class ByteUtils {
    private static final boolean DEFAULT_LITTLE_ENDIAN = true;

    public static byte getByte(byte[] data, int addr){
        return data[addr];
    }

    public static int getUnsignedByte(byte[] data, int addr){
        return ((int)data[addr]) & 0xff;
    }

    public static byte[] getBytes(byte[] data, int from, int to){
        return Arrays.copyOfRange(data, from, to);
    }

    public static byte[] getNullTerminatedBytes(byte[] data, int start){
        int end = start;
        while(getByte(data, end) != 0){
            end++;
        }
        return getBytes(data, start, end);
    }

    public static String getASCII(byte[] data, int from, int to){
        return new String(getBytes(data, from, to), StandardCharsets.US_ASCII);
    }

    public static String getUTF16(byte[] data, int from, int to){
        return new String(getBytes(data, from, to), StandardCharsets.UTF_16LE);
    }

    public static String getNullTerminatedUTF8(byte[] data, int start){
        return new String(getNullTerminatedBytes(data, start), StandardCharsets.UTF_16LE);
    }

    public static int getInt(byte[] data, int addr, boolean littleEndian){
        int start = littleEndian ? addr + 3 : addr;
        int end = littleEndian ? addr: addr + 3;
        int delta = littleEndian ? -1 : 1;
        int out = 0;
        for(int i = start; i != end + delta; i += delta){
            out = (out << 8) + getUnsignedByte(data, i);
        }
        return out;
    }

    public static int getInt(byte[] data, int addr){
        return getInt(data, addr, DEFAULT_LITTLE_ENDIAN);
    }

    public static short getShort(byte[] data, int addr, boolean littleEndian){
        return (short)getUnsignedShort(data, addr, littleEndian);
    }

    public static short getShort(byte[] data, int addr){
        return getShort(data, addr, DEFAULT_LITTLE_ENDIAN);
    }

    public static int getUnsignedShort(byte[] data, int addr, boolean littleEndian){
        int start = littleEndian ? addr + 1 : addr;
        int end = littleEndian ? addr: addr + 1;
        int delta = littleEndian ? -1 : 1;
        int out = 0;
        for(int i = start; i != end + delta; i += delta){
            out = (out << 8) + getUnsignedByte(data, i);
        }
        return out;
    }

    public static int getUnsignedShort(byte[] data, int addr){
        return getUnsignedShort(data, addr, DEFAULT_LITTLE_ENDIAN);
    }
}
