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
    private final String tabela = "DCC171.projeto_tarefa_pessoa";
    
    public PessoaTarefaProjetoDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        inserirProjetoTarefaPessoaQuery = conexao.prepareStatement("INSERT INTO " + tabela + " VALUES(?,?,?)");
        buscarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " WHERE nome_pessoa = ?");
        verificarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " WHERE nome_projeto = ? AND nome_tarefa = ? AND nome_pessoa = ?");
        alterarPessoaTarefaProjetoQuery = conexao.prepareStatement("UPDATE " + tabela + " SET nome_pessoa = ? WHERE nome_pessoa = ?");
        deletarPessoaProjetoQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome_projeto = ? AND nome_pessoa = ?");
        deletarPessoaTarefaProjetoQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome_projeto = ? AND nome_tarefa = ? AND nome_pessoa = ?");
//        listarPessoaProjetoQuery = conexao.prepareStatement("SELECT * FROM (SELECT " + tabela + ".* FROM " + tabela + " JOIN (SELECT * FROM " + tabelaPTP + " WHERE nome_projeto = ?) AS resultado1 ON resultado1.nome_pessoa = " + tabela + ".nome) AS resultado2");
//        listarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM (SELECT " + tabela + ".* FROM " + tabela + " JOIN (SELECT * FROM " + tabelaPTP + " WHERE nome_projeto = ? AND nome_tarefa = ?) AS resultado1 ON resultado1.nome_pessoa = " + tabela + ".nome) AS resultado2");
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
}
