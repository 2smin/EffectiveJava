package Chapter01.Item09;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) {

    }

    private void runTryCatch(String path) throws IOException {

       BufferedReader br = new BufferedReader(new FileReader(path));
        try{
            br.readLine(); //예외 발생 가능
        }finally {
            br.close(); //예외 발생 가능
        }

        //무조건 닫아주어야 한다.
        //close의 예외가 readline예외를 가려서 debug를 힘들게 만든다
    }

    private void runOverlappedTryCatch(String src, String dest) throws IOException {

        InputStream in = new FileInputStream(src);

        try{
            byte[] buf = new byte[10];
            in.read(buf);
            OutputStream out = new FileOutputStream(dest);
            out.write(buf);

            try{
                out.close();
            }catch (IOException e){

            }finally {
                out.close(); //예외발생 가능 / close 불가
            }
        }catch (IOException e){

        }finally {
            in.close(); //예외발생 가능
        }

        //in 에서 예외 발생하면 out의 예외를 잡을 수 없음.
        //finally에서도 예외가 발생할 수 있다.

    }

    private void runTryWithResources(String src, String dest) throws IOException{

        try(InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest))
        {
            byte[] buf = new byte[10];
            int n;
            while((n = in.read(buf)) >= 0){
                out.write(buf,0,n);
            }
        }
        // in /out 에서 예외 둘 다 발생 시, 나중 예외도 버려지지 않는다.
    }
}
