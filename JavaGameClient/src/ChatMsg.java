
// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;
import java.awt.*;
class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500: Mouse Event
	public String UserName;
	public String data;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size; // pen size
	
	public Color color;//color
	
	public String shape;//����
	public int x1, y1, x2, y2;// ��ǥ
	
	public String answer;// ����
	public int order;//��������
	public int score;//����
	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
}