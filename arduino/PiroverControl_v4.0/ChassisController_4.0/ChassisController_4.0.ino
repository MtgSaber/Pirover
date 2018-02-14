/*
 * Chassis Control rev 4
 * Author: Andrew Arnold (MtgSaber) 2/14/2018
 * 
 * The Arduino will recieve Serial commands from Raspi and interpret them
 * into chassis state changes. The command is a 14-digit hexadecimal number,
 * and the format is as follows:
 * 
 *    [ABCD]|##|[ABCD]|##|##|##|##|##
 *    leftState|leftSpeed|rightState|rightSpeed|servo0Position|servo1Position|servo2Position|servo3Position
 * 
 * The chassis control circuit Recieves the data for left and right speeds and states.   
 * These values are translated into the 3-signal formats for each side. These formats
 * are as follows:
 * 
 *    digital|digital|PWM
 *    state-bit0|state-bit1|motorSpeed
 *    
 * the state-bit descriptions:
 *    
 *    00 - coast
 *    11 - brake
 *    01 - direction 0
 *    10 - direction 1
 *    
 * Note that each of the two chassis motor pairs has a set of these signals.
 * 
 * The Servo Position values range from 0(00) to 180(B4), and these values
 * are converted into decimal for use with the Servo class functions.
 * 
 */
