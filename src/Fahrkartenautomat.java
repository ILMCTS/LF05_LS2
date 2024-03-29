import java.util.Scanner;

class Fahrkartenautomat {

	public static boolean selectedAny = false;

	public static void main(String[] args) {

		Scanner tastatur = new Scanner(System.in);

		double zuZahlenderBetrag;
		double eingezahlterGesamtbetrag;

		// begrüßen
		begreussung();

		// Fahrschein Auswahl
		zuZahlenderBetrag = fahrkartenbestellungErfassen(tastatur);

		if (!selectedAny) {
			System.out.println("Auswahl abgebrochen, bis zum nächsten mal!");
			return;
		}

		// Geldeinwurf
		eingezahlterGesamtbetrag = fahrkartenBezahlen(tastatur, zuZahlenderBetrag);

		// Fahrscheinausgabe
		fahrkartenAusgeben();

		// Rückgeldberechnung und -ausgabe
		rueckgeldAusgeben(zuZahlenderBetrag, eingezahlterGesamtbetrag);

		tastatur.close();
	}

	// A.2.8: leigt am runden, desshalb multiplizieren wir mit 100, fügen dann 1/10
	// hinzu
	// -> convertieren dann zu einer ganzzahl, damit gerundet wird
	// -> dann wieder durch 100%, das wir die rundung auf 2 kommastellen bekommen
	public static double round(double v) {
		int by100 = (int) (v * 100 + 0.1);
		return by100 / 100.0;
	}

	public static void begreussung() {
		System.out.println("Herzlich Willkommen\n");
	}

	public static double fahrkartenbestellungErfassen(Scanner tastatur) {

		final int leaveInt = 0;
		final int invalidNum = -1;

		int ticketAnzahl;
		int ticketNummer = invalidNum;
		double ticketPreis = 0.0f;
		double zuZahlenderBetrag = 0.0f;

		String[] fahrkartenNamen = { "Einzelfahrschein AB", "Einzelfahrschein BC", "Einzelfahrschein ABC",
				"Kurzstrecke AB", "Tageskarte AB", "Tageskarte BC", "Tageskarte ABC", "4-Fahrten-Karte AB",
				"4-Fahrten-Karte BC", "4-Fahrten-Karte BC", "Kleingruppen-Tageskarte AB", "Kleingruppen-Tageskarte BC",
				"Kleingruppen-Tageskarte ABC" };
		double[] fahrkartenPreise = { 3.00, 3.50, 3.80, 2.00, 8.60, 9.20, 10.00, 9.40, 12.60, 13.80, 25.50, 26.00,
				26.50 };

		while (ticketNummer != leaveInt) {

			System.out.println("Wählen Sie eine Nummer aus!");
			System.out.printf("- %2d. %s\n", 0, "Tickets bezahlen");

			for (int i = 0; i < fahrkartenNamen.length; i++) {
				String name = fahrkartenNamen[i];
				double preis = fahrkartenPreise[i];
				int num = i + 1;

				System.out.printf("- %2d. %s [%.2f €]\n", num, name, preis);

			}

			while (ticketNummer == invalidNum) {
				System.out.print("Nummer: ");
				ticketNummer = tastatur.nextInt();

				if (ticketNummer == leaveInt) {
					break;
				}

				boolean validNum = 0 < ticketNummer && ticketNummer - 1 < fahrkartenNamen.length;
				if (!validNum) {
					ticketNummer = invalidNum;
					System.out.println(">>falsche Eingabe<<");
					continue;
				}

				ticketPreis = fahrkartenPreise[ticketNummer - 1];
				break;
			}

			if (ticketNummer == leaveInt)
				break;

			// Ticketanzahl eingeben
			System.out.print("Anzahl der Tickets (1-10): ");
			ticketAnzahl = tastatur.nextInt();

			while (ticketAnzahl < 1 || ticketAnzahl > 10) {
				System.out.print("Fehlerhafte Eingabe - Ticketanzahl (1-10) erneut eingeben: ");
				ticketAnzahl = tastatur.nextInt();
			}

			double zwischensumme = ticketPreis * ticketAnzahl;
			ticketPreis = 0.0f;
			ticketNummer = invalidNum;
			selectedAny = true;

			// preis für alle Tickets berechnen
			zuZahlenderBetrag += zwischensumme;
			System.out.printf("\nPreis: %.2f\nZwischensumme: %.2f €\n\n", zwischensumme, zuZahlenderBetrag);
		}

		System.out.println();
		return zuZahlenderBetrag;
	}

	public static double fahrkartenBezahlen(Scanner tastatur, double zuZahlenderBetrag) {
		double eingezahlterGesamtbetrag;
		double nochZuZahlen;
		double eingeworfeneMuenze;

		eingezahlterGesamtbetrag = 0.0;
		nochZuZahlen = 0.0;
		while (eingezahlterGesamtbetrag < zuZahlenderBetrag) {
			nochZuZahlen = zuZahlenderBetrag - eingezahlterGesamtbetrag;
			System.out.printf("Noch zu zahlen: %.2f Euro\n", nochZuZahlen);
			System.out.print("Eingabe (mind. 5 Cent, höchstens 20 Euro): ");
			eingeworfeneMuenze = tastatur.nextDouble();

			if (eingeworfeneMuenze == 0.05f || eingeworfeneMuenze == 0.10f || eingeworfeneMuenze == 0.20f
					|| eingeworfeneMuenze == 0.50f || eingeworfeneMuenze == 1.0f || eingeworfeneMuenze == 2.0f
					|| eingeworfeneMuenze == 5.0f || eingeworfeneMuenze == 10.00f || eingeworfeneMuenze == 20.00f) {
				eingezahlterGesamtbetrag = eingezahlterGesamtbetrag + eingeworfeneMuenze;
			} else
				System.out.println(">> Kein gültiges Zahlungsmittel");
		}

		return eingezahlterGesamtbetrag;
	}

	public static void fahrkartenAusgeben() {
		System.out.println("\nFahrschein wird ausgegeben");

		for (int i = 0; i < 8; i++) {
			System.out.print("=");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("\n\n");
	}

	public static void rueckgeldAusgeben(double zuZahlenderBetrag, double eingezahlterGesamtbetrag) {
		double rueckgabebetrag;

		rueckgabebetrag = eingezahlterGesamtbetrag - zuZahlenderBetrag;
		if (rueckgabebetrag > 0.0) {
			System.out.printf("Der Rückgabebetrag in Höhe von %.2f Euro\n", rueckgabebetrag);
			System.out.println("wird in folgenden Münzen ausgezahlt:");

			// 2-Euro-Münzen
			rueckgabebetrag = muenzRueckgabe(rueckgabebetrag, 2.0, "2 Euro");

			// 1-Euro-Münzen
			rueckgabebetrag = muenzRueckgabe(rueckgabebetrag, 1.0, "1 Euro");

			// 50-Cent-Münzen
			rueckgabebetrag = muenzRueckgabe(rueckgabebetrag, 0.5, "50 Cent");

			// 20-Cent-Münzen
			rueckgabebetrag = muenzRueckgabe(rueckgabebetrag, 0.2, "20 Cent");

			// 10-Cent-Münzen
			rueckgabebetrag = muenzRueckgabe(rueckgabebetrag, 0.1, "10 Cent");

			// 5-Cent-Münzen
			rueckgabebetrag = muenzRueckgabe(rueckgabebetrag, 0.05, "5 Cent");
		}

		System.out.println("\nVergessen Sie nicht, den Fahrschein\n" + "vor Fahrtantritt entwerten zu lassen!\n"
				+ "Wir wünschen Ihnen eine gute Fahrt.");
	}

	public static double muenzRueckgabe(double rueckgabebetrag, double muenzenWert, String muenzenString) {

		while (round(rueckgabebetrag) >= muenzenWert) {
			System.out.println(muenzenString);
			rueckgabebetrag = rueckgabebetrag - muenzenWert;
		}
		return rueckgabebetrag;
	}
}