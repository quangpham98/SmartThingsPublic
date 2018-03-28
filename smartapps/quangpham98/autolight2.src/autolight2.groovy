/**
 *  Autolight2
 *
 *  Copyright 2018 Quang Pham
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Autolight2",
    namespace: "quangpham98",
    author: "Quang Pham",
    description: "autolight2",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("presence sensor is decteced ") {
        input "presenceSensor", "capability.motionSensor", required: true
	}
    
     section("light") {
        input "thelight", "capability.switch", required: true
    }
     }

   
    



def installed() {
	log.debug "Installed with settings: ${settings}"
subscribe(presenceSensor, "motion.active", motionDetectedHandler)
subscribe(thelight,"switch",lightstatus)

initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
subscribe(presenceSensor, "motion.active", motionDetectedHandler)
subscribe(thelight,"switch",lightstatus)

}

def motionDetectedHandler(evt) {
    
  
   def state=thelight.currentSwitch
    log.debug "the current state of sensor is $evt.value"
     
    if(state=="off"){
     
     thelight.on()}
          def fiveMinuteDelay = 60
	runIn(fiveMinuteDelay, turnOffSwitch)
}



     
 def turnOffSwitch() {
	thelight.off()
}
        
     
       
        

def lightstatus(evt){
   log.debug "$evt.value"
   def state=thelight.currentSwitch
    log.debug "the current state of thelight is $state"
    
    return evt.value}

