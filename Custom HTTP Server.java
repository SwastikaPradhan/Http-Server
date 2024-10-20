import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomHttpServer {

    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

           
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClientRequest(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
           
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

           
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            if ("GET".equalsIgnoreCase(method)) {
               
                handleGetRequest(path, out);
            } else {
                
                out.println("HTTP/1.1 405 Method Not Allowed");
                out.println("Content-Type: text/plain");
                out.println();
                out.println("405 Method Not Allowed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleGetRequest(String path, PrintWriter out) {
      
        if ("/".equals(path)) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>Welcome to Custom Java HTTP Server</h1></body></html>");
        } else {
           
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>404 - Page Not Found</h1></body></html>");
        }
    }
}
