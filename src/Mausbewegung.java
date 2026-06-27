import GLOOP.*;
import java.awt.*;
import java.awt.image.BufferedImage;
class Mausbewegung {
    private GLMaus maus   = new GLMaus();
    private Robot  robot;
    private int    zentrumX, zentrumY;
    private double bewegungX, bewegungY;

    Mausbewegung() {
        try { robot = new Robot(); } catch (AWTException e) { throw new RuntimeException(e); }
        // Unsichtbaren Cursor erstellen
        BufferedImage leerBild = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor unsichtbar = Toolkit.getDefaultToolkit().createCustomCursor(leerBild, new Point(0, 0), "leer");
        for (Window w : Window.getWindows()) w.setCursor(unsichtbar);

        Dimension bildschirm = Toolkit.getDefaultToolkit().getScreenSize();
        zentrumX = bildschirm.width  / 2;
        zentrumY = bildschirm.height / 2;
        robot.mouseMove(zentrumX, zentrumY);
    }

    void aktualisiere() {
        Point pos = MouseInfo.getPointerInfo().getLocation();
        bewegungX = pos.x - zentrumX;
        bewegungY = pos.y - zentrumY;
        robot.mouseMove(zentrumX, zentrumY);
    }

    double gibBewegungX() {
        return bewegungX;
    }
    double gibBewegungY() {
        return bewegungY;
    }
    GLMaus gibMaus()      {
        return maus;
    }
}
