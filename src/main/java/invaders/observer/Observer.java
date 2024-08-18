package invaders.observer;

import javafx.scene.text.Text;

public interface Observer {
    public void update();
    public void selfUpdate(int i);
}
