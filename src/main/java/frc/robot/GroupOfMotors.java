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
    public static final double MAX_POWER_FOR_MAX_SPEED_SLOW = 1023 * 0.5; // 1/3 power
    public static final double MAX_POWER_FOR_MAX_SPEED_FAST = 1023 * 0.8; // 1/2 power
    public static final double PULSE_DIS=0.116;
    public static final double SPEED_TO_TALON_SPEED = 0.1 / PULSE_DIS;
    public static final double MAX_SPEED_SLOW =2500;
    public static final double MAX_SPEED_FAST =7000;
    public static final double K_F_FAST = MAX_POWER_FOR_MAX_SPEED_FAST / (MAX_SPEED_FAST * SPEED_TO_TALON_SPEED);
    public static final double K_F_SLOW = MAX_POWER_FOR_MAX_SPEED_SLOW / (MAX_SPEED_SLOW * SPEED_TO_TALON_SPEED);
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
        motor1.configClosedloopRamp(0.5);
        motor1.configContinuousCurrentLimit(40);
        motor1.configPeakCurrentDuration(100);
        motor1.enableCurrentLimit(true);
        /*motor2.configClosedloopRamp(0.5);
        motor2.configContinuousCurrentLimit(40);
        motor2.configPeakCurrentDuration(100);
        motor2.enableCurrentLimit(true);*/
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

    public void _setValue(double value){
        if(Robot.driverInterface.isSpeedMode){
            _setSpeed(value * Robot.chassis.max_speed);
        }else{
            motor1.set(ControlMode.PercentOutput, reverse * value);
        }
    }

    public void _setSpeed(double speed){
        System.out.println("speed = " + reverse * speed * SPEED_TO_TALON_SPEED);
        motor1.set(ControlMode.Velocity, reverse * speed * SPEED_TO_TALON_SPEED);
    }

    public void StopMotors(){
        _setValue(0);
    }

    public void ResetEnc(){
        baseEncoder=motor1.getSelectedSensorPosition();
    }
    public double GetAbsPosition(){
        return motor1.getSelectedSensorPosition();
    }
    public double _GetPosition(){
        int reverseMode = Robot.chassis.isReverseMode ? -1 : 1;
        return reverse * (GetAbsPosition()-baseEncoder);
    }
    public double _GetPositionInMM(){
        return _GetPosition()*PULSE_DIS;
    }
    public void SetReverseMode(Boolean reverseMoode){
        reverse= reverseMoode? -1 : 1;
    }
    public void _SetSpeedMode(boolean isSpeedMode)
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
