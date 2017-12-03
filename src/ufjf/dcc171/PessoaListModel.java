package ufjf.dcc171;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class PessoaListModel implements ListModel {
    private final List<Pessoa> pessoas;
    private final List<ListDataListener> dataListeners;
    
    public PessoaListModel(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
        this.dataListeners =  new ArrayList<>();
    }

    @Override
    public int getSize() {
        return pessoas.size();
    }

    @Override
    public Pessoa getElementAt(int index) {
        return pessoas.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        this.dataListeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        this.dataListeners.remove(l);
    }
}
