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
界面类：
1、初始化界面、打开摄像头抓取视频图像
2、聊天消息显示(包括发送的和接收的)
3、图片的绘制(包括摄像头或取的和接收到的)
 */
public class SendMain extends JFrame {
	
	static Graphics g = null;
	BufferedImage self_image;
	static JTextArea area = new JTextArea(20,25);
	//TODO 添加滚动条
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
		this.setTitle("视频发送端");
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
		JButton send_button = new JButton("发送");
		send_button.addActionListener(new ActionListener() { //接口的匿名实例对象
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = field.getText();//+"\r\n"
				field.setText("");
				setText("我："+text+"\r\n");
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
		//TODO 确定对应的jar包
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		try {
			grabber.start();// 开始获取摄像头数据
			Java2DFrameConverter converter = new Java2DFrameConverter();
			while (true) {
				// 取得一张照片
				self_image = converter.getBufferedImage(grabber.grabFrame());
				//尝试两个线程同时调用一个方法
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
