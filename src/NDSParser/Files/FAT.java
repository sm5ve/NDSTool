package NDSParser.Files;

import NDSParser.Cart;

import java.util.ArrayList;

/**
 * Created by Spencer on 6/11/19.
 */
public class FAT {
    protected FileHandle[] table;
    protected final byte[] memory;
    public FAT(Cart c){
        table = new FileHandle[c.getFATsize() / 8];
        for(int i = 0; i < table.length; i++){
            int addr = c.getFATaddr() + i * 8;
            int start = c.getInt(addr);
            int end = c.getInt(addr + 4);
            FileHandle e = new FileHandle(start, end);
            table[i] = e;
        }
        this.memory = c.data;
    }

    protected FAT(byte[] memory){
        this.memory = memory;
    }

    public FileHandle getFileEntry(int id){
        return table[id];
    }

    public byte[] copyFile(int id){
        byte[] out = new byte[this.getFileEntry(id).end - this.getFileEntry(id).start];
        for(int i = this.getFileEntry(id).start; i < this.getFileEntry(id).end; i++){
            out[i - this.getFileEntry(id).start] = this.memory[i];
        }
        return out;
    }

    public int getNumberIDs(){
        return table.length;
    }
}
