package NDSParser; /**
 * Created by Spencer on 6/11/19.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class Cart {
    public final byte[] data;

    private static final boolean DEFAULT_ENDIANNESS = true;
    public Cart(File f) throws IOException {
        data = Files.readAllBytes(f.toPath());
    }

    public String getName(){
        return getASCII(0, 12);
    }

    public String getGamecode(){
        return getASCII(12, 16);
    }

    @Override
    public String toString(){
        return "NTR-" + getGamecode() + ": " + getName();
    }

    public byte getByte(int addr){
        return data[addr];
    }

    public int getUnsignedByte(int addr){
        return ((int)data[addr]) & 0xff;
    }

    public int getSize(){
        return data.length;
    }

    public byte[] getBytes(int from, int to){
        return Arrays.copyOfRange(data, from, to);
    }

    public byte[] getNullTerminatedBytes(int start){
        int end = start;
        while(getByte(end) != 0){
            end++;
        }
        return getBytes(start, end);
    }

    public String getASCII(int from, int to){
        return new String(getBytes(from, to), StandardCharsets.US_ASCII);
    }

    public String getUTF16(int from, int to){
        return new String(getBytes(from, to), StandardCharsets.UTF_16LE);
    }

    public String getNullTerminatedUTF8(int start){
        return new String(getNullTerminatedBytes(start), StandardCharsets.UTF_16LE);
    }

    public int getInt(int addr, boolean littleEndian){
        int start = littleEndian ? addr + 3 : addr;
        int end = littleEndian ? addr: addr + 3;
        int delta = littleEndian ? -1 : 1;
        int out = 0;
        for(int i = start; i != end + delta; i += delta){
            out = (out << 8) + getUnsignedByte(i);
        }
        return out;
    }

    public int getInt(int addr){
        return getInt(addr, DEFAULT_ENDIANNESS);
    }

    public short getShort(int addr, boolean littleEndian){
        return (short)getUnsignedShort(addr, littleEndian);
    }

    public short getShort(int addr){
        return getShort(addr, DEFAULT_ENDIANNESS);
    }

    public int getUnsignedShort(int addr, boolean littleEndian){
        int start = littleEndian ? addr + 1 : addr;
        int end = littleEndian ? addr: addr + 1;
        int delta = littleEndian ? -1 : 1;
        int out = 0;
        for(int i = start; i != end + delta; i += delta){
            out = (out << 8) + getUnsignedByte(i);
        }
        return out;
    }

    public int getUnsignedShort(int addr){
        return getUnsignedShort(addr, DEFAULT_ENDIANNESS);
    }


    public int getFATaddr(){
        return getInt(0x48);
    }

    public int getFATsize(){
        return getInt(0x4C);
    }

    public int getFNTaddr(){
        return getInt(0x40);
    }

    public int getFNTsize(){
        return getInt(0x44);
    }


}
