package com.example.sms_application

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.example.sms_application.SmsConstants.SMS_NUMBER

private const val TAG = "SendMessageActivity"
//private const val SMS_NUMBER = "+38162299066"

abstract class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //Do the checks
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            for (sms: SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {

                Log.d("receiveMessage", "Got sms message")

                if (sms.originatingAddress == SMS_NUMBER) {
                    broadcastResult(sms)
                }
            }
        }
    }

    protected abstract fun broadcastResult(sms: SmsMessage)

}