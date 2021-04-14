package file;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ReversedFileReader {
    private File[] files;
    private File outFile;

    public ReversedFileReader(File[] files, File outFile){
        this.files = files;
        this.outFile = outFile;
    }

    private String readElement(BufferedReader buff, char flag) throws IOException{
        if(flag == 'c'){
            int c = buff.read();
            return c == -1 ? null : String.valueOf((char) c);
        }
        return buff.readLine();
    }

    public String read(InputStream in, int num, char flag) throws IOException{
        if(num < 0){
            throw new IllegalArgumentException("Num must be non-negative");
        }
        if(num == 0){
            return "";
        }
        try(BufferedReader buffIn = new BufferedReader(new InputStreamReader(in))) {
            LinkedList<String> lastElements = new LinkedList<>();
            String s;
            // Если получаем null, значит достигли конца файла
            while((s = readElement(buffIn,flag)) != null ){
                if(lastElements.size() >= num){
                    lastElements.remove();
                }
                lastElements.add(s);
            }
            return String.join(flag == 'c'? "" : "\n",lastElements);
        }
    }


    public void write(String s, File f) throws IOException {
        if(f == null){
            System.out.println(s);
            return;
        }
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f));
        try(BufferedWriter buffOut = new BufferedWriter(out)){
            buffOut.write(s);
        }
    }

    //flag = 'c' - chars
    //flag = 'n' - strings
    public void transfer(int n, char flag) throws IOException{
        //список строк на вывод
        List<String> strList = new ArrayList<>();
        if(files.length == 0){
            strList.add(read(System.in,n,flag));
        }
        if(files.length == 1){
            strList.add(read(new FileInputStream(files[0]),n,flag));
        }
        if(files.length >= 2){
            for(File f : files){
                strList.add(f.getAbsolutePath());
                String lastStrings = read(new FileInputStream(f),n,flag);
                strList.add(lastStrings);
            }
        }
        write(String.join("\n",strList),outFile);

    }
}
