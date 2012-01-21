package br.com.caelum.cadastro.helper;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.modelo.Aluno;

public class Sincronismo {
	private String url = "http://www.caelum.com.br/mobile?dado=";
	private final Context context;

	public Sincronismo(Context context) {
		this.context = context;
	}

	public void sincronizar() {
		// ProgressDialog deve ser instanciado FORA DA THREAD para iniciá-lo antes da THREAD iniciar
		final ProgressDialog progress = ProgressDialog.show(context, "Aguarde...", "Enviando dados para a web!", true);

		// toast deve ser instanciado FORA DA THREAD devido a trabalhar internamente com outra thread e se perder quando está em outra thread diferente da principal
		final Toast aviso = Toast.makeText(context, "Dados enviados com sucesso!", Toast.LENGTH_LONG);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}

				AlunoDao dao = new AlunoDao(context);
				List<Aluno> lista = dao.getAll();
				dao.close();

				HttpClient httpClient = new DefaultHttpClient();

				String listaJSON = new AlunoConverter().toJSON(lista);

				Log.i("envio", listaJSON);

				try {
					HttpPost httpPost = new HttpPost(url);
					httpPost.setEntity(new StringEntity(listaJSON));

					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");

					HttpResponse response = httpClient.execute(httpPost);

					aviso.setText(EntityUtils.toString(response.getEntity()));
					aviso.show();
				} catch (Exception e) {
					aviso.setText(e.getMessage());
				}
				progress.dismiss();
			}
		}, "SincronismoCadastroAluno").start();
	}

}
