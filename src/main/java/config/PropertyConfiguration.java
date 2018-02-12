//package config;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//public class PropertyConfiguration {
//
//    private PropertyConfiguration() throws IllegalAccessException {
//        throw new IllegalAccessException("Utility class");
//    }
//
//    private static final Properties prop = new Properties();
//
//    static {
//        try (InputStream input = new FileInputStream("resources/app.properties")) {
//            prop.load(input);
//            prop.list(System.out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String getProperty(String property) {
//        return prop.getProperty(property);
//    }
//
//}
