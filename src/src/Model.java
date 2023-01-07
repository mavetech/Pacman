import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Model extends JPanel implements ActionListener {

    // describes the height and width of the playing field
    private Dimension d;
    // to display text
    private Font smallFont = new Font("Arial", Font.BOLD, 14);
    // if game is running
    private boolean inGame = false;
    // if pacman is alive
    private boolean dying = false;
    // size of each block in the game
    private final int BLOCK_SIZE = 24;
    // number of blocks in width and height
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;
    // initial ghosts
    private int N_GHOSTS = 6;
    private int lives, score;
    // for position of the ghosts
    private int[] dx, dy;
    // to determine number and position of ghosts
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;
    private Image heart, ghost;
    private Image up, down, left, right;
    // coordinates of pacman and delta change in horizontal and vertical diretion of pacman
    private int pacman_x, pacman_y, pacman_dx, pacman_dy;
    private int req_dx, req_dy;
    private final int validspeeds[] = {1,2,3,4,5,6};
    private final int maxSpeed = 6;
    private int currentSpeed = 3;
    // takes all data from levelData to redraw game
    private short[] screenData;
    private Timer timer;

    private final short levelData[] = {
            19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
            17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
            21, 0,  0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    
    public Model(){
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }

    class TAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if(inGame){
                if(key==KeyEvent.VK_LEFT){
                    req_dx = -1;
                    req_dy = 0;
                }
                else if(key== KeyEvent.VK_RIGHT){
                    req_dx = 1;
                    req_dy = 0;
                }
                else if(key== KeyEvent.VK_UP){
                    req_dx = 0;
                    req_dy = -1;
                }
                else if(key== KeyEvent.VK_DOWN){
                    req_dx = 0;
                    req_dy = 1;
                }
                else if(key== KeyEvent.VK_ESCAPE && timer.isRunning()){
                    inGame = false;
                }
            }
            else{
                if(key==KeyEvent.VK_SPACE){
                    inGame = true;
                    initGame();
                }
            }
        }
    }

    private void loadImages() {
        down = new ImageIcon("/src/images/down.gif").getImage();
        up = new ImageIcon("/src/images/up.gif").getImage();
        left = new ImageIcon("/src/images/left.gif").getImage();
        right = new ImageIcon("/src/images/right.gif").getImage();
        ghost = new ImageIcon("/src/images/ghost.gif").getImage();
        heart = new ImageIcon("/src/images/heart.png").getImage();
    }

    private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        // Timer determines how often the image is redrawn
        timer = new Timer(40, this);
        timer.start();
    }

    private void initGame() {
    }




    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
