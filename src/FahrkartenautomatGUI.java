
/*
* Basiselemente einer GUI
* Fenster mit Label, Textbox, Button
* und einem einfachen Layout
*/
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

public class FahrkartenautomatGUI extends JFrame {

	// Content Panel
	private JPanel pnlInhalt;

	// Label für die Überschrift
	private JLabel lblHeader;

	// Label für die Ausgabe
	private JLabel lblPreis;
	
	// Button zum bezahlen
	private JButton btnBezahlen;

	// Buttons für die Tickets
	private ArrayList<JButton> ticketBtns;

	// Speicher für den Betrag
	private double zuBezahlen;

	private double[] ticketPreise;
	private String[] ticketNamen;

	public FahrkartenautomatGUI() {
		// Fenstertitel setzen
		super("Ticketautomat");

		// Default Close Operation
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Content Panel erzeugen, Layout setzen
		this.pnlInhalt = new JPanel();
		GridLayout gl = new GridLayout(6, 1);
		this.pnlInhalt.setLayout(gl);

		// Ticket Preis und Namen Arrays setzen
		ticketNamen = new String[] { "Einzelfahrschein AB", "Einzelfahrschein BC", "Einzelfahrschein ABC",
				"Kurzstrecke AB", "Tageskarte AB", "Tageskarte BC", "Tageskarte ABC", "4-Fahrten-Karte AB",
				"4-Fahrten-Karte BC", "4-Fahrten-Karte BC", "Kleingruppen-Tageskarte AB", "Kleingruppen-Tageskarte BC",
				"Kleingruppen-Tageskarte ABC" };
		ticketPreise = new double[] { 3.00, 3.50, 3.80, 2.00, 8.60, 9.20, 10.00, 9.40, 12.60, 13.80, 25.50, 26.00,
				26.50 };

		// Überschrifts Label
		lblHeader = new JLabel();
		lblHeader.setText("Ticket wählen");
		lblHeader.setBackground(Color.yellow);
		pnlInhalt.add(lblHeader);

		// Preis Label
		lblPreis = new JLabel();
		lblPreis.setText("ok");
		pnlInhalt.add(lblPreis);

		// Ticket Buttons
		ticketBtns = new ArrayList<JButton>();
		for (int i = 0; i < ticketNamen.length; i++) {
			var ticketName = ticketNamen[i];
			var ticketPreis = ticketPreise[i];
			var ticketBtn = new JButton();
			
			ticketBtn.setText(String.format("%s - %.2f €", ticketName, ticketPreis));
			ticketBtn.setBackground(Color.lightGray);
			ticketBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog("Anzahl: ");
					int quantity;
					
					// Eingabe versuchen zu parsen
					try {
						quantity = Integer.parseInt(input);
					} catch (Exception ex) {
						//  "Ungültige Eingabe: Es wurde eine Ganzzahl erwartet\n\nDas kaufen von " + ticketName + " wurde abgebrochen"
						/*JOptionPane.showMessageDialog(ticketBtn,
						 * 
								String.format("Ungültige Eingabe: Es wurde eine Ganzzahl erwartet.\n\nDas hinzufügen von '%s' für %.2f € wurde abgebrochen!", ticketName, ticketPreis),
								"Fahrkartenautomat",
								JOptionPane.ERROR_MESSAGE);
						*/
						showMessage(String.format("Ungültige Eingabe: Es wurde eine Ganzzahl erwartet."
								+ "\n\nDas hinzufügen von '%s' für %.2f € wurde abgebrochen!", ticketName, ticketPreis),
								JOptionPane.ERROR_MESSAGE);
						
						// Exit on invalid number input
						return;
					}
					
					// Anzahl auf ihre Gültigkeit prüfen (mindestens 1 und maximal 10)
					if (quantity < 1 || 10 < quantity) {
						showMessage(String.format("Ungültige Eingabe: Die Anzahl sollte mindestens 1 und nicht größer als 10 sein."
								+ "\n\nDas hinzufügen von '%s' für %.2f € wurde abgebrochen!", ticketName, ticketPreis),
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					kaufeTicket(ticketPreis * quantity);
				}
			});
			
			pnlInhalt.add(ticketBtn);
		}
		
		// Bezahlen Button
		btnBezahlen = new JButton();
		btnBezahlen.setText("Bezahlen");
		btnBezahlen.setBackground(Color.green);
		btnBezahlen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bezahleAlles();
			}
		});
		pnlInhalt.add(btnBezahlen);

		// Action Listener setzen
		/* TODO!! */

		// Panel zum Fenster hinzuf�gen
		this.setContentPane(pnlInhalt);
		this.pack();
		this.setVisible(true);
	}

	public void kaufeTicket(double preis) {
		this.zuBezahlen = this.zuBezahlen + preis;

		// Preis Label aktualisieren
		this.updatePreisLabel();
	}

	public void bezahleAlles() {
		this.zuBezahlen = 0;

		// Preis Label aktualisieren
		this.updatePreisLabel();
	}

	private void updatePreisLabel() {
		// Preis Label aktualisieren
		lblPreis.setText(String.format("EUR %.2f €", this.zuBezahlen));
	}
	
	private void showMessage(String message, int messageType) {
		JOptionPane.showMessageDialog(null,
				message,
				"Fahrkartenautomat",
				messageType);
	}

	public static void main(String[] args) {
		var fka = new FahrkartenautomatGUI();
	}
}