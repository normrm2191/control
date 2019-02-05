/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class GroupOfMotors {
    
    public TalonSRX motor1;
  //  public TalonSRX motor2;
    public double reverse;
    public boolean isSpeedMode;
    public static final double K_P = 1.0 / 14.0;
    public static final double K_I = K_P / 40.0;
    public static final double K_D = 0;
    public static final double PULSE_DIS=0.116;
    public static final double MAX_SPEED =100;
    public static final double FINAL_SPEED =MAX_SPEED/PULSE_DIS;
    public double baseEncoder=0;
    
    

    public GroupOfMotors(int port1, int port2){
        motor1= new TalonSRX(port1);
    //    motor2= new TalonSRX(port2);
    //    motor2.follow(motor1)motor1;
        // set PID
        motor1.config_kP(0, K_P,0);
    //    motor2.config_kP(0, K_P,0);
        motor1.config_kI(0, K_I,0);
    //    motor2.config_kI(0, K_I,0);
        motor1.config_kD(0, K_D,0);
    //    motor2.config_kD(0, K_D,0);
        motor1.configContinuousCurrentLimit(40);
        motor1.configPeakCurrentDuration(200);
        motor1.enableCurrentLimit(true);
        motor1.configOpenloopRamp(0.3);
        /*motor2.configContinuousCurrentLimit(40);
        motor2.configPeakCurrentDuration(200);
        motor2.enableCurrentLimit(true);
        motor2.configOpenloopRamp(0.3);*/
        isSpeedMode = true;
        reverse = 1;
    }

    public void ConfigKP(double k_p){
        motor1.config_kP(0, k_p, 0);
    }

    public void ConfigKI(double k_i){
        motor1.config_kP(0, k_i, 0);
    }

    public void setValue(double value){
        System.out.println("set to motor = " + reverse * FINAL_SPEED * value );
        if(Robot.driverInterface.isSpeedMode){
            motor1.set(ControlMode.Velocity, reverse * FINAL_SPEED * value);
        }else{
        motor1.set(ControlMode.PercentOutput, reverse * value);
        }
    }

    public void StopMotors(){
        setValue(0);
    }

    public void ResetEnc(){
        baseEncoder=motor1.getSelectedSensorPosition(0);
    }
    public double GetAbsPosition(){
        return motor1.getSelectedSensorPosition(0);
    }
    public double GetPosition(){
        return reverse*(GetAbsPosition()-baseEncoder);
    }
    public double GetPositionInMM(){
        return GetPosition()*PULSE_DIS;
    }
    public void SetReverseMode(Boolean reverseMoode){
        reverse= reverseMoode? -1 : 1;
    }
    public void SetSpeedMode(boolean isSpeedMode)
    {
        this.isSpeedMode = isSpeedMode;
    }

}
