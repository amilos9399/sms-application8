package com.example.sms_application

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sms_application.SmsConstants.SMS_MESSAGE_STATUS_CHECK
import com.example.sms_application.SmsConstants.SMS_NUMBER
import android.widget.Button


class RelayStatus : AppCompatActivity() {

    lateinit var smsBroadcastReceiver : SmsBroadcastReceiver
    lateinit var layout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relay_sijal_status)
       var Iskljuceno1: Button =  findViewById(R.id.dugme)
        var Iskljuceno2: Button =  findViewById(R.id.dugme2)
        var Iskljuceno3: Button =  findViewById(R.id.dugme3)
        var Iskljuceno4: Button =  findViewById(R.id.dugme4)
        val Ukljuceno1: Button =  findViewById(R.id.dugmeuklj)
        val Ukljuceno2: Button =  findViewById(R.id.dugmeuklj2)
        val Ukljuceno3: Button =  findViewById(R.id.dugmeuklj3)
        val Ukljuceno4: Button =  findViewById(R.id.dugmeuklj4)

        Iskljuceno1.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_ISKLJUCI1)
        })
        Iskljuceno2.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_ISKLJUCI2)
        })
        Iskljuceno3.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_ISKLJUCI3)
        })
        Iskljuceno4.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_ISKLJUCI4)
        })
        Ukljuceno1.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_UKLJUCI1)
        })
        Ukljuceno2.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_UKLJUCI2)
        })
        Ukljuceno3.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_UKLJUCI3)
        })
        Ukljuceno4.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this,"pritisnut",Toast.LENGTH_SHORT)
            sendSmsIskljuci1(SmsConstants.SMS_MESSAGE_UKLJUCI4)
        })

        //getting relative layout that swipe is going to be performed on
        layout = findViewById(R.id.strana_relea)

        //setting listener that will listen to onSwipe event
        layout.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                Log.e("ViewSwipe", "Left")
                val intent = Intent(applicationContext, P_command::class.java)
                startActivity(intent)
            }
            override fun onSwipeRight() {
                Log.e("ViewSwipe", "Right")
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }

        })
    }

    override fun onStart() {
        var sacuvano: SharedPreferences? =
            applicationContext.getSharedPreferences("filesacuvano", 0) // 0 - for private mode
        var editor: SharedPreferences.Editor = sacuvano!!.edit()

        val status1: ImageView = findViewById(R.id.Status_R1)
        val status2: ImageView = findViewById(R.id.Status_R2)
        val status3: ImageView = findViewById(R.id.Status_R3)
        val status4: ImageView = findViewById(R.id.Status_R4)//
        //  val relays = arrayListOf(relayOne,relayTwo,relayThree,relayFour)
        val relays = arrayListOf(status1,status2,status3,status4)
        //                  FORMAT -> 1OFF2ON 3OFF4ON       Index 0,1,2,3...
        //sacuvano = getSharedPreferences("filesacuvano", 0)
        //  var textmessage2
        //   var textmessage2

        var textmessage = sacuvano.getString("porukaizmemo","1OFF2OFF3OFF4OFF")

        for ((index, value) in relays.withIndex()) {
            var delimiter = (index + 1).toString()
            //will take value after last number. In this input string 1OFF2ON 3OFF4ON it's getting OFF for first, ON for second and so on
            var textmessage1 = (textmessage!!.substringAfterLast(delimiter, "OFF").take(3))
            //     Log.i(index.toString(), "Prosao 1" + intent.action)
            if(textmessage1 == "OFF"){
                //  value.setTextColor(Color.RED)

                var   statusslikaID = resources.getIdentifier("crveno1", "drawable", packageName)
                relays[index].setImageResource(statusslikaID)

            }else {
                //    value.setTextColor(Color.GREEN)

                var   statusslikaID = resources.getIdentifier("zeleno1", "drawable", packageName)
                relays[index].setImageResource(statusslikaID)
                //Status1.setImageDrawable(getResources().getDrawable(R.drawable.crveno1))
            }
        }

        smsBroadcastReceiver = object : SmsBroadcastReceiver() {
            override fun broadcastResult(sms: SmsMessage) {

                val firstFourCharacters = sms.displayMessageBody.take(4)
                if (firstFourCharacters.contains("OFF", ignoreCase = true) || firstFourCharacters.contains("ON", ignoreCase = true)) {

                //  textmessage = sms.toString()
                  //  val relayOne: TextView = findViewById(R.id.relay1)
                  //  val relayTwo: TextView = findViewById(R.id.relay2)
                  //  val relayThree: TextView = findViewById(R.id.relay3)
                  //  val relayFour: TextView = findViewById(R.id.relay4)



                   // var index = 0

                    textmessage = sms.displayMessageBody

                    for ((index, value) in relays.withIndex()) {
                        var delimiter = (index + 1).toString()
                        //will take value after last number. In this input string 1OFF2ON 3OFF4ON it's getting OFF for first, ON for second and so on
                        var textmessage1 = sms.displayMessageBody.substringAfterLast(delimiter, "OFF").take(3)
                   //     Log.i(index.toString(), "Prosao 1" + intent.action)
                        if(textmessage1 == "OFF"){
                          //  value.setTextColor(Color.RED)

                         var   statusslikaID = resources.getIdentifier("crveno1", "drawable", packageName)
                            relays[index].setImageResource(statusslikaID)

                        }else {
                        //    value.setTextColor(Color.GREEN)

                         var   statusslikaID = resources.getIdentifier("zeleno1", "drawable", packageName)
                            relays[index].setImageResource(statusslikaID)
                           //Status1.setImageDrawable(getResources().getDrawable(R.drawable.crveno1))
                        }
                    }
                    editor.putString("porukaizmemo", textmessage);
                    editor.apply(); // commit changes

                }
            }
        }
        registerReceiver(smsBroadcastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
        super.onStart()
    }
    override fun onStop() {

        //unregister receiver when changing the action
        unregisterReceiver(smsBroadcastReceiver)
        super.onStop()
    }

    fun checkRelayStatus(view: View) {
        Log.d("receiveMessage", "Got rsms message")

        val smsManager = SmsManager.getDefault() as SmsManager
        smsManager.sendTextMessage(SMS_NUMBER, null, SMS_MESSAGE_STATUS_CHECK, null, null)

        Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
       // var progressBar: ProgressBar = findViewById(R.id.pBarTemp)
      //  progressBar.visibility = View.VISIBLE
    }

    fun sendSmsIskljuci1(konstanta: String) {

        Log.i(SmsConstants.TAG, "Sending sms Iskljuci1: " + intent.action)
        val smsManager2 = SmsManager.getDefault() as SmsManager

        var   Iskljuci1IDpress = resources.getIdentifier("offpress", "drawable", packageName)
        var   Iskljuci1ID = resources.getIdentifier("OFF", "drawable", packageName)
       // Mojedugme1.set
        //send message
        // val SMS_MESSAGE_UKLJUCI
        smsManager2.sendTextMessage(SMS_NUMBER, null, konstanta, null, null)

        Toast.makeText(this, "Slanje..", Toast.LENGTH_SHORT).show()
       /* val handler = Handler()
        handler.postDelayed({
            StatusON1.setImageResource(Iskljuci1ID)
            // do something after 1000ms
        }, 3000)*/

        //  var progressBar: ProgressBar = findViewById(R.id.pBarTemp)
        //progressBar.visibility = View.VISIBLE
    }


}