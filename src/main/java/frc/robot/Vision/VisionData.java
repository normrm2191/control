package frc.robot.Vision;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class VisionData implements Sendable {

    // contains the data from the Rasbery Pie

    public static final long MAX_VALID_TIME = 70;

    public static VisionData frontData = new VisionData(true);
    public static VisionData backData = new VisionData(false);

    public String name;


    public VisionData(boolean front){
        this.front = front;
        p1 = new point();
        p2 = new point();

        if(front) {
            setName("Front Vis");
        } else {
            setName("Back Vis");
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;   
    }

    @Override
    public String getName() {
        return name;
    }

    public class point {
        public double a; // angle
        public double d; // distance

        point() {
            a = 0;
            d = 0;
        }
    }

    public volatile point p1;
    public volatile point p2;
    public volatile boolean front;
    private volatile boolean found = false;
    public long time = 0;

    

    public void set(VisionData vd){
        p1.a = vd.p1.a;
        p1.d = vd.p1.d;
        p2.a = vd.p2.a;
        p2.d = vd.p2.d;
        front = vd.front;
        found = vd.found;
        time = System.currentTimeMillis();

//        System.out.println("point front : start - angle = " + frontData.p1.a + " length = " + 
//        frontData.p1.d + " / end - angle = " + frontData.p2.a + " length = " + frontData.p2.d);
//        System.out.println("point back : start - angle = " + backData.p1.a + " length = " + 
//        backData.p1.d + " / end - angle = " + backData.p2.a + " length = " + backData.p2.d);
    }
    public void set() {
        time = System.currentTimeMillis();
        if(front) {
            frontData.set(this);
        } else {
            backData.set(this);
        }
    }

    public boolean found() { 
        return found && (System.currentTimeMillis() - time) < MAX_VALID_TIME;
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

    public long time() {
        return time;
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
        builder.addBooleanProperty(name + "Found", this::found, null);
        builder.addDoubleProperty(name + " A1", this::a1, null);
        builder.addDoubleProperty(name + " D1", this::d1, null);
        builder.addDoubleProperty(name + " A2", this::a2, null);
        builder.addDoubleProperty(name + " D2", this::d2, null);
        builder.addDoubleProperty(name + " TIME", this::time, null);
    }
}
