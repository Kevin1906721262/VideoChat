package com.lxk.video;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import javax.imageio.ImageIO;
/*
����DatagramSocket�����ı���ͼƬ����
*/
public class Sender {
	public DatagramSocket dSender;
	public SocketAddress localAddr;
	public SocketAddress destAdd;

	private Sender() {
		try {
			// 1������������ַ�˿ڶ���
			localAddr = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 12000);
			// 2.�������͵�Socket����
			dSender = new DatagramSocket(localAddr);
			// 3�������Է���ַ�˿ڶ���
			destAdd = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 13000);//�������ñ�����ַ
		} catch (Exception e) {
		}
	}
	
	private static Sender sender = new Sender();
	//�˴�������ϰһ�µ�ʵ��ģ��,�����з���ͼƬ�ͷ�����������������
	//������һ�����в��ᴴ�����ʵ��,����û��Ҫʹ�õ�ʵ��
	public static Sender getInstance() {
		return sender;
	}
	
	public void send(String text) {
		byte[] temp = text.getBytes();
		byte[] buffer = new byte[temp.length+1];
		buffer[0] = 1;
		System.arraycopy(temp, 0, buffer, 1, temp.length);
		sendPackage(buffer);
	}
	
	public void send(BufferedImage img) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", bos);
			byte[] temp = bos.toByteArray();
	//			System.out.println("ͼƬ�ֽڴ�С��"+temp.length);//���15000-25000
			bos.close();
			byte[] buffer = new byte[temp.length+1];
			buffer[0] = 2;
			System.arraycopy(temp, 0, buffer, 1, temp.length);
			sendPackage(buffer);
		}catch (Exception e){
			
		}
	}
	
	public void sendPackage(byte[] buffer) {
		try {
			// 4.����Ҫ���͵����ݰ�,ָ������,ָ��Ŀ���ַ
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length,destAdd);
			// 5���������ݰ�
			dSender.send(dp);
		} catch (Exception e) {
		}
	}
}
