package application;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import map.LAK;

public class ApplicationFrame {
	
	public ApplicationFrame() throws IOException {
		initComponents();
	}
	
	private void initComponents() throws IOException {
		JFrame frame = new JFrame("Where to go with your subject?!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        //Initialize panels and components
        JPanel panelCompare = new JPanel(new GridBagLayout());
        //The compare button
        JButton compareButton = new JButton("COMPARE SELECTED CITIES");
		compareButton.setPreferredSize(new Dimension(160, 30));
		compareButton.setFont(new Font("Champagne & Limousines", Font.BOLD, 18));
		//The search field
		final JTextField searchbar = new JTextField(10);
		searchbar.setSize(200, 30);
		searchbar.setText("Type subject to search for...");
		JButton searchButton = new JButton("Search");
		searchButton.setPreferredSize(new Dimension(100, 30));
		searchButton.setFont(new Font("Champagne & Limousines", Font.BOLD, 18));
		//The scale
		final BufferedImage image = ImageIO.read(new File("Scale2.png"));
		final BufferedImage image2 = ImageIO.read(new File("ScaleTransparency.png"));
		JRadioButton lakVsEDMRadioButton = new JRadioButton("LAK vs EDM");
		lakVsEDMRadioButton.setFont(new Font("Champagne & Limousines", Font.BOLD, 15));
	    JRadioButton nbPaperRadioButton = new JRadioButton("# PAPERS");
	    nbPaperRadioButton.setFont(new Font("Champagne & Limousines", Font.BOLD, 15));
	    ButtonGroup bg = new ButtonGroup();
	    bg.add(lakVsEDMRadioButton);
	    bg.add(nbPaperRadioButton);
		lakVsEDMRadioButton.setSelected(true);
        final JLabel scale = new JLabel(new ImageIcon(image));
		//The map
		@SuppressWarnings("serial")
		final
		Applet map = new LAK()
        {{
        	init();
        }};
        map.setPreferredSize(new Dimension(800,600));
        //Set the constraints for the compare button panel
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        panelCompare.add(searchbar, gc);
        gc.weightx=0;
        gc.gridx = 1;
        gc.gridy = 0;
        panelCompare.add(searchButton, gc);
        GridBagConstraints gc2 = new GridBagConstraints();
        gc2.fill = GridBagConstraints.HORIZONTAL;
        gc2.weightx = 0;
        gc2.gridwidth=2;
        gc2.gridx=0;
        gc2.gridy=1;
        panelCompare.add(compareButton, gc2);
        GridBagConstraints gc3 = new GridBagConstraints();
        gc3.fill = GridBagConstraints.HORIZONTAL;
        gc3.weightx = 0;
        //gc3.gridwidth=2;
        gc3.gridx=0;
        gc3.gridy=2;
        panelCompare.add(scale, gc3);
        GridBagConstraints gc5 = new GridBagConstraints();
        gc5.fill = GridBagConstraints.HORIZONTAL;
        gc5.weightx = 0;
        gc5.gridwidth=2;
        gc5.gridx=1;
        gc5.gridy=2;
        panelCompare.add(lakVsEDMRadioButton, gc5);
        GridBagConstraints gc6 = new GridBagConstraints();
        gc6.fill = GridBagConstraints.HORIZONTAL;
        gc6.weightx = 0;
        gc6.gridwidth=2;
        gc6.gridx=1;
        gc6.gridy=3;
        panelCompare.add(nbPaperRadioButton, gc6);
        //Set the frame
        frame.add(panelCompare, BorderLayout.NORTH);
        frame.add(map, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(790,720));
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!searchbar.getText().contains("you searched for ") && !searchbar.getText().contains("Type subject to search for..."))
				{
					{{
						scale.setIcon(new ImageIcon(image));
						((LAK) map).colorCitiesToSubject(searchbar.getText());
					}};
					searchbar.setText("you searched for " + searchbar.getText());
				}
				else	
				{
					{{
						((LAK) map).reset();
					}};
					searchbar.setText("Type subject to search for...");
				}
		    }
		});
		compareButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((LAK) map).showCompareView();
			}
		});
		lakVsEDMRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((LAK) map).reset();
				scale.setIcon(new ImageIcon(image));
			}
	
		});
		nbPaperRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				((LAK) map).changeMarkers();
				scale.setIcon(new ImageIcon(image2));
			}
			
		});
	}
	 
    public static void main(String[] args) {
        /**
         * Set look and feel of app
         */
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        /**
         * Create GUI and components on Event-Dispatch-Thread
         */
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	try {
					new ApplicationFrame();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
    }
}
