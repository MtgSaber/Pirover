/*
 * PIROVER CONTROL - ARDUINO
 *
 * Author: Andrew Arnold (MtgSaber) 2017/06/30
 *
 * Description:
 *  Recieves Serial string inputs from Raspi, deciphers them, 
 *  and drives Pirover's servos and motor control circuit.
 *
 * Overview:
 *  updates status variables based on Serial input.
 *  analyzes a string from Serial to determine which variables to change and how to change them.
 *  Variables are:
 *          - chassis state [ fwd, rev, brk, cw, ccw, cst ]
 *          - chassis movement speed [ 0 ~ 100% (255) ]
 *          - camera arm yaw position [ 0 (full left) ~ 100% (full right) ]
 *          - camera arm pitch position [ 0 (full down) ~ 100% (full up) ]
 *
 *      Drives Motor Control Circuit.
 *      The motors are controlled as two groups: Left and Right.
 *          Each Group has a State [ fwd, rev, cst, brk ].
 *          The Arduino has three pairs of synchronized PWM output pins;
 *              Two have the same frequency, the third is twice that of the others.
 *
 *          It takes three pins to drive one group of motors:
 *              - a synchronized pair of PWM output pins
 *              - a digital output pin
 *
 *          Each motor group state is defined by the following pin values:
 *          ( D1: digital pin, either HIGH or LOW; P1 & P2: PWM pin, with a n% duty cycle)
 *              Forward (fwd):      D1: HIGH;       P1: >0%;            P2: sync'd w/ P1;
 *              Reverse (rev):       D1: LOW;        P1: >0%;            P2: sync'd w/ P1;
 *              Brake (brk):           D1: N/A;         p1: 100%;           P2: 0%;
 *              Coast (cst):           D1: N/A;          p1: 0%;              P2: 0%;
 */

char chassisState = 'c'; // c - coast; b - brake; n - clockwise; p - counter-clockwise; f - forward; r - reverse;
short speed = 0;
short yaw = 0;
short pitch = 0;

const short PIN_D1 = 12, PIN_PA1 = 10, PIN_PB1 = 9, // REFER TO CHASSIS CONTROL CIRCUIT DIAGRAM
                   PIN_D2 = 2, PIN_PA2 = 3, PIN_PB2 = 11;

void setup() {
    pinMode(PIN_D1, OUTPUT);
    pinMode(PIN_D2, OUTPUT);
    pinMode(PIN_PA1, OUTPUT);
    pinMode(PIN_PA2, OUTPUT);
    pinMode(PIN_PB1, OUTPUT);
    pinMode(PIN_PB2, OUTPUT);
    brk();
    Serial.begin(9600);
    while (!Serial) { ; }
}

void loop() {
	// wait for Serial input
    if (Serial.available() > 0) {
        String command = "";
		// build command
        while (Serial.available() > 0) {
            char input = Serial.read();
            if (input != ' ') {
                command += input;
            }
        }
		// process command. format is "t:[s]###". t = type, s = state, ### value (must be 3 numbers, even if less than 100).
        if (command[1] == ':') {
            switch (command[0]) {
                case 'c': // chassis change
                    short pos;
                    pos = command.substring(3, 3).toInt();
                    if (command[2] != chassisState || pos != speed) {
                        speed = pos;
                        switch (command[2]) {
                            case 'b': brk(); break;
                            case 'f': fwd(); break;
                            case 'r': rev(); break;
                            case 'c': cst(); break;
                            case 'n': ccw(); break;
                            case 'p': cw(); break;
                        }
                    }
                    break;
                case 'y': // yaw pos change
                    pos = command.substring(2, 3).toInt();
                    if (pos != yaw) {
                        setYaw(pos);
                    }
                    break;
                case 'p': // pitch pos change
                    pos = command.substring(2, 3).toInt();
                    if (pos != pitch) {
                        setPitch(pos);
                    }
                    break;
            }
        }
    }
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
    analogWrite(PIN_PA1, 0);
    analogWrite(PIN_PB1, 0);
    analogWrite(PIN_PA2, 0);
    analogWrite(PIN_PB2, 0);
    chassisState = 'c';
}

void brk() {
    analogWrite(PIN_PA1, 255);
    analogWrite(PIN_PB1, 0);
    analogWrite(PIN_PA2, 255);
    analogWrite(PIN_PB2, 0);
    chassisState = 'b';
}

void movLeft(bool directionFwd) {
    digitalWrite(PIN_D1, (directionFwd)? HIGH : LOW);
    analogWrite(PIN_PA1, speed);
    analogWrite(PIN_PB1, speed);
}

void movRight(bool directionFwd) {
    digitalWrite(PIN_D1, (directionFwd)? HIGH : LOW);
    analogWrite(PIN_PA2, speed);
    analogWrite(PIN_PB2, speed);
}

void setYaw(int pos) {
    
}

void setPitch(int pos) {
    
}
