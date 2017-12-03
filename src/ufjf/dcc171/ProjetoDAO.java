package ufjf.dcc171;

import java.util.List;

interface ProjetoDAO {
    public void criar(String nomeProjeto) throws Exception;
    public void alterar(Projeto projeto) throws Exception;
    public void deletar(Projeto projeto) throws Exception;
    public List<Projeto> listarTodos() throws Exception;
}
