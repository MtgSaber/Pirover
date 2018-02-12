/*
 * PIROVER CONTROL - MOTOR CONTROL CIRCUIT TEST
 * 
 * Author: Andrew Arnold (MtgSaber) 2018/02/12
 * 
 * Tests the functionality of the second revision of the chassis
 * motor control circuit. As detailed in the circuit drawing,
 * The motors will change state and speed in pairs 
 * (left side & right side), according to the PWM speed
 * signal from Arduino and the 2-bit digital state signal.
 * The state signal definition is as follows:
 * 
 * 10 - direction 1
 * 01 - direction 2
 * 00 - coast
 * 11 - brake
 * 
 * Note that each pair or "side" has its own speed and state signal
 * sets.
 * 
 * The speed of directions 1 & 2 is determined by the duty
 * cycle of the PWM speed signal.
 * 
 * This test starts the motor in direction 2 and speeds it up
 * to full speed from rest, lets it run for 5 seconds, brakes
 * the motor for 1 second, changes to direction 1 at full speed
 * for 5 seconds, coasts the motor for 1 second, then reverts to
 * direction 1 at full speed and immediately slows it down to rest.
 */


const short PINPWM_A = 3, PIND_B = 2, PIND_C = 4;
short spd = 0;

void setup() {
  pinMode(PINPWM_A, OUTPUT);
  pinMode(PIND_B, OUTPUT);
  pinMode(PIND_C, OUTPUT);
}

void loop() {
  digitalWrite(PIND_B, HIGH);
  digitalWrite(PIND_C, LOW);
  
  while (spd < 255) {
    analogWrite(PINPWM_A, spd++);
    delay(50);
  }
  
  delay(5000);
  
  digitalWrite(PIND_B, HIGH);
  digitalWrite(PIND_C, HIGH);
  delay(1000);
  
  digitalWrite(PIND_B, LOW);
  digitalWrite(PIND_C, HIGH);
  delay(5000);
  
  digitalWrite(PIND_B, LOW);
  digitalWrite(PIND_C, LOW);
  delay(1000);
  
  digitalWrite(PIND_B, LOW);
  digitalWrite(PIND_C, HIGH);
  while (spd > 0) {
    analogWrite(PINPWM_A, spd--);
    delay(50);
  }
}
