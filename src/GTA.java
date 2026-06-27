import GLOOP.*;
public class GTA {
    GLLicht    licht;
    GLBoden    boden;
    GLHimmel   himmel;
    GLKamera   kamera;
    GLTastatur tast;

    Spieler    spieler;
    Gegner[]   gegner;
    GLQuader[] gebaeude;

    // Zwei Kameramodi: V-Taste wechselt
    FirstPerson  fpKamera;
    ThirdPerson  tpKamera;
    Mausbewegung maus;

    boolean dritterPerson = false;
    // Speichert ob V im letzten Frame gedrückt war(verhindert schnelles hin- und herschalten)
    boolean vLetztesMal   = false;

    public static void main(String[] args) {
        new GTA();
    }

    public GTA() {
        licht  = new GLLicht(0, 1000, 0);
        boden  = new GLBoden("Texturen/strasse1.png");
        himmel = new GLHimmel("Texturen/himmel3.png");
        kamera = new GLKamera();
        tast   = new GLTastatur();

        kamera.setzePosition(0, 200, 500);
        kamera.setzeBlickpunkt(0, 100, 0);

        spieler = new Spieler(0, 0);

        // Gegnergenerierung
        double gx, gz;
        gegner = new Gegner[15];
        for (int i = 0; i < gegner.length; i++) {
            do {
                gx = -5000 + Math.random() * 10000;
                gz = -5000 + Math.random() * 10000;
            } while (gx * gx + gz * gz < 2000 * 2000);
            double groesse = 0.5 + Math.random() * 2.0;
            gegner[i] = new Gegner(gx, gz, groesse);
        }

        // Gebäudegenerierung
        gebaeude = new GLQuader[100];
        for (int i = 0; i < gebaeude.length; i++) {
            double bx = 250 + Math.random() * 150;
            double bz = 250 + Math.random() * 150;
            double by = 600 + Math.random() * 1400;
            double x  = -10000 + Math.random() * 20000;
            double z  = -10000 + Math.random() * 20000;
            gebaeude[i] = new GLQuader(x, by / 2, z, bx, by, bz);
            gebaeude[i].setzeFarbe(0.55, 0.55, 0.55);
            gebaeude[i].setzeTextur("Texturen/Gebaeude.jpg");
            gebaeude[i].setzeSelbstleuchten(0.5, 0.5, 0.5);
        }

        maus     = new Mausbewegung();
        fpKamera = new FirstPerson(kamera, spieler, gegner, maus);
        tpKamera = new ThirdPerson(kamera, spieler);

        fpKamera.initialisiereKamera();
        spielen();
    }

    // Hauptschleife
    private void spielen() {
        while (!tast.esc()) {
            maus.aktualisiere();
            // Wechseln der Perspektiven
            boolean vGedrueckt = tast.istGedrueckt('v');
            if (vGedrueckt && !vLetztesMal) {
                dritterPerson = !dritterPerson;
                if (dritterPerson) {
                    fpKamera.verbergeUI();
                }
            }
            vLetztesMal = vGedrueckt;

            // Keine Spielerdrehung in third person
            double mausDrehung;
            if (dritterPerson) {
                mausDrehung = 0.0;
            } else {
                mausDrehung = -maus.gibBewegungX() * 0.3;
            }
            spieler.aktualisiere(tast, mausDrehung);

            // Aktiven Kameramodus aktualisieren
            if (dritterPerson) {
                tpKamera.aktualisiere();
            } else {
                fpKamera.aktualisiere();
                fpKamera.zeigeUI();
            }

            // Alle Gegner auf den Spieler zubewegen
            for (Gegner g : gegner) g.aktualisiere(spieler.gibX(), spieler.gibZ());
            Sys.warte();
        }
    }
}
