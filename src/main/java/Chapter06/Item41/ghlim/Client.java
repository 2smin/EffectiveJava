package Chapter06.Item41.ghlim;

import java.io.*;

public class Client {

    public static void main(String[] args) throws IOException {
        DTO dto = new DTO(1,"abc");

        File f= new File("test.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(f));
        objectOutputStream.writeObject(dto);
    }
}
