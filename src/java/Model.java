import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
