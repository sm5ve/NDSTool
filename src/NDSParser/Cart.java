package NDSParser; /**
 * Created by Spencer on 6/11/19.
 */

import NDSParser.Utils.ByteUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Cart {
    public final byte[] data;

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
        return ByteUtils.getByte(this.data, addr);
    }

    public int getUnsignedByte(int addr){
        return ByteUtils.getUnsignedByte(data, addr);
    }

    public int getSize(){
        return data.length;
    }

    public byte[] getBytes(int from, int to){
        return ByteUtils.getBytes(data, from, to);
    }

    public byte[] getNullTerminatedBytes(int start){
        return ByteUtils.getNullTerminatedBytes(data, start);
    }

    public String getASCII(int from, int to){
       return ByteUtils.getASCII(data, from, to);
    }

    public String getUTF16(int from, int to){
        return ByteUtils.getUTF16(data, from, to);
    }

    public String getNullTerminatedUTF8(int start){
        return ByteUtils.getNullTerminatedUTF8(data, start);
    }

    public int getInt(int addr, boolean littleEndian){
        return ByteUtils.getInt(data, addr, littleEndian);
    }

    public int getInt(int addr){
        return ByteUtils.getInt(data, addr);
    }

    public short getShort(int addr, boolean littleEndian){
        return ByteUtils.getShort(data, addr, littleEndian);
    }

    public short getShort(int addr){
        return ByteUtils.getShort(data, addr);
    }

    public int getUnsignedShort(int addr, boolean littleEndian){
        return ByteUtils.getUnsignedShort(data, addr, littleEndian);
    }

    public int getUnsignedShort(int addr){
        return ByteUtils.getUnsignedShort(data, addr);
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
