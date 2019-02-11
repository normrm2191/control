package frc.robot.Vision;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionData {

    // contains the data from the Rasbery Pie


    public static VisionData fronData = new VisionData();
    public static VisionData backData = new VisionData();


    class point extends SendableBase {
        public double a; // angle
        public double d; // distance

        @Override
        public void initSendable(SendableBuilder builder) {
            
		}
    }

    public point p1;
    public point p2;
    public boolean front;
    public boolean found = false;
    public long time = 0;

    public void set() {
        time = System.currentTimeMillis();
        double[] pts = {p1.a,p1.d,p2.a,p2.d};
        if(front) {
<<<<<<< HEAD
            fronData = this;
            SmartDashboard.putNumberArray("front: points", pts );
        } else {
            SmartDashboard.putNumberArray("back: points", pts );
            backData = this;
=======
       //     fronData = this;
        } else {
       //     backData = this;
>>>>>>> arcade_mode
        }
    }

}
