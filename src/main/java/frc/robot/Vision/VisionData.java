package frc.robot.Vision;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionData implements Sendable {

    // contains the data from the Rasbery Pie


    public static VisionData frontData = new VisionData(true);
    public static VisionData backData = new VisionData(false);


    public VisionData(boolean front){
        this.front = front;
    }

    class point {
        public double a; // angle
        public double d; // distance

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

    public boolean found() { 
        return found;
    }
    public double a1() {
        return p1.a;
    }
    public double d1() {
        return p1.d;
    }
    public double a2() {
        return p2.a;
    }
    public double d2() {
        return p2.d;
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
        String name = getName();
        builder.addBooleanProperty(name + " Found", this::found, null);
        builder.addDoubleProperty(name + " A1", this::a1, null);
        builder.addDoubleProperty(name + " D1", this::d1, null);
        builder.addDoubleProperty(name + " A2", this::a2, null);
        builder.addDoubleProperty(name + " D2", this::d2, null);
    }
}
