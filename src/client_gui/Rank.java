package client_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import client.ClientSet;

public class Rank extends JPanel implements ActionListener {
	private ClientSet clientSet = null;
	private JTextArea ranking = null;
	private JButton goBack = null;
	private GUIMain gm = null;

	/**
	 * Create the panel.
	 * 
	 * @param gm
	 * @param m
	 */
	public Rank(GUIMain gm) {
		setBackground(new Color(224, 255, 255));
		this.gm = gm;
		this.clientSet = ClientSet.getInstance();
		setLayout(null);

		ranking = new JTextArea();
		ranking.setFont(new Font("»õ±¼¸²", Font.BOLD, 20));
		ranking.setBounds(168, 104, 463, 453);
		add(ranking);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(Rank.class.getResource("/img/rankTitle.png")));
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 30));
		lblNewLabel.setBounds(355, 45, 90, 52);
		add(lblNewLabel);

		goBack = new JButton("");
		goBack.setBackground(new Color(224, 255, 255));
		goBack.setBorderPainted(false);
		goBack.setIcon(new ImageIcon(Rank.class.getResource("/img/backBtn.png")));
		goBack.setBounds(12, 10, 110, 35);
		add(goBack);
		addLis();
		String rank = "rank/";
		clientSet.send(rank);
	}

	public void viewRank(String rank) {
		ranking.setText(rank);
	}

	private void addLis() {
		goBack.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(goBack)) {
			gm.gameHome();
		}
	}
}
