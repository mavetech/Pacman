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
        down = new ImageIcon("/Users/mafaquekhan/Documents/Personal/Pacman/src/src/images/down.gif").getImage();
        up = new ImageIcon("/Users/mafaquekhan/Documents/Personal/Pacman/src/src/images/up.gif").getImage();
        left = new ImageIcon("/Users/mafaquekhan/Documents/Personal/Pacman/src/src/images/left.gif").getImage();
        right = new ImageIcon("/Users/mafaquekhan/Documents/Personal/Pacman/src/src/images/right.gif").getImage();
        ghost = new ImageIcon("/Users/mafaquekhan/Documents/Personal/Pacman/src/src/images/ghost.gif").getImage();
        heart = new ImageIcon("/Users/mafaquekhan/Documents/Personal/Pacman/src/src/images/heart.png").getImage();
    }

    private void playGame(Graphics2D g){
        if(dying){
            death();
        }
        else{
            movePacman();
            drawPacman(g);
            moveGhosts(g);
            checkMaze();
        }

    }

    private void death() {
        lives--;
        if(lives == 0){
            inGame = false;
        }
        continueLevel();
    }

    private void checkMaze() {
        int i=0;
        boolean finished = true;
        while(i<N_BLOCKS * N_BLOCKS && finished){
            if((screenData[i] & 48)!=0){
                finished = false;
            }
            i++;
        }
        if(finished){
            score+=50;
            if(N_GHOSTS < MAX_GHOSTS){
                N_GHOSTS++;
            }
            if(currentSpeed < maxSpeed){
                currentSpeed++;
            }
            initLevel();
        }

    }

    private void moveGhosts(Graphics g) {
        int pos;
        int count;
        for(int i=0;i<N_GHOSTS;i++){
            if(ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0){
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);
                count = 0;
                if((screenData[pos] & 1) == 0 && ghost_dx[i] != 1){
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if((screenData[pos] & 2) == 0 && ghost_dy[i] != 1){
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if((screenData[pos] & 4) == 0 && ghost_dx[i] != -1){
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if((screenData[pos] & 8) == 0 && ghost_dx[i] != -1){
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }
                if(count==0){
                    if((screenData[pos] & 15)==15){
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    }
                    else{
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }
                }
                else{
                    count = (int) (Math.random()*count);
                    if(count>3){
                        count = 3;
                    }
                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }
            }
            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g, ghost_x[i] + 1, ghost_y[i] + 1);
            if(pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && (pacman_y > (ghost_y[i] -12)) && pacman_y < (ghost_y[i] +12) && inGame){
                dying = true;
            }
        }
    }

    private void drawGhost(Graphics g, int x, int y) {
        g.drawImage(ghost, x, y, this);
    }

    private void drawPacman(Graphics g) {
        if(req_dx == -1){
            g.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        }
        else if(req_dx == 1){
            g.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        }
        else if(req_dy == -1){
            g.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        }
        else if(req_dy == 1){
            g.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void movePacman() {

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacman_dx = req_dx;
                    pacman_dy = req_dy;
                }
            }

            // Check for standstill
            if ((pacman_dx == -1 && pacman_dy == 0 && (ch & 1) != 0)
                    || (pacman_dx == 1 && pacman_dy == 0 && (ch & 4) != 0)
                    || (pacman_dx == 0 && pacman_dy == -1 && (ch & 2) != 0)
                    || (pacman_dx == 0 && pacman_dy == 1 && (ch & 8) != 0)) {
                pacman_dx = 0;
                pacman_dy = 0;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacman_dx;
        pacman_y = pacman_y + PACMAN_SPEED * pacman_dy;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);
        drawMaze(g2d);
        drawScore(g2d);
        if(inGame){
            playGame(g2d);
        }
        else{
            showIntroScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void showIntroScreen(Graphics2D g) {
        String start = "Press Space to Start!";
        g.setColor(Color.YELLOW);
        g.drawString(start, SCREEN_SIZE/4, 150);

    }

    private void drawScore(Graphics g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 151, 79));
        String s = "Score: "+score;
        g.drawString(s, SCREEN_SIZE/2 + 96, SCREEN_SIZE + 16);
        for(int i=0;i<lives;i++){
            g.drawImage(heart, i*28, SCREEN_SIZE +1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));

                if ((levelData[i] == 0)) {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                i++;
            }
        }
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
        lives = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 3;
    }

    private void initLevel() {
        for(int i=0;i<N_BLOCKS*N_BLOCKS;i++){
            screenData[i] = levelData[i];
        }
    }

    // defines position of ghosts
    private void continueLevel(){
        int dx = 1;
        int random;
        for(int i=0;i<N_GHOSTS;i++){
            ghost_y[i] = 4 *  BLOCK_SIZE;
            ghost_x[i] = 4 *  BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) Math.random() * (currentSpeed + 1);
            if(random > currentSpeed){
                random = currentSpeed;
            }
            ghostSpeed[i] = validspeeds[random];
        }
        pacman_x = 7 * BLOCK_SIZE;
        pacman_y = 11 * BLOCK_SIZE;
        pacman_dx = 0;
        pacman_dy = 0;
        req_dx = 0;
        req_dy = 0;
        dying = false;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
