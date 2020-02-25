import org.kohsuke.args4j.*;

import java.util.ArrayList;
import java.util.List;


public class Parser {

    @Option(name = "-u", usage = "unpacking file", forbids = {"-z"})
    private boolean unpack = false;

    @Option(name = "-z", usage = "packing file", forbids = {"-u"})
    private boolean pack = false;

    @Option(name = "-out", usage = "output to this file (default: inputname.txt)", metaVar = "OUTPUT")
    private String out;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws CmdLineException {
        new Parser().parseArgs(args);
    }

    public void parseArgs(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        //try {
            parser.parseArgument(args);
            if (arguments.isEmpty() || (!pack && !unpack) || (!arguments.get(0).equals("pack-rle") || arguments.size() != 2))
                throw new CmdLineException(parser, "No argument is given");
        /*} catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("pack-rle [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println("\nExample: pack-rle [-u|-z] [-out outputname.txt] inputname.txt");
            throw new IllegalArgumentException();
        }*/
        String input = arguments.get(1);
        PackRLEKt.packRLE(pack, input, out);
    }
}