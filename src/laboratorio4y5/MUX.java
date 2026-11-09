/*
 * Creador: Amwix
 */
package laboratorio4y5;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import laboratorio4y5.Tema.Boton;

public final class MUX extends JFrame {

    /** Nombre de la ubicación de cada cámara (índice = código binario C2C1C0). */
    static final String[] NOMBRES = {
        "Pasillo", "Entrada principal", "Recepción", "Oficina 1",
        "Oficina 2", "Bodega", "Terraza", "Salida trasera"
    };

    Bit c0, c1, c2;
    Monitor salida;
    JLabel lblCodigo;
    JLabel[] canales = new JLabel[8];

    public MUX(MenuPrincipal p) {
        setTitle("Simulación MUX 8 a 1 - Cámaras");
        setSize(1000, 710);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        crearGUI();
        setVisible(true);
    }

    public void crearGUI(){
        JPanel raiz = Tema.prepararVentana(this);
        raiz.add(Tema.encabezado("Simulación MUX 8 a 1",
                "Configure las líneas de control y presione «Mostrar Cámara»"), BorderLayout.NORTH);

        // ===== PANEL DE CONTROL (izquierda) =====
        Tema.Tarjeta control = new Tema.Tarjeta(null, 18);
        control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
        control.setBorder(new EmptyBorder(18, 20, 18, 20));
        control.setPreferredSize(new Dimension(270, 0));

        control.add(izq(Tema.etiqueta("LÍNEAS DE CONTROL", Font.BOLD, 12, Tema.ACENTO)));
        control.add(Box.createVerticalStrut(14));

        c2 = new Bit();
        c1 = new Bit();
        c0 = new Bit();
        control.add(filaBit("C2", "bit más significativo", c2));
        control.add(Box.createVerticalStrut(10));
        control.add(filaBit("C1", "bit intermedio", c1));
        control.add(Box.createVerticalStrut(10));
        control.add(filaBit("C0", "bit menos significativo", c0));
        control.add(Box.createVerticalStrut(16));

        lblCodigo = Tema.etiqueta("", Font.BOLD, 13, Tema.TEXTO_SUAVE);
        control.add(izq(lblCodigo));
        control.add(Box.createVerticalStrut(14));

        Boton calcular = new Boton("Mostrar Cámara", Boton.Estilo.PRIMARIO);
        calcular.setAlignmentX(Component.LEFT_ALIGNMENT);
        calcular.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        control.add(calcular);
        control.add(Box.createVerticalStrut(20));

        control.add(izq(Tema.etiqueta("CANALES", Font.BOLD, 12, Tema.ACENTO)));
        control.add(Box.createVerticalStrut(8));
        JPanel lista = new JPanel(new GridLayout(4, 2, 6, 6));
        lista.setOpaque(false);
        lista.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 8; i++) {
            canales[i] = new JLabel("CAM " + i, SwingConstants.CENTER);
            canales[i].setOpaque(true);
            canales[i].setFont(Tema.fuente(Font.BOLD, 11));
            canales[i].setToolTipText(NOMBRES[i]);
            canales[i].setBorder(new EmptyBorder(5, 0, 5, 0));
            lista.add(canales[i]);
        }
        control.add(lista);
        control.add(Box.createVerticalGlue());

        // ===== MONITOR (derecha) =====
        salida = new Monitor();

        JPanel centro = new JPanel(new BorderLayout(18, 0));
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(18, 20, 0, 20));
        centro.add(control, BorderLayout.WEST);
        centro.add(salida, BorderLayout.CENTER);
        raiz.add(centro, BorderLayout.CENTER);

        // ===== BOTÓN ATRÁS =====
        Boton atras = new Boton("←  Atrás", Boton.Estilo.SECUNDARIO);
        atras.addActionListener(e -> {
            setVisible(false); // ocultar la ventana Cliente
            dispose();
            new MenuPrincipal().setVisible(true);
        });
        JPanel sur = Tema.barraInferior(atras);
        ((FlowLayout) sur.getLayout()).setAlignment(FlowLayout.LEFT);
        raiz.add(sur, BorderLayout.SOUTH);

        Runnable refrescarCodigo = () -> lblCodigo.setText("<html>Código:&nbsp; <font color='#E2E8F0' face='monospaced' size='5'>"
                + c2.texto() + " " + c1.texto() + " " + c0.texto() + "</font></html>");
        c0.alCambiar = c1.alCambiar = c2.alCambiar = refrescarCodigo;
        refrescarCodigo.run();
        marcarCanal(-1);

        calcular.addActionListener(e -> mostrarCamara());
    }

    void mostrarCamara() {
        String b0 = c0.texto();
        String b1 = c1.texto();
        String b2 = c2.texto();

        String codigo = b2 + b1 + b0;

        String archivo = switch (codigo) {
            case "000" -> "cam0.png";
            case "001" -> "cam1.jpeg";
            case "010" -> "cam2.jpg";
            case "011" -> "cam3.png";
            case "100" -> "cam4.png";
            case "101" -> "cam5.png";
            case "110" -> "cam6.jpeg";
            case "111" -> "cam7.jpg";
            default -> "";
        };

        int indice = Integer.parseInt(codigo, 2);
        Image img = Tema.cargarImagen(archivo);

        if (img == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la imagen:\n" + archivo,
                    "Error al cargar imagen",
                    JOptionPane.ERROR_MESSAGE);
            salida.mostrar(null, -1, null);
            marcarCanal(-1);
            return;
        }

        salida.mostrar(img, indice, codigo);
        marcarCanal(indice);
    }

    private void marcarCanal(int activo) {
        for (int i = 0; i < canales.length; i++) {
            boolean on = i == activo;
            canales[i].setBackground(on ? Tema.ACENTO : Tema.SUPERFICIE2);
            canales[i].setForeground(on ? Tema.FONDO : Tema.TEXTO_SUAVE);
        }
    }

    private static JComponent izq(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private static JPanel filaBit(String nombre, String ayuda, Bit bit) {
        JPanel f = new JPanel(new BorderLayout(10, 0));
        f.setOpaque(false);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(Tema.etiqueta(nombre, Font.BOLD, 15, Tema.TEXTO));
        textos.add(Tema.etiqueta(ayuda, Font.PLAIN, 11, Tema.TEXTO_SUAVE));

        f.add(textos, BorderLayout.CENTER);
        f.add(bit, BorderLayout.EAST);
        return f;
    }

    // =====================================================================
    /** Interruptor de un bit (0 / 1). */
    static final class Bit extends JComponent {
        private boolean valor;
        Runnable alCambiar;

        Bit() {
            setPreferredSize(new Dimension(70, 32));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("Clic para cambiar entre 0 y 1");
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    valor = !valor;
                    repaint();
                    if (alCambiar != null) alCambiar.run();
                }
            });
        }

        String texto() { return valor ? "1" : "0"; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suavizar(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(valor ? Tema.ACENTO_OSC : Tema.SUPERFICIE2);
            g2.fillRoundRect(0, 0, w, h, h, h);
            g2.setColor(valor ? Tema.ACENTO : Tema.BORDE);
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);

            int d = h - 8;
            int x = valor ? w - d - 4 : 4;
            g2.setColor(valor ? Color.WHITE : Tema.TEXTO_SUAVE);
            g2.fillOval(x, 4, d, d);

            g2.setFont(Tema.mono(Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            String t = texto();
            int tx = valor ? 4 + (w - d - 8 - fm.stringWidth(t)) / 2 : 4 + d + (w - d - 8 - fm.stringWidth(t)) / 2;
            g2.setColor(valor ? Color.WHITE : Tema.TEXTO_SUAVE);
            g2.drawString(t, tx, (h + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }

    // =====================================================================
    /** Pantalla de la cámara seleccionada (imagen ajustada sin deformar + superposiciones). */
    static final class Monitor extends JComponent {
        private Image imagen;
        private int indice = -1;
        private String codigo;

        void mostrar(Image img, int indice, String codigo) {
            this.imagen = img;
            this.indice = indice;
            this.codigo = codigo;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suavizar(g);
            int w = getWidth(), h = getHeight();
            RoundRectangle2D marco = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 18, 18);

            g2.setColor(new Color(0x020617));
            g2.fill(marco);

            Shape clipAnterior = g2.getClip();
            g2.clip(marco);

            if (imagen != null) {
                int iw = imagen.getWidth(this), ih = imagen.getHeight(this);
                if (iw > 0 && ih > 0) {
                    double esc = Math.min((double) w / iw, (double) h / ih);
                    int dw = (int) Math.round(iw * esc), dh = (int) Math.round(ih * esc);
                    g2.drawImage(imagen, (w - dw) / 2, (h - dh) / 2, dw, dh, this);
                }
                // degradado superior/inferior para legibilidad
                g2.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 170), 0, 60, new Color(0, 0, 0, 0)));
                g2.fillRect(0, 0, w, 60);
                g2.setPaint(new GradientPaint(0, h - 60, new Color(0, 0, 0, 0), 0, h, new Color(0, 0, 0, 170)));
                g2.fillRect(0, h - 60, w, 60);

                // REC
                g2.setColor(Tema.PELIGRO);
                g2.fillOval(18, 20, 12, 12);
                g2.setFont(Tema.fuente(Font.BOLD, 13));
                g2.setColor(Color.WHITE);
                g2.drawString("REC", 36, 31);
                g2.setFont(Tema.fuente(Font.BOLD, 15));
                g2.drawString("CAM " + indice + "  ·  " + NOMBRES[indice], 80, 31);

                g2.setFont(Tema.mono(Font.BOLD, 13));
                String c = "C2 C1 C0 = " + codigo.charAt(0) + " " + codigo.charAt(1) + " " + codigo.charAt(2);
                g2.drawString(c, w - g2.getFontMetrics().stringWidth(c) - 18, h - 18);
            } else {
                // líneas de barrido + "SIN SEÑAL"
                g2.setColor(new Color(255, 255, 255, 8));
                for (int y = 0; y < h; y += 4) g2.drawLine(0, y, w, y);

                g2.setFont(Tema.fuente(Font.BOLD, 26));
                FontMetrics fm = g2.getFontMetrics();
                String t = "SIN SEÑAL";
                g2.setColor(Tema.TEXTO_SUAVE);
                g2.drawString(t, (w - fm.stringWidth(t)) / 2, h / 2);

                g2.setFont(Tema.fuente(Font.PLAIN, 13));
                fm = g2.getFontMetrics();
                String s = "Seleccione C2, C1, C0 y presione «Mostrar Cámara»";
                g2.setColor(Tema.BORDE.brighter());
                g2.drawString(s, (w - fm.stringWidth(s)) / 2, h / 2 + 28);
            }

            g2.setClip(clipAnterior);
            g2.setColor(Tema.BORDE);
            g2.draw(marco);
            g2.dispose();
        }
    }
}
