package com.fly.www.video.view;



import cn.hutool.core.io.resource.ClassPathResource;
import com.fly.www.video.Login;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

public class Main extends JFrame {

	private JPanel contentPane;
	private CardLayout cardLayout=new CardLayout();
	public Main()  {
		setTitle("系统");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 300, 624, 500);
		setAlwaysOnTop(true);
		//设置应用左上角的icon
		try {
			setIconImage(new ImageIcon(new ClassPathResource("images/download.png").getUrl()).getImage());
		}catch (Exception e){
			e.printStackTrace();
		}
		setResizable(false);
		setLayout(null);

		contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(null, "主界面", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "操作页", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(117, 10, 483, 400);
		contentPane.add(panel);
		panel.setLayout(cardLayout);
		
//		添加面板

		SearchView searchView=new SearchView();
		panel.add(searchView," search_view");
//		Search_ks_view search_ks_view=new Search_ks_view();
//		panel.add(search_ks_view,"search_ks_view");

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "菜单", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 34, 100, 350);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
//		JButton btnNewButton_1 = new JButton("\u552E\u8D27");
//		btnNewButton_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				cardLayout.show(panel, "sell_view");
//			}
//		});
//		btnNewButton_1.setBounds(0, 59, 97, 23);
//		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("视频查询");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panel, " search_view");
			}
		});
		btnNewButton_2.setBounds(0, 50, 97, 23);
		panel_1.add(btnNewButton_2);
		
//		JButton btnNewButton_2_1 = new JButton("快手视频");
//		btnNewButton_2_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				cardLayout.show(panel, "search_ks_view");
//			}
//
//		});
//		btnNewButton_2_1.setBounds(0, 127, 97, 23);
//		panel_1.add(btnNewButton_2_1);
		
		JButton btnNewButton_2_1_1 = new JButton("退出");
		btnNewButton_2_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login=new Login();
				login.setVisible(true);
				dispose();
			}
		});
		btnNewButton_2_1_1.setBounds(0, 80, 97, 23);
		panel_1.add(btnNewButton_2_1_1);

	}
}
