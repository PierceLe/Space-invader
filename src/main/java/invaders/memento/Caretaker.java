package invaders.memento;

import invaders.memento.Memento;

public class Caretaker {
    /**
     * save memento
     */
    private Memento m;
    public void setMemento(Memento me){
        this.m = me;
    }
    public Memento getMemento(){
        return this.m;
    }
}
