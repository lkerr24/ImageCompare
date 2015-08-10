package gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.Font;


public class Frame extends JFrame{
	private static int WIDTH=100, HEIGHT=100;
	private static final long serialVersionUID = 1L;
	private JTextArea textURL;
	private JButton downloadButton;
	private JButton searchButton;
	private JTextPane selectedImagePane, resultPane;
	private StyledDocument docSelectedImages, docResults;
	private JMenuBar menuBar;
	private JMenu mnFiles;
	private JMenuItem mntmLoadFile;
	private JMenuItem mntmClearImages;
	private ArrayList<String> filePaths;
	//private HashMap<String, HashMap<String, TimeEntry>> records = new HashMap<String, HashMap<String, TimeEntry>>(); //the first string is image name.
	//private Table table;
	private JFrame frame;
	
	public Frame() {
		frame = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setVisible(true);
		//pack();
		//table = new Table(records);
		//table.read();
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFiles = new JMenu("Images");
		menuBar.add(mnFiles);
		
		mntmLoadFile = new JMenuItem("Select Images");
		mnFiles.add(mntmLoadFile);
		
		mntmClearImages = new JMenuItem("Clear Images");
		mnFiles.add(mntmClearImages);
		mntmLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filePaths = openFile();
				if(filePaths!=null){
					try {
						//docSelectedImages.insertString(docSelectedImages.getLength(), System.getProperty("user.dir"), null);
						int i=1; 
						for(String filePath : filePaths){
							 //docSelectedImages.insertString(docSelectedImages.getLength(), DownloadSites.getFileName(filePath)+"\n", null);
							 Image img = new ImageIcon(filePath).getImage();  
							 Image newimg = img.getScaledInstance(WIDTH, HEIGHT,  java.awt.Image.SCALE_SMOOTH); 
							 selectedImagePane.insertIcon (new ImageIcon(newimg));
							 docSelectedImages.insertString(docSelectedImages.getLength(), "     ", null);
							 if(i++%12==0){
								 docSelectedImages.insertString(docSelectedImages.getLength(), "\n\n", null);
							 }
						 }
						 
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		mntmClearImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(frame,
					    "Do you want to clear selected images and search results?",
					    "Message",
					    JOptionPane.YES_NO_OPTION);

				if(n == JOptionPane.YES_OPTION){
					selectedImagePane.setText("");
					resultPane.setText("");
					filePaths=null;
				}
			}
		});
		
		JLabel labelURL = new JLabel("Please input urls:");
		labelURL.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		textURL = new JTextArea();
		scrollPane_1.setViewportView(textURL);
		//textURL.setText("http://www.liopa.co.uk"+ "\n");
		textURL.setEditable(true);
		
		JLabel labelSelectedImages = new JLabel("Selected images:");
		labelSelectedImages.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		selectedImagePane = new JTextPane();
		scrollPane_2.setViewportView(selectedImagePane);
		docSelectedImages = selectedImagePane.getStyledDocument();
		
		JLabel labelResults = new JLabel("Search results:");
		labelResults.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		resultPane = new JTextPane();
		docResults = resultPane.getStyledDocument();
		JScrollPane scrollPane_3 = new JScrollPane(resultPane);
		
		downloadButton = new JButton("Download");
		downloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String urls = textURL.getText();
				PrintWriter out = null;
				try {
					out = new PrintWriter("resources/urls.csv");
					out.print(urls);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				out.close();
				//DownloadSites download = new DownloadSites();
				//download.start(docResults);
			}
		});
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(filePaths!=null){
					resultPane.setText("");
					//new SearchIdentical(records, filePaths, resultPane, docResults).start();
					//table.save();
				}
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(83)
							.addComponent(downloadButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
								.addComponent(labelURL))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(labelSelectedImages, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
								.addComponent(labelResults, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE))
							.addGap(8))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(searchButton)
							.addGap(340))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(8)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelURL)
						.addComponent(labelSelectedImages))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(labelResults)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE))
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(downloadButton)
						.addComponent(searchButton))
					.addContainerGap())
		);
		
		getContentPane().setLayout(groupLayout);
	}
	
	private ArrayList<String> openFile() {
		 ArrayList<String> filePaths = new ArrayList<String>();
		 File[] files;
		 String filePath;
		 JFileChooser fileChooser = new JFileChooser();
		 fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		 fileChooser.setMultiSelectionEnabled(true);
		 int result = fileChooser.showOpenDialog(this);

		 if ( result == JFileChooser.CANCEL_OPTION ) {
			 files = null;
			 filePaths = null;
		 }
		 else {
			 files = fileChooser.getSelectedFiles();
			 System.out.println("Selected files:");
			 for(File file: files){
				 filePath = file.getAbsoluteFile().toString();
				 filePaths.add(filePath);
				 System.out.println(filePath);
			 }
			 System.out.println("*****************************************************************");
			 System.out.println();
		 }	 
		 return filePaths;
	 }
}