import java.io.*;
import java.nio.charset.Charset;


public class Main
{
    static String[] tab = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static int getindex(String a)
    {
        for (int i = 0; i < tab.length; i++)
        {
            if (a.equals(tab[i])) return i;
        }
        return -1;
    }


    public static void main(String[] args) throws IOException
    {
        //args[0] 1- szyfrowanie 0 deszyfrowanie args[1]ścieżka do filderu zawierającego pliki które mają być zaszyfrowane
        if (args.length != 2)
        {
            System.out.print("Wrong input");
            return;
        }

//https://stackoverflow.com/questions/5259872/building-an-array-of-files-in-a-directory
        File[] fileList = new File(args[1]).listFiles();
        for (int i = 0; i < fileList.length; i++)
        {
            System.out.println(fileList[i]);
        }

        FileInputStream in = null;
        FileOutputStream out = null;

        for (File file : fileList)
        {
            String outputString = "";

            if (args[0].equals("1"))
            {
                in = new FileInputStream(file);
                int c;
                int position = 0;
                //szyfrowanie pliku
                while ((c = in.read()) != -1)
                {
                    String a = "" + (char) c;
                    a = a.toLowerCase();
                    {
                        if (a.equals("ą")) a = "a";
                        if (a.equals("ć")) a = "c";
                        if (a.equals("ę")) a = "e";
                        if (a.equals("ł")) a = "l";
                        if (a.equals("ń")) a = "n";
                        if (a.equals("ó")) a = "o";
                        if (a.equals("ś")) a = "s";
                        if (a.equals("ź")) a = "z";
                        if (a.equals("ż")) a = "z";
                    }

                    int encoder = getindex(a);
                    if (encoder == -1)
                    {
                        System.out.print(a);
                        outputString += a;
                    } else
                    {
                        encoder += position;
                        String output = "" + tab[encoder % 26];
                        System.out.print(output);
                        outputString += output;
                    }
                    position++;
                }


                if (in != null)
                {
                    in.close();
                }
                //zapis zaszyfrowanego pliku
                out = new FileOutputStream(file);
                out.write(outputString.getBytes(Charset.forName("UTF-8")));
                if (out != null)
                {
                    out.close();
                }
            } else
            {
                //deszyfrowanie
                in = new FileInputStream(file);
                int c;
                int position = 0;
                while ((c = in.read()) != -1)
                {
                    String a = "" + (char) c;
                    int encoder = getindex(a);
                    if (encoder == -1)
                    {
                        System.out.print(a);
                        outputString += a;
                    } else
                    {
                        encoder -= position;
                        while (encoder < 0) encoder += 26;
                        String output = "" + tab[(encoder % 26)];
                        System.out.print(output);
                        outputString += output;
                    }
                    position++;
                }
                if (in != null)
                {
                    in.close();
                }
                //zapis rozszyfrowanego pliku
                out = new FileOutputStream(file);
                out.write(outputString.getBytes(Charset.forName("UTF-8")));
                if (out != null)
                {
                    out.close();
                }
            }
        }
    }
}