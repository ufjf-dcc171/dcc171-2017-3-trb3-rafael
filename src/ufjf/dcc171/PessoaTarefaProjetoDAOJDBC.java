package ufjf.dcc171;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class PessoaTarefaProjetoDAOJDBC implements PessoaTarefaProjetoDAO {
    private Connection conexao;
    private PreparedStatement inserirProjetoTarefaPessoaQuery;
    private PreparedStatement buscarPessoaTarefaProjetoQuery;
    private PreparedStatement verificarPessoaTarefaProjetoQuery;
    private PreparedStatement alterarPessoaTarefaProjetoQuery;
    private PreparedStatement deletarPessoaProjetoQuery;
    private PreparedStatement deletarPessoaTarefaProjetoQuery;
    private PreparedStatement alterarTarefaPessoaTarefaProjetoQuery;
    private PreparedStatement alterarProjetoPessoaTarefaProjetoQuery;
    private final String tabela = "DCC171.projeto_tarefa_pessoa";
    
    public PessoaTarefaProjetoDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        inserirProjetoTarefaPessoaQuery = conexao.prepareStatement("INSERT INTO " + tabela + " VALUES(?,?,?)");
        buscarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " WHERE nome_pessoa = ?");
        verificarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " WHERE nome_projeto = ? AND nome_tarefa = ? AND nome_pessoa = ?");
        alterarPessoaTarefaProjetoQuery = conexao.prepareStatement("UPDATE " + tabela + " SET nome_pessoa = ? WHERE nome_pessoa = ?");
        deletarPessoaProjetoQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome_projeto = ? AND nome_pessoa = ?");
        deletarPessoaTarefaProjetoQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome_projeto = ? AND nome_tarefa = ? AND nome_pessoa = ?");
        alterarTarefaPessoaTarefaProjetoQuery = conexao.prepareStatement("UPDATE " + tabela + " SET nome_tarefa= ? WHERE nome_tarefa= ? AND nome_projeto = ?");
        alterarProjetoPessoaTarefaProjetoQuery = conexao.prepareStatement("UPDATE " + tabela + " SET nome_projeto= ? WHERE nome_projeto = ?");
    }
    
    private void inserirPessoaPTP(String nomeProjeto, String nomeTarefa, String nomePessoa) throws Exception {
        inserirProjetoTarefaPessoaQuery.clearParameters();
        inserirProjetoTarefaPessoaQuery.setString(1, nomeProjeto);
        inserirProjetoTarefaPessoaQuery.setString(2, nomeTarefa);
        inserirProjetoTarefaPessoaQuery.setString(3, nomePessoa);
        inserirProjetoTarefaPessoaQuery.executeUpdate();
    }
    
    @Override
    public String buscaPessoaTabelaProjeto(String nomePessoa) throws Exception {
        String nome = "";
        buscarPessoaTarefaProjetoQuery.clearParameters();
        buscarPessoaTarefaProjetoQuery.setString(1, nomePessoa);
        ResultSet resultado = buscarPessoaTarefaProjetoQuery.executeQuery();
        while (resultado.next()) {
            nome = resultado.getString(1);
        }
        return nome;
    }
    
    @Override
    public boolean inserirTabelaPTP(String nomePessoa, String nomeTarefa, String nomeProjeto) throws Exception {
        verificarPessoaTarefaProjetoQuery.clearParameters();
        verificarPessoaTarefaProjetoQuery.setString(1, nomeProjeto);
        verificarPessoaTarefaProjetoQuery.setString(2, nomeTarefa);
        verificarPessoaTarefaProjetoQuery.setString(3, nomePessoa);
        ResultSet resultado = verificarPessoaTarefaProjetoQuery.executeQuery();
        while (resultado.next()) {
            if(resultado.getString(1).length() > 0) return false;
        }
        
        inserirPessoaPTP(nomeProjeto, nomeTarefa, nomePessoa);
        
        return true;
    }
    
    @Override
    public void alterarPessoaTarefaProjeto(String oldPessoaNome, String newPessoaNome) throws Exception {
        alterarPessoaTarefaProjetoQuery.clearParameters();
        alterarPessoaTarefaProjetoQuery.setString(1, newPessoaNome);
        alterarPessoaTarefaProjetoQuery.setString(2, oldPessoaNome);
        alterarPessoaTarefaProjetoQuery.executeUpdate();
    }
    
    @Override
    public void deletarProjeto(String nomeProjeto, Pessoa p) throws Exception {
        deletarPessoaProjetoQuery.clearParameters();
        deletarPessoaProjetoQuery.setString(1, nomeProjeto);
        deletarPessoaProjetoQuery.setString(2, p.getNome());
        deletarPessoaProjetoQuery.executeUpdate();
    }
    
    @Override
    public void deletarPessoa(String nomePessoa, String nomeTarefa, String nomeProjeto) throws Exception {
        deletarPessoaTarefaProjetoQuery.clearParameters();
        deletarPessoaTarefaProjetoQuery.setString(1, nomeProjeto);
        deletarPessoaTarefaProjetoQuery.setString(2, nomeTarefa);
        deletarPessoaTarefaProjetoQuery.setString(3, nomePessoa);
        deletarPessoaTarefaProjetoQuery.executeUpdate();
    }
    
    @Override
    public void alterarTarefaPessoaTarefaProjeto(String oldNomeTarefa, String newNomeTarefa, String nomeProjeto) throws Exception {
        alterarTarefaPessoaTarefaProjetoQuery.clearParameters();
        alterarTarefaPessoaTarefaProjetoQuery.setString(1, newNomeTarefa);
        alterarTarefaPessoaTarefaProjetoQuery.setString(2, oldNomeTarefa);
        alterarTarefaPessoaTarefaProjetoQuery.setString(3, nomeProjeto);
        alterarTarefaPessoaTarefaProjetoQuery.executeUpdate();
    }
    
    @Override
    public void alterarProjetoPessoaTarefaProjeto(String oldNomeProjeto, String newNomeProjeto) throws Exception {
        alterarProjetoPessoaTarefaProjetoQuery.clearParameters();
        alterarProjetoPessoaTarefaProjetoQuery.setString(1, newNomeProjeto);
        alterarProjetoPessoaTarefaProjetoQuery.setString(2, oldNomeProjeto);
        alterarProjetoPessoaTarefaProjetoQuery.executeUpdate();
    }
}
