import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Connection extends Thread {

    private BufferedReader input;
    private DataOutputStream output;
    private Socket socket;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new DataOutputStream(socket.getOutputStream());


    }

    @Override
    public void run() {
        try {

            System.out.println("Connected IPAddress :  #" + socket.getInetAddress() + "PORT NUMBER : " + socket.getPort());
            String s;
            int x =-1  ;
            while ((s = input.readLine()) != null) {
                if (s.contains("GET / HTTP/1.1")) {
                  x = 0 ;
                }

                else if (s.contains(".css")) {

                    x = 1;
                }
                else if (s.contains(".js")) {

                   x = 2;
                }
                else if (s.contains(".png")) {

                    x =3;
                }
                else if (s.contains(".jpg")) {

                    x =4;
                }
                else if (s.contains("/gl")) {
                     x = 5 ;

                }
                else if (s.contains("/ghp")) {
x = 6 ;
                }
                else if (s.contains("/bzu")) {
                    x = 7 ;

                }

                else if (s.contains(".html")) {

                    x = 8;
                }

                System.out.println(s);
                if (s.isEmpty()) {
                    break;
                }
            }
            System.out.println("X : " + x);
            String response ="";
        switch (x) {
            case 0 :   ReadHTMLFILE("Master\\index.html"); break;
            case 1 :  ReadCssFile () ; break ;
            case 2 :  ReadJSFILE () ; break ;

            case 3 : readPngFile () ;break;
            case 4 : readJpegFile() ; break;
            case 5 : redirectPage("www.google.com") ; break;
            case 6 : redirectPage("www.github.com") ; break;
            case 7 : redirectPage("www.birzeit.edu") ; break;
            case 8 :   ReadHTMLFILE("Master\\pageHtml.html"); break;

            default:ErrorPage() ;


        }

            System.out.println(response);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ErrorPage() throws IOException {
        String s = String.format("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "<title>Error</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "       <h1 style=\"color: red;\"> The file is not found</h1>\n" +
                "       <h2><b>Name : Dania Abdullah <br> Student ID : 1215276</b></h2>\n" +
                "       <h3>IP Address : %s And Port Number : %d </h3>\n" +
                "    </body>\n" +
                "</html>" , socket.getLocalAddress() , socket.getPort()) ;

        String  response = "HTTP/1.1 404 \r\n"; //version of http and 200 is status code which means all okay
        response += "Content-Type: text/html \r\n"; //response is in html format
        response += "Connection: close \r\n";
        response += "Content-Length: "+s.length() + " \r\n"; //length of response file
        response += "\r\n"; //after blank line we have to append file data
        output.write((response+s).getBytes());

        output.flush();
    }


    private void readPngFile() throws IOException {
        File file = new File("Master\\img2.png");
        InputStream inputStream = new FileInputStream(file);
        byte [] bytes = inputStream.readAllBytes() ;
        String  response = "HTTP/1.1 200 \r\n"; //version of http and 200 is status code which means all okay
        response += "Content-Type: image/png \r\n"; //response is in html format
        response += "Connection: close \r\n";
        response += "Content-Length: "+bytes.length + " \r\n"; //length of response file
        response += "\r\n"; //after blank line we have to append file data
        output.write(response.getBytes());
        output.write(bytes);
        output.flush();

    }
    private void readJpegFile() throws IOException {
        File file = new File("Master\\img1.jpg");
        InputStream inputStream = new FileInputStream(file);
        byte [] bytes = inputStream.readAllBytes() ;
        String  response = "HTTP/1.1 200 \r\n"; //version of http and 200 is status code which means all okay
        response += "Content-Type: image/jpeg \r\n"; //response is in html format
        response += "Connection: close \r\n";
        response += "Content-Length: "+bytes.length + " \r\n"; //length of response file
        response += "\r\n"; //after blank line we have to append file data
        output.write(response.getBytes());
        output.write(bytes);
        output.flush();

    }

    private void ReadCssFile() {
        File file = new File("Master\\style2.css");
        try {
            String s = "";
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                s += in.nextLine();
            }
            String  response = "HTTP/1.1 200 \r\n"; //version of http and 200 is status code which means all okay
            response += "Content-Type: text/css \r\n"; //response is in html format
            response += "Connection: close \r\n";
            response += "Content-Length: "+s.length() + " \r\n"; //length of response file
            response += "\r\n"; //after blank line we have to append file data
            output.write((response+s).getBytes());

            output.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private  void ReadHTMLFILE(String str) {
        File file ;
     file = new File(str);


        try {
            String s = "";
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                s += in.nextLine();
            }
            String  response = "HTTP/1.1 200 \r\n"; //version of http and 200 is status code which means all okay
            response += "Content-Type: text/html \r\n"; //response is in html format
            response += "Connection: close \r\n";
            response += "Content-Length: "+s.length() + " \r\n"; //length of response file
            response += "\r\n"; //after blank line we have to append file data
            output.write((response+s).getBytes());

            output.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private  void ReadJSFILE() {
        //ReadCssFile();

        File file = new File("Master\\js.html");
        try {
            String s = "";
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                s += in.nextLine();
            }
            String  response = "HTTP/1.1 200 \r\n"; //version of http and 200 is status code which means all okay
            response += "Content-Type: text/html \r\n"; //response is in html format
            response += "Connection: close \r\n";
            response += "Content-Length: "+s.length() + " \r\n"; //length of response file
            response += "\r\n"; //after blank line we have to append file data
            output.write((response+s).getBytes());

            output.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private  void redirectPage(String website) throws IOException {
        output.write("HTTP/1.1 302 Found\r\n".getBytes());
        output.write("Date: Tue, 11 Jan 2011 13:09:20 GMT\r\n".getBytes());
        output.write("Content-type: text/plain\r\n".getBytes());
        output.write("Server: vinit\r\n".getBytes());
        output.write(("Location: http://"+website+"\r\n").getBytes());
        output.write("Connection: Close".getBytes());
        output.write("\r\n\r\n".getBytes());
        output.flush();
    }


}
