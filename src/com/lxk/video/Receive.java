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
���ղ������ı���ͼƬ����
*/
public class Receive extends Thread{
	static BufferedImage img;
	public void run() {
		DatagramSocket recvSocket = null;
		try {
			recvSocket = new DatagramSocket(13000);
			while (true) {
				// 2.ָ�����ջ�������С:ÿ����40000�ֽ�
				byte[] buffer = new byte[30000]; // 3.ָ�����ջ�������С:ÿ����20�ֽ�
				// 3.�����������ݰ�����
				DatagramPacket packet = new DatagramPacket(buffer,
						buffer.length);
				// 4.�����ȴ����ݵ���,����յ�����,����packet�еĻ�������
				//System.out.println("UDP�������ȴ���������:");
				recvSocket.receive(packet);
				// 5��������յ�������
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
			int len = getValidLength(buffer); //��ȡ��Ч����
			byte[] data = new byte[len-1];
			System.arraycopy(buffer, 1, data, 0, data.length);
			String text = new String(data)+"\r\n";
//			System.out.println("�յ���Ϣ��"+text);
			SendMain.setText("�յ���Ϣ��"+text);
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
