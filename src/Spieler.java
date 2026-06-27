import GLOOP.*;
class Spieler {
    private double kopfRadius    = 20;
    private double koerperRadius = 18;
    private double beineRadius   = 14;
    private double beineHoehe    = 45;
    private double koerperHoehe  = 65;

    private double beineY   = beineHoehe / 2.0;
    private double koerperY = beineHoehe + koerperHoehe / 2.0;
    private double kopfY    = beineHoehe + koerperHoehe + kopfRadius;
    private double kameraY  = kopfY;

    private double bewegungsgeschwindigkeit = 9.0;

    private GLZylinder beine;
    private GLZylinder koerper;
    private GLKugel    kopf;

    private double x, z;
    private double drehWinkel = 0.0;
    private double vorwaertsX =  0.0, vorwaertsZ = -1.0;
    private double rechtsX    =  1.0, rechtsZ    =  0.0;

    Spieler(double startX, double startZ) {
        x = startX;
        z = startZ;

        beine   = new GLZylinder(x, beineY,   z, beineRadius,   beineHoehe);
        koerper = new GLZylinder(x, koerperY, z, koerperRadius, koerperHoehe);
        kopf    = new GLKugel   (x, kopfY,    z, kopfRadius);

        kopf.drehe(90, 90, 0);

        beine.setzeTextur  ("Texturen/beine2.png");
        koerper.setzeTextur("Texturen/body2.png");
        kopf.setzeTextur   ("Texturen/kopf2.png");

        beine.setzeSelbstleuchten  (0.4, 0.4, 0.4);
        koerper.setzeSelbstleuchten(0.4, 0.4, 0.4);
        kopf.setzeSelbstleuchten   (0.4, 0.4, 0.4);
    }

    void aktualisiere(GLTastatur tast, double mausDrehung) {
        drehWinkel += mausDrehung;

        beine.setzeDrehung  (90, drehWinkel, 180);
        koerper.setzeDrehung(90, drehWinkel, 180);

        // Aus dem Drehwinkel Vorwärts- und Rechtsvektor berechnen (für WASD-Bewegung und FirstPerson)
        double winkelRad = Math.toRadians(drehWinkel);
        vorwaertsX = -Math.sin(winkelRad);
        vorwaertsZ = -Math.cos(winkelRad);
        rechtsX    =  Math.cos(winkelRad);
        rechtsZ    = -Math.sin(winkelRad);

        // Bewegungsvektor aus gedrückten Tasten zusammensetzen
        double dx = 0, dz = 0;
        if (tast.istGedrueckt('w'))   {
            dx += vorwaertsX * bewegungsgeschwindigkeit;
            dz += vorwaertsZ * bewegungsgeschwindigkeit;
        }
        if (tast.istGedrueckt('s'))  {
            dx -= vorwaertsX * bewegungsgeschwindigkeit;
            dz -= vorwaertsZ * bewegungsgeschwindigkeit;
        }
        if (tast.istGedrueckt('d')) {
            dx += rechtsX    * bewegungsgeschwindigkeit;
            dz += rechtsZ    * bewegungsgeschwindigkeit;
        }
        if (tast.istGedrueckt('a'))  {
            dx -= rechtsX    * bewegungsgeschwindigkeit;
            dz -= rechtsZ    * bewegungsgeschwindigkeit;
        }

        x += dx;
        z += dz;
        beine.setzePosition  (x, beineY,   z);
        koerper.setzePosition(x, koerperY, z);
        kopf.setzePosition   (x, kopfY,    z);
    }

    double gibX() {
        return x;
    }

    double gibZ() {
        return z;
    }

    double gibY() {
        return koerperY;
    }
    double gibDrehWinkel() {
        return drehWinkel;
    }

    double gibVorwaertsX() {
        return vorwaertsX;
    }

    double gibVorwaertsZ() {
        return vorwaertsZ;
    }

    double gibRechtsX() {
        return rechtsX;
    }

    double gibRechtsZ() {
        return rechtsZ;
    }

    double[] gibKameraPosition() {
        return new double[]{ x, kameraY, z };
    }
}
