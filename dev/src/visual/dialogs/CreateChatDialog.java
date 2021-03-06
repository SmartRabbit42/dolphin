package visual.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import data.Data;
import data.containers.Chat;
import data.containers.User;
import general.Helper;
import visual.panels.CreateChatAddUserPanel;
import general.exceptions.InvalidParameterException;
import network.Network;
import network.netMsg.messages.IncludedOnChatMsg;
import visual.Client;
import visual.VisualConstants;
import visual.components.DButton;
import visual.components.DDialog;
import visual.components.DLabel;
import visual.components.DPanel;
import visual.components.DScrollPane;
import visual.components.DTextField;

public class CreateChatDialog extends DDialog {
	
	private static final long serialVersionUID = 8544625027506291911L;

	protected static final String CreateChatAddUserPanel = null;

	private Client client;
	private Network network;
	private Data data;

	private DPanel panUsers;
	private DTextField txtName;
	private ArrayList<CreateChatAddUserPanel> createChatAddUserPanels;
	
	public CreateChatDialog(Client client, Network network, Data data) {
		super(client, Dialog.ModalityType.DOCUMENT_MODAL);
		
		this.client = client;
		this.network = network;
		this.data = data;
		
		this.createChatAddUserPanels = new ArrayList<CreateChatAddUserPanel>();
		
		initializeComponent();
	}
	
	private void initializeComponent() {
		setBackground(VisualConstants.BACK_COLOR);
		
		DPanel panList = new DPanel(VisualConstants.BACK_COLOR);
		panList.setLayout(new BoxLayout(panList, BoxLayout.Y_AXIS));
		
		DLabel lblTitle = new DLabel("Create Chat");
		
		DLabel lblName = new DLabel("chat name:");
		
		txtName = new DTextField();
		txtName.setMaximumSize(new Dimension(500, 50));

		panUsers = new DPanel(VisualConstants.BACK_COLOR);
		panUsers.setLayout(new BoxLayout(panUsers, BoxLayout.Y_AXIS));
		
		DButton btnAddUser = new DButton("+");
		btnAddUser.setSize(new Dimension(30, 30));
		btnAddUser.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAddUser.addActionListener(e -> btnAddUserClick());
		
		panUsers.add(btnAddUser);
		
		DScrollPane jspUsers = new DScrollPane(panUsers);
		
		panList.add(lblTitle);
		panList.add(Box.createRigidArea(new Dimension(0,5)));
		panList.add(lblName);
		panList.add(txtName);
		panList.add(Box.createRigidArea(new Dimension(0,5)));
		panList.add(jspUsers);
		panList.add(Box.createRigidArea(new Dimension(0,5)));

		DPanel panButtons = new DPanel(VisualConstants.BACK_COLOR);
		panButtons.setLayout(new BoxLayout(panButtons, BoxLayout.X_AXIS));
		
		DButton btnCancel = new DButton("cancel");
		btnCancel.addActionListener(e -> setVisible(false));
		
		DButton btnCreate = new DButton("create");
		btnCreate.addActionListener(e -> btnCreateClick());
		
		panButtons.add(Box.createHorizontalGlue());
		panButtons.add(btnCancel);
		panButtons.add(Box.createRigidArea(new Dimension(10, 0)));
		panButtons.add(btnCreate);

		Container contentPane = getContentPane();
		contentPane.add(panList, BorderLayout.CENTER);
		contentPane.add(panButtons, BorderLayout.PAGE_END);
		
		adjust();
	}
	
	private void btnAddUserClick() {
		CreateChatAddUserPanel createChatAddUserPanel = new CreateChatAddUserPanel(data.getAddedUsers());
		panUsers.add(createChatAddUserPanel, createChatAddUserPanels.size());
		createChatAddUserPanels.add(createChatAddUserPanel);
		adjust();
	}
	
	private void btnCreateClick() {
		try {
			Chat newChat = new Chat(txtName.getText());
			newChat.setId(Helper.generateId(data.getLocalUser().getFullAddress()));
			
			List<User> members = newChat.getMembers();
			
			List<String> membersId = new ArrayList<String>();
			List<String> membersAddress = new ArrayList<String>();
			
			members.add(data.getLocalUser());
			
			membersId.add(data.getLocalUser().getId());
			membersAddress.add(data.getLocalUser().getFullAddress());
			
			for (CreateChatAddUserPanel ccaup : createChatAddUserPanels) {
				User user = ccaup.getUser();
				if (user != null && !members.contains(user)) {
					members.add(user);
					
					membersId.add(user.getId());
					membersAddress.add(user.getFullAddress());
				}
			}
			
			IncludedOnChatMsg aocm = new IncludedOnChatMsg();
			aocm.setName(newChat.getName());
			aocm.setChatId(newChat.getId());
			aocm.setDate(newChat.getStart().getTime());
			aocm.setMembersId(membersId);
			aocm.setMembersAddress(membersAddress);
			
			network.spreadMessage(members, aocm, true);
			
			data.getChats().add(newChat);
			client.addChat(newChat);
			
			setVisible(false);
		} catch (InvalidParameterException e) { 
			JOptionPane.showMessageDialog(client,
			        e.getMessage(),
			        "error",
			        JOptionPane.INFORMATION_MESSAGE);
		}
	}
}