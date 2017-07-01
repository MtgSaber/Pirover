/*
 * PIROVER CONTROL - ARDUINO
 *
 * Author: Andrew Arnold (MtgSaber) 2017/06/30
 *
 * Description:
 * 	Recieves PWM inputs from Raspi, deciphers them, 
 * 	and drives Pirover's servos and motor control circuit.
 *
 * Overview:
 * 	updates a status variable whenever A0 goes HIGH.
 * 	Checks for a range of pwm values from A1 to determine which variable to update.
 * 	Variables are:
 *			- chassis state [ fwd, rev, brk, cw, ccw, cst ]
 *			- chassis movement speed [ 0 ~ 100%]
 *			- camera arm yaw position [ 0 (full left) ~ 100% (full right) ]
 *			- camera arm pitch position [ 0 (full down) ~ 100% (full up) ]
 *
 *		Drives Motor Control Circuit.
 *     	The motors are controlled as two groups: Left and Right.
 *			Each Group has a State [ fwd, rev, cst, brk ].
 *			The Arduino has three pairs of synchronized PWM output pins;
 *				Two have the same frequency, the third is twice that of the others.
 *
 *			It takes three pins to drive one group of motors:
 *				- a synchronized pair of PWM output pins
 *				- a digital output pin
 *
 *			Each motor group state is defined by the following pin values:
 *			( D1: digital pin, either HIGH or LOW; P1 & P2: PWM pin, with a n% duty cycle)
 *				Forward (fwd):	D1: HIGH;	P1: >0%;		P2: sync'd w/ P1;
 *				Reverse (rev):		D1: LOW;		P1: >0%;		P2: sync'd w/ P1;
 *				Brake (brk):			D1: N/A;		p1: 100%;	P2: 0%;
 *				Coast (cst):			D1: N/A;		p1: 0%;		P2: 0%;
 */

unsigned char chassisState = 'c'; // c - coast; b - brake; n - clockwise; p - counter-clockwise; f - forward; r - reverse;
short speed = 0;
short yaw = 0;
short pitch = 0;

const short PIN_UPDATE = A0, PIN_VAR = A1, PIN_STATE = A2,
				   PIN_SPEED = A3, PIN_YAW = A4, PIN_PITCH = A5;

void setup() {
  // put your setup code here, to run once:
}

void loop() {
  // put your main code here, to run repeatedly:

}

void fwd() {
	movLeft(true);
	movRight(true);
	chassisState = 'f';
}

void rev() {
	movLeft(false);
	movRight(false);
	chassisState = 'r';
}

void cw() {
	movLeft(true);
	movRight(false);
	chassisState = 'p';
}

void ccw() {
	movLeft(false);
	movRight(true);
	chassisState = 'n';
}

void cst() {
	
	chassisState = 'c';
}

void brk() {
	
	chassisState = 'b';
}

void movLeft(bool directionFwd) {
	
}

void movRight(bool directionFwd) {
	
}