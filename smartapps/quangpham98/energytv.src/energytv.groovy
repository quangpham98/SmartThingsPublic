/**
 *  EnergyTv
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
    name: "EnergyTv",
    namespace: "quangpham98",
    author: "Quang Pham",
    description: "Open window when TV on",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("oven") {
        input "oven", "capability.switch", required: true}
    
    section("Door is decteced ") {
        input "doorcontrol", "capability.lock", required: true
	}
    
    section("Monitor the temperature...") {
		input "temperatureSensor1", "capability.temperatureMeasurement"
    }
    section("When the temperature rises above...") {
		input "temperature1", "number", title: "Temperature?"}
    
    section("TV status"){
       input"TVstatus","capability.switch",required:true
        }
    
        
    
    }
    
    
    
   
   
    



def installed() {
	log.debug "Installed with settings: ${settings}"
subscribe(doorcontrol, "lock", motionDetectedHandler)
subscribe(temperatureSensor1, "temperature", temperatureHandler)
subscribe(TVstatus,"switch",TVsta)
subscribe(oven,"switch",ovenstatus)}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
subscribe(doorcontrol, "lock", motionDetectedHandler)
subscribe(temperatureSensor1, "temperature", temperatureHandler)
subscribe(TVstatus,"switch",TVsta)
subscribe(oven,"switch",ovenstatus)
}

def motionDetectedHandler(evt) {
    log.debug "$evt.value"
  log.debug "the current state of door is $evt.value"
    }
def Tvsta(evt){
 log.debug "$evt.value"
 log.debug "the current state of TV is $evt.value"
 }


def temperatureHandler(evt) {
	log.trace "temperature: $evt.value, $evt"
    def TV=TVstatus.currentSwitch
    log.debug "the current state of TV is $TV"
    def hot=temperature1
    log.debug "the current state temp is $evt.doubleValue"
    if(evt.doubleValue>=hot&&TV=="on"){
    doorcontrol.unlock()
    }}

def ovenstatus(evt){
    log.debug "$evt.value"
    log.debug "the current state of oven is $evt.value"
   def oven1=oven.currentSwitch
   if(oven1=="off"){

   oven.on()
   TVstatus.on()
   }
   }


