import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Button extends JButton implements ActionListener
{
    public ButtonState state;
    int x, y;

    public Button()
    {
    }

    public Button(int x, int y)
    {
        this.x = x;
        this.y = y;
        state = ButtonState.Empty;
        this.addActionListener(this);
    }

    public void EnemyMove()
    {
        setBackground(!(Client.AmICross) ? Color.GREEN : Color.BLUE);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (Client.active && state == ButtonState.Empty)
        {
            Client.active = false;
            setBackground(Client.AmICross ? Color.GREEN : Color.BLUE);
            state = Client.AmICross ? ButtonState.Cross : ButtonState.Circle;
            try
            {
                Client.sendMsg(x, y);
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            try
            {
                Client.WaitMessage();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
}
