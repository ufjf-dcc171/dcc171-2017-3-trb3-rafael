package ufjf.dcc171;

import java.util.List;

interface PessoaDAO {
    public void criar(Pessoa pessoa) throws Exception;
    public void alterar(Pessoa pessoa) throws Exception;
    public void deletar(Pessoa pessoa) throws Exception;
    public List<Pessoa> listarTodos() throws Exception;
}
