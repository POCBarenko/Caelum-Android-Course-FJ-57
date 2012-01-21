package br.com.caelum.cadastro.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.dao.AlunoDao;

public class SMSReceiver extends BroadcastReceiver /* trata eventos assincronos do android */{

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		bundle.remove("pdus");
		SmsMessage smsMessage[] = new SmsMessage[messages.length];

		for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}

		AlunoDao dao = new AlunoDao(context);
		if (dao.isAluno(smsMessage[0].getDisplayOriginatingAddress())) {
			Toast.makeText(context, "Sms de Aluno: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG).show();

			MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
			mp.start();

			abortBroadcast();
		}
		dao.close();
	}

}
