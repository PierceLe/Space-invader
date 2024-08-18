package invaders.singleton;
public class DifficultChooser {
    /**
     * return path to the wanted difficulty level
     */
    private String path;
    private static DifficultChooser d;
    private DifficultChooser(){
    }
    public static DifficultChooser getInstance(){
        if (d == null) {
            d  = new DifficultChooser();
        }
        return d;
    }

    public String getEasy(){
        return "src/main/resources/config_easy.json";
    }

    public String getMedium(){
        return "src/main/resources/config_medium.json";
    }

    public String getHard(){
        return "src/main/resources/config_hard.json";
    }
}
