package com.example.sms_application

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sms_application.SmsConstants.SMS_MESSAGE_STATUS_CHECK
import com.example.sms_application.SmsConstants.SMS_NUMBER


class P_command : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var smsBroadcastReceiver : SmsBroadcastReceiver
    lateinit var layout: RelativeLayout
    val vremena = arrayListOf("10","20","30","40","50","60","70","80","90")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_komanda)

        //getting relative layout that swipe is going to be performed on
        layout = findViewById(R.id.relativeLayoutRelay1)
        var potrosac:Spinner = findViewById(R.id.spinner2)
        var vreme:Spinner = findViewById(R.id.spinner3)
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item
        ).also{adapter1 ->
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val adapter2: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item
        ).also{adapter2 ->
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        potrosac.onItemSelectedListener = this
        vreme.onItemSelectedListener = this
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //setting listener that will listen to onSwipe event
        potrosac.adapter = adapter1
        vreme.adapter = adapter2
        layout.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                Log.e("ViewSwipe", "Left")
                val intent = Intent(applicationContext, RelayStatus::class.java)
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
        super.onStart()
        var sacuvano: SharedPreferences? =
            applicationContext.getSharedPreferences("filesacuvano", 0) // 0 - for private mode
        var editor: SharedPreferences.Editor = sacuvano!!.edit()


        //  val relays = arrayListOf(relayOne,relayTwo,relayThree,relayFour)

        //                  FORMAT -> 1OFF2ON 3OFF4ON       Index 0,1,2,3...
        sacuvano = getSharedPreferences("filesacuvano", 0)
        //  var textmessage2
        //   var textmessage2

        var textmessage = sacuvano.getString("porukaizmemo","1OFF2OFF3OFF4OFF")



        smsBroadcastReceiver = object : SmsBroadcastReceiver() {
            override fun broadcastResult(sms: SmsMessage) {

                val firstFourCharacters = sms.displayMessageBody.take(4)

                    editor.putString("porukaizmemo", textmessage);
                    editor.apply(); // commit changes

                }
            }
        }
     //   registerReceiver(smsBroadcastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
     //   super.onStart()

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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}