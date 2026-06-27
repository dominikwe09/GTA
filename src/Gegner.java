import GLOOP.*;
class Gegner {
    private double kopfRadiusBasis     = 20;
    private double koerperRadiusBasis  = 18;
    private double beineRadiusBasis    = 14;
    private double beineHoeheBasis     = 45;
    private double koerperHoeheBasis   = 65;

    private double basisGeschwindigkeit = 2.5;

    private double hpAbstandBasis = 28;

    private double koerperRadius;
    private double beineY, koerperY, kopfY;
    private double lebensbalkenY;
    private double hpVersatz;
    private double x, z;
    private double geschwindigkeit;

    private GLZylinder beine;
    private GLZylinder koerper;
    private GLKugel    kopf;
    private GLQuader[] hpQuader = new GLQuader[4];


    private int     hp             = 4;
    private boolean lebendig       = true;
    private boolean wurdeVersteckt = false;

    Gegner(double x, double z, double groesse) {
        this.x = x;
        this.z = z;

        double kopfRadius   = kopfRadiusBasis    * groesse;
        koerperRadius       = koerperRadiusBasis * groesse;
        double beineRadius  = beineRadiusBasis   * groesse;
        double beineHoehe   = beineHoeheBasis    * groesse;
        double koerperHoehe = koerperHoeheBasis  * groesse;

        beineY        = beineHoehe / 2.0;
        koerperY      = beineHoehe + koerperHoehe / 2.0;
        kopfY         = beineHoehe + koerperHoehe + kopfRadius;
        lebensbalkenY = kopfY + kopfRadius + 25 * groesse;

        geschwindigkeit = basisGeschwindigkeit / (0.3 + groesse * 0.7);
        hpVersatz = hpAbstandBasis * groesse;

        beine   = new GLZylinder(x, beineY,   z, beineRadius,   beineHoehe);
        koerper = new GLZylinder(x, koerperY, z, koerperRadius, koerperHoehe);
        kopf    = new GLKugel   (x, kopfY,    z, kopfRadius);

        beine.setzeFarbe          (0.55, 0.05, 0.05);
        beine.setzeTextur         ("Texturen/beine2.png");
        beine.setzeSelbstleuchten (0.40, 0.40, 0.40);
        koerper.setzeFarbe        (0.90, 0.10, 0.10);
        koerper.setzeTextur       ("Texturen/body.png");
        koerper.setzeSelbstleuchten(0.40, 0.40, 0.40);
        kopf.setzeTextur          ("Texturen/kopf.png");
        kopf.setzeSelbstleuchten  (0.40, 0.40, 0.40);

        double hpBreite = 25 * groesse;
        double hpHoehe  = 10 * groesse;
        for (int i = 0; i < 4; i++) {
            hpQuader[i] = new GLQuader(0, 0, 0, hpBreite, hpHoehe, hpHoehe);
            hpQuader[i].setzeFarbe        (1.0, 0.0, 0.0);
            hpQuader[i].setzeSelbstleuchten(1.0, 0.0, 0.0);
        }
    }

    // Bewegt den Gegner jeden Frame auf den Spieler zu und dreht ihn zum Spieler hin
    void aktualisiere(double spielerX, double spielerZ) {
        if (!lebendig) return;

        double richtungX = spielerX - x;
        double richtungZ = spielerZ - z;
        double dist2     = richtungX * richtungX + richtungZ * richtungZ;

        if (dist2 > 1.0) {
            double entfernung = Math.sqrt(dist2);
            double nx = richtungX / entfernung;
            double nz = richtungZ / entfernung;

            x += nx * geschwindigkeit;
            z += nz * geschwindigkeit;
            beine.setzePosition  (x, beineY,   z);
            koerper.setzePosition(x, koerperY, z);
            kopf.setzePosition   (x, kopfY,    z);

            double winkel = Math.toDegrees(Math.atan2(nx, nz));
            beine.setzeDrehung  (-90, winkel + 180,       0);
            koerper.setzeDrehung(-90, winkel + 180,       0);
            kopf.setzeDrehung   (90, winkel + 90,  0);

            for (int i = 0; i < 4; i++) {
                double versatz = (i - 1.5) * hpVersatz;
                hpQuader[i].setzePosition(x + (-nz) * versatz, lebensbalkenY, z + nx * versatz);
            }
        }
    }

    // Zieht dem Gegner HP ab und zeigt den neuen Stand an
    void treffer(int schaden) {
        if (!lebendig) return;
        hp -= schaden;
        if (hp < 0) {
            hp = 0;
        }
        aktualisiereBalken();
        if (hp == 0) {
            lebendig = false;
            verstecken();
        }
    }

    // Färbt die HP-Segmente
    private void aktualisiereBalken() {
        for (int i = 0; i < 4; i++) {
            double helligkeit;
            if (hp > (3 - i)) {
                helligkeit = 1.0;
            } else {
                helligkeit = 0.08;
            }
            hpQuader[i].setzeFarbe        (helligkeit, 0, 0);
            hpQuader[i].setzeSelbstleuchten(helligkeit, 0, 0);
        }
    }

    private void verstecken() {
        if (wurdeVersteckt) return;
        wurdeVersteckt = true;
        beine.setzeSichtbarkeit  (false);
        koerper.setzeSichtbarkeit(false);
        kopf.setzeSichtbarkeit   (false);
        for (GLQuader q : hpQuader) q.setzeSichtbarkeit(false);
    }

    double  gibX()        {
        return x;
    }
    double  gibZ()        {
        return z;
    }
    double  gibRadius()   {
        return koerperRadius + 10;
    }
    boolean istLebendig() {
        return lebendig;
    }
}
