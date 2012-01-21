package br.com.caelum.cadastro.tela.listaalunos;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.modelo.Aluno;

public class ListaAlunosAdapter extends ArrayAdapter<Aluno> {

	private final List<Aluno> alunos;
	private final Activity activity;

	public ListaAlunosAdapter(Activity activity, int textViewResourceId, List<Aluno> alunos) {
		super(activity, textViewResourceId, alunos);
		this.activity = activity;
		this.alunos = alunos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Aluno aluno = alunos.get(position);

		View view = activity.getLayoutInflater().inflate(R.layout.item, null);

		LinearLayout fundo = (LinearLayout) view.findViewById(R.item.fundo);
		if (position % 2 == 0) {
			fundo.setBackgroundColor(0xffff0000);
		} else {
			fundo.setBackgroundColor(0xff00ff00);
		}

		ImageView foto = (ImageView) view.findViewById(R.item.foto);
		Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon);
		if (aluno.getFoto() != null) {
			bm = BitmapFactory.decodeFile(aluno.getFoto());
		}
		bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
		foto.setImageBitmap(bm);
		TextView nome = (TextView) view.findViewById(R.item.nome);
		nome.setText(aluno.toString());

		return view;
	}

	@Override
	public long getItemId(int position) {
		return alunos.get(position).getId();
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public Aluno getItem(int position) {
		return alunos.get(position);
	}

}
