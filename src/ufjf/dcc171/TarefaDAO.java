package ufjf.dcc171;

import java.util.List;

interface TarefaDAO {
    public void criar(Tarefa tarefa, String nomeProjeto) throws Exception;
    public String buscar(String nomeTarefa, String nomeProjeto) throws Exception;
    public void alterar(Tarefa tarefa) throws Exception;
    public void deletar(String nomeTarefa, String nomeProjeto) throws Exception;
    public void deletarProjeto(String nomeProjeto) throws Exception;
    public List<Tarefa> listarTodos() throws Exception;
    public List<Tarefa> buscarTarefaConcluida() throws Exception;
    public List<Tarefa> buscarTarefaFazer() throws Exception;
    public List<Tarefa> buscarTarefaProjeto(String nomeProjeto) throws Exception;
}
