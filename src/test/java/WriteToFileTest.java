import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class WriteToFileTest extends TestCase {
    public void test_append() throws IOException{
        String path = "test_file.txt";
        new File(path).createNewFile();
        String text = "blabla";
        writeListToFile(text, path);
        List content = Files.readAllLines(Paths.get(path));
        writeListToFile(text, path);
        content = Files.readAllLines(Paths.get(path));
        writeListToFile(text, path);
        String actual = Files.readAllLines(Paths.get(path)).get(0);
        assertEquals("blablablablablabla", actual);
    }

    private void writeListToFile (String text, String path){
        try {
            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
