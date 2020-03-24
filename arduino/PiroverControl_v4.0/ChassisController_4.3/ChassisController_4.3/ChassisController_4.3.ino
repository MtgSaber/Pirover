/*
 * Chassis Control rev 4.1.2
 * Author: Andrew Arnold (MtgSaber) 3/24/2018
 * 
 * The Arduino will recieve Serial commands from Raspi and interpret them
 * into chassis state changes. The command is a string of  'chars'. These chars are interpreted
 * as 2-digit Hexadecimal numbers and the string  will always begin with 0x7F and end with 0x7F.
 * 
 * The format:
 * 0x7F|motorStates|MSBs|leftSpeed|rightSpeed|servo1Position|servo2Position|servo3Position|servo4Position|0x7F
 * 
 * The chassis control circuit recieves the data for left and right speeds and states.   
 * These values are translated into the 3-signal formats for each side. These formats
 * are as follows:
 * 
 *    digital|digital|PWM
 *    state-bit0|state-bit1|motorSpeed
 * 
 * motorStates represents the motor states, ranging from 0x00 to 0x0F.
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
 * MSBs ranges from 0x00 to 0x3F.
 * 
 * All chars after MSBs range from 0x00 to 0xFE.
 * 
 * MSBs is used to calculate the full value of the remaining values. If a bit-position is 1 then
 * the corresponding value is increased by 0x80, effectively setting the MSB of the byte. This is
 * in place because C++ chars only go up to 0x7F, while bytes go up to 0xFF. This allows for more
 * precise motor speeds and servo positions. The format of MSBs:
 * 
 * 0|0|leftSpeed's MSB|rightSpeed's MSB|servo1Position's MSB|servo2Position's MSB|servo3Position's MSB|servo4Position's MSB
 * 
 * The range of leftSpeed and rightSpeed is effectively: [0x00, 0x7E],[0x80, 0xFE]
 * 
 * The range of servo1-servo4 is effectively: [0x00, 0x7E],[0x80, 0xB4].
 * The Servo library measures in whole degrees [0-180], hence the unusual range.
 * 
 * 
 * 
 * GOOD FOR TESTING
 * 
 * 
 */


const byte LEFTD0 = 2, LEFTD1 = 4, LEFTPWM = 3, RIGHTD0 = 7, RIGHTD1 = 8, RIGHTPWM = 11;
//    SERVO_0 = , SERVO_1 = , SERVO_2 = , SERVO_3 = ;


String command;
bool leftStateBits[2], rightStateBits[2];
byte servo_0_pos, servo_1_pos, servo_2_pos, servo_3_pos, leftSpd, rightSpd;
char input;
bool buildingCommand = false;

void setup() {
  TCCR2B = (TCCR2B & 0b11111000) | 0x06;  // what does this do?

  // pin mode setup
  pinMode(LEFTD0, OUTPUT);
  pinMode(LEFTD1, OUTPUT);
  pinMode(LEFTPWM, OUTPUT);
  pinMode(RIGHTD0, OUTPUT);
  pinMode(RIGHTD1, OUTPUT);
  pinMode(RIGHTPWM, OUTPUT);

  // initialize motors to coast with speed=0
  digitalWrite(LEFTD0, LOW);
  digitalWrite(LEFTD1, LOW);
  digitalWrite(RIGHTD0, LOW);
  digitalWrite(RIGHTD1, LOW);
  digitalWrite(LEFTPWM, LOW);
  digitalWrite(RIGHTPWM, LOW);
  
  Serial.begin(9600);
}

// Collects Serial inputs, constructs commands from them, processes them, and changes outputs accordingly.
void loop() {
  // wait for input
  if (Serial.available()) {
    // build command
    while (Serial.available() > 0) {
      input = Serial.read();
      if (input == 0x7F) {
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
  // Appears to fail silently on bad inputs
  if (command.length() != 8) return;
  if (command[0] > 0xF) return;
  byte msbs = command[1];
  if (msbs > 0x3F) return;
  for (byte i=7; i>3; i--) {
    if (msbs % 2 == 1) {
      if (command[i] > 0x34) return;
    } else if (command[i] > 0x7E) return;
    msbs = msbs / 2;
  }

  
  byte states = command[0];
  rightStateBits[1] = states % 2;
  states = states / 2;
  rightStateBits[0] = states % 2;
  states = states / 2;
  leftStateBits[1] = states % 2;
  states = states / 2;
  leftStateBits[0] = states % 2;


  msbs = command[1];
  servo_3_pos = command[7] + (msbs%2 != 0? 0x80 : 0x00);
  msbs = msbs / 2;
  servo_2_pos = command[6] + (msbs%2 != 0? 0x80 : 0x00);
  msbs = msbs / 2;
  servo_1_pos = command[5] + (msbs%2 != 0? 0x80 : 0x00);
  msbs = msbs / 2;
  servo_0_pos = command[4] + (msbs%2 != 0? 0x80 : 0x00);
  msbs = msbs / 2;
  rightSpd = command[3] + (msbs%2 != 0? 0x80 : 0x00);
  msbs = msbs / 2;
  leftSpd = command[2] + (msbs%2 != 0? 0x80 : 0x00);

  setOutputs();
}

// Realizes the output variables into actual Pin signals
void setOutputs() {
  // DO STUFF
  digitalWrite(LEFTD0, (leftStateBits[0]? HIGH : LOW));
  digitalWrite(LEFTD1, (leftStateBits[1]? HIGH : LOW));
  analogWrite(LEFTPWM, leftSpd);
  digitalWrite(RIGHTD0, (rightStateBits[0]? HIGH : LOW));
  digitalWrite(RIGHTD1, (rightStateBits[1]? HIGH : LOW));
  analogWrite(RIGHTPWM, rightSpd);
  confirm();
}

void confirm() {
}
