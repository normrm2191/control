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

    public final static double r = 500;
    public static Vector MID_TO_CAMERA;
    public static Vector START_LINE;
    public static Vector END_LINE;
    public static Vector LINE;
    public static Vector FIRST_STRAIGHT;
    public static Vector SECOND_STRAIGHT;

    public VectorsCalculate(double angle_start, double dis_start , double angle_end, double dis_end){
        START_LINE = new Vector(angle_start, dis_start);
        END_LINE = new Vector(angle_end, dis_end);
        System.out.println("Calc - Start = " + START_LINE);
        System.out.println("Calc - end = " + END_LINE);
        Set_LINE();
        System.out.println("Calc - line = " + LINE);
        Set_MID_TO_CAMERA();
        System.out.println("Calc - mid = " + MID_TO_CAMERA);
        Set_SECOND_STRAIGHT();
        System.out.println("Calc - sec = " + SECOND_STRAIGHT);
        Set_FIRST_STRAIGHT();
        System.out.println("Calc - first = " + FIRST_STRAIGHT);
    }

    public Vector Get_First_vector(){
        return FIRST_STRAIGHT;
    }

    public Vector Get_Line_vector(){
        return LINE;
    }

    public Vector Get_Final_Vector(){
        return SECOND_STRAIGHT;
    }
   /* public static Vector calculateVector(double angle_start, double dis_start , double angle_end, double dis_end){
        START_LINE = new Vector(angle_start, dis_start);
        END_LINE = new Vector(angle_end, dis_end);
        Set_LINE();
        Set_MID_TO_CAMERA();
        Set_SECOND_STRAIGHT();
        Set_FIRST_STRAIGHT();
        return FIRST_STRAIGHT;
    } */

    public void Set_LINE(){
        VectorPoint temp_startLine= VectorPoint.convertToPoint(START_LINE);
        VectorPoint temp_endLine= VectorPoint.convertToPoint(END_LINE);
        VectorPoint temp = VectorPoint.sub(temp_endLine , temp_startLine);
        LINE = Vector.ConvertToVector(temp);
    }

    public void Set_MID_TO_CAMERA(){
     //   VectorPoint temp = new VectorPoint(0,0);  //??
        Vector temp = Vector.ConvertToVector(new VectorPoint(35,260));
        MID_TO_CAMERA = temp;
    }

    public void Set_SECOND_STRAIGHT(){
        double angle = LINE.angle;
        double dis = 260;
        SECOND_STRAIGHT = new Vector(angle,dis);
    }

   /* public static Vector getR(double angle_start, double dis_start , double angle_end, double dis_end){
        START_LINE = new Vector(angle_start, dis_start);
        END_LINE = new Vector(angle_end, dis_end);
        Set_LINE();
        double angle = 90 - Math.abs(LINE.angle) ;
        angle = LINE.angle >= 0 ? -angle : angle;
        R = new Vector(angle , r); 
        return R;
    } */

    public void Set_FIRST_STRAIGHT(){
        double angle = 90 - Math.abs(LINE.angle) ;
        angle = LINE.angle >= 0 ? -angle : angle;
        Vector tempR = new Vector(angle , r); 
        System.out.println("R = " + tempR);
        VectorPoint endTurn = VectorPoint.sub(VectorPoint.convertToPoint(START_LINE),
                     VectorPoint.convertToPoint(SECOND_STRAIGHT));
        System.out.println(" to end turn vector = " + endTurn);
//        double angle_temp1 = temp1.GetAngleOfVector();
//        double length_temp1 = temp1.GetLengthOfVector();
        VectorPoint turnCenter = VectorPoint.sub(endTurn, VectorPoint.convertToPoint(tempR));
        System.out.println(" to center of turn = " + turnCenter);
//        double angle_temp2 = temp2.GetAngleOfVector();
//        double length_temp2 = temp2.GetLengthOfVector();
        VectorPoint center2center = VectorPoint.add(VectorPoint.convertToPoint(MID_TO_CAMERA), turnCenter);
        System.out.println("center to center of turn = " + center2center);
//        double angle_temp3 = temp3.GetAngleOfVector();
//        double length_temp3 = temp3.GetLengthOfVector();
        double length = Math.sqrt(Math.pow(center2center.GetLengthOfVector(),2) - Math.pow(r, 2));
        angle = Math.asin(r / center2center.GetLengthOfVector()) * 180 / Math.PI;
        if(tempR.angle > 0) {
            angle = center2center.GetAngleOfVector() + angle;
        } else {
            angle = center2center.GetAngleOfVector() - angle;
        }
        FIRST_STRAIGHT = new Vector(angle, length);       
    }
}
