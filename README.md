

This module collects and analyzes the following touch and key -based biometric metrics from user interactions while collecting BATTERY metrics aswell

* **Finger Pressure:** 
    * **Units:** Pascals (if hardware supported) or simulated units.
    * **Description:** Indicates the force applied by the finger on the screen.
    * **Calculated by:** event.pressure  the pressureChangeMagnitude * pressureChangeDirection


* **Hold Time:**  
    * **Units:** Seconds (s) or milliseconds (s).
    * **Description:** The duration of a continuous touch interaction. 
    * **Calculated by:** measuring the time difference between touch-down and touch-up events

* **Finger Blocked Area:** 
    * **Units:** Pixels or related screen-density-independent unit.
    * **Description:** Approximate area of finger contact with the screen.
    * **Calculated by:**  event.getSize() 

* **Finger Orientation:**
    * **Units:** Degrees (°).
    * **Description:** Angle of finger movement during gestures (e.g., swipes).
    * **Calculated by:** event.historySize  i.e., event.getHistoricalX and event.getHistoricalY 

**Metric: Flight Time**

* **Units:** Milliseconds (ms) or seconds (s) 
* **Description:** The time duration between a finger leaving the screen (touch-up event) and subsequently touching down again (touch-down event).
* **Calculation:** Subtract the timestamp of a touch-down event from the timestamp of the previous touch-up event.

**Key Hold Time:**  
    * **Units:** Seconds (s) or milliseconds.
    * **Description:** The duration of a continuous touch interaction. 
    * **Calculated by:** measuring the time difference between touch-down and touch-up events

* **Context Matters:** Analyze flight time in conjunction with other metrics. A short flight time alone doesn't provide definitive insights.

Note, that key events do not return key pressure and finger area since this is done by the motion event for touch 


* **Battery Current:**
    * **Units:** Amperes (A)
    * **Description:** The instantaneous rate of flow of electric charge within the device's battery. Positive values usually indicate charging, and negative values indicate discharging. 
    * **Source:**  Android BatteryManager

* **Battery Voltage:**
    * **Units:** Volts (V)
    * **Description:** The electrical potential difference between the battery's positive and negative terminals.
    * **Source:**  Android BatteryManager



**Important Considerations**


* **Rapid Fluctuations:** Battery current, in particular, can fluctuate based on the device's activity even over short periods.

* **Contextual Analysis:** These metrics are most meaningful when analyzed in conjunction with other usage data



**Important Considerations**

* **Hardware Limitations:** The accuracy and availability of certain metrics (especially pressure) may depend on the device's hardware capabilities. 
* **Data Privacy:** Users are prompted by anadroid to send permit the app to run in the fore ground 

