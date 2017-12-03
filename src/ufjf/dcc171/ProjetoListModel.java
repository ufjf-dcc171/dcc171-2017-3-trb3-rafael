package ufjf.dcc171;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ProjetoListModel implements ListModel {
    private final List<Projeto> projeto;
    private final List<ListDataListener> dataListeners;
    
    public ProjetoListModel(List<Projeto> projeto) {
        this.projeto = projeto;
        this.dataListeners =  new ArrayList<>();
    }

    @Override
    public int getSize() {
        return projeto.size();
    }

    @Override
    public Projeto getElementAt(int index) {
        return projeto.get(index);
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
