package br.com.caelum.cadastro.tela.formulario;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.R.drawable;
import br.com.caelum.cadastro.R.formulario;
import br.com.caelum.cadastro.R.layout;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.modelo.Aluno;

public class Formulario extends Activity {
	private static final int PHOTO_CODE = 1234;
	private Aluno aluno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulario);

		aluno = (Aluno) getIntent().getSerializableExtra("alunoSelecionado");
		if (aluno == null) {
			aluno = new Aluno();
		} else {
			((EditText) findViewById(R.formulario.nome)).setText(aluno.getNome());
			((EditText) findViewById(R.formulario.endereco)).setText(aluno.getEndereco());
			// ((EditText) findViewById(R.formulario.foto)).setText(aluno.getFoto());
			((RatingBar) findViewById(R.formulario.nota)).setRating(aluno.getNota().floatValue());
			((EditText) findViewById(R.formulario.site)).setText(aluno.getSite());
			((EditText) findViewById(R.formulario.telefone)).setText(aluno.getTelefone());
			
			loadImage();
			
			Button b = (Button) findViewById(R.formulario.btn);
			b.setText("Alterar");
		}

		Button botao = (Button) findViewById(R.formulario.btn);
		botao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aluno.setNome(((EditText) findViewById(R.formulario.nome)).getText().toString());
				aluno.setEndereco(((EditText) findViewById(R.formulario.endereco)).getText().toString());
				// aluno.setFoto(((EditText) findViewById(R.formulario.foto)).getText().toString());
				aluno.setNota((double) ((RatingBar) findViewById(R.formulario.nota)).getRating());
				aluno.setSite(((EditText) findViewById(R.formulario.site)).getText().toString());
				aluno.setTelefone(((EditText) findViewById(R.formulario.telefone)).getText().toString());

				AlunoDao dao = new AlunoDao(Formulario.this);
				try {
					dao.save(aluno);
				} finally {
					dao.close();
				}

				finish(); // Fecha a tela e volta para o pai sem precisar clicar no botao voltar
			}
		});

		ImageButton imgBt = (ImageButton) findViewById(R.formulario.foto);
		imgBt.setImageResource(R.drawable.icon);

		imgBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				File arquivo = new File(String.format("%s/%s.jpg", Environment.getExternalStorageDirectory(), System.currentTimeMillis()));
				Uri outputFileUri = Uri.fromFile(arquivo);

				aluno.setFoto(arquivo.getAbsolutePath());

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

				startActivityForResult(intent, PHOTO_CODE);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PHOTO_CODE) {
			if (resultCode != RESULT_OK) {
				aluno.setFoto(null);
			}
			loadImage();
		}
	}

	private void loadImage() {
		if (aluno.getFoto() != null) {
			Bitmap bm = BitmapFactory.decodeFile(aluno.getFoto());
			bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
			
			ImageButton bt = (ImageButton)findViewById(R.formulario.foto);
			bt.setImageBitmap(bm);
		}
	}
}
