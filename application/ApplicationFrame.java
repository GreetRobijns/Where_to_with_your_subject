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
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import map.LAK;

public class ApplicationFrame {
	
	public ApplicationFrame() {
		initComponents();
	}
	
	private void initComponents() {
		JFrame frame = new JFrame("Where to go with your subject?!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        //Initialize panels and components
        JPanel panelCompare = new JPanel(new GridBagLayout());
        //The compare button
        JButton compareButton = new JButton("COMPARE SELECTED COUNTRIES");
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
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("Scale.png"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        JLabel scale = new JLabel(new ImageIcon(image));
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
        gc3.gridwidth=2;
        gc3.gridx=0;
        gc3.gridy=2;
        panelCompare.add(scale, gc3);
        //Set the frame
        frame.add(panelCompare, BorderLayout.NORTH);
        frame.add(map, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(790,720));
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!searchbar.getText().contains("you searched for "))
				{
					{{
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
            	new ApplicationFrame();
            }
        });
    }
}
