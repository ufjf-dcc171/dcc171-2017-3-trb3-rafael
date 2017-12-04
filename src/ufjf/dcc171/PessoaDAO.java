package ufjf.dcc171;

import java.util.List;

interface PessoaDAO {
    public void criar(Pessoa pessoa, String nomeTarefa, String nomeProjeto) throws Exception;
    public Pessoa buscar(Pessoa pessoa) throws Exception;
    public void alterar(Pessoa pessoa) throws Exception;
    public void deletar(String nomeProjeto, String nomeTarefa, String nomePessoa) throws Exception;
    public void deletarTarefa(String nomeProjeto, String nomeTarefa) throws Exception;
    public void deletarProjeto(String nomeProjeto) throws Exception;
    public List<Pessoa> listarTodos() throws Exception;
    public List<Pessoa> listarPessoaTarefaProjeto(String nomeProjeto, String nomeTarefa) throws Exception;
}
