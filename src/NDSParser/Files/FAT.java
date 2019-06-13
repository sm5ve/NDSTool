package NDSParser.Files;

import NDSParser.Cart;

import java.util.ArrayList;

/**
 * Created by Spencer on 6/11/19.
 */
public class FAT {
    protected FileHandle[] table;
    protected final Cart c;
    public FAT(Cart c){
        this.c = c;
        table = new FileHandle[c.getFATsize() / 8];
        for(int i = 0; i < table.length; i++){
            int addr = c.getFATaddr() + i * 8;
            int start = c.getInt(addr);
            int end = c.getInt(addr + 4);
            FileHandle e = new FileHandle(start, end);
            table[i] = e;
        }
    }

    protected FAT(Cart c, Object o){
        this.c = c;
    }

    public FileHandle getFileEntry(int id){
        return table[id];
    }

    public byte[] copyFile(int id){
        return this.c.getBytes(this.getFileEntry(id).start, this.getFileEntry(id).end);
    }

    public int getNumberIDs(){
        return table.length;
    }
}
