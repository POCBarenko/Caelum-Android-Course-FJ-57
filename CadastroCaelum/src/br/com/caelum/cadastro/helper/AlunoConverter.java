package br.com.caelum.cadastro.helper;

import java.util.List;

import org.json.JSONException;
import org.json.JSONStringer;

import br.com.caelum.cadastro.modelo.Aluno;

public class AlunoConverter {
	public String toJSON(List<Aluno> alunos) {
		try {
			JSONStringer json = new JSONStringer();
			json.object().key("list").array().object().key("aluno").array();
			
			for(Aluno aluno: alunos) {
				json.object()
					.key("aluno").object()
						.key("id").value(aluno.getId())
						.key("nome").value(aluno.getNome())
						.key("telefone").value(aluno.getTelefone())
						.key("endereco").value(aluno.getEndereco())
						.key("site").value(aluno.getSite())
						.key("nota").value(aluno.getNota())
					.endObject()
				.endObject();
			}
			
			return json.endArray().endObject().endArray().endObject().toString();
		}catch (JSONException e) {
			throw new RuntimeException(e);		
		}
	}
}
