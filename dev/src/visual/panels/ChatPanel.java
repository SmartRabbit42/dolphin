package visual.panels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;

import data.Data;
import data.containers.Chat;
import visual.popups.ChatPopup;
import network.Network;
import visual.Client;

public class ChatPanel extends JButton {

	private static final long serialVersionUID = -5194642801471406001L;

	private Client client;
	private Network network;
	private Data data;
	
	private Chat chat;
	
	private JLabel lblName;
	
	public ChatPanel(Client client, Network network, Data data, Chat chat) {
		this.client = client;
		this.network = network;
		this.data = data;
		
		this.chat = chat;
		
		initializeComponents();
		
		update();
	}

	private void initializeComponents() {
		setLayout(null);
		
		Dimension chatDimension = new Dimension(250, 30);
		
		setPreferredSize(chatDimension);
		setMaximumSize(chatDimension);
		setMinimumSize(chatDimension);
		
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		setComponentPopupMenu(new ChatPopup(client, network, data, chat));
		
		lblName = new JLabel();
		lblName.setAlignmentX(LEFT_ALIGNMENT);
		lblName.setBounds(0, 0, 250, 15);
		
		add(lblName);
	}
	
	public void update() {
		lblName.setText(chat.getName());
	}
	
	public Chat getChat() {
		return chat;
	}
}