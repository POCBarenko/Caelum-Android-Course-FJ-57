package br.com.caelum.cadastro.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.tela.listaalunos.ListaAlunos;

public class SMSReceiver extends BroadcastReceiver /* trata eventos assincronos do android */{

	private static final int NOTIFICATION_ID = 1234567;

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

			notificar(context, smsMessage[0].getMessageBody());
			
			abortBroadcast(); //aborta propagação de broadcasts, ou seja, as outras apps nao receberão a notificação da mensagem.
		}
		dao.close();
	}

	private void notificar(Context context, String message) {
		Notification notification = new Notification(R.drawable.icon, message, System.currentTimeMillis());
		
		Intent listaAlunosIntent = new Intent(context, ListaAlunos.class);
		listaAlunosIntent.putExtra("notificationId", NOTIFICATION_ID);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, listaAlunosIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		notification.setLatestEventInfo(context, "Mensagem de aluno", "Você recebeu um SMS de um aluno", pendingIntent);
		
		NotificationManager notMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notMan.notify(NOTIFICATION_ID, notification);
	}

}
