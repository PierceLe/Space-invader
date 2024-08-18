package invaders.engine;

import java.util.List;
import java.nio.channels.ReadPendingException;
import java.util.ArrayList;

import invaders.ConfigReader;
import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import invaders.observer.Observer;
import invaders.observer.ScoreObserver;
import invaders.observer.TimeObserver;
import invaders.observer.TimeScore;
import javafx.stage.Stage;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
// import javafx.scene.layout.StackPane;
import org.json.simple.JSONObject;

import invaders.entities.Player;
import invaders.memento.Caretaker;
import invaders.memento.Memento;
import invaders.memento.Originator;
import invaders.gameobject.GameObject;

public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;
    private Stage s;
    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;

    private Text timerText;
    private Text playerScore;

    private Button difficultyButton;
    private Button undoButton;
    private Button rmFastProBut;
    private Button rmSlowProBut;
    private Button rmFastEneBut;
    private Button rmSlowEneBut;

    private int realTime = 0;
    private int count = 0;
    private int factor;
    private Player player = null;
    private boolean isGameEnd = false;
    private Observer timeObserver;
    private Observer scoreObserver;
    private TimeScore timeScoreInfo;
    private Originator originator;
    private Caretaker careTaker;
    private boolean isUndo = false;
    private boolean signalChange = false;
    private int specialCount = 0;
    private Renderable tempo = null;

    private boolean isRmSlowPro = false;
    private boolean isRmFastPro = false;
    private boolean isRmSlowEne = false;
    private boolean isRmFastEne = false;
    // private static final double VIEWPORT_MARGIN = 280.0;

	public GameWindow(GameEngine model){
        this.s = s;
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();
        this.player = this.model.getPlayer();

        this.difficultyButton = new Button("change difficulty");
        this.difficultyButton.setLayoutX(100);
        this.difficultyButton.setLayoutY(100);
        this.difficultyButton.setFocusTraversable(false); // make button not to interact with keyboard signal
        this.difficultyButton.setOnAction(e -> {
            setDiffBut();
        });

        this.undoButton = new Button("Undo");
        this.undoButton.setLayoutX(100);
        this.undoButton.setLayoutY(50);
        this.undoButton.setFocusTraversable(false); // make button not to interact with keyboard signal
        this.undoButton.setOnAction(e -> {
            this.isUndo = true;
        });

        this.rmSlowProBut = new Button("Rm Slow Pro");
        this.rmSlowProBut.setLayoutX(100);
        this.rmSlowProBut.setLayoutY(150);
        this.rmSlowProBut.setFocusTraversable(false); // make button not to interact with keyboard signal
        this.rmSlowProBut.setOnAction(e -> {
            this.isRmSlowPro = true;
        });

        this.rmFastProBut = new Button("Rm Fast Pro");
        this.rmFastProBut.setLayoutX(100);
        this.rmFastProBut.setLayoutY(200);
        this.rmFastProBut.setFocusTraversable(false); // make button not to interact with keyboard signal
        this.rmFastProBut.setOnAction(e -> {
            this.isRmFastPro = true;
        });

        this.rmSlowEneBut = new Button("Rm Slow Ene");
        this.rmSlowEneBut.setLayoutX(100);
        this.rmSlowEneBut.setLayoutY(250);
        this.rmSlowEneBut.setFocusTraversable(false); // make button not to interact with keyboard signal
        this.rmSlowEneBut.setOnAction(e -> {
            this.isRmSlowEne = true;
        });

        this.rmFastEneBut = new Button("Rm Fast Ene");
        this.rmFastEneBut.setLayoutX(100);
        this.rmFastEneBut.setLayoutY(300);
        this.rmFastEneBut.setFocusTraversable(false); // make button not to interact with keyboard signal
        this.rmFastEneBut.setOnAction(e -> {
            this.isRmFastEne = true;
        });

        pane = new Pane();
        this.timerText = new Text(this.width - 150, 15, "Time: 0");
        this.timerText.setFill(Color.RED);
        this.playerScore = new Text(this.width - 150, 50, "Score: 0");
        this.playerScore.setFill(Color.RED);

        this.timeObserver = new TimeObserver(timerText, 0);
        this.scoreObserver = new ScoreObserver(playerScore, 0);
        this.timeScoreInfo = new TimeScore();
        
        this.timeScoreInfo.addObserver(this.scoreObserver);
        this.timeScoreInfo.addObserver(this.timeObserver);

        pane.getChildren().add(timerText);
        pane.getChildren().add(playerScore);
        pane.getChildren().add(difficultyButton);
        pane.getChildren().add(undoButton);
        pane.getChildren().add(rmSlowProBut);
        pane.getChildren().add(rmFastProBut);
        pane.getChildren().add(rmSlowEneBut);
        pane.getChildren().add(rmFastEneBut);
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);
    }

	public void run(){

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), t ->{
            this.draw(false);
            

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private void draw(boolean b) {
        if(isGameEnd){
            return;
        }
        this.updateTime();
        model.update();
        List<Renderable> renderables = model.getRenderables();
        int aliveEnemyCount = 0;
        for (Renderable r : renderables){
            if (r.getRenderableObjectName().equals("Enemy")){
                Enemy tempE = (Enemy) r;
                if(tempE.isAlive()){
                    aliveEnemyCount += 1;
                }
            }
        }
        if((aliveEnemyCount == 0 || this.player == null || !this.player.isAlive()) && !b){
            this.isGameEnd = true;
        }
        updatePlayerScore();
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (Renderable entity : renderables){
            if (!entity.isAlive()){
                for (EntityView entityView : entityViews){
                    if (entityView.matchesEntity(entity)){
                        entityView.markForDelete();
                    }
                }
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }

        
        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);
        
        if(b){
            return;
        }

        if (model.isSpacePressed()) {
            setState();
            model.setIsSpace(false);
        }
        if (isUndo) {

            undo();
            isUndo = false;
        }
        if(isRmSlowPro){
            removePro("Slow");
            isRmSlowPro = false;
        }else if (isRmFastPro) {
            removePro("Fast");
            isRmFastPro = false;
        }else if (isRmSlowEne) {
            removeEnemy("Slow");
            isRmSlowEne = false;
        }else if(isRmFastEne){
            removeEnemy("Fast");
            isRmFastEne = false;
        }

    }

	public Scene getScene() {
        return scene;
    }

    private void updateTime(){
        if(isGameEnd){
            return;
        }
        this.count += 1;
        if (this.count % 100 == 0){
            this.realTime += 1;
            // System.out.println(this.realTime);
            this.timeScoreInfo.timeUpdate(this.realTime);
        }
        
    }

    private void updatePlayerScore(){
        int score = this.player.getScore();
        this.timeScoreInfo.scoreUpdate(score);
    }

    public void setStage(Stage stage){
        this.s = stage;
    }

    private void setDiffBut(){
        this.s.close();
    }
    /**
     * save information for undoing when space is pressed
     */
    private void setState() {
        originator = new Originator(this.player.getScore(), this.realTime, this.model.getRenderables(),this.model.getGameObjects());
        careTaker = new Caretaker();
        careTaker.setMemento(originator.createMemento());
        this.signalChange = true;
    }
    /**
     * trigger when undo button is pressed
     */
    private void undo(){
        if(this.careTaker == null){
            return;
        }
        if (!this.signalChange){
            return;
        }
        // List<Renderable> tempRens = new ArrayList<>(this.model.getRenderables());
        for(Renderable r : this.model.getRenderables()){
            if(r.getRenderableObjectName().equals("Player")){
                continue;
            }
            r.takeDamage(r.getHealth());
        }
        this.draw(true);//In order to remove previous before undo
        for(EntityView enti: new ArrayList<>(entityViews)){
            if(((EntityViewImpl)enti).getRenderable() instanceof Bunker){
                pane.getChildren().remove(enti.getNode());
                entityViews.remove(enti);
            }
        }
        Memento tempM = this.careTaker.getMemento();
        int oldScore = tempM.getScore();
        int oldTime = tempM.getTime();
        List<Renderable> oldRens = tempM.getRenderables();
        List<GameObject> oldObjectList = tempM.getGameObjects();
        this.player.setScore(oldScore);
        this.realTime = oldTime;
        this.model.setRenders(oldRens);
        this.model.setGameObjects(oldObjectList);
        System.out.println("123456");
        this.signalChange = false;
    }

    /**
     * 
     * @param strategy slow or fast
     * remove enemy projectiles due to decleared strategy
     */
    private void removePro(String strategy){
        List<Renderable> newR = new ArrayList<>(this.model.getRenderables());
        for(Renderable r: newR){
            if (r.getRenderableObjectName().equals("EnemyProjectile")){
                EnemyProjectile tempEPro = (EnemyProjectile) r;
                if (tempEPro.getProjectile().getStrategyName().equals(strategy)){
                    tempEPro.takeDamage(1);
                }
            }
        }
    }

    /**
     * 
     * @param strategy slow or fast
     * remove enemy due to strategy
     */
    private void removeEnemy(String strategy){
        List<Renderable> newR = new ArrayList<>(this.model.getRenderables());
        for(Renderable r: newR){
            if (r.getRenderableObjectName().equals("Enemy")){
                Enemy tempE = (Enemy) r;
                if (tempE.getEnemyType().equals(strategy)){
                    tempE.takeDamage(tempE.getHealth());
                }
            }
        }
    }
}
