package ufjf.dcc171;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAOJDBC implements ProjetoDAO {
    private Connection conexao;
    private PreparedStatement insereQuery;
    private PreparedStatement alteraQuery;
    private PreparedStatement removeQuery;
    private PreparedStatement listarQuery;
    private final String tabela = "DCC171.projeto";

    public ProjetoDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        insereQuery = conexao.prepareStatement("INSERT INTO "+tabela+"(nome) VALUES(?)");
        listarQuery = conexao.prepareStatement("SELECT nome FROM "+tabela+" ORDER BY nome ASC");
    }

    @Override
    public void criar(String nomeProjeto) throws Exception {
        insereQuery.clearParameters();
        insereQuery.setString(1, nomeProjeto);
        insereQuery.executeUpdate();
    }

    @Override
    public void alterar(Projeto projeto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(Projeto projeto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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
}
