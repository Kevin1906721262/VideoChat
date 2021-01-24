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
利用DatagramSocket发送文本及图片数据
*/
public class Sender {
	public DatagramSocket dSender;
	public SocketAddress localAddr;
	public SocketAddress destAdd;

	private Sender() {
		try {
			// 1、创建本机地址端口对象
			localAddr = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 12000);
			// 2.创建发送的Socket对象
			dSender = new DatagramSocket(localAddr);
			// 3、创建对方地址端口对象
			destAdd = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 13000);//测试暂用本机地址
		} catch (Exception e) {
		}
	}
	
	private static Sender sender = new Sender();
	//此处纯粹练习一下单实例模型,主类中发送图片和发送文字是两个方法
	//但是在一个类中不会创建多个实例,所以没必要使用单实例
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
	//			System.out.println("图片字节大小："+temp.length);//大概15000-25000
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
			// 4.创建要发送的数据包,指定内容,指定目标地址
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length,destAdd);
			// 5、发送数据包
			dSender.send(dp);
		} catch (Exception e) {
		}
	}
}
