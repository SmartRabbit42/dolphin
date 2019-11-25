package visual.components;

import java.awt.Font;

import javax.swing.JLabel;

public class DLabel extends JLabel {

	private static final long serialVersionUID = -7938264165072962935L;

	private Font font = new Font("Arial", Font.BOLD, 20);
	
	public DLabel() {
		super();
		
		initializeComponent();
	}
	
	public DLabel(String text) {
		super(text);
		
		initializeComponent();
	}
	
	public DLabel(String text, Font font) {
		super(text);
		
		this.font = font;
		
		initializeComponent();
	}
	
	private void initializeComponent(){
		setFont(font);
	}
}