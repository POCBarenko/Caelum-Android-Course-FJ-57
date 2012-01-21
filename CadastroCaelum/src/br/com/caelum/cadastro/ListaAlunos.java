package br.com.caelum.cadastro;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.modelo.Aluno;

public class ListaAlunos extends Activity {
	private ListView listaAlunos;
	private Aluno alunoSelecionado;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista); // renderiza a tela
	}

	private void loadListaAlunos(ArrayAdapter<Aluno> adapter) {
		listaAlunos = (ListView) findViewById(R.id.listaAlunos);

		listaAlunos.setAdapter(adapter);

		listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long idPosicao) {
				Intent edicao = new Intent(ListaAlunos.this, Formulario.class);
				edicao.putExtra("alunoSelecionado", (Aluno)listaAlunos.getItemAtPosition(posicao));
				startActivity(edicao);
			}
		});

		listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicao, long idPosicao) {
				alunoSelecionado = (Aluno) adapterView.getItemAtPosition(posicao);
				registerForContextMenu(listaAlunos);
				return false; // true nao faz callback da classe (executa apenas o longClick).
								// false faz callback (executa também o evento de click).
			}
		});
	}

	// Menu principal da aplicacao
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem novo = menu.add(0, 0, 0, "Novo");
		novo.setIcon(android.R.drawable.ic_menu_add);

		MenuItem sincronizar = menu.add(0, 1, 0, "Sincronizar");
		sincronizar.setIcon(android.R.drawable.ic_menu_rotate);

		MenuItem galeria = menu.add(0, 2, 0, "Galeria");
		galeria.setIcon(android.R.drawable.ic_menu_camera);

		MenuItem mapa = menu.add(0, 3, 0, "Mapa");
		mapa.setIcon(android.R.drawable.ic_menu_mapmode);

		return super.onCreateOptionsMenu(menu);
	}

	// implementacao do menu principal da aplicacao
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Toast.makeText(this, "Novo", Toast.LENGTH_SHORT).show();
			Intent novo = new Intent(this, Formulario.class);
			startActivity(novo);
			break;
		case 1:
			Toast.makeText(this, "Sincronizar", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Intent galeria = new Intent(this, Galeria.class);
			startActivity(galeria);
			break;
		case 3:
			Toast.makeText(this, "Mapa", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// submenu dos itens da aplicacao
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add(0, 0, 0, "Ligar");
		menu.add(0, 1, 0, "Enviar SMS");
		menu.add(0, 2, 0, "Achar no Mapa");
		menu.add(0, 3, 0, "Navegar no Site");
		menu.add(0, 4, 0, "Excluir");
		menu.add(0, 5, 0, "Enviar email");
		menu.add(0, 6, 0, "Compartilhar");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	// implementacao dos itens do submenu da aplicacao
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent ligar = new Intent(Intent.ACTION_CALL);
			ligar.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
			startActivity(ligar);
			break;
		case 1:
			Intent sms = new Intent(Intent.ACTION_VIEW);
			sms.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
			sms.putExtra("sms_body", "Mensagem sms padrão");
			startActivity(sms);
			break;
		case 2:
			Intent geo = new Intent(Intent.ACTION_VIEW);
			geo.setData(Uri.parse("geo:0,0?z=17&q=" + alunoSelecionado.getEndereco()));
			startActivity(geo);
			break;
		case 3:
			Intent site = new Intent(Intent.ACTION_VIEW);
			site.setData(Uri.parse("http://" + alunoSelecionado.getSite()));
			startActivity(site);
			break;
		case 4:
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Excluir").setMessage("Deseja excluir o aluno?")
					.setPositiveButton("Claro!", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							AlunoDao dao = new AlunoDao(ListaAlunos.this);
							try {
								dao.delete(alunoSelecionado);
							} finally {
								dao.close();
							}
							onResume();
						}
					}).setNegativeButton("Não", null).show();
			break;
		case 5:
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("message/rfc822");
			email.putExtra(Intent.EXTRA_EMAIL, new String[] {"caelum@caelum.com.br"});
			email.putExtra(Intent.EXTRA_SUBJECT, new String[] {"Este é o titulo do email"});
			email.putExtra(Intent.EXTRA_TEXT, new String[] {"Este é o conteudo do email"});
			startActivity(Intent.createChooser(email, "Selecione a sua aplicação de email!"));
			break;
		case 6:
			Intent social = new Intent(Intent.ACTION_SEND);
			social.setType("text/plain");
			social.putExtra(Intent.EXTRA_EMAIL, new String[] {"caelum@caelum.com.br"});
			social.putExtra(Intent.EXTRA_SUBJECT, new String[] {"Este é o titulo"});
			social.putExtra(Intent.EXTRA_TEXT, new String[] {"Este é o conteudol"});
			startActivity(Intent.createChooser(social, "Selecione a sua aplicação de rede social!"));
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		AlunoDao dao = new AlunoDao(ListaAlunos.this);
		final List<Aluno> alunos = dao.getAll();
		dao.close();
		ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Aluno aluno = alunos.get(position);

				View view = ListaAlunos.this.getLayoutInflater().inflate(R.layout.item, null);

				LinearLayout fundo = (LinearLayout) view.findViewById(R.item.fundo);
				if (position % 2 == 0) {
					fundo.setBackgroundColor(0xffff0000);
				} else {
					fundo.setBackgroundColor(0xff00ff00);
				}

				ImageView foto = (ImageView) view.findViewById(R.item.foto);
				Bitmap bm = BitmapFactory.decodeResource(ListaAlunos.this.getResources(), R.drawable.icon);
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
		};
		loadListaAlunos(adapter);

	}
}