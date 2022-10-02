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
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sms_application.SmsConstants.SMS_MESSAGE_STATUS_CHECK
import com.example.sms_application.SmsConstants.SMS_NUMBER


class P_command : AppCompatActivity() {
    val pozicije2 = arrayOf("10", "20", "30", "40","50","60","70","80","90","100","110","120","130","140","150","160","170","180")
    val pozicije3 = arrayOf("Klima", "Pumpa", "Svetlo", "Spoljne svetlo")


    lateinit var layout1: ConstraintLayout
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

                Toast.makeText(
                    applicationContext,
                    "izabrano je " + pozicije2[position] + " minuta rada",
                    Toast.LENGTH_SHORT
                ).show()
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

            Toast.makeText(
                applicationContext,
                "izabran je " + pozicije3[position] ,
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }

        layout1 = findViewById(R.id.strana_P_komande)

        //setting listener that will listen to onSwipe event
        layout1.setOnTouchListener(object : OnSwipeTouchListener() {
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


}




























