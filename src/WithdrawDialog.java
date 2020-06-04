import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class WithdrawDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblProvideAmount;
	private JTextField txtWithdrawAmount;
	private JLabel lblProvideDescription;
	private JTextField txtWithdrawDescription;
	private JLabel lblAmountWarning;
	private String withdrawDescription;
	private boolean ok = false;
	private JButton okButton = new JButton("OK");


	public static void main(String[] args) {
		try {
			WithdrawDialog dialog = new WithdrawDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WithdrawDialog() {
		setBounds(100, 100, 318, 195);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.BLUE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblProvideAmount = new JLabel("Please provide the amount to be withdrawn:");
		lblProvideAmount.setForeground(Color.WHITE);
		lblProvideAmount.setBounds(10, 11, 249, 14);
		contentPanel.add(lblProvideAmount);
		
		txtWithdrawAmount = new JTextField();
		txtWithdrawAmount.setBounds(10, 36, 282, 20);
		contentPanel.add(txtWithdrawAmount);
		txtWithdrawAmount.setColumns(10);
		txtWithdrawAmount.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent){
				setControls();
			}
			public void insertUpdate(DocumentEvent documentEvent){
				setControls();
			}
			public void removeUpdate(DocumentEvent documentEvent){
				setControls();
			}
		});
		lblProvideDescription = new JLabel("(Optional) Provide a description:");
		lblProvideDescription.setForeground(Color.WHITE);
		lblProvideDescription.setBounds(10, 67, 249, 14);
		contentPanel.add(lblProvideDescription);
		
		txtWithdrawDescription = new JTextField();
		txtWithdrawDescription.setBounds(10, 92, 282, 20);
		contentPanel.add(txtWithdrawDescription);
		txtWithdrawDescription.setColumns(10);
		
		lblAmountWarning = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
		lblAmountWarning.setBounds(260, 6, 32, 32);
		lblAmountWarning.setToolTipText("Not a valid value.");
		contentPanel.add(lblAmountWarning);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.BLUE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						okButton_MouseClicked(arg0);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						cancelButton_MouseClicked(e);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			setControls();
		}
		
		
	}
	protected void okButton_MouseClicked(MouseEvent arg0) {
		this.ok = true;
		this.setVisible(false);
		setControls();
	}
	protected void cancelButton_MouseClicked(MouseEvent e) {
		this.ok = false;
		this.setVisible(false);
		setControls();
	}
	private void setControls(){
		
		boolean amountIsNotNumeric = false;
		
		try {
			amountIsNotNumeric = (Double.valueOf(txtWithdrawAmount.getText()) == 0);
		} catch (NumberFormatException e) {
			amountIsNotNumeric = true;
		}
		withdrawDescription = txtWithdrawAmount.getText();
		
		boolean amountIsNotEmpty = (this.txtWithdrawAmount.getText().isEmpty() == false);
		boolean showWarning = amountIsNotNumeric && amountIsNotEmpty;
//		System.out.println(String.format("setControls: notEmpty = %b, notNumeric = %b, showWarning = %b", amountIsNotEmpty, amountIsNotNumeric, showWarning));

		this.lblAmountWarning.setVisible(showWarning);
		this.okButton.setEnabled(showWarning == false);

		
	}
	public Double getWithdrawAmount() {
		return Double.parseDouble(txtWithdrawAmount.getText());
	}
	public String getWithdrawDescription() {
		return withdrawDescription;
	}
	public boolean isOk() {
		return ok;
	}
}
