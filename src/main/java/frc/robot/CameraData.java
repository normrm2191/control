/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Add your docs here.
 */
public class CameraData {
    
    public volatile double startAngle;
	public volatile double distance;
	public volatile double endAngle;
	public volatile boolean targetFound;
	private Socket socket;
	private ServerSocket socketServer;
    private InputStreamReader read;
    
    public CameraData() throws IOException{
        int portNumber = 1;
		socketServer = new ServerSocket(portNumber);
		startAngle = 0;
		distance = 0;
		endAngle = 0;
		targetFound = false;
    }
}
