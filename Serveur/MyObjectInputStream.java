package inputOutput;

// Importing input output classes
import java.io.*;
// Importing utility classes
import java.util.*;

public class MyObjectInputStream extends ObjectInputStream {
 
    // Constructor of this class
    // 1. Default
    public MyObjectInputStream() throws IOException
    {
 
        // Super keyword refers to parent class instance
        super();
    }
 
    // Constructor of this class
    // 1. Parameterized constructor
    public MyObjectInputStream(InputStream o) throws IOException
    {
        super(o);
    }
 
    // Method of this class
    public void writeStreamHeader() throws IOException
    {
        return;
    }
}