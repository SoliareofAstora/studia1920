import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintStream;

public class Server
{
    static ServerSocket Receiver;
    static int ClientPorts[] = {666, 777};

    public static void main(String[] args) throws IOException, InterruptedException
    {
        ConnectPlayers();
        PlayGame();
    }

    private static void ConnectPlayers() throws IOException{
        System.out.println("Czekam na połączenia");
        Receiver = new ServerSocket(404);

        int players = 0;
        while (players < 2)
        {
            Socket input = Receiver.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input.getInputStream()));
            String newAddress = reader.readLine();
            reader.close();
            input.close();

            Socket ClientSocket = new Socket("localHost", Integer.parseInt(newAddress));
            PrintStream print = new PrintStream(ClientSocket.getOutputStream());
            print.println("" + ClientPorts[players]);
            ClientSocket.close();

            players++;
            System.out.println("połączono z player " + players + " na porcie " + Integer.toString(ClientPorts[players - 1]));
        }

        System.out.println("Gracze połączeni, setup logiki");
        sendMsg(0,0);
        sendMsg(1,1);
    }

    private static void PlayGame() throws InterruptedException, IOException
    {
        boolean player = true;
        while (true){
            int temp = receiveMsg();

            sendMsg(temp,player?1:0);
            player =!player;
        }
    }

    private static void sendMsg(int msg, int address) throws IOException{
        Socket ClientSocket = new Socket("localHost", ClientPorts[address]);
        PrintStream print = new PrintStream(ClientSocket.getOutputStream());
        print.println(msg);
        ClientSocket.close();
    }

    static int receiveMsg() throws IOException{
        Socket input = Receiver.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input.getInputStream()));
        String output = reader.readLine();
        System.out.println(output);
        input.close();
        reader.close();
        return Integer.parseInt(output);
    }
}
