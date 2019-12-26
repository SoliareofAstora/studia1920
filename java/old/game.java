import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class Start {
    public static void main(String[] args) {
        var GAME = new game();
        GAME.init();
    }
}



public class game{
    JButton up;
    JButton down;
    JButton left;
    JButton right;

    JButton start;
    JButton pause;
    JButton stop;

    JPanel panel;
    JFrame ramka;
    JLabel current_state;

    enum buttons{
        up,
        down,
        left,
        right,
        start,
        pause,
        stop
    }

    class button_pressed implements ActionListener {
        private buttons button_type;
        button_pressed(buttons type){
            button_type=type;
        }
        public void actionPerformed( ActionEvent e ) {
            perform_action(button_type);
        }
    }

    public void init_UI() {
        ramka = new JFrame( "Moja ramka do zabawy z GUI" );
        ramka.setSize( 600, 750 );

        current_state = new JLabel("Loading");
        up = new JButton("UP");
        up.addActionListener(new button_pressed(buttons.up));
        up.setBounds(0,600,150,50);
        down = new JButton("DOWN");
        down.addActionListener(new button_pressed(buttons.down));
        down.setBounds(150,600,150,50);
        left = new JButton("LEFT");
        left.addActionListener(new button_pressed(buttons.left));
        left.setBounds(300,600,150,50);
        right = new JButton("RIGHT");
        right.addActionListener(new button_pressed(buttons.right));
        right.setBounds(450,600,150,50);
        start = new JButton("START");
        start.addActionListener(new button_pressed(buttons.start));
        start.setBounds(0,650,200,50);
        pause = new JButton("PAUSE");
        pause.addActionListener(new button_pressed(buttons.pause));
        pause.setBounds(200,650,200,50);
        stop = new JButton("STOP/RESET");
        stop.addActionListener(new button_pressed(buttons.stop));
        stop.setBounds(400,650,200,50);

        ramka.getContentPane().add(up);
        ramka.getContentPane().add(down);
        ramka.getContentPane().add(left);
        ramka.getContentPane().add(right);
        ramka.getContentPane().add(start);
        ramka.getContentPane().add(pause);
        ramka.getContentPane().add(stop);

        panel = new JPanel();
        panel.setBounds(0,0,600,600);
        panel.setBackground(new Color(59, 7, 171));
        ramka.getContentPane().add(panel);

        ramka.getContentPane().add(current_state,BorderLayout.SOUTH);

        ramka.setVisible( true );
        ramka.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    private enum game_states{
        drawing,
        animating,
        paused
    }
    private enum arrow_direction {
        up,
        down,
        left,
        right
        }
    class coords{
        public int x;
        public int y;
        coords(int nx, int ny){
            x=nx;
            y=ny;
        }
        public coords shift_coords(arrow_direction direction){
            switch (direction) {
                case up:
                    return new coords(x,y-1);
                case down:
                    return new coords(x,y+1);
                case left:
                    return new coords(x-1,y);
                case right:
                    return new coords(x+1,y);
            }
            return null;
        }
    }

    private boolean check_free_space(coords check){
        for (arrow a : image_history) {
            if(check.x==a.cords.x & check.y==a.cords.y){
                return false;
            }
        }
        return true;
    }

    class arrow{
        public coords cords;
        public arrow_direction direction;
        arrow(coords cds, arrow_direction dir){
            cords=cds;
            direction = dir;
        }
    }
    private coords current_position;
    private int current_frame = 0;
    private List<arrow> image_history;

    private game_states game_state = game_states.drawing;

    public void init(){
        System.out.println("gramy dalej");
        init_UI();
        reset();
    }

    public void reset(){
        current_frame = 0;
        current_position = new coords(6,6);
        image_history = new LinkedList<>();
        current_state.setText(game_state.toString());
    }

    private AtomicBoolean rendering = new AtomicBoolean(false);
    private Thread animation_thread;

    public void start_animation(){
        current_state.setText(game_state.toString());
        animation_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                rendering.set(true);
                while (rendering.get()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!rendering.get()) return;

                    draw_arrow(current_frame);
                    current_frame+=1;
                    if (current_frame==image_history.size()){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!rendering.get()) return;
                        current_frame = 0;
                        clear_canvas();
                    }
                }

            }
        });
        animation_thread.start();
    }

    public void stop_animation(){
        rendering.set(false);
    }

    public void clear_canvas(){
        var g = panel.getGraphics();
        g.setColor(new Color(59, 7, 171));
        g.fillRect(0,0,600,600);
    }
    public void draw_arrow(int index){
        var arrow = image_history.get(index);
        var g = panel.getGraphics();
        g.setColor(Color.WHITE);
        var x =50*arrow.cords.x;
        var y = 50*arrow.cords.y;

        switch(arrow.direction){
            case up:
                g.fillRect(x+22,y+10,6,30);
                g.fillPolygon(new int[] {x+25,x+35,x+15},new int[] {y,y+20,y+20},3);
                break;
            case down:
                g.fillRect(x+22,y+10,8,30);
                g.fillPolygon(new int[] {x+25,x+35,x+15},new int[] {y+50,y+30,y+30},3);
                break;
            case left:
                g.fillRect(x+10,y+22,30,8);
                g.fillPolygon(new int[] {x,x+20,x+20},new int[] {y+25,y+35,y+15},3);
                break;
            case right:
                g.fillRect(x+10,y+22,30,8);
                g.fillPolygon(new int[] {x+50,x+30,x+30},new int[] {y+25,y+35,y+15},3);
                break;
        }
    }

    public void redraw_all(){
        clear_canvas();
        for (int i = 0; i < image_history.size(); i++) {
            draw_arrow(i);
        }
    }

    private void perform_action(buttons action){
        if (game_state == game_states.drawing) {
            switch (action) {
                case up:
                    create_arrow(arrow_direction.up);
                    break;
                case down:
                    create_arrow(arrow_direction.down);
                    break;
                case left:
                    create_arrow(arrow_direction.left);
                    break;
                case right:
                    create_arrow(arrow_direction.right);
                    break;
            }
        }
        switch (action){
            case start:
                if (game_state == game_states.drawing) {
                    game_state = game_states.animating;
                    current_frame = 0;
                    clear_canvas();
                    start_animation();
                }
                if (game_state == game_states.paused){
                    game_state = game_states.animating;
                    start_animation();
                }
                break;
            case pause:
                if (game_state == game_states.animating) {
                    game_state = game_states.paused;
                    stop_animation();
                }
                break;
            case stop:
                if (game_state==game_states.drawing){
                    reset();
                    clear_canvas();
                }
                else {
                    game_state=game_states.drawing;
                    stop_animation();
                    redraw_all();
                }
                break;
        }
        current_state.setText(game_state.toString());
    }

    private void create_arrow(arrow_direction type){
        boolean OK = true;
        switch (type) {
            case up:
                if (current_position.y==0)OK = false;
                break;
            case down:
                if (current_position.y==11)OK = false;
                break;
            case left:
                if (current_position.x==0)OK = false;
                break;
            case right:
                if (current_position.x==11)OK = false;
                break;
        }
        if (!OK){
            System.out.println("Trying to draw on outer space");
            return;
        }
        if(!check_free_space(current_position.shift_coords(type))){
            System.out.println("Space occupied");
            return;
        }
        image_history.add(new arrow(current_position,type));
        draw_arrow(image_history.size()-1);
        current_position = current_position.shift_coords(type);
    }

}