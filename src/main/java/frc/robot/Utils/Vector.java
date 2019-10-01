package frc.robot.Utils;


public class Vector {
    public double angle; // in dig
    public double length; // in mm

    public Vector(double angle, double length) {
     //   this.angle = Math.toRadians(angle);
     this.angle = angle;
        this.length = length;
    }
    public Vector() {
        this.angle = 0;
        this.length = 0;
    }


    public Vector(Vector v) {
        angle = v.angle;
        length = v.length;
    }

    public Vector(VectorPoint p) {
        length = p.GetLengthOfVector();
        angle = p.GetAngleOfVector();
    }

    public double x() {
        return length * Math.sin(Math.toRadians(angle));
    }
    public double y() {
        return length * Math.cos(Math.toRadians(angle));
    }

/*    public void normalized() {
        angle = normalizeAngle(angle);
    }

    public static double normalizeAngle(double angle) {
        double res = angle % Math.PI * 2;
        if(res > Math.PI) {
            return res - 2 * Math.PI;
        } else if(res < - Math.PI) {
            return res + 2 * Math.PI;
        } else {
            return res;
        }
    }

    public void reverse() {
        angle += Math.PI;
        normalized();
    }

    Vector reverse(Vector v) {
        Vector res = new Vector(v);
        res.reverse();
        return res;
    }

    public static Vector add(Vector v1, Vector v2) {
        VectorPoint p = new VectorPoint(v1);
        p.x += v2.x();
        p.y += v2.y();
        return new Vector(p);
    }
    public Vector ReverseVector(Vector v){
        if(v.angle>0){
            v.angle=v.angle-Math.PI;
        }else{
            v.angle=v.angle-Math.PI;
        }
        return v;
   
   
       }
    public Vector UdiRequestedVactor(Vector v1, Vector t2){
    Vector v8 = new Vector(1, 46);
     VectorPoint v = new VectorPoint(335, 387);
     Vector d = new Vector(1, 1);
     Vector r=new Vector(1, 1);
     Vector r2 = new Vector();
     if(v8.angle<0){
        r2 = new Vector(r.angle+Math.PI/2, r.length);
    }else{
        r2 = new Vector(r.angle-Math.PI/2, r.length); 
    }
     Vector v4 = add(v1,ConvertToVector(v));
     Vector v5=add((v4), d);
     Vector m = add((v5), r);
        
     Vector udiVector =new Vector(r2.angle+-Math.atan(r2.length/m.length), Math.sqrt(Math.pow(m.length,2)+Math.pow(r2.length,2)));
     return udiVector;
    }

    public static void main(String[] args) {
        Vector v1 = new Vector(-30,100);
        System.out.println(v1.toString());
        Vector v2 = new Vector(60,200);
        System.out.println(v2.toString());
        System.out.println(add(v1,v2).toString());
        System.out.println(add(v2,v1).toString());
    }
    
    */
    
    @Override
    public String toString() {
        return String.format("Vector - %.2fdeg/%.2fmm at %.2f/%.2f",angle, length, x(), y());
    }

   public static Vector ConvertToVector(VectorPoint p){
       double angle = Math.abs(Math.atan(p.x/p.y) * 180 / Math.PI);
       angle = p.x > 0 ? angle : -angle;
       return new Vector(angle, Math.sqrt(Math.pow(p.x,2)+Math.pow(p.y,2)));
   }

}