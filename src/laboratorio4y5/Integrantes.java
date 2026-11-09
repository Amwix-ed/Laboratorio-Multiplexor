/*
 * Creador: Amwix
 */
package laboratorio4y5;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import laboratorio4y5.Tema.Boton;

public class Integrantes extends JFrame {

    public Integrantes(MenuPrincipal p) {
        setTitle("Creador");
        setSize(360, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        crearGUI();
        setVisible(true);
    }

    public void crearGUI(){
        JPanel raiz = Tema.prepararVentana(this);
        raiz.add(Tema.encabezado("Creador", "Desarrollado por Amwix"), BorderLayout.NORTH);

        JPanel panelIntegrantes = new JPanel(new GridLayout(1, 1, 18, 0));
        panelIntegrantes.setOpaque(false);
        panelIntegrantes.setBorder(new EmptyBorder(22, 24, 6, 24));

        panelIntegrantes.add(crearTarjeta("user.png", "Amwix", "Creador"));

        raiz.add(panelIntegrantes, BorderLayout.CENTER);

        // 🔙 BOTÓN ATRÁS
        Boton atras = new Boton("←  Atrás", Boton.Estilo.SECUNDARIO);
        atras.addActionListener(e -> {
            setVisible(false); // ocultar la ventana Cliente
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        raiz.add(Tema.barraInferior(atras), BorderLayout.SOUTH);
    }

    private JPanel crearTarjeta(String archivoImagen, String nombre, String rol) {

        Tema.Tarjeta panel = new Tema.Tarjeta(null, 20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(26, 14, 22, 14));

        // === FOTO CIRCULAR ===
        Avatar avatar = new Avatar(Tema.cargarImagen(archivoImagen), nombre);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === NOMBRE ===
        JLabel lblNombre = Tema.etiqueta(nombre, Font.BOLD, 16, Tema.TEXTO);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === ROL ===
        JLabel lblRol = new JLabel(rol);
        lblRol.setFont(Tema.fuente(Font.PLAIN, 12));
        lblRol.setForeground(Tema.ACENTO);
        lblRol.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRol.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(avatar);
        panel.add(Box.createVerticalStrut(18));
        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblRol);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /** Foto recortada en círculo con anillo de acento; si no hay imagen, muestra las iniciales. */
    private static final class Avatar extends JComponent {
        private static final int TAM = 140;
        private final Image imagen;
        private final String iniciales;

        Avatar(Image imagen, String nombre) {
            this.imagen = imagen;
            StringBuilder sb = new StringBuilder();
            for (String parte : nombre.split("\\s+")) {
                if (!parte.isEmpty() && sb.length() < 2) sb.append(Character.toUpperCase(parte.charAt(0)));
            }
            this.iniciales = sb.toString();
            Dimension d = new Dimension(TAM, TAM);
            setPreferredSize(d);
            setMaximumSize(d);
            setMinimumSize(d);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suavizar(g);
            int x = (getWidth() - TAM) / 2;
            Ellipse2D circulo = new Ellipse2D.Float(x + 4, 4, TAM - 8, TAM - 8);

            g2.setColor(Tema.SUPERFICIE2);
            g2.fill(circulo);

            if (imagen != null && imagen.getWidth(this) > 0) {
                Shape clip = g2.getClip();
                g2.clip(circulo);
                int iw = imagen.getWidth(this), ih = imagen.getHeight(this);
                double esc = Math.max((TAM - 8.0) / iw, (TAM - 8.0) / ih);
                int dw = (int) (iw * esc), dh = (int) (ih * esc);
                g2.drawImage(imagen, x + 4 + (TAM - 8 - dw) / 2, 4 + (TAM - 8 - dh) / 2, dw, dh, this);
                g2.setClip(clip);
            } else {
                g2.setFont(Tema.fuente(Font.BOLD, 40));
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(Tema.TEXTO);
                g2.drawString(iniciales, x + (TAM - fm.stringWidth(iniciales)) / 2, (TAM + fm.getAscent() - fm.getDescent()) / 2);
            }

            g2.setStroke(new BasicStroke(3f));
            g2.setColor(Tema.ACENTO);
            g2.draw(circulo);
            g2.dispose();
        }
    }
}
