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
        input "presenceSensor", "capability.presenceSensor", required: true
	}
    
     section("light") {
        input "thelight", "capability.switch", required: true
    }
      section("Turn off at...") {
		input name: "startTime", title: "Turn off Time?", type: "time"
	}
	section("And turn them off at...") {
		input name: "stopTime", title: "End", type: "time"
	}

}

   
    



def installed() {
	log.debug "Installed with settings: ${settings}"
subscribe(presenceSensor, "presence.present", motionDetectedHandler)
subscribe(thelight,"switch",lightstatus)
schedule(startTime, "startTimerCallback")
schedule(stopTime, "stopTimerCallback")
initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
schedule(startTime, "startTimerCallback")
schedule(stopTime, "stopTimerCallback")
	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
subscribe(presenceSensor, "presence.present", motionDetectedHandler)
subscribe(thelight,"switch",lightstatus)

}

def motionDetectedHandler(evt) {
    log.debug "$evt.value"
  
   def state=thelight.currentSwitch
    log.debug "the current state of sensor is $evt.value"
     
    if(state=="off"){
     
     thelight.on()}
     }
            
        
     
       
        

def lightstatus(evt){
   log.debug "$evt.value"
   def state=thelight.currentSwitch
    log.debug "the current state of thelight is $state"
    
    return evt.value}

def startTimerCallback() {
log.debug "Turning off switches"
def state=thelight.currentSwitch
if(state=="on"){
thelight.off()}

}

def stopTimerCallback() {
log.debug "Turning on switches"
thelight.on()
}