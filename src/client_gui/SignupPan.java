package client_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientSet;

public class SignupPan extends JPanel implements ActionListener {
	private ClientSet clientSet = null;
	private JTextField pw;
	private JTextField id;
	private JTextField pwchk;
	private JButton signup;
	private JButton goBack;
	private JLabel idReg = null;
	private JLabel pwReg = null;
	private JLabel pwchkReg = null;
	private GUIMain gm = null;
	private String idok = "사용 가능한 ID";
	private String pwok = "사용 가능한 비밀번호";
	private String pwchkok = "비밀번호가 일치합니다";

	/**
	 * Create the panel.
	 */
	public SignupPan(GUIMain gm) {
		setBackground(new Color(255, 240, 245));
		this.gm = gm;
		this.clientSet = ClientSet.getInstance();
		setLayout(null);

		JLabel title = new JLabel("");
		title.setIcon(new ImageIcon(SignupPan.class.getResource("/img/signupTitle.png")));
		title.setFont(new Font("굴림", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		title.setBounds(338, 206, 124, 52);
		add(title);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(SignupPan.class.getResource("/img/logo2.png")));
		lblNewLabel.setBounds(201, 10, 397, 263);
		add(lblNewLabel);

		pw = new JTextField();
		pw.setBounds(280, 367, 209, 26);
		add(pw);
		pw.setColumns(10);

		id = new JTextField();
		id.setColumns(10);
		id.setBounds(280, 327, 209, 26);
		add(id);

		pwchk = new JTextField();
		pwchk.setColumns(10);
		pwchk.setBounds(280, 403, 209, 26);
		add(pwchk);

		JLabel ID = new JLabel("ID");
		ID.setFont(new Font("굴림", Font.BOLD, 20));
		ID.setBounds(141, 332, 138, 21);
		add(ID);

		JLabel PW = new JLabel("\uBE44\uBC00\uBC88\uD638");
		PW.setFont(new Font("굴림", Font.BOLD, 20));
		PW.setBounds(141, 372, 138, 21);
		add(PW);

		JLabel PWchk = new JLabel("\uBE44\uBC00\uBC88\uD638 \uD655\uC778");
		PWchk.setFont(new Font("굴림", Font.BOLD, 20));
		PWchk.setBounds(141, 408, 138, 21);
		add(PWchk);

		signup = new JButton("");
		signup.setBorderPainted(false);
		signup.setForeground(new Color(255, 240, 245));
		signup.setBackground(new Color(255, 240, 245));
		signup.setIcon(new ImageIcon(SignupPan.class.getResource("/img/signupBtn2.png")));
		signup.setBounds(325, 474, 143, 63);
		add(signup);

		idReg = new JLabel("5~10\uC790 \uC774\uB0B4\uB85C \uC785\uB825");
		idReg.setBounds(500, 332, 271, 15);
		add(idReg);

		pwReg = new JLabel("\uD2B9\uC218\uBB38\uC790 \uD3EC\uD568 6~15\uC790 \uC774\uD558\uB85C \uC785\uB825");
		pwReg.setBounds(500, 372, 271, 15);
		add(pwReg);

		pwchkReg = new JLabel("\uBE44\uBC00\uBC88\uD638 \uB2E4\uC2DC \uC785\uB825");
		pwchkReg.setBounds(500, 408, 271, 15);
		add(pwchkReg);

		goBack = new JButton("");
		goBack.setBorderPainted(false);
		goBack.setForeground(new Color(255, 240, 245));
		goBack.setBackground(new Color(255, 240, 245));
		goBack.setIcon(new ImageIcon(SignupPan.class.getResource("/img/backBtn.png")));
		goBack.setBounds(24, 31, 111, 31);
		add(goBack);
		addLis();
	}

	// ID
	public String idchk(String id) {
		String txt = "";
		if (!idLeng(id)) {
			txt = "ID는 5~10자 이내로만 입력 가능합니다!";
		}
		return txt;
	}

	// id 길이 체크
	private boolean idLeng(String id) {
		if (id.length() < 5 || id.length() > 10) {
			return false;
		}
		return true;
	}

	// 비밀번호 특수문자 체크
	private boolean pwsign(String pw) {
		String sign = "~!@#$%^&*";
		for (int i = 0; i < sign.length(); i++) {
			if (pw.contains("" + sign.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	// 비밀번호 길이
	private boolean pwLeng(String pw) {
		if (pw.length() < 6 || pw.length() > 15) {
			return false;
		}
		return true;
	}

	// PW
	public String pwchk(String pw) {
		String txt = "";
		if (!pwsign(pw)) {
			txt = "비밀번호는 반드시 특수문자를 포함해야 합니다!";
		} else if (!pwLeng(pw)) {
			txt = "비밀번호는 6~15자 이내로만 입력 가능합니다!";
		}
		return txt;
	}

	public void addLis() {
		signup.addActionListener(this);
		goBack.addActionListener(this);
		id.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				String txt = idchk(id.getText());
				if (txt.equals("")) {
					txt = idok;
					idReg.setForeground(Color.blue);
				} else {
					idReg.setForeground(Color.red);
				}
				idReg.setText(txt);
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		pw.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				String txt = pwchk(pw.getText());
				if (txt.equals("")) {
					txt = pwok;
					pwReg.setForeground(Color.blue);
				} else {
					pwReg.setForeground(Color.red);
				}
				pwReg.setText(txt);
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
		pwchk.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (pwchk.getText().equals(pw.getText())) {
					pwchkReg.setForeground(Color.blue);
					pwchkReg.setText(pwchkok);
				} else {
					pwchkReg.setForeground(Color.red);
					pwchkReg.setText("비밀번호가 일치하지 않습니다!");
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
		gm.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(signup)) {
			if (idReg.getText().equals(idok) && pwReg.getText().equals(pwok) && pwchkReg.getText().equals(pwchkok)) {
				String attempt = "signup/" + id.getText() + "/" + pw.getText();
				String sign = clientSet.memberchk(attempt);
				if (sign.equals("1")) {
					idReg.setForeground(Color.red);
					idReg.setText("이미 사용중인 ID 입니다!");
				} else if (sign.equals("2")) {
					signupWell();
				}
			}
		} else if (e.getSource().equals(goBack)) {
			gm.home();
		}
	}

	private void signupWell() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JInternalFrame itf = new JInternalFrame("회원가입 성공", false, false, false, false);
				itf.setBounds(200, 200, 350, 200);
				itf.setLayout(null);

				JLabel great = new JLabel("회원가입 성공!");
				great.setFont(new Font("굴림", Font.BOLD, 25));
				great.setBounds(80, 50, 200, 30);
				itf.add(great);

				JLabel oneMore = new JLabel("로그인 창으로 돌아갑니다.");
				oneMore.setFont(new Font("굴림", Font.BOLD, 15));
				oneMore.setBounds(70, 85, 200, 20);
				itf.add(oneMore);

				itf.setVisible(true);
				add(itf);
				gm.setVisible(true);

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				gm.home();
			}
		}).start();
	}
}
