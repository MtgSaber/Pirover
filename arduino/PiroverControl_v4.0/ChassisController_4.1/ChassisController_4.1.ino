/*
 * Chassis Control rev 4.1
 * Author: Andrew Arnold (MtgSaber) 3/9/2018
 * 
 * The Arduino will recieve Serial commands from Raspi and interpret them
 * into chassis state changes. The command is a string of seven 'chars'. These chars are interpreted
 * as 2-digit Hexadecimal numbers and the string  will always begin with 0xFF and end with 0xFF.
 * 
 * The chassis control circuit recieves the data for left and right speeds and states.   
 * These values are translated into the 3-signal formats for each side. These formats
 * are as follows:
 * 
 *    digital|digital|PWM
 *    state-bit0|state-bit1|motorSpeed
 * 
 * The first digit of the command represents the motor states, ranging from 0x00 to 0x0F.
 * This number is split into two two-bit vectors that each represent the left and right
 * motor states, respectively. Each bit vector tranlates into the following motor states:
 * 
 *    00 - coast
 *    11 - brake
 *    01 - direction 0
 *    10 - direction 1
 *    
 * Note that each of the two chassis motor pairs has a set of these signals.
 * 
 * The next two chars represent the speeds of the motors, ranging from 0x00 to 0xFE.
 * The remaining chars represent the positions of the robot servos, ranging from 0x00 to 0xB4.
 * This unusual range is all that is needed due to the restrictions of the Servo library.
 */

/*
const short LEFTD0 = , LEFTD1 = , LEFTPWM = , RIGHTD0 = , RIGHTD1 = , RIGHTPWM = ,
    SERVO_0 = , SERVO_1 = , SERVO_2 = , SERVO_3 = ;
*/

String command;
bool leftStateBits[2], rightStateBits[2];
byte servo_0_pos, servo_1_pos, servo_2_pos, servo_3_pos, leftSpd, rightSpd;
const short testLED = 3;

void setup() {
  Serial.begin(9600);
}

// Collects Serial inputs, constructs commands from them, processes them, and changes outputs accordingly.
void loop() {
  // wait for input
  char input;
  bool buildingCommand = false;
  if (Serial.available()) {
    // build command
    while (Serial.available() > 0) {
      input = Serial.read();
      if (input == 0xFF) {
        if (buildingCommand)
          processCommand();
        buildingCommand = !buildingCommand;
        command = "";
      } else if (buildingCommand)
        command += input;
    }
  }
}

// Interprets command and sets output variables accordingly
void processCommand() {
  if (command.length() != 7) return;
  if (command[0] > 0xF) return;
  for (byte i=3; i<7; i++)
    if (command[i] > 0xB4) return;

  byte states = command[0];
  rightStateBits[1] = states % 2;
  states = states / 2;
  rightStateBits[0] = states % 2;
  states = states / 2;
  leftStateBits[1] = states % 2;
  states = states / 2;
  leftStateBits[0] = states % 2;

  leftSpd = command[1];
  rightSpd = command[2];
  servo_0_pos = command[3];
  servo_1_pos = command[4];
  servo_2_pos = command[5];
  servo_3_pos = command[6];

  setOutputs();
}

// Realizes the output variables into actual Pin signals
void setOutputs() {
  // DO STUFF
  confirm();
}

void confirm() {
  for (byte i=0; i<7; i++)
    Serial.write(command[i]);
}

