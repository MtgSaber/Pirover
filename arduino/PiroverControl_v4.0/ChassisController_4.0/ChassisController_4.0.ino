/*
 * Chassis Control rev 4
 * Author: Andrew Arnold (MtgSaber) 2/14/2018
 * 
 * The Arduino will recieve Serial commands from Raspi and interpret them
 * into chassis state changes. The command is a 14-digit hexadecimal number
 * enclosed between start and end delimiters, and the format is as follows:
 * 
 *    ~|[ABCD]|##|[ABCD]|##|##|##|##|##|^
 *    BEGIN|leftState|leftSpeed|rightState|rightSpeed|servo0Position|servo1Position|servo2Position|servo3Position|END
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

const char HEX_DECIMAL_SYMBOLS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

short servo_0_pos, servo_1_pos, servo_2_pos, servo_3_pos, leftSpd, rightSpd;
bool leftStateBits[2], rightStateBits[2];

void setup() {
  
}

// Collects Serial inputs, constructs commands from them, processes them, and changes outputs accordingly.
void loop() {
  // wait for input
  String command;
  if (Serial.available()) {
    // build command
    while (Serial.available() > 0) {
      char input = Serial.read();
      if (input != ' ') {
        if (input == '~')
          command = input;
        else if (input == '^') {
          command += input;
          processCommand(command);
          setOutputs();
        } else
          command += input;
      }
    }
  }
}

// Interprets command and sets output variables accordingly
void processCommand(String command) {
  if (command.length() != 16) return;
  if (command[0] != '~') return;
  for (short i=1; i < 14; i++) {
    short hex = hexToDecimal(command[i]);
    if (hex < 0)
      return;
    if ((i == 1 || i == 4) && (hex < 10 || hex > 13))
      return;
  }

  getStateBits(command[1], leftStateBits);
  getStateBits(command[4], leftStateBits);

  servo_0_pos = hexToDecimal(command[7]) + hexToDecimal(command[8]);
  servo_1_pos = hexToDecimal(command[9]) + hexToDecimal(command[10]);
  servo_2_pos = hexToDecimal(command[11]) + hexToDecimal(command[12]);
  servo_3_pos = hexToDecimal(command[13]) + hexToDecimal(command[14]);
  leftSpd = hexToDecimal(command[2]) + hexToDecimal(command[3]);
  rightSpd = hexToDecimal(command[5]) + hexToDecimal(command[6]);
}

// Realizes the output variables into actual Pin signals
void setOutputs() {
  
}

// Sets the motor state bits based on the input format
void getStateBits(char code, bool bits[]) {
  switch(code) {
    case 'A': bits[0] = false; bits[1] = false; break;
    case 'B': bits[0] = true; bits[1] = true; break;
    case 'C': bits[0] = false; bits[1] = true; break;
    case 'D': bits[0] = true; bits[1] = false; break;
  }
}

// Returns the decimal number equivalent of the hex symbol provided; -1 if invalid symbol.
short hexToDecimal(char hexDigit) {
  for (short i=0; i < 15; i++)
    if (hexDigit == HEX_DECIMAL_SYMBOLS[i])
      return i;
  return -1;
}

