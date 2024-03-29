package NDSParser.Files;

import NDSParser.Cart;
import NDSParser.Files.NARC.NARC;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Spencer on 6/11/19.
 */
public class FNT {
    protected FNTEntry[] entries = new FNTEntry[0xffff];

    protected int registered = 0;
    protected HashMap<Integer, ArrayList<Integer>> fileTree = new HashMap<>();

    public FNT(Cart c){
        int addr = c.getFNTaddr();

        int subdirs = c.getUnsignedShort(addr + 0x6);

        FNTEntry root = new FNTEntry("", 0xf000, FNTEntryType.SUBDIRECTORY, 0xf000);
        this.addFNTEntry(root);

        processSubtable(c, addr + c.getInt(addr), c.getUnsignedShort(addr + 4), 0xf000);
        for(int i = 0; i < subdirs - 1; i++){
            int parent = 0xf000 | (i + 1);
            processSubtable(c, addr + c.getInt(addr + (i + 1) * 8), c.getUnsignedShort(addr + (i + 1) * 8 + 4), parent);
        }
    }

    protected FNT(){

    }

    public ArrayList<Integer> getFileIDsInDirectory(int id) throws BadFileException {
        if(id < 0 || id >= entries.length || entries[id] == null || entries[id].getType() != FNTEntryType.SUBDIRECTORY){
            throw new BadFileException();
        }
        if(fileTree.get(id) == null){
            return new ArrayList<>();
        }
        return fileTree.get(id);
    }

    public FNTEntry getFileFromID(int id) throws BadFileException{
        if(id < 0 || id >= entries.length || entries[id] == null){
            throw new BadFileException();
        }
        return entries[id];
    }

    protected void processSubtable(Cart c, int addr, int fileBase, int parent){
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
                addr += len + 1;
                FNTEntry entry = new FNTEntry(name, fileBase, FNTEntryType.FILE, parent);
                fileBase++;
                addFNTEntry(entry);
            }
            if(type > 0x80){
                int len = type - 0x80;
                String name = c.getASCII(addr + 1, addr + 1 + len);
                int id = c.getUnsignedShort(addr + 1 + len);
                addr += len + 3;
                FNTEntry entry = new FNTEntry(name, id, FNTEntryType.SUBDIRECTORY, parent);
                addFNTEntry(entry);
            }
        }
    }

    protected void addFNTEntry(FNTEntry entry){
        int parent = entry.getParent();
        if(this.entries[entry.getId()] != null){
            System.err.println("huh");
        }
        this.entries[entry.getId()] = entry;
        if(!this.fileTree.containsKey(parent)){
            this.fileTree.put(parent, new ArrayList<Integer>());
        }
        this.fileTree.get(parent).add(entry.getId());
        if(entry.getId() != 0xf000){
            registered++;
        }
    }

    public static enum FNTEntryType{
        FILE, SUBDIRECTORY
    }

    public static class FNTEntry{
        private final String name;
        private final int id;
        private final FNTEntryType type;
        private final int parent;

        public FNTEntry(String name, int id, FNTEntryType type, int parent){
            this.name = name;
            this.id = id;
            this.type = type;
            this.parent = parent;
        }

        public String getName(){
            return name;
        }

        public int getId(){
            return this.id;
        }

        public FNTEntryType getType(){
            return type;
        }

        public int getParent(){
            return parent;
        }

        @Override
        public String toString(){
            return "FNTEntry{name: " + name + ", id: " + Integer.toHexString(id) + ", type: " + type + ", parent: " + Integer.toHexString(parent) + "}";
        }
    }

    public boolean isEmpty(){
        return registered == 0;
    }

    public boolean hasEntry(int id){
        return this.entries[id] != null;
    }

}
