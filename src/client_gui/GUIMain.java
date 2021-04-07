package client_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.ClientSet;

public class GUIMain extends JFrame implements ActionListener {
	public ClientSet clientSet = null;
	public HomePan hp = null;
	public Rank rank = null;
	private JPanel contentPane;
	private JTextField inputID;
	private JPasswordField inputPW;
	private JLabel ID;
	private JLabel PW;
	private JButton login = null;
	private JButton signup = null;
	private String userId;
	private JLabel idReg;
	private JLabel pwReg;

	/**
	 * Create the frame.
	 * 
	 * @param clientSet
	 */
	public GUIMain(ClientSet clientset) {
		this.clientSet = clientset;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 810, 630);
		setFrame();
		this.setVisible(true);
	}

	private void addLis() {
		login.addActionListener(this);
		signup.addActionListener(this);
	}

	private void setFrame() {
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 250, 205));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		inputID = new JTextField();
		inputID.setBounds(309, 409, 166, 27);
		contentPane.add(inputID);
		inputID.setColumns(10);

		inputPW = new JPasswordField();
		inputPW.setBounds(309, 457, 166, 27);
		contentPane.add(inputPW);

		login = new JButton("");
		login.setBorderPainted(false);
		login.setBackground(new Color(250, 250, 210));
		login.setForeground(new Color(250, 250, 210));
		login.setIcon(new ImageIcon(GUIMain.class.getResource("/img/loginBtn.png")));
		login.setBounds(502, 406, 103, 89);
		contentPane.add(login);

		signup = new JButton("");
		signup.setBorderPainted(false);
		signup.setForeground(new Color(250, 250, 210));
		signup.setBackground(new Color(250, 250, 210));
		signup.setIcon(new ImageIcon(GUIMain.class.getResource("/img/signupBtn.png")));
		signup.setBounds(502, 528, 103, 23);
		contentPane.add(signup);

		ID = new JLabel("ID");
		ID.setFont(new Font("굴림", Font.BOLD, 20));
		ID.setBounds(205, 415, 92, 21);
		contentPane.add(ID);

		PW = new JLabel("Password");
		PW.setFont(new Font("굴림", Font.BOLD, 20));
		PW.setBounds(205, 463, 92, 21);
		contentPane.add(PW);

		JLabel txt = new JLabel("\uC544\uC9C1 \uD68C\uC6D0\uC774 \uC544\uB2C8\uB77C\uBA74?");
		txt.setBounds(343, 532, 132, 15);
		contentPane.add(txt);

		idReg = new JLabel("");
		idReg.setForeground(Color.RED);
		idReg.setBounds(309, 438, 166, 15);
		contentPane.add(idReg);

		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(GUIMain.class.getResource("/img/logo.png")));
		logo.setBounds(151, 20, 481, 427);
		contentPane.add(logo);

		pwReg = new JLabel("");
		pwReg.setForeground(Color.RED);
		pwReg.setBounds(309, 490, 166, 15);
		contentPane.add(pwReg);

		addLis();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(signup)) {
			this.remove(contentPane);
			contentPane = new SignupPan(this);
			this.setContentPane(contentPane);
			this.setVisible(true);
		} else if (e.getSource().equals(login)) {
			if (inputID.getText().equals("")) {
				idReg.setText("ID를 입력하세요!");
			} else if (inputPW.getText().equals("")) {
				pwReg.setText("비밀번호를 입력하세요!");
			} else {
				String attempt = "login/" + inputID.getText() + "/" + inputPW.getText();
				System.out.println(attempt + "로 로그인 시도!");
				String log = clientSet.memberchk(attempt);
				if (log.equals("1")) {
					idReg.setText("존재하지 않는 ID입니다!");
				} else if (log.equals("2")) {
					idReg.setText("");
					pwReg.setText("비밀번호가 틀립니다!");
				} else if (log.equals("3")) {
					userId = inputID.getText();
					gameHome();
				}
			}
		}
	}

	public void gameHome() {
		this.remove(contentPane);
		hp = new HomePan(this);
		contentPane = hp;
		this.setContentPane(contentPane);
		this.setVisible(true);
	}

	public void ranking() {
		this.remove(contentPane);
		rank = new Rank(this);
		contentPane = rank;
		this.setContentPane(contentPane);
		this.setVisible(true);
	}

	public void gameGo() {
		this.remove(contentPane);
		Run r = new Run();
		contentPane = r;
		r.updatePan(this);
		this.setContentPane(contentPane);
		this.setVisible(true);
	}

	public void home() {
		this.remove(contentPane);
		setFrame();
		this.setContentPane(contentPane);
		this.setVisible(true);
	}
}
