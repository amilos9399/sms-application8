package com.example.sms_application

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sms_application.SmsConstants.SMS_MESSAGE_STATUS_CHECK
import com.example.sms_application.SmsConstants.SMS_NUMBER


class P_command : AppCompatActivity() , AdapterView.OnItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_komanda)
        val pozicije = resources.getStringArray(R.array.vremenaString)
        val spiner2 = findViewById<Spinner>(R.id.padajuci_meni1)
        if (spiner2 != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item, pozicije
            )
            adapter.setDropDownViewResource(R.layout.spinnerpadajdole)
            spiner2.adapter = adapter
          //  spiner2.onItemSelectedListener = this
        }
        var mypref = PreferenceManager.getDefaultSharedPreferences(this);

        layout = findViewById(R.id.strana_P_komande)

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

    fun OnItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val spiner2 = parent.getItemAtPosition(position).toString()
        Toast.makeText(this, spiner2, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    // lateinit var layout: RelativeLayout
    //  val spinner2: Spinner = findViewById(R.id.spinner2)
    lateinit var layout: RelativeLayout

    override fun onStart() {
        super.onStart()
        var sacuvano: SharedPreferences? =
            applicationContext.getSharedPreferences("filesacuvano1", 0) // 0 - for private mode
        var editor: SharedPreferences.Editor = sacuvano!!.edit()
        //  val relays = arrayListOf(relayOne,relayTwo,relayThree,relayFour)

        //                  FORMAT -> 1OFF2ON 3OFF4ON       Index 0,1,2,3...
        sacuvano = getSharedPreferences("filesacuvano1", 0)
        //  var textmessage2
        //   var textmessage2
        var textmessage = sacuvano.getString("porukaizmemo1", "1OFF2OFF3OFF4OFF")
        smsBroadcastReceiver = object : SmsBroadcastReceiver() {
            override fun broadcastResult(sms: SmsMessage) {

                val firstFourCharacters = sms.displayMessageBody.take(4)

                editor.putString("porukaizmemo1", textmessage);
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
        Log.d("receiveMessage", "Got sms message")

        val smsManager = SmsManager.getDefault() as SmsManager
        smsManager.sendTextMessage(SMS_NUMBER, null, SMS_MESSAGE_STATUS_CHECK, null, null)

        Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
        // var progressBar: ProgressBar = findViewById(R.id.pBarTemp)
        //  progressBar.visibility = View.VISIBLE
    }

   override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

}


