package ufjf.dcc171;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAOJDBC implements ProjetoDAO {
    private Connection conexao;
    private PreparedStatement inserirQuery;
    private PreparedStatement buscarQuery;
    private PreparedStatement alterarQuery;
    private PreparedStatement alterarPessoaTarefaProjetoQuery;
    private PreparedStatement deletarQuery;
    private PreparedStatement listarQuery;
    private final String tabela = "DCC171.projeto";
    private PessoaTarefaProjetoDAO pessoaTarefaProjetoDAO;

    public ProjetoDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        inserirQuery = conexao.prepareStatement("INSERT INTO "+tabela+"(nome) VALUES(?)");
        buscarQuery = conexao.prepareStatement("SELECT nome FROM "+tabela+" WHERE nome = ?");
        alterarQuery = conexao.prepareStatement("UPDATE "+tabela+" SET nome = ? WHERE nome = ?");
//        alterarPessoaTarefaProjetoQuery = conexao.prepareStatement("UPDATE " + tabelaPTP + " SET nome_projeto= ? WHERE nome_projeto = ?");
        deletarQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome = ?");
        listarQuery = conexao.prepareStatement("SELECT nome FROM "+tabela+" ORDER BY nome ASC");
        pessoaTarefaProjetoDAO = new PessoaTarefaProjetoDAOJDBC();
    }

    @Override
    public void criar(String nomeProjeto) throws Exception {
        if(buscar(nomeProjeto).equals("")) {
            inserirQuery.clearParameters();
            inserirQuery.setString(1, nomeProjeto);
            inserirQuery.executeUpdate();
        }
    }

    @Override
    public String buscar(String nomeProjeto) throws Exception {
        String p = "";
        buscarQuery.clearParameters();
        buscarQuery.setString(1, nomeProjeto);
        ResultSet resultado = buscarQuery.executeQuery();
        while (resultado.next()) {
            p =resultado.getString(1);
        }
        return p;
    }

    @Override
    public void alterar(String oldNomeProjeto, String newNomeProjeto) throws Exception {
        alterarQuery.clearParameters();
        alterarQuery.setString(1, newNomeProjeto);
        alterarQuery.setString(2, oldNomeProjeto);
        alterarQuery.executeUpdate();
        
        pessoaTarefaProjetoDAO.alterarProjetoPessoaTarefaProjeto(oldNomeProjeto, newNomeProjeto);
    }

    @Override
    public void deletar(String nomeProjeto) throws Exception {
        deletarQuery.clearParameters();
        deletarQuery.setString(1, nomeProjeto);
        deletarQuery.executeUpdate();    }

    @Override
    public List<Projeto> listarTodos() throws Exception {
        List<Projeto> projetos = new ArrayList<>();
        listarQuery.clearParameters();
        ResultSet resultado = listarQuery.executeQuery();
        while (resultado.next()) {
            Projeto p = new Projeto(resultado.getString(1));
            projetos.add(p);
        }
        return projetos;
    }
    
    public void alterarPessoaTarefaProjeto(String oldNomeProjeto, String newNomeProjeto) throws Exception {
        pessoaTarefaProjetoDAO.alterarProjetoPessoaTarefaProjeto(oldNomeProjeto, newNomeProjeto);
//        alterarPessoaTarefaProjetoQuery.clearParameters();
//        alterarPessoaTarefaProjetoQuery.setString(1, newNomeProjeto);
//        alterarPessoaTarefaProjetoQuery.setString(2, oldNomeProjeto);
//        alterarPessoaTarefaProjetoQuery.executeUpdate();
    }
}
