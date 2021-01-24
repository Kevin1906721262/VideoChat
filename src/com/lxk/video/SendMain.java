package com.lxk.video;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/*
�����ࣺ
1����ʼ�����桢������ͷץȡ��Ƶͼ��
2��������Ϣ��ʾ(�������͵ĺͽ��յ�)
3��ͼƬ�Ļ���(��������ͷ��ȡ�ĺͽ��յ���)
 */
public class SendMain extends JFrame {
	
	static Graphics g = null;
	BufferedImage self_image;
	static JTextArea area = new JTextArea(20,25);
	//TODO ��ӹ�����
	Sender sender = Sender.getInstance();
	public static void main(String[] args) {
		new Receive().start();
		new SendMain().init();
	}
	
	public void init() {
		initUI();
		initVideo();
	}
	
	private void initUI() {
		this.setTitle("��Ƶ���Ͷ�");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 500);
		this.setLocationRelativeTo(null);
		JPanel text_panel = new JPanel();
		JPanel video_panel = new JPanel();
		text_panel.setPreferredSize(new Dimension(300,500));
		video_panel.setPreferredSize(new Dimension(500,500));
		video_panel.setBackground(Color.cyan);
		this.setLayout(new BorderLayout());
		this.add(video_panel,BorderLayout.EAST);
		text_panel.add(area);
		final JTextField field = new JTextField(15);
		text_panel.add(field);
		JButton send_button = new JButton("����");
		send_button.addActionListener(new ActionListener() { //�ӿڵ�����ʵ������
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = field.getText();//+"\r\n"
				field.setText("");
				setText("�ң�"+text+"\r\n");
				sender.send(text);
			}
		});
		
		text_panel.setLayout(new FlowLayout());
		text_panel.add(send_button);
		this.add(text_panel);
		this.setVisible(true);
		g = video_panel.getGraphics();
	}
	
	public static void setText(String text) {
		area.setText(area.getText()+text);
	}
	
	private void initVideo() {
		//TODO ȷ����Ӧ��jar��
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		try {
			grabber.start();// ��ʼ��ȡ����ͷ����
			Java2DFrameConverter converter = new Java2DFrameConverter();
			while (true) {
				// ȡ��һ����Ƭ
				self_image = converter.getBufferedImage(grabber.grabFrame());
				//���������߳�ͬʱ����һ������
				if(Receive.img!=null) {
					g.drawImage(Receive.img, 0, 0, 500, 500, null);
					g.drawImage(self_image, 0, 0, 200, 200, null);
				}else {
					g.drawImage(self_image, 0, 0, 200, 200, null);
				}
				sender.send(self_image);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
