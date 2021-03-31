package file;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ReversedFileReaderTest {
    static Path txtFileDirectory;

    @BeforeAll
    static void setupFileDirectory() throws URISyntaxException {
        txtFileDirectory = new File(
                ReversedFileReaderTest.class.getResource("").toURI()).toPath();
    }

    @Test
    void testReadLastChars() throws IOException {
        File txtFile = txtFileDirectory.resolve("File1.txt").toFile();

        ReversedFileReader rfr = new ReversedFileReader(null,null);
        int num = 17;
        String res = rfr.readLastChars(new FileInputStream(txtFile),num);

        assertEquals(num, res.length());
        assertEquals("line;\n" + "Third Line.", res);

    }

    @Test
    void testReadLastStrings() throws IOException {
        File txtFile = txtFileDirectory.resolve("File1.txt").toFile();

        ReversedFileReader rfr = new ReversedFileReader(null,null);
        int num = 2;
        String res = rfr.readLastString(new FileInputStream(txtFile),num);

        assertEquals("Second line;\n"+"Third Line.", res);

    }

    @Test
    void testReadEmptyFile() throws IOException {
        File txtFile = txtFileDirectory.resolve("EmptyFile.txt").toFile();
        ReversedFileReader rfr = new ReversedFileReader(null,null);

        int num = 2;
        String resStr = rfr.readLastString(new FileInputStream(txtFile),num);
        assertTrue(resStr.isEmpty());

        String resChars = rfr.readLastChars(new FileInputStream(txtFile),num);
        assertEquals(0, resChars.length());
        assertTrue(resChars.isEmpty());
    }

    @Test
    void testTransfer() throws IOException {
        File txtFile = txtFileDirectory.resolve("File1.txt").toFile();
        File out = txtFileDirectory.resolve("new.txt").toFile();
        ReversedFileReader rfr = new ReversedFileReader(new File[]{txtFile},out);

        int strNum = 2;
        rfr.transferLastStrings(strNum);
        String strRes = rfr.readLastString(new FileInputStream(out),strNum);

        assertEquals("Second line;\n"+"Third Line.", strRes);

        int charNum = 17;
        rfr.transferLastChars(charNum);
        String charsRes = rfr.readLastChars(new FileInputStream(out),charNum);

        assertEquals(charNum, charsRes.length());
        assertEquals("line;\n"+"Third Line.", charsRes);

        out.delete();
    }

    @Test
    void testTwoFilesTransfer() throws IOException {
        File txtFile1 = txtFileDirectory.resolve("File1.txt").toFile();
        File txtFile2 = txtFileDirectory.resolve("File2.txt").toFile();
        File out = txtFileDirectory.resolve("new.txt").toFile();
        ReversedFileReader rfr = new ReversedFileReader(new File[]{txtFile1,txtFile2},out);

        int strNum = 1;
        rfr.transferLastStrings(strNum);
        String expectedStr = txtFile1.getAbsolutePath().toString() + "\n" + "Third Line.\n" +
                txtFile2.getAbsolutePath() + "\n" + "Sixth line.";
        String strRes = rfr.readLastString(new FileInputStream(out),strNum*4);

        assertEquals(expectedStr, strRes);

        int charNum = 20;
        rfr.transferLastChars(charNum);
        String file1Path = txtFile1.getAbsolutePath().toString();
        String file2Path = txtFile2.getAbsolutePath().toString();
        String expectedChar = file1Path + "\n" + "nd line;\nThird Line.\n" +
                 file2Path + "\n" + "th line;\nSixth line.";
        String charsRes = rfr.readLastString(new FileInputStream(out),6);

        assertEquals(expectedChar, charsRes);

        out.delete();
    }

}
