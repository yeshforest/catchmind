
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;



import java.util.*;
import java.awt.*;

public class JavaGameClientView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel u1_lblUserName,u2_lblUserName,u3_lblUserName,u4_lblUserName,lblAnswer,my_lblUserName;
	private JLabel u1_score,u2_score,u3_score,u4_score,lbltext;
	private JTextPane u1_textArea,u2_textArea,u3_textArea,u4_textArea;
	private int User_Count=0;
	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn,fline,frec,rec,fcir,cir,line;
	Color b_color=Color.WHITE ;//버튼색
	Color b_bg_color=Color.DARK_GRAY;//버튼 바탕색 저장

	JPanel panel;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null; 
	private Graphics gc2 = null;
	
	int x, y, ox, oy;         // 움직인 후의 좌표(x, y)와 움직이기 전의 좌표(ox, oy)
	Color c ;//색 저장용
	/*도형그리기 변수들*/
	int sx,sy,ex,ey;//사각형 그리기 startx,y, endx,y  
	 int xdif,ydif,xlen,ylen;
	private String shape="fd";//도형보내기용
	
	private Image tmpImage = null; 
	private Graphics gc3 = null;//도형 중간과정
	
	String[] array= {"null","null","null","null"};//username 저장용
	int[] score= {0,0,0,0};//username 점수 저장용
	private String answer;//정답
	private int k=0;//순서 넘기는 변수
	
	 
	//private JPanel contentPane1;


	
	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public JavaGameClientView(String username, String ip_addr, String port_no)  {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 653);//Jframe크기
		//그위에 JPanel로 contentPane만든다
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 102, 153));
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		

		
		JPanel u1_contentPane = new JPanel();
		u1_contentPane.setBackground(new Color(0, 0, 102));
		u1_contentPane.setBounds(6, 20, 169, 61);
		contentPane.add(u1_contentPane);
		
				u1_lblUserName = new JLabel("Name");
				u1_lblUserName.setVerticalAlignment(SwingConstants.TOP);
				u1_lblUserName.setForeground(Color.WHITE);
				u1_contentPane.add(u1_lblUserName);
				u1_lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
				u1_lblUserName.setBackground(Color.WHITE);
				u1_lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
				u1_lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
				u1_lblUserName.setText("waiting..");
				
				u1_score = new JLabel("score");
				u1_score.setVerticalAlignment(SwingConstants.TOP);
				u1_score.setHorizontalAlignment(SwingConstants.CENTER);
				u1_score.setForeground(Color.WHITE);
				u1_score.setFont(new Font("굴림", Font.BOLD, 14));
				u1_score.setBorder(new LineBorder(new Color(0, 0, 0)));
				u1_score.setBackground(Color.WHITE);
				u1_contentPane.add(u1_score);


		JScrollPane u1_scrollPane = new JScrollPane();
		u1_scrollPane.setBounds(6, 310, 178, 174);
		contentPane.add(u1_scrollPane);
		
				u1_textArea = new JTextPane();
				u1_textArea.setBackground(new Color(255, 255, 204));
				u1_scrollPane.setViewportView(u1_textArea);
				u1_textArea.setEditable(true);
				u1_textArea.setFont(new Font("굴림체", Font.PLAIN, 14));

		txtInput = new JTextField();
		txtInput.setBounds(331, 505, 301, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
		btnSend.setBounds(644, 504, 69, 40);
		contentPane.add(btnSend);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		

		imgBtn = new JButton("\uBC11\uADF8\uB9BC");
		imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
		imgBtn.setBounds(559, 554, 151, 40);
		contentPane.add(imgBtn);

		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon("images/exit.png"));
		btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(663, 10, 50, 40);
		contentPane.add(btnNewButton);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(187, 54, 526, 430);
		contentPane.add(panel);
		gc = panel.getGraphics();
		
		// Image 영역 보관용. paint() 에서 이용한다.
		createPaint();
		
		JPanel u2_contentPane = new JPanel();
		u2_contentPane.setBackground(new Color(0, 0, 102));
		u2_contentPane.setBounds(6, 91, 169, 63);
		contentPane.add(u2_contentPane);
		
		u2_lblUserName = new JLabel("waiting..");
		u2_lblUserName.setForeground(Color.WHITE);
		u2_lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		u2_lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		u2_lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		u2_lblUserName.setBackground(Color.WHITE);
		u2_contentPane.add(u2_lblUserName);
		
		u2_score = new JLabel("score");
		u2_score.setVerticalAlignment(SwingConstants.TOP);
		u2_score.setHorizontalAlignment(SwingConstants.CENTER);
		u2_score.setForeground(Color.WHITE);
		u2_score.setFont(new Font("굴림", Font.BOLD, 14));
		u2_score.setBorder(new LineBorder(new Color(0, 0, 0)));
		u2_score.setBackground(Color.WHITE);
		u2_contentPane.add(u2_score);
		
		JPanel u3_contentPane = new JPanel();
		u3_contentPane.setBackground(new Color(0, 0, 102));
		u3_contentPane.setBounds(6, 164, 169, 63);
		contentPane.add(u3_contentPane);
		
		u3_lblUserName = new JLabel("waiting..");
		u3_lblUserName.setForeground(Color.WHITE);
		u3_lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		u3_lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		u3_lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		u3_lblUserName.setBackground(Color.WHITE);
		u3_contentPane.add(u3_lblUserName);
		
		u3_score = new JLabel("score");
		u3_score.setVerticalAlignment(SwingConstants.TOP);
		u3_score.setHorizontalAlignment(SwingConstants.CENTER);
		u3_score.setForeground(Color.WHITE);
		u3_score.setFont(new Font("굴림", Font.BOLD, 14));
		u3_score.setBorder(new LineBorder(new Color(0, 0, 0)));
		u3_score.setBackground(Color.WHITE);
		u3_contentPane.add(u3_score);
		
		JButton rmv_btn = new JButton("");
		rmv_btn.setIcon(new ImageIcon("images/eraser.png"));
		rmv_btn.setFont(new Font("굴림", Font.PLAIN, 16));
		rmv_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			c = new Color(255,255,255);//밑그림 기능 구현하려면 여기 고쳐야한다.
			}
		});
		rmv_btn.setBounds(270, 505, 50, 42);
		contentPane.add(rmv_btn);
		
		JButton g_btn = new JButton("");
		g_btn.setIcon(new ImageIcon("images/green.PNG"));
		g_btn.setFont(new Font("굴림", Font.PLAIN, 16));
		g_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			c = new Color(0,255,0);//
			}
		});
		g_btn.setBounds(159, 500, 50, 50);
		contentPane.add(g_btn);
		
		JButton y_btn = new JButton("");
		y_btn.setIcon(new ImageIcon("images/yellow.PNG"));
		y_btn.setFont(new Font("굴림", Font.PLAIN, 16));
		y_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			c = new Color(255,255,0);
			}
		});
		y_btn.setBounds(210, 500, 50, 50);
		contentPane.add(y_btn);
		
		JButton blue_btn = new JButton("");
		blue_btn.setIcon(new ImageIcon("images/blue.PNG"));
		blue_btn.setFont(new Font("굴림", Font.PLAIN, 16));
		blue_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			c = new Color(0,0,255);//빨간색 setting
			}
		});
		blue_btn.setBounds(108, 500, 50, 50);
		contentPane.add(blue_btn);
		
		JButton r_btn = new JButton("");
		r_btn.setIcon(new ImageIcon("images/red.PNG"));
		r_btn.setFont(new Font("굴림", Font.PLAIN, 16));
		r_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			c = new Color(255,0,0);//빨간색 setting
			}
		});
		r_btn.setBounds(57, 500, 50, 50);
		contentPane.add(r_btn);
		
		JButton b_btn = new JButton("");
		b_btn.setIcon(new ImageIcon("images/black.PNG"));
		b_btn.setFont(new Font("굴림", Font.PLAIN, 16));
		b_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			c = new Color(0,0,0);
			}
		});
		b_btn.setBounds(6, 500, 50, 50);
		contentPane.add(b_btn);
		
		JButton rmv_btn_1 = new JButton("전체지우기");
		rmv_btn_1.setFont(new Font("굴림", Font.PLAIN, 16));
		rmv_btn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeAll();
				ChatMsg obcm = new ChatMsg(UserName, "700", "eraseAll");
				SendObject(obcm);
			}
		});
		rmv_btn_1.setBounds(417, 554, 130, 40);
		contentPane.add(rmv_btn_1);
		
		fline = new JButton("");
		fline.setBackground(b_bg_color);
		fline.setIcon(new ImageIcon("images/free.PNG"));
		fline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shape="fd";
				fline.setBackground(b_bg_color);
				frec.setBackground(b_color);
				rec.setBackground(b_color);
				fcir.setBackground(b_color);
				cir.setBackground(b_color);
				line.setBackground(b_color);
			}
		});
		fline.setFont(new Font("굴림", Font.PLAIN, 16));
		fline.setBounds(6, 551, 50, 50);
		contentPane.add(fline);
		
		frec = new JButton("");
		frec.setIcon(new ImageIcon("images/rect.PNG"));
		frec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			shape="fillrect";
			frec.setBackground(b_bg_color);
			fline.setBackground(b_color);
			rec.setBackground(b_color);
			fcir.setBackground(b_color);
			cir.setBackground(b_color);
			line.setBackground(b_color);
			}
		});
		frec.setFont(new Font("굴림", Font.PLAIN, 16));
		frec.setBounds(57, 551, 50, 50);
		contentPane.add(frec);
		
		rec = new JButton("");
		rec.setIcon(new ImageIcon("images/wrect.PNG"));
		rec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			shape="drawrect";
			fline.setBackground(b_color);
			frec.setBackground(b_color);
			rec.setBackground(b_bg_color);
			fcir.setBackground(b_color);
			cir.setBackground(b_color);
			line.setBackground(b_color);
			}
		});
		rec.setFont(new Font("굴림", Font.PLAIN, 16));
		rec.setBounds(108, 551, 50, 50);
		contentPane.add(rec);
		
		fcir = new JButton("");
		fcir.setIcon(new ImageIcon("images/circle.PNG"));
		fcir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			shape="filloval";
			fline.setBackground(b_color);
			frec.setBackground(b_color);
			rec.setBackground(b_color);
			fcir.setBackground(b_bg_color);
			cir.setBackground(b_color);
			line.setBackground(b_color);
			}
		});
		fcir.setFont(new Font("굴림", Font.PLAIN, 16));
		fcir.setBounds(159, 551, 50, 50);
		contentPane.add(fcir);
		
		cir = new JButton("");
		cir.setIcon(new ImageIcon("images/wcircle.PNG"));
		cir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			shape="drawoval";
			fline.setBackground(b_color);
			frec.setBackground(b_color);
			rec.setBackground(b_color);
			fcir.setBackground(b_color);
			cir.setBackground(b_bg_color);
			line.setBackground(b_color);
			}
		});
		cir.setFont(new Font("굴림", Font.PLAIN, 16));
		cir.setBounds(210, 551, 50, 50);
		contentPane.add(cir);
		
		line = new JButton("");
		line.setIcon(new ImageIcon("images/line.PNG"));
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			shape="drawline";
			fline.setBackground(b_color);
			frec.setBackground(b_color);
			rec.setBackground(b_color);
			fcir.setBackground(b_color);
			cir.setBackground(b_color);
			line.setBackground(b_bg_color);
			}
		});
		line.setFont(new Font("굴림", Font.PLAIN, 16));
		line.setBounds(257, 551, 50, 50);
		contentPane.add(line);
		
		JPanel u4_contentPane = new JPanel();
		u4_contentPane.setBackground(new Color(0, 0, 102));
		u4_contentPane.setBounds(6, 237, 169, 63);
		contentPane.add(u4_contentPane);
		
		u4_lblUserName = new JLabel("waiting..");
		u4_lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		u4_lblUserName.setForeground(Color.WHITE);
		u4_lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		u4_lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		u4_lblUserName.setBackground(Color.WHITE);
		u4_contentPane.add(u4_lblUserName);
		
		u4_score = new JLabel("score");
		u4_score.setVerticalAlignment(SwingConstants.TOP);
		u4_score.setHorizontalAlignment(SwingConstants.CENTER);
		u4_score.setForeground(Color.WHITE);
		u4_score.setFont(new Font("굴림", Font.BOLD, 14));
		u4_score.setBorder(new LineBorder(new Color(0, 0, 0)));
		u4_score.setBackground(Color.WHITE);
		u4_contentPane.add(u4_score);
		
		lblAnswer = new JLabel("입장중....");
		lblAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnswer.setForeground(Color.WHITE);
		lblAnswer.setFont(new Font("굴림", Font.BOLD, 20));
		lblAnswer.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblAnswer.setBackground(Color.WHITE);
		lblAnswer.setBounds(187, 10, 313, 40);
		contentPane.add(lblAnswer);
		
		my_lblUserName = new JLabel("name");
		my_lblUserName.setVerticalAlignment(SwingConstants.TOP);
		my_lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		my_lblUserName.setForeground(Color.WHITE);
		my_lblUserName.setFont(new Font("굴림", Font.BOLD, 19));
		my_lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		my_lblUserName.setBackground(Color.WHITE);
		my_lblUserName.setBounds(319, 555, 85, 49);
		contentPane.add(my_lblUserName);
		my_lblUserName.setText(username);
		
		lbltext = new JLabel("");
		lbltext.setHorizontalAlignment(SwingConstants.CENTER);
		lbltext.setForeground(Color.WHITE);
		lbltext.setFont(new Font("굴림", Font.BOLD, 20));
		lbltext.setBorder(new LineBorder(new Color(0, 0, 0)));
		lbltext.setBackground(Color.WHITE);
		lbltext.setBounds(512, 10, 140, 36);
		contentPane.add(lbltext);
	
		
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + UserName);
			//누가 들어오면 server로 이걸 보낸다.
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");			
			SendObject(obcm);
	
			
			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			imgBtn.addActionListener(action2);
			MyMouseEvent mouse = new MyMouseEvent();
			panel.addMouseMotionListener(mouse);
			panel.addMouseListener(mouse);
			MyMouseWheelEvent wheel = new MyMouseWheelEvent();
			panel.addMouseWheelListener(wheel);


		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	public void paint(Graphics g) {
		super.paint(g);
		// Image 영역이 가려졌다 다시 나타날 때 그려준다.
		//게임시작
		ChatMsg obcm = new ChatMsg(UserName, "150", "START");
		if(User_Count==4) {
			//서버에 게임시작을 알림
		
			SendObject(obcm);
			
		}
		gc.drawImage(panelImage, 0, 0, this);	
	}
	
	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
					} else
						continue;
					switch (cm.code) {
					case "100":
						SetPlayer(cm);
						break;
					case "150":
						StartGame(cm);
						break;
					case "200": // chat message
						if (cm.UserName.equals(UserName)) {
							AppendTextR(msg); // 내 메세지는 우측에
							ans_check(cm);//답인지 체크
						}
						else
							AppendText(msg);
						break;
					case "300": // Image 첨부
						if (cm.UserName.equals(UserName))							
						AppendImage(cm.img);
						break;
					case "500": // Mouse Event 수신
						DoMouseEvent(cm);
						break;
					case "600"://제시어 표시
						SetWord(cm);
						break;
					case "650"://점수표시
						setScore(cm);
					break;
					
					case "700": // erase all
						//화면 삭제하는 코드
						removeAll();
						break;
						
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}

	// Mouse Event 수신 처리
	public void DoMouseEvent(ChatMsg cm) {
		
		    
		if (cm.UserName.matches(UserName)) // 본인 것은 이미 Local 로 그렸다.
			return;
		
		//색 지정
		gc2.setColor(cm.color);
		//pensize 지정
		 Graphics2D g2d = (Graphics2D)gc2;
        g2d.setStroke(new BasicStroke(cm.pen_size,BasicStroke.CAP_ROUND,0)); 
		
        
		if (cm.mouse_e.getID() ==  MouseEvent.MOUSE_PRESSED) {
			
		     ox=cm.mouse_e.getX(); oy=cm.mouse_e.getY();//직선 시작점 기억
		     sx=cm.x1;sy=cm.y1;//사각형 시작점 기억
		}
		
		else if(cm.mouse_e.getID() ==  MouseEvent.MOUSE_DRAGGED) {
			// 여기서 그리기
			if(cm.shape.equals("fd")) {//free drawing인경우
			 x=cm.mouse_e.getX(); y=cm.mouse_e.getY(); //다음좌표 기억
			 gc2.drawLine(ox, oy, x, y);//그리기
			 ox=x; oy=y;
			 
			}
			 
			}
		else if(cm.mouse_e.getID() ==  MouseEvent.MOUSE_RELEASED) {
			
			ex=cm.x2;ey=cm.y2;//released인경우 끝 점 기억
			
			 xdif=ex-sx;
			 ydif=ey-sy;
			 xlen=Math.abs(ex-sx);
			 ylen=Math.abs(ey-sy);
			    
		if(cm.shape.equals("fillrect")) {
				
				  if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
						 gc2.fillRect(ex,sy,xlen,ylen);
					   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
						   gc2.fillRect(sx,sy,xlen,ylen);
					   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
						   gc2.fillRect(ex,ey,xlen,ylen);
					   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
						   gc2.fillRect(sx,ey,xlen,ylen);
				 
				}
		else if(cm.shape.equals("drawrect")) {
			  if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
					 gc2.drawRect(ex,sy,xlen,ylen);
				   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
					   gc2.drawRect(sx,sy,xlen,ylen);
				   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
					   gc2.drawRect(ex,ey,xlen,ylen);
				   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
					   gc2.drawRect(sx,ey,xlen,ylen);
			 
			}
		else if(cm.shape.equals("filloval")) {
			 if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
				 gc2.fillOval(ex,sy,xlen,ylen);
			   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
				   gc2.fillOval(sx,sy,xlen,ylen);
			   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
				   gc2.fillOval(ex,ey,xlen,ylen);
			   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
				   gc2.fillOval(sx,ey,xlen,ylen);
			 
			}
		else if(cm.shape.equals("drawoval")) {
			 if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
				 gc2.drawOval(ex,sy,xlen,ylen);
			   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
				   gc2.drawOval(sx,sy,xlen,ylen);
			   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
				   gc2.drawOval(ex,ey,xlen,ylen);
			   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
				   gc2.drawOval(sx,ey,xlen,ylen);
			 
			}
		else if(cm.shape.equals("drawline")) {
			 gc2.drawLine(sx, sy, ex, ey);//직선
			 
			}
		}
		gc.drawImage(panelImage, 0, 0, panel);
	}

	public void SendMouseEvent(MouseEvent e) {
		ChatMsg cm = new ChatMsg(UserName, "500", "MOUSE");
		cm.mouse_e = e;
		cm.pen_size = pen_size;
		cm.color=c;
		cm.shape=shape;
		cm.x1=sx;cm.y1=sy;cm.x2=ex;cm.y2=ey;
		SendObject(cm);
	}

	class MyMouseWheelEvent implements MouseWheelListener {
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			if (e.getWheelRotation() < 0) { // 위로 올리는 경우 pen_size 증가
				if (pen_size < 20)
					pen_size++;
			} else {
				if (pen_size > 2)
					pen_size--;
			}
			//lblMouseEvent.setText("mouseWheelMoved Rotation=" + e.getWheelRotation() 
				//+ " pen_size = " + pen_size + " " + e.getX() + "," + e.getY());

		}
		
	}
	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {
		
		//마우스 dragged하면
		
		 @Override
		public void mouseDragged(MouseEvent e) {
			//몇번째 버튼이 눌러졌는지 알기
			//lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," + e.getY());// 좌표출력가능
		
			gc2.setColor(c);
			Graphics2D g2d = (Graphics2D)gc2;
			 g2d.setStroke(new BasicStroke(pen_size,BasicStroke.CAP_ROUND,0)); 
			 
			    x=e.getX(); y=e.getY();              // 마우스의 현재 위치를 알아온다.
			    if(shape.equals("fd")) {gc2.drawLine(ox, oy, x, y);}
			    ox=x; oy=y;
			    ex = e.getX();
			    ey = e.getY();
			    
			    //freedrawing				   
			    
			    xdif=ex-sx; ydif=ey-sy;
			    xlen=Math.abs(ex-sx); ylen=Math.abs(ey-sy);
			    
			  //  System.out.println(sx+" "+sy+" "+ex+" "+ey);
			    if(shape.equals("fillrect")) {
			    	gc.drawImage(tmpImage,0,0,panel);
					   gc2.drawImage(tmpImage,0,0,panel);
					   if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
						 gc2.fillRect(ex,sy,xlen,ylen);
					   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
						   gc2.fillRect(sx,sy,xlen,ylen);
					   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
						   gc2.fillRect(ex,ey,xlen,ylen);
					   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
						   gc2.fillRect(sx,ey,xlen,ylen);
					 }
			    else if(shape.equals("drawrect")) {
					 gc.drawImage(tmpImage,0,0,panel);
					 gc2.drawImage(tmpImage,0,0,panel);
					  if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
							 gc2.drawRect(ex,sy,xlen,ylen);
						   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
							   gc2.drawRect(sx,sy,xlen,ylen);
						   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
							   gc2.drawRect(ex,ey,xlen,ylen);
						   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
							   gc2.drawRect(sx,ey,xlen,ylen);
					
					 }
			    else if(shape.equals("filloval")) {
					gc.drawImage(tmpImage,0,0,panel);
					 gc2.drawImage(tmpImage,0,0,panel);
					  if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
							 gc2.fillOval(ex,sy,xlen,ylen);
						   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
							   gc2.fillOval(sx,sy,xlen,ylen);
						   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
							   gc2.fillOval(ex,ey,xlen,ylen);
						   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
							   gc2.fillOval(sx,ey,xlen,ylen);
				    
					 }
			    else if(shape.equals("drawoval")) {
					 gc.drawImage(tmpImage,0,0,panel);
					 gc2.drawImage(tmpImage,0,0,panel);
					  if(ydif>0&&xdif<0)//오른쪽 위에서 왼쪽 아래로
							 gc2.drawOval(ex,sy,xlen,ylen);
						   else if(xdif>0&&ydif>0)//왼쪽 위에서 오른쪽 아래로
							   gc2.drawOval(sx,sy,xlen,ylen);
						   else if(xdif<0&&ydif<0)//오른쪽 아래에서 왼쪽 위로
							   gc2.drawOval(ex,ey,xlen,ylen);
						   else if(xdif>0&&ydif<0)//왼쪽 아래에서 오른쪽 위로
							   gc2.drawOval(sx,ey,xlen,ylen);
			
					 }
			    else if(shape.equals("drawline")) {
					 gc.drawImage(tmpImage,0,0,panel);
					 gc2.drawImage(tmpImage,0,0,panel);
					 gc2.drawLine(sx,sy,ex,ey);
					 }
			// panelImnage는 paint()에서 이용한다.
			gc.drawImage(panelImage, 0, 0, panel);
			//다른사람들한테 내위치 알려줌
			SendMouseEvent(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseMoved " + e.getX() + "," + e.getY());
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseClicked " + e.getX() + "," + e.getY());

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseEntered " + e.getX() + "," + e.getY());

		}

		@Override
		public void mouseExited(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseExited " + e.getX() + "," + e.getY());
			// panel.setBackground(Color.CYAN);

		}

		@Override
		public void mousePressed(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mousePressed " + e.getX() + "," + e.getY());
			gc3.drawImage(panelImage,0,0,panel);
			  ox=e.getX(); oy=e.getY();      
			  sx=e.getX(); sy=e.getY();//사각형 시작좌표, 원 시작좌표
			  
			  
			SendMouseEvent(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseReleased " + e.getX() + "," + e.getY());
			 ex=e.getX(); ey=e.getY();//사각형 끝나는좌표
			 
			 if(shape.equals("fillrect")) {// 사각형
			 gc2.fillRect(sx,sy,ex-sx,ey-sy);
			 }
			 repaint();
	
			SendMouseEvent(e);

		}
	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == imgBtn) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				fd.setVisible(true);
				if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
					ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
					ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
					obcm.img = img;
					SendObject(obcm);
				}
			}
		}
	}
	public void removeAll() {
		tmpImage=null;
		panelImage = null;
		panel.repaint();
		createPaint();
		
	}
	public void createPaint() {
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 = panelImage.getGraphics();
		gc2.setColor(panel.getBackground());
		gc2.fillRect(0,0, panel.getWidth(),  panel.getHeight());
		gc2.setColor(Color.BLACK);
		gc2.drawRect(0,0, panel.getWidth()-1,  panel.getHeight()-1);
		
		
		tmpImage = createImage(panel.getWidth(), panel.getHeight());
		gc3 = tmpImage.getGraphics();
		gc3.setColor(panel.getBackground());
		gc3.fillRect(0,0, panel.getWidth(),  panel.getHeight());
		gc3.setColor(Color.BLACK);
		gc3.drawRect(0,0, panel.getWidth()-1,  panel.getHeight()-1);
	}
	public void setScore(ChatMsg cm){//label에 score 쓰는 함수
		lbltext.setText(cm.UserName+"님 정답!");//맞은사람 이름표시
		
		if(cm.UserName.equals(array[0])) {
		u1_score.setText(String.valueOf(cm.score));
		}
		if(cm.UserName.equals(array[1])) {
			u2_score.setText(String.valueOf(cm.score));
			}
		if(cm.UserName.equals(array[2])) {
			u3_score.setText(String.valueOf(cm.score));
			}
		if(cm.UserName.equals(array[3])) {
			u4_score.setText(String.valueOf(cm.score));
			}
		
	}
	//ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	

	//String[] array= {"null","null","null","null"};
	public void SetPlayer(ChatMsg cm) {
		if (User_Count==0) {
			u1_lblUserName.setText(cm.UserName);
			array[0]=cm.UserName;
		}
		if (User_Count==1) {
			u2_lblUserName.setText(cm.UserName);
			array[1]=cm.UserName;
		}
		if (User_Count==2) {
			u3_lblUserName.setText(cm.UserName);
			array[2]=cm.UserName;
		}
		if (User_Count==3) {
			u4_lblUserName.setText(cm.UserName);
			array[3]=cm.UserName;
		}
		User_Count++;
		ChatMsg obcm = new ChatMsg(UserName, "150", "START");
		if(User_Count==4) {
			//서버에 게임시작을 알림
		
			SendObject(obcm);
			
		}
	}

	public void StartGame(ChatMsg cm) {
		//게임 시작되면 할 일
	}
	
	public void SetWord(ChatMsg cm) {
		answer=cm.answer;
		//array(k)한테만 보내기
		k=cm.order;
		k=k%4;
		System.out.println(cm.order +"in setword "+cm.UserName);
		
		if(cm.UserName.equals(array[k])){//username이 array[k]랑 같으면
			lblAnswer.setText(answer);
			
		}
		else {
			lblAnswer.setText("답을 맞춰보세요");	
		}
		
		}
	public void ans_check(ChatMsg cm) {
		if(answer.equals(cm.data)) {//답이 맞으면 
			
			//System.out.println("정답입니다.");
			System.out.println(k +"in ans check "+cm.UserName);
			
			
			ChatMsg obcm = new ChatMsg(UserName, "650", "correct");//맞았다고 서버에 알리기
			for(int i=0;i<array.length;i++) {
			if(array[i].matches(UserName)) {//맞춘사람 score를 100 올려주고 
				score[i]+=100;
				obcm.score=score[i];	//chatmsg에 저장하여 서버로 보낸다.
				System.out.println(array[i]+"score"+obcm.score);
			}
			}
			
			SendObject(obcm);
			removeAll();
			ChatMsg rmcm = new ChatMsg(UserName, "700", "eraseAll");//화면모두지우기
			SendObject(rmcm);
			
		}
		
		
	}
	
	public void AppendIcon(ImageIcon icon) {
		int len = u1_textArea.getDocument().getLength();
		// 끝으로 이동
		u1_textArea.setCaretPosition(len);
		u1_textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {
	
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.		
		StyledDocument doc = u1_textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg+"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = u1_textArea.getDocument().getLength();
		u1_textArea.setCaretPosition(len);
	}
	// 화면 우측에 출력
	public void AppendTextR(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.	
		StyledDocument doc = u1_textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);	
	    doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(),msg+"\n", right );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = u1_textArea.getDocument().getLength();
		u1_textArea.setCaretPosition(len);
		//textArea.replaceSelection("\n");

	}
	
	public void AppendImage(ImageIcon ori_icon) {
		
		Image ori_img = ori_icon.getImage();
		gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
		gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
	}


	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
}
