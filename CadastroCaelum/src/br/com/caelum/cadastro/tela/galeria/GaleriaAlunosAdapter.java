package br.com.caelum.cadastro.tela.galeria;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.R.drawable;
import br.com.caelum.cadastro.modelo.Aluno;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

class GaleriaAlunosAdapter extends BaseAdapter {
	private List<Aluno> alunos;
	private Context context;

	public GaleriaAlunosAdapter(List<Aluno> alunos, Context context) {
		this.alunos = alunos;
		this.context = context;
		
	}

	@Override
	public int getCount() {
		return alunos.size();
	}

	@Override
	public Object getItem(int position) {
		return alunos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return alunos.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView foto = new ImageView(context);
		Aluno aluno = alunos.get(position);
		
		if(aluno.getFoto() != null) {
			Bitmap bm = BitmapFactory.decodeFile(aluno.getFoto());
			foto.setImageBitmap(Bitmap.createScaledBitmap(bm, 200, 200, true));
		}else {
			foto.setImageResource(R.drawable.icon);
		}
		return foto;
	}

}
