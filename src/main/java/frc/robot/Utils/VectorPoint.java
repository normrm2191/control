package frc.robot.Utils;


public class VectorPoint {
    public double x; // in rad
    public double y; // in mm

    public VectorPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public VectorPoint() {
        this(0,0);
    }
    public VectorPoint(Vector v) {
        this(v.x(), v.y());
    }

    public double distance(double px, double py) {
        double dx = px - x;
        double dy = py - y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public double distance() {
        return distance(0,0);
    }
    public double distance(VectorPoint p) {
        return distance(p.x,p.y);
    }

    public double angle(double px, double py) {
        double dx = x - px;
        double dy = y - py;
        double res = 0;
        if(dy == 0) {
            res = Math.PI;
        } else {
            res = Math.atan(dx/dy);
        }
        if(dx < 0) {
            return -res;
        } else {
            return res;
        }
    }
    public static VectorPoint convertToPoint(Vector v){
        VectorPoint p = new VectorPoint();
        p.x=v.length*Math.cos(v.angle)  ;
        p.y=v.length*Math.sin(v.angle);
        return p;
        
     }
     public VectorPoint ReverseVectorpointV(VectorPoint v){// the function get VectorPoint and Return the his Reverse
         v.y=-v.y;
         v.x=-v.x;
         return v;
     }

     public void reverseVecotorPoint(){
         y = -y;
         x = -x;
     }

    public VectorPoint ReverseVectorPoint(Vector v){// the function get Vector and retrurn his Reverse Vectorpoint
       VectorPoint t= convertToPoint(v);
       ReverseVectorpointV(t);
       return t;
     }
     
     public static VectorPoint add(VectorPoint v1 , VectorPoint v2){
         return new VectorPoint(v1.x + v2.x, v1.y + v2.y);
     }

     public static VectorPoint sub (VectorPoint v1 , VectorPoint v2){
        return new VectorPoint(v1.x - v2.x, v1.y - v2.y);
    }
    public static double GetAngleOfVector(VectorPoint v){
        return Math.atan(v.y/v.x);
    }
    public double GetAngleOfVector(){
        return Math.atan(y/x);
    }
    public static double GetLengthOfVector(VectorPoint p){
        return Math.sqrt(Math.pow(p.y, 2)+Math.pow(p.x,2));
    }
    public double GetLengthOfVector(){
        return Math.sqrt(Math.pow(y, 2)+Math.pow(x,2));
    }
}