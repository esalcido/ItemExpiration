import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import javax.swing.*;

public class ItemGUIManagement extends Applet {
    JButton button_1;
    JTextField textfield_1;
    JButton button_2;
    JButton button_3;

    public void init() {
        ItemManagementLayout customLayout = new ItemManagementLayout();

        setFont(new Font("Helvetica", Font.PLAIN, 12));
        setLayout(customLayout);

        button_1 = new JButton("button_1");
        add(button_1);

        textfield_1 = new JTextField("textfield_1");
        add(textfield_1);

        button_2 = new JButton("button_2");
        add(button_2);

        button_3 = new JButton("button_3");
        add(button_3);

        setSize(getPreferredSize());

    }

    public static void main(String args[]) {
        ItemGUIManagement applet = new ItemGUIManagement();
        Frame window = new Frame("ItemManagement");

        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        applet.init();
        window.add("Center", applet);
        window.pack();
        window.setVisible(true);
    }
}

class ItemManagementLayout implements LayoutManager {

    public ItemManagementLayout() {
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);

        Insets insets = parent.getInsets();
        dim.width = 320 + insets.left + insets.right;
        dim.height = 240 + insets.top + insets.bottom;

        return dim;
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        return dim;
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        Component c;
        c = parent.getComponent(0);
        if (c.isVisible()) {c.setBounds(insets.left+40,insets.top+184,72,24);}
        c = parent.getComponent(1);
        if (c.isVisible()) {c.setBounds(insets.left+32,insets.top+24,256,144);}
        c = parent.getComponent(2);
        if (c.isVisible()) {c.setBounds(insets.left+128,insets.top+184,72,24);}
        c = parent.getComponent(3);
        if (c.isVisible()) {c.setBounds(insets.left+216,insets.top+184,72,24);}
    }
}
