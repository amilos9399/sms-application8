package com.example.sms_application

/*import com.example.sms_application.SmsConstants.SMS_MESSAGE_UKLJUCI1
import com.example.sms_application.SmsConstants.SMS_MESSAGE_UKLJUCI2
import com.example.sms_application.SmsConstants.SMS_MESSAGE_UKLJUCI3
import com.example.sms_application.SmsConstants.SMS_MESSAGE_UKLJUCI4
import com.example.sms_application.SmsConstants.SMS_MESSAGE_ISKLJUCI1
import com.example.sms_application.SmsConstants.SMS_MESSAGE_ISKLJUCI2
import com.example.sms_application.SmsConstants.SMS_MESSAGE_ISKLJUCI3
import com.example.sms_application.SmsConstants.SMS_MESSAGE_ISKLJUCI4*/
import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sms_application.SmsConstants.CELSIUS_DEGREES
import com.example.sms_application.SmsConstants.MY_PERMISSIONS_REQUEST_RECEIVE_SMS
import com.example.sms_application.SmsConstants.SMS_MESSAGE_TEMPERATURE_CHECK
import com.example.sms_application.SmsConstants.SMS_NUMBER
import com.example.sms_application.SmsConstants.TAG

class MainActivity : AppCompatActivity() {

    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver




    lateinit var layout: RelativeLayout
   // public static SharedPreferences getDefaultSharedPreferences (Context context)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //intialize shared preference
        var mypref = PreferenceManager.getDefaultSharedPreferences(this);

        //getting relative layout that swipe is going to be performed on
        layout = findViewById(R.id.relativeLayoutMain)

        //setting listener that will listen to onSwipe event
        layout.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                Log.e("ViewSwipe", "Left")
                val intent = Intent(applicationContext, RelayStatus::class.java)
                startActivity(intent)
            }

            override fun onSwipeRight() {
                Log.e("ViewSwipe", "Right")

            }

        })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            //if you don't have permission request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS
                ),
                MY_PERMISSIONS_REQUEST_RECEIVE_SMS
            )
        } else {
//            Toast.makeText(
//                applicationContext,
//                "Permissions needed for application to work correctly",
//                Toast.LENGTH_LONG
//            ).show()
        }

    }

    override fun onStart() {

        smsBroadcastReceiver = object : SmsBroadcastReceiver() {
            override fun broadcastResult(sms: SmsMessage) {

                var progressBar:ProgressBar = findViewById(R.id.pBarTemp)
                progressBar.visibility = View.INVISIBLE

                val temperature: TextView = findViewById(R.id.temp)
//              Format -> Temp C +022.5
                val firstFourCharacters = sms.displayMessageBody.take(4)
                if (firstFourCharacters.toUpperCase() == "TEMP") {

                    val temp =
                        sms.displayMessageBody.substringAfterLast("+")
                            .drop(1)

//                  TODO: set minimal temperature as newest one or highest as highest  one

                    val minTemp: TextView = findViewById(R.id.temp_min_value)
                    val maxTemp: TextView = findViewById(R.id.temp_max_value)

                    val minTempValue: Double? = minTemp.text.toString().toDoubleOrNull()

                    val maxTempValue: Double? = maxTemp.text.toString().toDoubleOrNull()
                    val currentTempValue = temp.toDoubleOrNull()

                    if (currentTempValue != null && minTempValue!=null && maxTempValue!=null ) {
                        if(currentTempValue < minTempValue){
                            minTemp.text = currentTempValue.toString()
                        }else if(currentTempValue > maxTempValue){
                            maxTemp.text = currentTempValue.toString()
                        }
                    }

                    temperature.text = temp + CELSIUS_DEGREES
//                    Toast.makeText(
//                        applicationContext,
//                        sms.displayMessageBody,
//                        Toast.LENGTH_LONG
//                    ).show()
                }

            }

        }
        var progressBar: ProgressBar = findViewById(R.id.pBarTemp)
        progressBar.visibility = View.GONE

        registerReceiver(
            smsBroadcastReceiver,
            IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        )
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(smsBroadcastReceiver)
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {


        val temperature: TextView = findViewById(R.id.temp)
        outState.putString("currentTemperature", temperature.text.toString())

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        val currTemp: String? = savedInstanceState.getString("currentTemperature")
        if(currTemp != null){
            val temperature: TextView = findViewById(R.id.temp);
            temperature.text = currTemp;
        }
        super.onRestoreInstanceState(savedInstanceState)

    }

    //TODO: move to send SMS page
    fun sendSmsForTemperature(view: View) {

        Log.i(TAG, "Sending sms message to received: " + intent.action)
        val smsManager = SmsManager.getDefault() as SmsManager

        //send message
        smsManager.sendTextMessage(SMS_NUMBER, null, SMS_MESSAGE_TEMPERATURE_CHECK, null, null)

        Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()

        var progressBar:ProgressBar = findViewById(R.id.pBarTemp)
        progressBar.visibility = View.VISIBLE

//        openSmsPage()
    }

    fun sendSmsMessage(view: View) {

        Log.i(TAG, "Sending sms message to received: " + intent.action)
        val smsManager1 = SmsManager.getDefault() as SmsManager
        smsManager1.sendTextMessage(SMS_NUMBER, null, "test message", null, null)
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
    }

    fun relayStatus(view: View) {
        val intent = Intent(this, RelayStatus::class.java)
        startActivity(intent)
    }


}