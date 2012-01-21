package br.com.caelum.cadastro.helper;

import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DownloadFileTask extends AsyncTask<URL, Integer, Long> {
	private final Context context;
	private final ProgressDialog pd;

	public DownloadFileTask(Context context, ProgressDialog pd) {
		this.context = context;
		this.pd = pd;
	}

	@Override
	protected Long doInBackground(URL... urls) {
		int count = urls.length;
		long totalSize = 0;
		for (int i = 0; i < count; i++) {
			totalSize += downloadFile(urls[i]);
			publishProgress((int) ((i / (float) count) * 100));
		}
		return totalSize;
	}

	private long downloadFile(URL url) {
		return 30;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		setprogressPercent(progress[0]);
	}

	private void setprogressPercent(Integer integer) {
		pd.setProgress(integer);
	}

	@Override
	protected void onPostExecute(Long result) {
		showDialog("Downloaded " + result + " bytes");
	}

	private void showDialog(String string) {
		Toast.makeText(context, string, Toast.LENGTH_LONG);
	}
}
