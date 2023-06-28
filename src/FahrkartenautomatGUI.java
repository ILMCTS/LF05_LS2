
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

	// Panel für die Buttons
	private JPanel pnlTickets;

	// Speicher für den Betrag
	private double zuBezahlen;

	private double[] ticketPreise;
	private String[] ticketNamen;

	public FahrkartenautomatGUI() {
		// Fenstertitel setzen
		super("Ticketautomat");

		// Default Close Operation
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Not Resizable
		this.setResizable(false);

		// Content Panel erzeugen, Layout setzen
		this.pnlInhalt = new JPanel();
		this.pnlInhalt.setLayout(new GridLayout(2, 2));

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
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblHeader.setBackground(Color.yellow);
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		pnlInhalt.add(lblHeader);

		// Preis Label
		lblPreis = new JLabel();
		lblPreis.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPreis.setHorizontalAlignment(SwingConstants.CENTER);
		this.updatePreisLabel();
		pnlInhalt.add(lblPreis);

		// Ticket Panel
		pnlTickets = new JPanel();
		this.pnlTickets.setLayout(new GridLayout((int) (ticketNamen.length * 0.65), 1));

		// Ticket Buttons
		ticketBtns = new ArrayList<JButton>();
		for (int i = 0; i < ticketNamen.length; i++) {
			var ticketName = ticketNamen[i];
			var ticketPreis = ticketPreise[i];
			var ticketBtn = new JButton();

			ticketBtn.setText(ticketName);
			ticketBtn.setToolTipText(String.format("%.2f €", ticketPreis));
			ticketBtn.setBackground(Color.lightGray);
			ticketBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog(
							String.format("Jedes '%s' Ticket kostet %.2f €\n\nAnzahl:", ticketName, ticketPreis));
					int quantity;

					// Eingabe versuchen zu parsen
					try {
						quantity = Integer.parseInt(input);
					} catch (Exception ex) {
						showMessage(String.format(
								"Ungültige Eingabe: Es wurde eine Ganzzahl erwartet."
										+ "\n\nDas hinzufügen von '%s' für %.2f € wurde abgebrochen!",
								ticketName, ticketPreis), JOptionPane.ERROR_MESSAGE);

						// Exit on invalid number input
						return;
					}

					// Anzahl auf ihre Gültigkeit prüfen (mindestens 1 und maximal 10)
					if (quantity < 1 || 10 < quantity) {
						showMessage(String.format(
								"Ungültige Eingabe: Die Anzahl sollte mindestens 1 und nicht größer als 10 sein."
										+ "\n\nDas hinzufügen von '%s' für %.2f € wurde abgebrochen!",
								ticketName, ticketPreis), JOptionPane.ERROR_MESSAGE);
						return;
					}

					kaufeTicket(ticketPreis * quantity);
				}
			});

			pnlTickets.add(ticketBtn);
		}
		pnlInhalt.add(pnlTickets);

		// Bezahlen Button
		btnBezahlen = new JButton();
		btnBezahlen.setText("Bezahlen");
		btnBezahlen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bezahleAlles();
			}
		});
		pnlInhalt.add(btnBezahlen);

		// Panel zum Fenster hinzufügen
		this.setContentPane(pnlInhalt);
		this.pack();
	}

	public void kaufeTicket(double preis) {
		this.zuBezahlen = this.zuBezahlen + preis;

		// Preis Label aktualisieren
		this.updatePreisLabel();
	}

	public void bezahleAlles() {

		if (this.zuBezahlen == 0) {
			showMessage(String.format("Es wurden noch keine Tickets ausgewählt."), JOptionPane.ERROR_MESSAGE);

			// exit...
			return;
		}

		// INPUT ZEUG...
		while (0 < this.zuBezahlen) {
			try {
				String input = JOptionPane.showInputDialog("OKAY");
				double value = Double.parseDouble(input);

				if (value != .05 && value != .1 && value != .2 && value != .5 && value != 1 && value != 2 && value != 5
						&& value != 10 && value != 20) {
					throw new Exception("Ungültige Eingabe");
				}

				// Preis aktualisieren
				this.zuBezahlen -= value;
				this.updatePreisLabel();
			} catch (Exception ex) {
				// Ungültige Eingabe
				showMessage(String
						.format("Ungültige Eingabe: Es wurde ein ungültiger Geldschein oder Geldstück Wert eingegeben."
								+ "\nGültig: mind. 5 Cent, höchstens 20 Euro (Beispiel: 0,20 für 20 Cent oder 2 für 2 Euro)"
								+ "\n\nDas einwerfen ist fehlgeschlagen, bitte versuchen sie ein Gültiges Geldstück bzw. Geldschein"),
						JOptionPane.ERROR_MESSAGE);
			}
		}

		// ggf. Rückgeld
		if (Math.abs(this.zuBezahlen) != 0) {
			var orig = Math.abs(this.zuBezahlen);

			// Preis auf 0 setzen
			this.zuBezahlen = 0;
			this.updatePreisLabel();

			// Rückgeld auszahlen
			showMessage(String.format("Ihr Rückgeld: %.2f €", orig), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void updatePreisLabel() {
		// Preis Label aktualisieren
		lblPreis.setText(String.format("Preis: %.2f €", this.zuBezahlen));
	}

	private void showMessage(String message, int messageType) {
		JOptionPane.showMessageDialog(null, message, "Fahrkartenautomat", messageType);
	}

	public static void main(String[] args) {
		var gui = new FahrkartenautomatGUI();
		gui.setVisible(true);
	}
}