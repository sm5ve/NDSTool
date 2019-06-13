package NDSParser.Commands;

import NDSParser.Cart;
import NDSParser.Files.FolderObject;

import java.util.HashMap;
import java.util.Scanner;


/**
 * Created by Spencer on 6/11/19.
 */
public class CommandLine {
    private FolderObject cd;
    private final Scanner sc;
    private final Cart c;

    private HashMap<String, Command> commands = new HashMap<String, Command>();

    public CommandLine(FolderObject cd, Cart c){
        this.cd = cd;
        this.c = c;
        this.sc = new Scanner(System.in);

        commands.put("ls", new CommandLS());
        commands.put("cd", new CommandCD());
        commands.put("print", new CommandPrint());
        commands.put("printstr", new CommandPrintString());
        commands.put("play", new CommandPlay());
        commands.put("stop", new CommandStop());
    }

    public void eval(){
        String command = sc.nextLine();
        String cmd;
        String[] args;
        if(command.indexOf(' ') != -1){
            cmd = command.substring(0, command.indexOf(' '));
            args = command.substring(command.indexOf(' ') + 1).split(" ");
        }
        else{
            cmd = command;
            args = new String[0];
        }
        if(this.commands.containsKey(cmd)){
            this.cd = this.commands.get(cmd).eval(this.cd, this.c, args);
        }
        else{
            System.out.println("Unknown command '" + cmd + "'");
        }

    }
}
