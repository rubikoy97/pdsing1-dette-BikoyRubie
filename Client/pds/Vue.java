package pds;

import javax.swing.*;
import java.awt.*;

public class Vue extends GUI {

    private static final long serialVersionUID = -4897594027287541463L;

    private JButton btnShowTraffic = new JButton("Show traffic");

    private JTextPane showTrafficPane;

    private JComboBox<String> selectMonth;

    private JTextField txSelectId;

    Vue() {
        super();

        JPanel showTrafficPanel = new JPanel();
        showTrafficPane = new JTextPane();
        JScrollPane showTrafficScrollPane = new JScrollPane(showTrafficPane);
        showTrafficScrollPane.setPreferredSize(new Dimension(100,100));
        showTrafficPanel.setPreferredSize(new Dimension(600, 550));

        showTrafficPanel.setLayout(new BoxLayout(showTrafficPanel, BoxLayout.X_AXIS));
        showTrafficPanel.add(showTrafficScrollPane);
        showTrafficPanel.add(btnShowTraffic);


        //
        JPanel centre = new JPanel();
        centre.add(showTrafficPanel);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(centre);
        this.add(main, BorderLayout.CENTER);

        JLabel lbSelectedId = new JLabel("Enter shop id : ");
        txSelectId = new JTextField();
        txSelectId.setPreferredSize(new Dimension(100,20));

        String[] months = { "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre" };

        selectMonth = new JComboBox<>(months);
        selectMonth.setSelectedIndex(0);

        JPanel header = new JPanel();

        header.add(lbSelectedId);
        header.add(txSelectId);
        header.add(selectMonth);

        this.add(header, BorderLayout.NORTH);

    }

    public void setText(String text) {
        this.showTrafficPane.setText(text);
    }

    public int getSelectedMonth() {
        return this.selectMonth.getSelectedIndex() + 1;
    }

    public String getEnteredId() {
        return this.txSelectId.getText();
    }

    public JButton getBtnShowTraffic() {
        return btnShowTraffic;
    }

    public void clearText() {
        this.showTrafficPane.setText("");
    }

}
