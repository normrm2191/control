package frc.robot.Vision;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

public class SocketHandlerTask extends Thread {

    public Socket socket;
    public boolean isFront;

    Gson gson;
    JsonStreamParser parser;

    VisionData visionData;

    public SocketHandlerTask(Socket socket) {
        super();
        this.socket = socket;
        gson = new Gson();
<<<<<<< HEAD
        try{
        parser = new JsonStreamParser(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
=======
  //      parser = new JsonStreamParser(new InputStreamReader(socket.getInputStream()));
>>>>>>> arcade_mode
        start();
    }

    @Override
    public void run() {
        while (socket != null && socket.isConnected()) {
            try {
                JsonElement element = parser.next();
                visionData = gson.fromJson(element, VisionData.class);
                visionData.set();
            } catch (Exception ex) {
                System.out.println("error in parse data  " + ex);
                if (socket.isInputShutdown() || socket.isClosed() || !socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (Exception e2) {

                        break;
                    }
                }
            }
        }
    }
<<<<<<< HEAD
}
=======
  }}
>>>>>>> arcade_mode
