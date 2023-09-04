package DN12;

/**
 * Program pretvori vsebino napisano v levem stolpcu v velike črke v desnem stolpcu
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DN12 {
    public static void main(String[] args) {
        JFrame okno = new JFrame();
        okno.setTitle("VELIKE ČRKE");
        okno.setSize(800,800);
        okno.setLocation(500,500);

        okno.setLayout(new GridBagLayout());

        final JTextArea crkeTA = new JTextArea();
        final JTextArea velikeCrkeTA = new JTextArea();
        JButton gumb = new JButton(" ---> PRETVORI");

        Insets ins = new Insets(10,10,10,10);

        GridBagConstraints abc = new GridBagConstraints();

        abc.gridx = 0;
        abc.gridy = 0;
        abc.fill = GridBagConstraints.BOTH;
        abc.weightx = 0.5;
        abc.weighty = 1;
        abc.insets = ins;

        okno.add(crkeTA, abc);

        abc = new GridBagConstraints();
        abc.gridx = 1;
        abc.gridy = 1;
        abc.insets = ins;
        okno.add(gumb, abc);

        abc = new GridBagConstraints();
        abc.gridx = 2;
        abc.gridy = 0;
        abc.fill = GridBagConstraints.BOTH;
        abc.weightx = 0.5;
        abc.weighty = 1;
        abc.insets = ins;

        okno.add(velikeCrkeTA, abc);

        okno.setVisible(true);

        /**
         * Anonimni notranji razred
         */
        gumb.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                velikeCrkeTA.setText(crkeTA.getText().toUpperCase());
            }
        });

    }
}
