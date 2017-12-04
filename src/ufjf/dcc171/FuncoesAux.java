package ufjf.dcc171;

import java.util.List;

interface FuncoesAux {
    public void criar(Object objeto) throws Exception;
    public void alterar(Object objeto) throws Exception;
    public void deletar(Object objeto) throws Exception;
    public List<Object> listarTodos() throws Exception;
}
