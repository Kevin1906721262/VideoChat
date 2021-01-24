package com.lxk.video;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

/*
接收并处理文本和图片数据
*/
public class Receive extends Thread{
	static BufferedImage img;
	public void run() {
		DatagramSocket recvSocket = null;
		try {
			recvSocket = new DatagramSocket(13000);
			while (true) {
				// 2.指定接收缓冲区大小:每个包40000字节
				byte[] buffer = new byte[30000]; // 3.指定接收缓冲区大小:每个包20字节
				// 3.创建接收数据包对象
				DatagramPacket packet = new DatagramPacket(buffer,
						buffer.length);
				// 4.阻塞等待数据到来,如果收到数据,存入packet中的缓冲区中
				//System.out.println("UDP服务器等待接收数据:");
				recvSocket.receive(packet);
				// 5、处理接收到的数据
				dataHanlder(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			recvSocket.close();
		}
	}

	private void dataHanlder(byte[] buffer) {
		if(buffer[0]==1) {
			int len = getValidLength(buffer); //获取有效长度
			byte[] data = new byte[len-1];
			System.arraycopy(buffer, 1, data, 0, data.length);
			String text = new String(data)+"\r\n";
//			System.out.println("收到消息："+text);
			SendMain.setText("收到消息："+text);
		}else if(buffer[0]==2) {
			byte[] data = new byte[buffer.length-1];
			System.arraycopy(buffer, 1, data, 0, data.length);
			ByteArrayInputStream bis = null;
			try {
				bis = new ByteArrayInputStream(data);
				img = ImageIO.read(bis);
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private int getValidLength(byte[] buffer) {
		int i=0;
		while(buffer[i]!='\0') i++;
		return i;
	}
	
}
