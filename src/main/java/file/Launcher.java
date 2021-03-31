package file;

import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;


public class Launcher {

    public static final int DEFAULT_N_VALUE = 10; // по-умолчанию 10 строк

    //exclusive = true - можно ввести только 1 ключ -c или -n
    //multiplicity = "0..1" - может быть всего 1 из ключей -c или -n, либо они могут отсутствовать
    @ArgGroup(exclusive = true,multiplicity = "0..1")
    private Operation operation = new Operation();
    static class Operation{
        @Option(names ="-c", description = "Read last <c> characters of input files")
        private Integer c;
        @Option(names ="-n", description = "Read last <n> lines of input files")
        private Integer n;
    }

    @Parameters(index = "0..*" ,description = " the input files")
    private File[] inputFiles;

    @Option(names ="-o", description = "the output file")
    private File outputFile;

    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            throw new NoSuchElementException("No command found: expected to find \"tail\"");
        }
        if(!args[0].equals("tail")){
            throw new IllegalArgumentException(args[0] + " command is not found");
        }
        new Launcher().launch(Arrays.copyOfRange(args,1,args.length));
    }

    public void launch(String[] args) throws IOException {
        CommandLine.populateCommand(this,args);
        ReversedFileReader rfr = new ReversedFileReader(inputFiles == null ? new File[0] : inputFiles, outputFile);
        if(operation.c != null){
            rfr.transferLastChars(operation.c);
        } else{
            rfr.transferLastStrings(operation.n == null ? DEFAULT_N_VALUE : operation.n);
        }
    }
}
