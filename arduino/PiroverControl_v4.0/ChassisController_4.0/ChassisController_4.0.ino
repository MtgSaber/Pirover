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

/*
const short LEFTD0 = , LEFTD1 = , LEFTPWM = , RIGHTD0 = , RIGHTD1 = , RIGHTPWM = ,
    SERVO_0 = , SERVO_1 = , SERVO_2 = , SERVO_3 = ;
*/

short servo_0_pos, servo_1_pos, servo_2_pos, servo_3_pos, leftSpd, rightSpd;
bool leftStateBits[2], rightStateBits[2];

void setup() {
  
}

void loop() {
  // wait for input
  if (Serial.available()) {
    // build command
    while (Serial.available() > 0) {
      char input = Serial.read();
      if (input != ' ') {
          command += input;
      }
    }
  }
}

void getStateBits(char code, bool bits[]) {
  switch(code) {
    case 'A': bits[0] = false; bits[1] = false; break;
    case 'B': bits[0] = true; bits[1] = true; break;
    case 'C': bits[0] = false; bits[1] = true; break;
    case 'D': bits[0] = true; bits[1] = false; break;
  }
}

short hexToDecimal(char hexDigit) {
  switch(hexDigit) {
    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:
    case 9: return hexDigit - '0';
    case 'A': return 10;
    case 'B': return 11;
    case 'C': return 12;
    case 'D': return 13;
    case 'E': return 14;
    case 'F': return 15;
  }
}

