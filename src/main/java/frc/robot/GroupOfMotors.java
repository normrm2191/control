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
    public static final double K_P = 1.0 / 5.0;
    public static final double K_I = 0;
    public static final double K_D = 0;
    public static final double MAX_POWER_FOR_MAX_SPEED_SLOW = 1023 * 0.33; // 1/3 power
    public static final double MAX_POWER_FOR_MAX_SPEED_FAST = 1023 * 0.5; // 1/2 power
    public static final double PULSE_DIS=0.116;
    public static final double MAX_SPEED_SLOW =250;
    public static final double MAX_SPEED_FAST =700;
    public static final double K_F_FAST = MAX_POWER_FOR_MAX_SPEED_FAST * PULSE_DIS / MAX_SPEED_FAST;
    public static final double K_F_SLOW = MAX_POWER_FOR_MAX_SPEED_SLOW * PULSE_DIS / MAX_SPEED_SLOW;
    public double baseEncoder=0;


    public GroupOfMotors(int port1, int port2, boolean in_fast_mode){
        System.out.println("Starting motor - " + port1);
        motor1= new TalonSRX(port1);
    //    motor2= new TalonSRX(port2);
    //    motor2.follow(motor1);
        // set PID
        motor1.config_kP(0, K_P,0);
    //    motor2.config_kP(0, K_P,0);
        motor1.config_kI(0, K_I,0);
    //    motor2.config_kI(0, K_I,0);
        motor1.config_kD(0, K_D,0);
    //    motor2.config_kD(0, K_D,0);
        isSpeedMode = true;
        reverse = 1;
        if(in_fast_mode) {
            ConfigKF(K_F_FAST);
        } else {
            ConfigKF(K_F_SLOW);
        }
    }

    public void ConfigKP(double k_p){
        motor1.config_kP(0, k_p, 0);
    }

    public void ConfigKI(double k_i){
        motor1.config_kP(0, k_i, 0);
    }

    public void ConfigKD(double k_d){
        motor1.config_kD(0, k_d, 0);
    }
    public void ConfigKF(double k_f){
        motor1.config_kF(0, k_f, 0);
    }

    public void setValue(double value){
        if(Robot.driverInterface.isSpeedMode){
            motor1.set(ControlMode.Velocity, reverse * Robot.chassis.max_speed * value);
        }else{
            motor1.set(ControlMode.PercentOutput, reverse * value);
        }
    }

    public void setValue(int speed){
        motor1.set(ControlMode.Velocity, reverse * speed / PULSE_DIS);
    }

    public void StopMotors(){
        setValue(0);
    }

    public void ResetEnc(){
        baseEncoder=motor1.getSelectedSensorPosition();
    }
    public double GetAbsPosition(){
        return motor1.getSelectedSensorPosition();
    }
    public double GetPosition(){
        int reverseMode = Robot.chassis.isReverseMode ? -1 : 1;
        return reverse * (GetAbsPosition()-baseEncoder) * reverseMode;
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
    public void SetSlow(){
        ConfigKF(K_F_SLOW);
    }
    public void SetFast(){
        ConfigKF(K_F_FAST);
    }
}
