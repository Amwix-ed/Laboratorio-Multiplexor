/*
 * Creador: Amwix
 */
package laboratorio4y5;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Estilo visual común de la aplicación (solo Swing, sin librerías externas).
 * Paleta oscura tipo "centro de monitoreo".
 */
public final class Tema {

    private Tema() {}

    // ===== PALETA =====
    public static final Color FONDO       = new Color(0x0F172A);
    public static final Color SUPERFICIE  = new Color(0x1E293B);
    public static final Color SUPERFICIE2 = new Color(0x273449);
    public static final Color BORDE       = new Color(0x334155);
    public static final Color TEXTO       = new Color(0xE2E8F0);
    public static final Color TEXTO_SUAVE = new Color(0x94A3B8);
    public static final Color ACENTO      = new Color(0x38BDF8);
    public static final Color ACENTO_OSC  = new Color(0x0284C7);
    public static final Color EXITO       = new Color(0x22C55E);
    public static final Color PELIGRO     = new Color(0xEF4444);

    // ===== TIPOGRAFÍA =====
    private static final String FAMILIA = elegirFamilia();

    private static String elegirFamilia() {
        String[] disponibles = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        for (String preferida : new String[]{"Segoe UI", "Inter", "Helvetica Neue", "Arial"}) {
            for (String f : disponibles) {
                if (f.equalsIgnoreCase(preferida)) return f;
            }
        }
        return Font.SANS_SERIF;
    }

    public static Font fuente(int estilo, float tamano) {
        return new Font(FAMILIA, estilo, Math.round(tamano));
    }

    public static Font mono(int estilo, float tamano) {
        return new Font(Font.MONOSPACED, estilo, Math.round(tamano));
    }

    // ===== VENTANAS =====
    /** Aplica fondo y contenedor principal a una ventana. */
    public static JPanel prepararVentana(JFrame f) {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        f.setContentPane(raiz);
        f.getRootPane().setBackground(FONDO);
        return raiz;
    }

    /** Encabezado con título, subtítulo y una línea de acento inferior. */
    public static JPanel encabezado(String titulo, String subtitulo) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = suavizar(g);
                g2.setPaint(new GradientPaint(0, 0, SUPERFICIE, getWidth(), 0, FONDO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setPaint(new GradientPaint(0, 0, ACENTO, getWidth(), 0, new Color(ACENTO.getRed(), ACENTO.getGreen(), ACENTO.getBlue(), 0)));
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel t = new JLabel(titulo);
        t.setFont(fuente(Font.BOLD, 22));
        t.setForeground(TEXTO);

        p.add(t);
        if (subtitulo != null) {
            JLabel s = new JLabel(subtitulo);
            s.setFont(fuente(Font.PLAIN, 13));
            s.setForeground(TEXTO_SUAVE);
            p.add(Box.createVerticalStrut(4));
            p.add(s);
        }
        return p;
    }

    /** Barra inferior con espacio para botones alineados a la derecha. */
    public static JPanel barraInferior(JComponent... componentes) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        p.setBackground(FONDO);
        p.setBorder(new EmptyBorder(12, 20, 16, 20));
        for (JComponent c : componentes) p.add(c);
        return p;
    }

    public static JLabel etiqueta(String texto, int estilo, float tamano, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente(estilo, tamano));
        l.setForeground(color);
        return l;
    }

    // ===== UTILIDADES =====
    public static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        return g2;
    }

    public static Image cargarImagen(String archivo) {
        URL url = Tema.class.getResource("/laboratorio4y5/img/" + archivo);
        return url == null ? null : new ImageIcon(url).getImage();
    }

    static Color mezclar(Color a, Color b, float t) {
        return new Color(
                Math.round(a.getRed()   + (b.getRed()   - a.getRed())   * t),
                Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                Math.round(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t));
    }

    // ===== TARJETA REDONDEADA =====
    public static class Tarjeta extends JPanel {
        private final int radio;

        public Tarjeta(LayoutManager layout, int radio) {
            super(layout);
            this.radio = radio;
            setOpaque(false);
            setBorder(new EmptyBorder(16, 16, 16, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            RoundRectangle2D r = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, radio, radio);
            g2.setColor(SUPERFICIE);
            g2.fill(r);
            g2.setColor(BORDE);
            g2.draw(r);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===== BOTÓN REDONDEADO CON HOVER =====
    public static class Boton extends JButton {
        public enum Estilo { PRIMARIO, SECUNDARIO }

        private final Estilo estilo;
        private boolean encima;
        private String descripcion;

        public Boton(String texto, Estilo estilo) {
            super(texto);
            this.estilo = estilo;
            setFont(fuente(Font.BOLD, 14));
            setForeground(estilo == Estilo.PRIMARIO ? FONDO : TEXTO);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 22, 10, 22));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { encima = true;  repaint(); }
                @Override public void mouseExited(MouseEvent e)  { encima = false; repaint(); }
            });
        }

        /** Botón grande con una segunda línea de descripción (usado en el menú). */
        public Boton conDescripcion(String desc) {
            this.descripcion = desc;
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(14, 22, 14, 22));
            return this;
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            if (descripcion != null) d.height += 18;
            return d;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            boolean presionado = getModel().isPressed();
            Color base, borde;
            if (estilo == Estilo.PRIMARIO) {
                base = presionado ? ACENTO_OSC : encima ? mezclar(ACENTO, Color.WHITE, 0.15f) : ACENTO;
                borde = base;
            } else {
                base = presionado ? SUPERFICIE : encima ? SUPERFICIE2 : SUPERFICIE;
                borde = encima ? ACENTO : BORDE;
            }
            RoundRectangle2D r = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 14, 14);
            g2.setColor(base);
            g2.fill(r);
            g2.setColor(borde);
            g2.draw(r);

            if (descripcion != null) {
                // barra de acento a la izquierda
                g2.setColor(encima ? ACENTO : BORDE);
                g2.fillRoundRect(0, 10, 4, getHeight() - 20, 4, 4);

                Insets in = getInsets();
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int y = in.top + fm.getAscent();
                g2.setColor(TEXTO);
                g2.drawString(getText(), in.left, y);

                g2.setFont(fuente(Font.PLAIN, 12));
                g2.setColor(TEXTO_SUAVE);
                g2.drawString(descripcion, in.left, y + g2.getFontMetrics().getHeight() + 4);

                g2.setFont(fuente(Font.BOLD, 18));
                g2.setColor(encima ? ACENTO : TEXTO_SUAVE);
                FontMetrics fa = g2.getFontMetrics();
                g2.drawString("›", getWidth() - in.right - fa.stringWidth("›"), (getHeight() + fa.getAscent()) / 2 - 3);
                g2.dispose();
                return;
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
