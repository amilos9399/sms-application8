package com.example.sms_application

import android.R.string
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sms_application.SmsConstants.SMS_NUMBER


class P_command : AppCompatActivity() {
    lateinit var smsBroadcastReceiver : SmsBroadcastReceiver
    lateinit var layout: RelativeLayout
    val pozicije2 = arrayOf(
        "10",
        "20",
        "30",
        "40",
        "50",
        "60",
        "70",
        "80",
        "90",
        "100",
        "110",
        "120",
        "130",
        "140",
        "150",
        "160",
        "170",
        "180",
        "190",
        "200",
        "210",
        "220",
        "230",
        "240"
    )
    val pozicije3 = arrayOf("1.Pumpa", "2.Svetlo", "3.Spoljne svetlo", "4.Terasa")
    var Pvrednost: Int = 48
    var P2vrednost: Int = 1
    lateinit var layout2: ConstraintLayout
    public val strDate = "Current Time :  + getCurrentDate()"
    //    val pozicije = resources.getStringArray(R.array.vremenaString)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_p_komanda)
        var mypref = PreferenceManager.getDefaultSharedPreferences(this);
        val spinner = findViewById<Spinner>(R.id.padajuci_meni1)
        val spinner2 = findViewById<Spinner>(R.id.padajuci_meni2)
        val meni =
            ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, pozicije2)
        val meni2 =
            ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, pozicije3)
        val P_komanda: Button = findViewById(R.id.dugmesalji_P)

        P_komanda.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Slanje", Toast.LENGTH_SHORT).show()
            sendSmsIskljuci1(prebaci_kar(P2vrednost, Pvrednost))
        })


        meni.setDropDownViewResource(R.layout.spinner_dropdown_item)
        meni2.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = meni;
        spinner2.adapter = meni2;
        // spinner.dropDownWidth = 50
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(

                parent: AdapterView<*>?,


                view: View?,
                position: Int,
                id: Long
            ) {
                Pvrednost = pozicije2[position].toInt()
                Toast.makeText(
                    applicationContext,
                    "izabrano je " + pozicije2[position] + " minuta rada",
                    Toast.LENGTH_SHORT
                ).show()
                var izlaz: TextView = findViewById(R.id.textView8)
                izlaz.text = prebaci_kar(P2vrednost, Pvrednost)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(

                parent: AdapterView<*>?,


                view: View?,
                position: Int,
                id: Long
            ) {
                P2vrednost = position
                Toast.makeText(
                    applicationContext,
                    "izabran je " + pozicije3[position],
                    Toast.LENGTH_SHORT
                ).show()
                var izlaz: TextView = findViewById(R.id.textView8)
                izlaz.text = prebaci_kar(P2vrednost, Pvrednost)
             }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        layout2 = findViewById(R.id.strana_P_komande)

        //setting listener that will listen to onSwipe event
        layout2.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                Log.e("ViewSwipe", "Left")
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onSwipeRight() {
                Log.e("ViewSwipe", "Right")
                val intent = Intent(applicationContext, RelayStatus::class.java)
                startActivity(intent)
            }

        })

    }


        override fun onStart() {
            var sacuvano: SharedPreferences? =
                applicationContext.getSharedPreferences("filesacuvano", 0) // 0 - for private mode
            var editor: SharedPreferences.Editor = sacuvano!!.edit()

            /*val status1: ImageView = findViewById(R.id.Status_R1)
            val status2: ImageView = findViewById(R.id.Status_R2)
            val status3: ImageView = findViewById(R.id.Status_R3)
            val status4: ImageView = findViewById(R.id.Status_R4)
            //  val relays = arrayListOf(relayOne,relayTwo,relayThree,relayFour)
            val relays = arrayListOf(status1,status2,status3,status4)*/
            //                  FORMAT -> 1OFF2ON 3OFF4ON       Index 0,1,2,3...
            //sacuvano = getSharedPreferences("filesacuvano", 0)
            //  var textmessage2
            //   var textmessage2

            var textmessage = sacuvano.getString("porukaizmemo","1OFF2OFF3OFF4OFF")

            /*for ((index, value) in relays.withIndex()) {
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
            }*/

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

                         /*for ((index, value) in relays.withIndex()) {
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
                         }*/
                        editor.putString("porukaizmemo", textmessage);
                        editor.apply(); // commit changes

                    }
                }
            }
            registerReceiver(smsBroadcastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
            super.onStart()


        }


    override fun onStop(){
        super.onStop()

    }
    fun sendSmsIskljuci1(konstanta: String) {

        Log.i(SmsConstants.TAG, "Sending sms Iskljuci1: " + intent.action)
        val smsManager2 = SmsManager.getDefault() as SmsManager

        var Iskljuci1IDpress = resources.getIdentifier("offpress", "drawable", packageName)
        var Iskljuci1ID = resources.getIdentifier("OFF", "drawable", packageName)
        // Mojedugme1.set
        //send message
        // val SMS_MESSAGE_UKLJUCI
        smsManager2.sendTextMessage(SMS_NUMBER, null, konstanta, null, null)

        //Toast.makeText(this, "Slanje..", Toast.LENGTH_SHORT).show()
        /* val handler = Handler()
         handler.postDelayed({
             StatusON1.setImageResource(Iskljuci1ID)
             // do something after 1000ms
         }, 3000)*/

        //  var progressBar: ProgressBar = findViewById(R.id.pBarTemp)
        //progressBar.visibility = View.VISIBLE

    }
    fun prebaci_kar(broj2:Int, broj: Int): String {
        var Pvrednost1: Int = broj / 10 + 48
        var P2vrednost1: Int = broj2 + 49
        var karakter: String = "P"+ (P2vrednost1.toChar().toString()) + (Pvrednost1.toChar().toString())


        return karakter
    }

}




























