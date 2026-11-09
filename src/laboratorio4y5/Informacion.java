/*
 * Creador: Amwix
 */
package laboratorio4y5;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import laboratorio4y5.Tema.Boton;

public final class Informacion extends JFrame {

    public Informacion(MenuPrincipal p) {
        setTitle("Información del Sistema");
        setSize(640, 620);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        crearGUI();
        setVisible(true);

    }

    public void crearGUI(){
        JPanel raiz = Tema.prepararVentana(this);
        raiz.add(Tema.encabezado("Sistema de monitoreo por cámaras",
                "Usando un multiplexor (MUX) 8 a 1"), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBorder(new EmptyBorder(18, 24, 0, 24));

        // ---- Descripción ----
        Tema.Tarjeta desc = new Tema.Tarjeta(new BorderLayout(), 16);
        JTextArea texto = new JTextArea(
                "Este sistema simula un centro de vigilancia con 8 cámaras. "
                + "El MUX 8:1 permite seleccionar una sola cámara según las líneas "
                + "de control C2, C1 y C0.");
        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setOpaque(false);
        texto.setBorder(null);
        texto.setFont(Tema.fuente(Font.PLAIN, 14));
        texto.setForeground(Tema.TEXTO);
        desc.add(texto);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // ---- Tabla del MUX ----
        JLabel tituloTabla = Tema.etiqueta("TABLA DEL MUX", Font.BOLD, 12, Tema.ACENTO);
        tituloTabla.setBorder(new EmptyBorder(18, 4, 8, 0));
        tituloTabla.setAlignmentX(Component.LEFT_ALIGNMENT);

        Tema.Tarjeta tabla = new Tema.Tarjeta(new GridLayout(9, 1), 16);
        tabla.setBorder(new EmptyBorder(6, 8, 6, 8));
        tabla.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabla.add(fila("C2", "C1", "C0", "Cámara", true, false));
        for (int i = 0; i < 8; i++) {
            tabla.add(fila(String.valueOf((i >> 2) & 1), String.valueOf((i >> 1) & 1),
                    String.valueOf(i & 1), "Cam " + i + "  ·  " + MUX.NOMBRES[i], false, i < 7));
        }

        cuerpo.add(desc);
        cuerpo.add(tituloTabla);
        cuerpo.add(tabla);
        raiz.add(cuerpo, BorderLayout.CENTER);

        // 🔙 BOTÓN ATRÁS
        Boton atras = new Boton("←  Atrás", Boton.Estilo.SECUNDARIO);
        atras.addActionListener(e -> {
            setVisible(false); // ocultar la ventana Cliente
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        raiz.add(Tema.barraInferior(atras), BorderLayout.SOUTH);
    }

    private JPanel fila(String c2, String c1, String c0, String camara, boolean cabecera, boolean separador) {
        JPanel f = new JPanel(new GridBagLayout());
        f.setOpaque(false);
        if (cabecera) {
            f.setBorder(new MatteBorder(0, 0, 1, 0, Tema.ACENTO_OSC));
        } else if (separador) {
            f.setBorder(new MatteBorder(0, 0, 1, 0, Tema.BORDE));
        }

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 10, 0, 10);

        String[] bits = {c2, c1, c0};
        for (int i = 0; i < 3; i++) {
            gc.gridx = i;
            gc.weightx = 0;
            JLabel b = new JLabel(bits[i], SwingConstants.CENTER);
            b.setPreferredSize(new Dimension(40, 24));
            if (cabecera) {
                b.setFont(Tema.fuente(Font.BOLD, 12));
                b.setForeground(Tema.TEXTO_SUAVE);
            } else {
                b.setFont(Tema.mono(Font.BOLD, 15));
                b.setForeground("1".equals(bits[i]) ? Tema.ACENTO : Tema.TEXTO_SUAVE);
            }
            f.add(b, gc);
        }

        gc.gridx = 3;
        gc.weightx = 1;
        JLabel cam = new JLabel(cabecera ? camara : "→   " + camara);
        cam.setFont(Tema.fuente(cabecera ? Font.BOLD : Font.PLAIN, cabecera ? 12 : 14));
        cam.setForeground(cabecera ? Tema.TEXTO_SUAVE : Tema.TEXTO);
        f.add(cam, gc);
        return f;
    }
}
