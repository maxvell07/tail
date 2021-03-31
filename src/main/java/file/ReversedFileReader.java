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


    public String readLastChars(InputStream in, int num) throws IOException{
        if(num < 0){
            throw new IllegalArgumentException("Num must be non-negative");
        }
        if(num == 0){
            return "";
        }
        StringBuilder res = new StringBuilder();
        try(BufferedReader buffIn = new BufferedReader(new InputStreamReader(in))) {
            LinkedList<Character> chars = new LinkedList<>();
            int c;
            // Если получаем -1, значит достигли конца файла
            while((c = buffIn.read()) != -1){
                if(chars.size() >= num){
                    chars.remove();
                }
                chars.add((char) c);
            }
            for(Character ch : chars){
                res.append(ch);
            }
        }
        return res.toString();
    }

    public String readLastString(InputStream in, int num) throws IOException{
        if(num < 0){
            throw new IllegalArgumentException("Num must be non-negative");
        }
        if(num == 0){
             return "";
        }
        try(BufferedReader buffIn = new BufferedReader(new InputStreamReader(in))) {
            LinkedList<String> strings = new LinkedList<>();
            String s;
            //считываем строку из файла/консоли. Если строка == null, тогда достигли конца файла
            while((s = buffIn.readLine()) != null){
                // Если в списке закончилось место, тогда удаляю первый элемент
                if(strings.size() >= num){
                    strings.remove();
                }
                // вставляю новый элемент
                strings.add(s);
            }
            // соединяю строки из списка, вставляя между ними символ перехода на новую строку
            return String.join("\n", strings);
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

    public void transferLastChars(int n) throws IOException{
        //список строк на вывод
        List<String> strList = new ArrayList<>();
        if(files.length == 0){
            strList.add(readLastChars(System.in,n));
        }
        if(files.length == 1){
            strList.add(readLastChars(new FileInputStream(files[0]),n));
        }
        if(files.length >= 2){
            for(File f : files){
                strList.add(f.getAbsolutePath());
                String lastStrings = readLastChars(new FileInputStream(f),n);
                strList.add(lastStrings);
            }
        }
        write(String.join("\n",strList),outFile);

    }

    public void transferLastStrings(int n) throws IOException{
        List<String> strList = new ArrayList<>();
        if(files.length == 0){
            strList.add(readLastString(System.in,n));
        }
        if(files.length == 1){
            strList.add(readLastString(new FileInputStream(files[0]),n));
        }
        if(files.length >= 2){
            for(File f : files){
                strList.add(f.getAbsolutePath());
                String lastStrings = readLastString(new FileInputStream(f),n);
                strList.add(lastStrings);
            }
        }
        write(String.join("\n",strList),outFile);
    }

}
