package com.fly.www.video.view;

import cn.hutool.core.io.resource.ClassPathResource;
import com.fly.www.video.pojo.VideoInfo;
import com.fly.www.video.utils.VideoUtils;
import org.apache.commons.lang3.StringUtils;


import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class SearchView extends JPanel {
	private List<VideoInfo> videos = new ArrayList<VideoInfo>();

	private AudioClip audioClip;
	private JLabel saveJLabel;
	private JTextField jt;
	private JLabel jLabel;
	private JTable table;
	private JButton jb;
	private File openFile;
	private JTextField tf_file_path ;
	private JButton downDY;

	private JLabel saveJLabel_1;
	private JTextField jt1;
	private JLabel jLabel_1;
	private JTable table_1;
	private JButton jb1;
	private File openFile_1;
	private JTextField tf_file_path_1 ;
	private JButton downKS;

	/**
	 * Create the panel.
	 */
	public SearchView() {
		setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 450, 400);
		add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("抖音视频查询", null, panel, null);
		panel.setLayout(null);

		final JPanel panel_3 = new JPanel();
		panel_3.setBounds(5, 0, 415, 90);
		panel.add(panel_3);
		panel_3.setLayout(null);

		jLabel = new JLabel();
		jLabel.setText("链接:");
		jLabel.setBounds(10, 11, 30, 21);
		panel_3.add(jLabel);

		jt = new JTextField();
		jt.setToolTipText("链接");
		jt.setBounds(63, 11, 210, 21);
		panel_3.add(jt);
		jt.setColumns(10);

		final String tips[]= {"序号","下载路径","描述","封面","码率"};
		final JRadioButton  rb1 = new JRadioButton("抖音(分享)");
		rb1.setBounds(10, 38, 90, 23);
		panel_3.add(rb1);

		final	JRadioButton rb2 = new JRadioButton("抖音(主页)");
		rb2.setBounds(100, 38, 90, 23);
		panel_3.add(rb2);
		jb = new JButton("搜索");
		jb.setBounds(280, 10, 97, 23);
		panel_3.add(jb);
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text=jt.getText();
				videos = new ArrayList<VideoInfo>();
				//分享路径
				if (rb1.isSelected()) {
					TableModel tableModel=new DefaultTableModel(findVideosByShare(text,"1"),tips){
						public boolean isCellEditable(int row, int column) {
							return false;
						}// 表格不允许被编辑
					};
					table.setModel(tableModel);

				}
				//个人主页
				if (rb2.isSelected()) {
					TableModel tableModel=new DefaultTableModel(findVideos(text,"1"),tips){
						public boolean isCellEditable(int row, int column) {
							return false;
						}// 表格不允许被编辑
					};
					table.setModel(tableModel);
				}

			}
		});

		ButtonGroup buttonGroup=new ButtonGroup();
		buttonGroup.add(rb1);
		buttonGroup.add(rb2);

		saveJLabel = new JLabel();
		saveJLabel.setText("保存路径:");
		saveJLabel.setBounds(10, 65, 70, 21);
		panel_3.add(saveJLabel);

		tf_file_path = new JTextField();
		tf_file_path.setText("D:\\douyin\\抖音");
		tf_file_path.setBounds(80, 65, 120, 21);
		panel_3.add(tf_file_path);

		JButton btn_browse = new JButton("选择目录");
		btn_browse.setBounds(220, 65, 90, 23);
		panel_3.add(btn_browse);


		btn_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSystemView fsv = FileSystemView.getFileSystemView();
				File defaultFolder = fsv.getHomeDirectory();
				if(StringUtils.isNotBlank(tf_file_path.getText())) {
					defaultFolder = new File(tf_file_path.getText());
				}
				JFileChooser chooser = new JFileChooser(defaultFolder); //文件选择
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showOpenDialog(panel);        //打开文件选择窗
				openFile = chooser.getSelectedFile();  	//获取选择的文件
				if(openFile != null) {
					tf_file_path.setText(openFile.getPath());//获取选择文件的路径
				}
			}
		});


		downDY = new JButton("下载");
		downDY.setBounds(320, 65, 80, 23);
		panel_3.add(downDY);
		downDY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downDY.setEnabled(false);
				String savePath = tf_file_path.getText();
				if(!savePath.endsWith("/")) {//保存目录设置为以“/”结尾
					savePath = savePath+"/";
				}
				if(videos.size()>0){
					for (VideoInfo videoInfo: videos){
						VideoUtils.downloadVideo(videoInfo.getDownloadUrl(), videoInfo.getAuthor(), videoInfo.getDesc(), ".mp4",savePath);
//						CutVideoUtils.downloadVideo(videoInfo.getOriginCover(), videoInfo.getAuthor(), videoInfo.getDesc(), ".jpeg",savePath);
					}
				}
				downVideoEnd(panel_3);
				downDY.setEnabled(true);

			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 415, 200);
		panel.add(scrollPane);
		

		
		Object objects[][]=findVideos("","1");
		
		table = new JTable(objects,tips);
		scrollPane.setViewportView(table);
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("快手视频查询", null, panel_1, null);
		panel_1.setLayout(null);

		final JPanel panel_3_1 = new JPanel();
		panel_3_1.setLayout(null);
		panel_3_1.setBounds(5, 0, 415, 90);
		panel_1.add(panel_3_1);

		jLabel_1 = new JLabel();
		jLabel_1.setText("链接:");
		jLabel_1.setBounds(10, 11, 30, 21);
		panel_3_1.add(jLabel_1);
		jt1 = new JTextField();
		jt1.setToolTipText("链接");
		jt1.setColumns(10);
		jt1.setBounds(63, 11, 210, 21);
		panel_3_1.add(jt1);



		 jb1 = new JButton("搜索");
		jb1.setBounds(280, 10, 97, 23);
		panel_3_1.add(jb1);
		final String tips1[]= {"序号","下载路径","描述","封面","码率"};

		final	JRadioButton rb3 = new JRadioButton("快手(分享)");
		rb3.setBounds(10, 38, 90, 23);
		panel_3_1.add(rb3);

		final JRadioButton rb4 = new JRadioButton("快手(主页)");
		rb4.setBounds(100, 38, 90, 23);
		panel_3_1.add(rb4);
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str=jt1.getText();
				videos = new ArrayList<VideoInfo>();
				//分享路径
				if (rb3.isSelected()) {
//					查询结果反馈到表格
					TableModel tableModel=new DefaultTableModel(findVideosByShare(str,"2"),tips1){
						public boolean isCellEditable(int row, int column) {
							return false;
						}// 表格不允许被编辑
					};;
					table_1.setModel(tableModel);

				}
				//个人主页
				if (rb4.isSelected()) {
//					查询结果反馈到表格
					TableModel tableModel=new DefaultTableModel(findVideos(str,"2"),tips1){
						public boolean isCellEditable(int row, int column) {
							return false;
						}// 表格不允许被编辑
					};
					table_1.setModel(tableModel);

				}
			}
		});

		ButtonGroup buttonGroup1=new ButtonGroup();
		buttonGroup1.add(rb3);
		buttonGroup1.add(rb4);

		saveJLabel_1 = new JLabel();
		saveJLabel_1.setText("保存路径:");
		saveJLabel_1.setBounds(10, 65, 70, 21);
		panel_3_1.add(saveJLabel_1);

		tf_file_path_1 = new JTextField();
		tf_file_path_1.setText("D:\\kuaishou\\快手");
		tf_file_path_1.setBounds(80, 65, 120, 21);
		panel_3_1.add(tf_file_path_1);

		JButton btn_browse_1 = new JButton("选择目录");
		btn_browse_1.setBounds(220, 65, 90, 23);
		panel_3_1.add(btn_browse_1);



		btn_browse_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSystemView fsv = FileSystemView.getFileSystemView();
				File defaultFolder = fsv.getHomeDirectory();
				if(StringUtils.isNotBlank(tf_file_path_1.getText())) {
					defaultFolder = new File(tf_file_path_1.getText());
				}

				JFileChooser chooser = new JFileChooser(defaultFolder); //文件选择
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showOpenDialog(panel_1);        //打开文件选择窗
				openFile_1 = chooser.getSelectedFile();  	//获取选择的文件
				if(openFile_1 != null) {
					tf_file_path_1.setText(openFile_1.getPath());//获取选择文件的路径
				}
			}
		});
		downKS= new JButton("下载");
		downKS.setBounds(320, 65, 80, 23);
		panel_3_1.add(downKS);

		downKS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String savePath = tf_file_path_1.getText();
				downKS.setEnabled(false);
				if(!savePath.endsWith("/")) {//保存目录设置为以“/”结尾
					savePath = savePath+"/";
				}
				if(videos.size()>0){
					for (VideoInfo videoInfo: videos){
						VideoUtils.downloadVideo(videoInfo.getDownloadUrl(), videoInfo.getAuthor(), videoInfo.getDesc(), ".mp4",savePath);
//						CutVideoUtils.downloadVideo(videoInfo.getOriginCover(), videoInfo.getAuthor(), videoInfo.getDesc(), ".jpeg",savePath);
					}
				}
				downVideoEnd(panel_3_1);
				downKS.setEnabled(true);

			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 100, 415, 200);
		panel_1.add(scrollPane_1);

		table_1 = new JTable(findVideos("","2"),tips1) ;
		scrollPane_1.setViewportView(table_1);

		try {
			URL url = new ClassPathResource("audios/download-complete.wav").getUrl();
			audioClip = Applet.newAudioClip(url);//下载完成的音频
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public Object[][] findVideosByShare(String url,String type) {
		Object[][] arr= new Object[0][5];
		if(StringUtils.isBlank(url)){
			return arr;
		}
		try{
			if(StringUtils.equals(type,"1")){
				videos = VideoUtils.getDyVideoAndAudio(url);
			}else{
				videos = VideoUtils.getKsVideoAndAudio(url);
			}
			java.lang.reflect.Field[] s=VideoInfo.class.getDeclaredFields();
			arr=new Object[videos.size()][s.length];
			int i=0;
			int j=0;
			for ( VideoInfo stock : videos) {
				arr[i][j]=i+1;
				arr[i][j+1]=stock.getDownloadUrl();
				arr[i][j+2]=stock.getDesc();
				arr[i][j+3]=stock.getOriginCover();
				arr[i][j+4]=stock.getRatio();
				i++;
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return arr;
	}
	public Object[][] findVideos(String url,String type) {
		Object[][] arr= new Object[0][5];
		if(StringUtils.isBlank(url)){
			return arr;
		}
		try{
			if(StringUtils.equals(type,"1")){
				videos = VideoUtils.getDyVideoAndAudioById(url);

			}else{
				videos = VideoUtils.getKsVideoAndAudioById(url);
			}
			java.lang.reflect.Field[] s=VideoInfo.class.getDeclaredFields();
			arr=new Object[videos.size()][s.length];
			int i=0;
			int j=0;
			for ( VideoInfo stock : videos) {
				arr[i][j]=i+1;
				arr[i][j+1]=stock.getDownloadUrl();
				arr[i][j+2]=stock.getDesc();
				arr[i][j+3]=stock.getOriginCover();
				arr[i][j+4]=stock.getRatio();
				i++;
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return arr;
	}
	public  void downVideoEnd(Component parentComponent){
		audioClip.play();//播放下载完成的音频
		JOptionPane.showMessageDialog(parentComponent, "视频下载完毕！", "提醒", JOptionPane.INFORMATION_MESSAGE);
	}
		public  JFrame createFrame(){
			JFrame frame=new JFrame();
			frame.setAlwaysOnTop(true);// 设置顶置
			frame.setVisible(false);// 隐藏顶置窗口
			frame.setBounds(400, 200, 0, 0);	
		return  frame;
		}
}
