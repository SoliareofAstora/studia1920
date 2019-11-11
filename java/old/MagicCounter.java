import java.util.Scanner;

class Start {

    public static void main(String[] args) {

        MagicCounter counter = new MagicCounter();
        int [] commands = {5,-2,0,0,5,0,5,2,0,-2,3,0,2,0,-111,0,0,2,-111,2};
        int [] results = {5,3,3,0,5,5,5,15,15,15,15,15,17,17,17,17,17,17,17,19};
        for (int i = 0; i < commands.length; i++) {
            int c = commands[i];
            int r = results[i];

            System.out.print(String.format( "%5d", c )+"  ");
            counter.compute(c);
            int g = counter.get();

            System.out.print(String.format( "%5d", g )+"  ");
            System.out.print(String.format( "%5d", r )+"  "+(g==r)+" \n");

        }

//        MagicCounter counter = new MagicCounter();
//        int [] commands = {1,2,3,0,2,3,4};
//        int [] results = {1,3,6,6,6,12,16};
//        for (int i = 0; i < commands.length; i++) {
//            int c = commands[i];
//            int r = results[i];
//
//            System.out.print(String.format( "%5d", c )+"  ");
//            counter.compute(c);
//            int g = counter.get();
//
//            System.out.print(String.format( "%5d", g )+"  ");
//            System.out.print(String.format( "%5d", r )+"  "+(g==r)+" \n");
//
//        }


    }
}

public class MagicCounter {
    private int accumulator;
    private int previous_value=1;
    private int holder = 0;

    public void compute(int code) {
        if (holder>0){
            accumulator = holder*code + accumulator;
            holder = 0;
            previous_value = code;
            return;
        }
        if (holder<0){
            if (holder>-10){
                holder+=1;
            }
            if (holder == code){
                holder = 0;
            }
            return;
        }
        if (previous_value == 0) {
            if (code < 0) {
                holder = code;
                previous_value = code;
                return;
            }
            if (code>0){
                holder = code;
                return;
            }
        }
        if (code == 0 & previous_value==0) {
            reset();
        } else {
            accumulator += code;
            previous_value = code;
        }
    }

    public int get() {
        return accumulator;
    }

    public void reset() {
        accumulator = 0;
        previous_value = 1;
        holder = 0;
    }
}
