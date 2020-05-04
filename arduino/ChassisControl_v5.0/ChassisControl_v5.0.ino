
/*
 * PIROVER CHASSIS CONTROL - ARDUINO
 *
 * Author: Andrew Arnold (MtgSaber) 2020/04/13
 *
 * Description:
 *  Recieves Serial string inputs from Raspi, deciphers them, 
 *  and drives Pirover's servos and motor control circuit.
 *
 * Electrical-Layer Overview:
 *     Updates status variables based on Serial input.
 *     Analyzes a string from Serial to determine which variables to change and how to change them.
 *     Variables are:
 *             - LeftSpeed [0 ~ 255]
 *             - RightSpeed [0 ~ 255]
 *             - LeftState [f, r, b, c]
 *             - RightState [f, r, b, c]
 *             - ShoulderYaw [0 ~ 255]
 *             - ShoulderPitch [0 ~ 255]
 *             - ElbowAngle [0 ~ 255]
 *             - EyeYaw [0 ~ 255]
 *             - EyePitch [0 ~ 255]
 *             - HeadlightPwr [0 | 1]
 *
 *     Motor Control Signals:
 *         The chassis has two pairs of motors, left and right. Each pair is controlled by a dedicated set of signals.
 *         These signals are:
 *             D1 & D2: Digital signals used to control the input pins of the L293B chip. These are either HIGH or LOW.
 *             P:       PWM signal used to control the ENABLE pin of the L293B chip. The duty of this signal determines the
 *                      speed at which the motors turn, the intensity of fast-stopping, and whether the motor should cost.
 *                      This signal ranges between a duty cycle N of 0 - 255, where 255 is a full duty cycle.
 *         The states of the motor are given by these signals as:
 *             Forward (f):    D1: High    D2: Low     P: N>0
 *             Reverse (r):    D1: Low     D2: High    P: N>0
 *             Brake (b):      D1: High    D2: High    P: N>0
 *             Coast (c):      D1: Low     D2: Low     P: 255
 *
 *     Servo Signals:
 *         These signals are determined by the Servo library. The servo specifications have yet to be determined.
 *
 * USB-Layer Overview:
 *     This layer will likely be an extension of the scheme used in v4.0. Arduino will act as a "server", and Raspi
 *     will act as a "client". Raspi will send command requests to Ardino, which will respond with either a confirmation
 *     or an error report.
 */

byte PIN_LEFT_D1 = 0;
byte PIN_LEFT_D2 = 0;
byte PIN_LEFT_P = 0;
byte PIN_RIGHT_D1 = 0;
byte PIN_RIGHT_D2 = 0;
byte PIN_RIGHT_P = 0;
byte PIN_ARM_YAW = 0;
byte PIN_ARM_PITCH = 0;
byte PIN_ARM_ELBOW = 0;
byte PIN_EYE_YAW = 0;
byte PIN_EYE_PITCH = 0;
byte PIN_HEADLIGHT = 0;


void setup() {
    pinMode(PIN_LEFT_D1, OUTPUT);
    pinMode(PIN_LEFT_D2, OUTPUT);
    pinMode(PIN_LEFT_P, OUTPUT);
    pinMode(PIN_RIGHT_D1, OUTPUT);
    pinMode(PIN_RIGHT_D2, OUTPUT);
    pinMode(PIN_RIGHT_P, OUTPUT);
    pinMode(PIN_ARM_YAW, OUTPUT);
    pinMode(PIN_ARM_PITCH, OUTPUT);
    pinMode(PIN_ARM_ELBOW, OUTPUT);
    pinMode(PIN_EYE_YAW, OUTPUT);
    pinMode(PIN_EYE_PITCH, OUTPUT);
    pinMode(PIN_HEADLIGHT, OUTPUT);
    
    set_motor_pins(3, 3, 0x3F, 0x3F); // applies 50% braking force on start-up.
    Serial.begin(9600);
    while (!Serial) { ; }
}


void loop() {
  
}


/*
 * Applies signals to motor control pins.
 * 
 * l_stat is the multiplexed signal for A and B inputs on the left side of the L293B chip.
 * The first bit is the A input, second bit is the B input.
 * r_stat is the same, but for the right pair of motors.
 * 
 * l_spd is the intensity of the action on the left motor pair. See top comment.
 * r_spd same as l_spd, but for the right pair of motors.
 */
byte set_motor_pins(byte l_stat, byte r_stat, byte l_spd, byte r_spd) {
    // scales the input speed to match the higher native resolution
    // and shifts one step higher so that the maximum value can be achieved.
    analogWrite(PIN_LEFT_P, (l_spd)? l_spd*2+1 : 0);
    // decodes the bit flags for the L293B input pins
    digitalWrite(PIN_LEFT_D1, (l_stat / 2)? HIGH : LOW);
    digitalWrite(PIN_LEFT_D2, (l_stat % 2)? HIGH : LOW);
    
    analogWrite(PIN_RIGHT_P, (r_spd)? r_spd*2+1 : 0);
    digitalWrite(PIN_RIGHT_D1, (r_stat / 2)? HIGH : LOW);
    digitalWrite(PIN_RIGHT_D2, (r_stat % 2)? HIGH : LOW);
}
