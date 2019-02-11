package frc.robot.Vision;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionData implements Sendable{

    // contains the data from the Rasbery Pie


    public static VisionData frontData = new VisionData();
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

    

    public void set(VisionData vd){
        p1.a = vd.p1.a;
        p1.d = vd.p1.d;
        p2.a = vd.p2.a;
        p2.d = vd.p2.d;
        front = vd.front;
        found = vd.found;
        time = vd.time;
    }
    public void set() {
        time = System.currentTimeMillis();
        double[] pts = {p1.a,p1.d,p2.a,p2.d};
        if(front) {
            frontData.set(this);
        } else {
            backData.set(this);
        }
    }

    @Override
    public String getName() {
        return front ? "Front vision" : "Back vision";
    }

    @Override
    public void setName(String name) {
        
    }

    @Override
    public String getSubsystem() {
        return null;
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        //builder.addStringProperty(getName(), () -> , null);
    }

}
