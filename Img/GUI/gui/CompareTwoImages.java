package gui;
import imageCompare.DivideIntoBlocks;
import imageCompare.FFT;
import imageCompare.Grayscale;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.op.FFTException;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class CompareTwoImages {

	private JFrame frame;
	private JButton openButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompareTwoImages window = new CompareTwoImages();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public CompareTwoImages() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 367, 298);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		openButton = new JButton("Open a file...");
		
		JFileChooser uploadImage = new JFileChooser();
		int returnVal = uploadImage.showOpenDialog();
		
		JFormattedTextField savedImageFTF = new JFormattedTextField();
		// This may not be necessary if there is a way of gathering data from this pane when the scan button is pressed
		savedImageFTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				//get the text from the input and set it as the filename in Grayscale
			}
		});
		savedImageFTF.setToolTipText("Image to match against");
		savedImageFTF.setBounds(126, 11, 106, 20);
		frame.getContentPane().add(savedImageFTF);
		
		JEditorPane uploadImageEditorPane = new JEditorPane();
		// This may not be necessary if there is a way of gathering data from this pane when the scan button is pressed
		uploadImageEditorPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// take the image location, save the image and set it as the filename in grayscale
			}
		});
		uploadImageEditorPane.setBounds(10, 11, 106, 212);
		frame.getContentPane().add(uploadImageEditorPane);
		
		JButton btnScan = new JButton("Scan");
		btnScan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//perform FFT comparison on both of the images in the window
				if (savedImageFTF.getText()!=null && uploadImageEditorPane.getText()!=null){
					String filename1 = savedImageFTF.getText();
					try {
						BufferedImage greyImage1 = Grayscale.makeGrey(filename1);
						BufferedImage[][] imgs1 = DivideIntoBlocks.divide(greyImage1);
						double[] mags1 = FFT.computeMagnitude(imgs1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ImageDecoderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FFTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String filename2 = uploadImageEditorPane.getText();
					//BufferedImage greyImage2 = Grayscale.makeGrey(filename2);
					//BufferedImage[][] imgs2 = DivideIntoBlocks.divide(greyImage2);
					//float[][] mags2 = FFT.computeMagnitude(imgs2);
				}
			}
		});
		btnScan.setBounds(136, 41, 89, 23);
		frame.getContentPane().add(btnScan);
		
		//matchFoundEditorPane will show the green tick/red cross if a match is found or not respectively
		JEditorPane matchFoundEditorPane = new JEditorPane();
		matchFoundEditorPane.setBounds(126, 89, 106, 134);
		frame.getContentPane().add(matchFoundEditorPane);
	}
}
