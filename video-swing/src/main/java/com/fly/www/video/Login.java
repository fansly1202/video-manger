package com.fly.www.video;

import cn.hutool.core.io.resource.ClassPathResource;
import com.fly.www.video.dao.UserDao;
import com.fly.www.video.pojo.User;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.fly.www.video.view.Main;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField jtf1;
	private JPasswordField jtf2;
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		setTitle("登录入口");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 300, 450, 300);
		try{
			setIconImage(new ImageIcon(new ClassPathResource("images/download.png").getUrl()).getImage());

		}catch (Exception e){
			e.printStackTrace();
		}
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("重置");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jtf1.setText("");
				jtf2.setText("");
			}
		});
		btnNewButton.setFont(new Font("", Font.BOLD, 17));
		btnNewButton.setBounds(64, 195, 100, 25);
		contentPane.add(btnNewButton);
		JLabel jL1 = new JLabel("");
		jL1.setForeground(Color.RED);
		jL1.setBounds(126, 170, 185, 15);
		contentPane.add(jL1);
		
		JButton btnNewButton_1 = new JButton("登录");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username=jtf1.getText();
				char password[]=jtf2.getPassword();
				UserDao userDao=new UserDao();
				User user=userDao.selectUserByName(username);
				if(user==null) {
					JOptionPane.showMessageDialog(null, "账号不存在！","提示",JOptionPane.INFORMATION_MESSAGE);
					
				}else {
					if(!user.getPassword().equals(String.valueOf(password))) {
						JOptionPane.showMessageDialog(null, "账号或者密码错误！","提示",JOptionPane.INFORMATION_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(null, "登录成功！","提示",JOptionPane.INFORMATION_MESSAGE);
						dispose();
						Main main=new Main();
						main.setVisible(true);
					}
				}
				
			}
		});
		btnNewButton_1.setFont(new Font("", Font.BOLD, 17));
		btnNewButton_1.setBounds(257, 195, 100, 25);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel = new JLabel("系统");
		lblNewLabel.setFont(new Font("", Font.BOLD, 27));
		lblNewLabel.setBounds(200, 10, 300, 46);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("账号:");
		lblNewLabel_1.setFont(new Font("", Font.BOLD, 18));
		lblNewLabel_1.setBounds(67, 86, 58, 21);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("密码:");
		lblNewLabel_2.setFont(new Font("", Font.BOLD, 18));
		lblNewLabel_2.setBounds(67, 141, 58, 21);
		contentPane.add(lblNewLabel_2);
		
		jtf1 = new JTextField();
		jtf1.setFont(new Font("", Font.BOLD, 17));
		jtf1.setBounds(126, 87, 228, 21);
		contentPane.add(jtf1);
		jtf1.setColumns(10);
		
		jtf2 = new JPasswordField();
		jtf2.setBounds(126, 142, 228, 21);
		contentPane.add(jtf2);
		
		
	}
}
