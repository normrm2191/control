package frc.robot.Vision;

public class VisionData {

    // contains the data from the Rasbery Pie


    public static final VisionData fronData = new VisionData();
    public static final VisionData backData = new VisionData();


    class point {
        public double a; // angle
        public double d; // distance
    }

    public point p1;
    public point p2;
    public boolean front;
    public boolean found = false;
    public long time = 0;

    public void set() {
        time = System.currentTimeMillis();
        if(front) {
       //     fronData = this;
        } else {
       //     backData = this;
        }
    }

}
