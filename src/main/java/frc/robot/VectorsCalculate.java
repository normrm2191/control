/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.Utils.Vector;
import frc.robot.Utils.VectorPoint;


/**
 * Add your docs here.
 */
public class VectorsCalculate {

    public final static double r = 700;
    public static Vector MID_TO_CAMERA;
    public static Vector START_LINE;
    public static Vector END_LINE;
    public static Vector LINE;
    public static Vector FIRST_STRAIGHT;
    public static Vector SECOND_STRAIGHT;
    public static Vector R;

    public VectorsCalculate(){
    }

    public static Vector calculateVector(double angle_start, double dis_start , double angle_end, double dis_end){
        START_LINE = new Vector(angle_start, dis_start);
        END_LINE = new Vector(angle_end, dis_end);
        Set_LINE();
        Set_MID_TO_CAMERA();
        Set_SECOND_STRAIGHT();
        Set_FIRST_STRAIGHT();
        return FIRST_STRAIGHT;
    }

    public static void Set_LINE(){
        VectorPoint temp_startLine= VectorPoint.convertToPoint(START_LINE);
        VectorPoint temp_endLine= VectorPoint.convertToPoint(END_LINE);
        VectorPoint temp = VectorPoint.sub(temp_endLine , temp_startLine);
        LINE = Vector.ConvertToVector(temp);
    }

    public static void Set_MID_TO_CAMERA(){
     //   VectorPoint temp = new VectorPoint(0,0);  //??
        Vector temp = new Vector(-10 , 200);
        MID_TO_CAMERA = temp;
    }

    public static void Set_SECOND_STRAIGHT(){
        double angle = LINE.angle;
        double dis = 1000;
        SECOND_STRAIGHT = new Vector(angle,dis);
    }

    public static void Set_FIRST_STRAIGHT(){
        double angle = 90 - Math.abs(LINE.angle) ;
        angle = LINE.angle >= 0 ? -angle : angle;
        Vector tempR = new Vector(angle , r); 
        VectorPoint temp1 = VectorPoint.sub(VectorPoint.convertToPoint(START_LINE), VectorPoint.convertToPoint(SECOND_STRAIGHT));
        double angle_temp1 = temp1.GetAngleOfVector();
        double length_temp1 = temp1.GetLengthOfVector();
        VectorPoint temp2 = VectorPoint.sub(temp1, VectorPoint.convertToPoint(tempR));
        double angle_temp2 = temp2.GetAngleOfVector();
        double length_temp2 = temp2.GetLengthOfVector();
        VectorPoint temp3 = VectorPoint.add(VectorPoint.convertToPoint(MID_TO_CAMERA), temp2);
        double angle_temp3 = temp3.GetAngleOfVector();
        double length_temp3 = temp3.GetLengthOfVector();
        double length = Math.sqrt(Math.pow(temp3.GetLengthOfVector(),2) - Math.pow(r, 2));
        angle = Math.abs(Math.asin(r / temp3.GetLengthOfVector()) * 180 / Math.PI) + Math.abs(temp3.GetAngleOfVector());
        angle = LINE.angle > 0 ? -angle : angle;
        FIRST_STRAIGHT = new Vector(angle, length);       
    }
    public static void main(String[] args) {
        Vector vector = VectorsCalculate.calculateVector(-50, 3800, -20, 4000);
        double angle = vector.angle;
        double length = vector.length;
        System.out.println("angle = " + angle + " length = " + length);
    }
}
