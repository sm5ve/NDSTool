package NDSParser.Files.NARC;

import NDSParser.Cart;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FNT;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Spencer on 6/11/19.
 */
public class BFNT extends FNT {

    public BFNT(Cart c, NARC n, int base) throws BadNARCException {
        super(c, null);


        if(!c.getASCII(base, base + 4).equals("BTNF")){
            throw new BadNARCException();
        }

        int addr = base + 8;

        //int addr = n.imgBase + c.getInt(base + 8);

        int subdirs = c.getUnsignedShort(addr + 0x6);

        //System.out.println(subdirs);

        FNTEntry root = new FNTEntry("", 0xf000, FNTEntryType.SUBDIRECTORY, 0xf000);
        this.addFNTEntry(root);

        processSubtable(addr + c.getInt(addr), c.getUnsignedShort(addr + 4), 0xf000);
        for(int i = 0; i < subdirs - 1; i++){
            processSubtable(addr + c.getInt(addr + (i + 1) * 8), c.getUnsignedShort(addr + (i + 1) * 8 + 4), c.getUnsignedShort(addr + (i + 1) * 8 + 6));
        }

        //for(FNTEntry entry : entries){
        //    if(entry != null){
        //        System.out.println(entry.getName());
        //    }
        //}
    }

    @Override
    protected void processSubtable(int addr, int fileBase, int parent){
        while(true){
            int type = c.getUnsignedByte(addr);
            if(type == 0){
                break;
            }
            if(type == 0x80){
                continue;
            }
            if(type < 0x80){
                int len = type;
                String name = c.getASCII(addr + 1, addr + 1 + len);
                System.out.println(name);
                addr += len + 1;
                FNTEntry entry = new FNTEntry(name, fileBase, FNTEntryType.FILE, parent);
                fileBase++;
                addFNTEntry(entry);
            }
            if(type > 0x80){
                int len = type - 0x80;
                String name = c.getASCII(addr + 1, addr + 1 + len);
                System.out.println(name);
                int id = c.getUnsignedShort(addr + 1 + len);
                addr += len + 3;
                FNTEntry entry = new FNTEntry(name, id, FNTEntryType.SUBDIRECTORY, parent);
                addFNTEntry(entry);
            }
        }
    }

}
