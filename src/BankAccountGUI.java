
import java.awt.EventQueue;
import java.awt.Dialog.ModalityType;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class BankAccountGUI extends JFrame {
	private final String CHEQUING_FILE = "chequing.txt";
	private final String SAVINGS_FILE = "savings.txt";
	private JPanel contentPane;
	private JLabel lblAccount;
	private JLabel lblCurrentBalance;
	private JTextField txtCurrentBalance;
	private JButton btnWithdraw;
	private JButton btnDeposit;
	private BankAccount chequing;
	private BankAccount savings;
	private BankAccount currentAccount;
	private JComboBox<String> cmbxAccounts = new JComboBox<String>();
	private boolean selected = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BankAccountGUI frame = new BankAccountGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BankAccountGUI() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_WindowClosing(e);
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 253, 143);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblAccount = new JLabel("Account");
		lblAccount.setForeground(Color.WHITE);
		lblAccount.setBounds(10, 11, 89, 14);
		contentPane.add(lblAccount);
		
		lblCurrentBalance = new JLabel("Current Balance:");
		lblCurrentBalance.setForeground(Color.WHITE);
		lblCurrentBalance.setBounds(10, 42, 97, 14);
		contentPane.add(lblCurrentBalance);
		
		txtCurrentBalance = new JTextField();
		txtCurrentBalance.setEditable(false);
		txtCurrentBalance.setBounds(108, 39, 121, 20);
		contentPane.add(txtCurrentBalance);
		txtCurrentBalance.setColumns(10);
		
		btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnWithdraw_MouseClicked(arg0);
			}
		});
		btnWithdraw.setBackground(UIManager.getColor("Button.light"));
		btnWithdraw.setBounds(10, 70, 89, 23);
		contentPane.add(btnWithdraw);
		
		btnDeposit = new JButton("Deposit");
		btnDeposit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnDeposit_MouseClicked(e);
			}
		});
		btnDeposit.setBackground(UIManager.getColor("Button.light"));
		btnDeposit.setBounds(140, 70, 89, 23);
		contentPane.add(btnDeposit);
		cmbxAccounts.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				cmbxAccounts_ItemStateChanged(arg0);
			}
		});
		
		cmbxAccounts.setBounds(109, 8, 118, 20);
		contentPane.add(cmbxAccounts);
		cmbxAccounts.addItem("Chequing");
		cmbxAccounts.addItem("Savings");
		
		Serializer s1 = new Serializer();
		try {
			chequing = (BankAccount)s1.deserialize(CHEQUING_FILE);
			if (chequing == null){
				throw new Exception();
			}
		} catch (Exception e) {
			chequing = new BankAccount("Chequing");
		}
		
		Serializer s2 = new Serializer();
		try {
			savings = (BankAccount)s2.deserialize(SAVINGS_FILE);
			if (savings == null){
				throw new Exception();
			}
		} catch (Exception e) {
			savings = new BankAccount("Savings");
		}
		
		this.currentAccount = this.chequing;

		
		setControls();
		
	}

	private void setControls(){
		
		if (this.selected == false){
			this.btnWithdraw.setEnabled(false);
			this.btnDeposit.setEnabled(false);
		}
		
		if (currentAccount != null) {
			if (currentAccount.getBalance() < 0) {
				txtCurrentBalance.setForeground(Color.RED);
			}else{
				txtCurrentBalance.setForeground(Color.BLACK);
			}
			if (this.currentAccount == this.chequing){
				this.txtCurrentBalance.setText(String.format("$%.2f", chequing.getBalance()));
			}else if (this.currentAccount == this.savings){
				this.txtCurrentBalance.setText(String.format("$%.2f", savings.getBalance()));
			}
		}
		
	}

	protected void cmbxAccounts_ItemStateChanged(ItemEvent arg0) {
		
		this.selected = true;
		if (cmbxAccounts.getSelectedIndex() == 0){
			this.currentAccount = this.chequing;
		}else if (cmbxAccounts.getSelectedIndex() == 1){
			this.currentAccount = this.savings;
		}
		setControls();
		
	}
	protected void btnWithdraw_MouseClicked(MouseEvent arg0) {
		
		WithdrawDialog withdrawDialog = new WithdrawDialog();
		withdrawDialog.setLocationRelativeTo(this);
		withdrawDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		withdrawDialog.setVisible(true);
		
		if (withdrawDialog.isOk()){
			if (this.currentAccount == this.chequing){
				this.chequing.withdraw(withdrawDialog.getWithdrawAmount(), withdrawDialog.getWithdrawDescription());
			}else if (this.currentAccount == this.savings){
				this.savings.withdraw(withdrawDialog.getWithdrawAmount(), withdrawDialog.getWithdrawDescription());
			}
			setControls();
		}
		
	}
	protected void btnDeposit_MouseClicked(MouseEvent e) {
		
		DepositDialog depositDialog = new DepositDialog();
		depositDialog.setLocationRelativeTo(this);
		depositDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		depositDialog.setVisible(true);
		
		if (depositDialog.isOk()){
			if (this.currentAccount == this.chequing){
				this.chequing.deposit(depositDialog.getDepositAmount(), depositDialog.getDepositDescription());
			}else if (this.currentAccount == this.savings){
				this.savings.deposit(depositDialog.getDepositAmount(), depositDialog.getDepositDescription());
			}
			setControls();
		}
	}
	protected void this_WindowClosing(WindowEvent arg0) {
		Serializer s1 = new Serializer();
		try {
			s1.serialize(this.chequing, CHEQUING_FILE);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		Serializer s2 = new Serializer();
		try {
			s2.serialize(this.savings, SAVINGS_FILE);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
