package frc.robot.Utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.Robot;

public class CameraHandler extends Thread {

    static final int WIDTH = 320;
    static final int HEIGHT = 240;

    UsbCamera camera;
    CvSink videoInF;
    CvSink videoInB;
    CvSource videoOut;
    VideoSink server;
    Mat mat = new Mat();
    CvSink activeIn;

    public CameraHandler() {
        try {
            CameraServer cs = CameraServer.getInstance();
            camera = cs.startAutomaticCapture();
            camera.setExposureAuto();
            camera.setFPS(25);
            camera.setResolution(WIDTH, HEIGHT);
            videoInF = cs.getVideo(camera);
            camera = CameraServer.getInstance().startAutomaticCapture(1);
            camera.setExposureAuto();
            camera.setFPS(25);
            camera.setResolution(WIDTH, HEIGHT);
            videoInB = cs.getVideo(camera);
            server = cs.getServer();
            videoOut = cs.putVideo("Camera", WIDTH, HEIGHT);
            server.setSource(videoOut);
            activeIn = videoInF;
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Camera NOT started - exception " + ex);
        }
    }

    static final double TOPdeg2Pix = WIDTH/45;
    static final double BOTdeg2Pix = WIDTH/60;
    static final double CENER = 200;
    static final Scalar clr = new Scalar(250,20,100);
    static final Scalar clr2 = new Scalar(20,20,250);
    static final int lineWidth = 3;
    static final Point p1center = new Point(CENER, 0);
    static final Point p2center = new Point(CENER, HEIGHT);
    static final Point p1_R10deg = new Point(CENER + 10 * TOPdeg2Pix, 0);
    static final Point p2_R10deg = new Point(CENER + 10 * BOTdeg2Pix, HEIGHT);
    static final Point p1_L10deg = new Point(CENER - 10 * TOPdeg2Pix, 0);
    static final Point p2_L10deg = new Point(CENER - 10 * BOTdeg2Pix, HEIGHT);
    static final Point p1_L20deg = new Point(CENER - 20 * TOPdeg2Pix, 0);
    static final Point p2_L20deg = new Point(CENER - 20 * BOTdeg2Pix, HEIGHT);
    static final Point pCenter = new Point(CENER , HEIGHT + 386);
    static final int R1 = 418;
    static final int R2 = 500;
    Point p1 = new Point(CENER,0);
    Point p2 = new Point(CENER,HEIGHT);

    @Override
    public void run() {
        while (true) {
            try {
                // add code to swithc cameras
                activeIn.grabFrame(mat);
                if (mat.width() != 0) {
                    // add the lines
                    Imgproc.line(mat, p1center , p2center, clr, lineWidth);
                    Imgproc.line(mat, p1_R10deg , p2_R10deg, clr, lineWidth);
                    Imgproc.line(mat, p1_L10deg , p2_L10deg, clr, lineWidth);
                    Imgproc.line(mat, p1_L20deg , p2_L20deg, clr, lineWidth);
                    Imgproc.circle(mat, pCenter, R1, clr, lineWidth);
                    Imgproc.circle(mat, pCenter, R2, clr, lineWidth);
                    // get the error in degrees from Arcade Drive
                    double error = 5;
                    p1.x = CENER + error * TOPdeg2Pix;
                    p2.x = CENER + error * BOTdeg2Pix;
                    Imgproc.line(mat, p1 , p2, clr2, lineWidth);
                    videoOut.putFrame(mat);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
