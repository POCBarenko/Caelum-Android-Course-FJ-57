package br.com.caelum.cadastro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.modelo.Aluno;

public class Formulario extends Activity {
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
	}

}
