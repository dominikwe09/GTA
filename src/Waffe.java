import GLOOP.*;
class Waffe {
    private int    magazinGroesse = 6;
    private int    schaden        = 1;
    private int    ladeDauer      = 80;
    private double uiAbstand = 100;
    private double uiVersatz = -22;
    private double kugelRadius    = 3;
    private double kugelAbstand   = 10;

    private int     munition  = magazinGroesse;
    private boolean wirdGeladen = false;
    private int     ladeTimer   = 0;

    private GLKugel[] kugeln;

    Waffe() {
        kugeln = new GLKugel[magazinGroesse];
        for (int i = 0; i < magazinGroesse; i++) {
            kugeln[i] = new GLKugel(0, 0, 0, kugelRadius);
            kugeln[i].setzeFarbe        (1, 0.82, 0);
            kugeln[i].setzeSelbstleuchten(1, 0.82, 0);
        }
    }

    void aktualisiere() {
        if (munition == 0 && !wirdGeladen) {
            starteNachladen();
        }

        if (wirdGeladen) {
            ladeTimer--;
            if (ladeTimer <= 0) {
                fertigLaden();
            }
        }
    }

    void aktualisiereUI(double[] kameraPos, double vorwaertsX, double vorwaertsY, double vorwaertsZ, double rechtsX, double rechtsZ) {
        double basisX = kameraPos[0] + vorwaertsX * uiAbstand;
        double basisY = kameraPos[1] + vorwaertsY * uiAbstand + uiVersatz;
        double basisZ = kameraPos[2] + vorwaertsZ * uiAbstand;

        for (int i = 0; i < magazinGroesse; i++) {
            double versatz = (i - 2.5) * kugelAbstand;
            kugeln[i].setzePosition(basisX + rechtsX * versatz, basisY, basisZ + rechtsZ * versatz);

            if (i < munition) {
                // Gelb = noch geladen
                kugeln[i].setzeFarbe        (1.0, 0.82, 0.0);
                kugeln[i].setzeSelbstleuchten(1.0, 0.82, 0.0);
            } else if (wirdGeladen) {
                // Orange = am nachladen
                double ladefortschritt = 1.0 - (double) ladeTimer / ladeDauer;
                if (i < (int)(ladefortschritt * magazinGroesse)) {
                    kugeln[i].setzeFarbe        (1.0, 0.5, 0.0);
                    kugeln[i].setzeSelbstleuchten(1.0, 0.5, 0.0);
                } else {
                    // Dunkelgrau = nicht nachgeladen
                    kugeln[i].setzeFarbe        (0.2, 0.2, 0.2);
                    kugeln[i].setzeSelbstleuchten(0.2, 0.2, 0.2);
                }
            } else {
                // Dunkelgrau = verbraucht
                kugeln[i].setzeFarbe        (0.2, 0.2, 0.2);
                kugeln[i].setzeSelbstleuchten(0.2, 0.2, 0.2);
            }
        }
    }

    boolean schiessen() {
        if (wirdGeladen || munition <= 0) {
            return false;
        }
        munition--;
        return true;
    }

    void versteckenUI() {
        for (int i = 0; i < magazinGroesse; i++) {
            kugeln[i].setzeSichtbarkeit(false);
        }
    }

    void zeigenUI() {
        for (int i = 0; i < magazinGroesse; i++) {
            kugeln[i].setzeSichtbarkeit(true);
        }
    }

    int gibSchaden() {
        return schaden;
    }

    private void starteNachladen() {
        if (wirdGeladen || munition == magazinGroesse) return;
        wirdGeladen = true;
        ladeTimer   = ladeDauer;
    }

    private void fertigLaden() {
        munition    = magazinGroesse;
        wirdGeladen = false;
    }
}
