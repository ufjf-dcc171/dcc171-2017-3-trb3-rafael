package ufjf.dcc171;

interface PessoaTarefaProjetoDAO {
    public String buscaPessoaTabelaProjeto(String nomePessoa) throws Exception;
    public boolean inserirTabelaPTP(String nomePessoa, String nomeTarefa, String nomeProjeto) throws Exception;
    public void alterarPessoaTarefaProjeto(String oldPessoaNome, String newPessoaNome) throws Exception;
    public void deletarProjeto(String nomeProjeto, Pessoa p) throws Exception;
    public void deletarPessoa(String nomePessoa, String nomeTarefa, String nomeProjeto) throws Exception;
    public void alterarProjetoPessoaTarefaProjeto(String oldNomeProjeto, String newNomeProjeto) throws Exception;
    public void alterarTarefaPessoaTarefaProjeto(String oldNomeTarefa, String newNomeTarefa, String nomeProjeto) throws Exception;
}
