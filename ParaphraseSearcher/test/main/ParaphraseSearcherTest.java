package main;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ParaphraseSearcherTest {

    @Test
    public void testCreate() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("../test.properties")));
            ParaphraseSearcher paraphraseSearcher = new ParaphraseSearcher(properties.getProperty("test.filename"));
        } catch(IOException e) {
            Assert.fail();
        }
    }
}