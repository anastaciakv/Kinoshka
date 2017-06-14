package de.proximity.kinoshka.movielist.testutils;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestUtils {

    static InputStream getInputStreamFromFile(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public static String loadJSONFromFile(Object obj, String fileName) {
        String json = null;
        try {
            InputStream is = getInputStreamFromFile(obj, fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean assertListsEqual(List<?> expected, List<?> actual) {
        if (expected.size() != actual.size()) return false;
        for (int i = 0; i < actual.size(); i++)
            if (expected.get(i).equals(actual.get(i))) {
                return true;
            }
        return false;
    }
}
