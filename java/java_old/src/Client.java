import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

public class Client
{
    static JFrame frame = new JFrame();
    static JPanel p = new JPanel();
    static Button map[][] = new Button[20][20];
    public static ServerSocket Receiver;
    public static boolean active = false;
    public static boolean AmICross = false;

    public static void main(String[] args) throws IOException
    {
        Connect();
        Initialize();
    }

    private static void Connect() throws IOException
    {
        System.out.println("klient");
        int TempPortAddress = 1080;
        Socket Sender;
        Receiver = new ServerSocket(TempPortAddress);
        Sender = new Socket("localHost", 404);
        PrintStream print = new PrintStream(Sender.getOutputStream());
        print.println("" + TempPortAddress);
        print.close();

        Sender = Receiver.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Sender.getInputStream()));
        String newAddress = reader.readLine();
        Receiver.close();
        reader.close();

        Receiver = new ServerSocket(Integer.parseInt(newAddress));
        System.out.println("Połączono na " + newAddress);
        print.close();
        Sender.close();
    }

    private static void Initialize() throws IOException
    {
        frame.setSize(800, 800);
        frame.setResizable(false);
        p.setLayout(new GridLayout(20, 20));
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                map[i][j] = new Button(i, j);
                p.add(map[i][j]);
            }
            frame.add(p);
            frame.setVisible(true);
        }
        if (receiveMsg() == 0)
        {
            AmICross = true;
            active = true;
        } else
        {
            WaitMessage();
        }
        frame.setName(Client.AmICross ? "cross" : "circle");
    }

    public static void WaitMessage() throws IOException
    {
        active = false;
        int x = receiveMsg();
        if (x==9999){
            System.out.println("YOU LOST");
            System.exit(0);
        }
        int y = x % 100;
        x = (x - y) / 100;
        map[x][y].EnemyMove();
        active = true;
    }

    static int receiveMsg() throws IOException
    {
        Socket input = Receiver.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input.getInputStream()));
        String output = reader.readLine();
        System.out.println(output);
        input.close();
        reader.close();
        return Integer.parseInt(output);
    }

    public static void sendMsg(int msg) throws IOException
    {
        Socket Sender = new Socket("localHost", 404);
        PrintStream print = new PrintStream(Sender.getOutputStream());
        print.println(msg);
        print.close();
        Sender.close();
    }

    public static void sendMsg(int x, int y) throws IOException
    {
        int horizontal = 0;
        int vertical = 0;
        int cross1 = 0;
        int cross2 = 0;
        if (map[x][y].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
        {
            horizontal++;
            vertical++;
            cross1++;
            cross2++;
			/*w prawo*/
            int temp = x + 1;

            while (temp < 20)
            {
                if (map[temp][y].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    horizontal++;
                    temp++;
                } else break;

            }
			/*w lewo*/
            temp = x - 1;

            while (temp > -1)
            {
                if (map[temp][y].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    horizontal++;
                    temp--;
                } else break;

            }
			/*w gore*/
            temp = y - 1;

            while (temp > -1)
            {
                if (map[x][temp].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    vertical++;
                    temp--;
                } else break;

            }
			/*w dol*/
            temp = y + 1;

            while (temp < 20)
            {
                if (map[x][temp].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    vertical++;
                    temp++;
                } else break;

            }

            int temp_x = x + 1;
            int temp_y = y + 1;
            while (temp_x < 20 & temp_y < 20)
            {
                if (map[temp_x][temp_y].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    cross1++;
                    temp_x++;
                    temp_y++;
                } else break;

            }
            temp_x = x - 1;
            temp_y = y - 1;
            while (temp_x > -1 & temp_y > -1)
            {
                if (map[temp_x][temp_y].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    cross1++;
                    temp_x--;
                    temp_y--;
                } else break;

            }

            temp_x = x + 1;
            temp_y = y - 1;
            while (temp_x < 20 & temp_y > -1)
            {
                if (map[temp_x][temp_y].state.equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    cross2++;
                    temp_x++;
                    temp_y--;
                } else break;

            }

            temp_x = x - 1;
            temp_y = y + 1;
            while (temp_x > -1 & temp_y < 20)
            {
                if (map[temp_x][temp_y].state .equals(AmICross ? ButtonState.Cross : ButtonState.Circle))
                {
                    cross2++;
                    temp_x--;
                    temp_y++;
                } else break;
            }
        }
        if (horizontal > 4 || vertical > 4 || cross1 > 4 || cross2 > 4)
        {
            sendMsg(9999);
            System.out.println("YOU WON!!!");
            System.exit(0);
        }

        int msg = 100 * x + y;
        sendMsg(msg);
    }
}