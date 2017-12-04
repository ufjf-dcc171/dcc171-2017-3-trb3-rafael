package ufjf.dcc171;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Trabalho3 {
    
    public static void main(String[] args) {
        Organizador org = new Organizador(getData());
        org.setVisible(true);
    }
    
    private static List<Projeto> getData() {
        List<Projeto> projetos = null;
        try {
            ProjetoDAO projetoDAO = new ProjetoDAOJDBC();
            projetos = projetoDAO.listarTodos();
        } catch (Exception ex) {
            Logger.getLogger(Trabalho3.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projetos;
    }
}
