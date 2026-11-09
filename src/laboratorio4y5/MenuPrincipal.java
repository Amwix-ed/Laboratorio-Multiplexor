/*
 * Creador: Amwix
 */
package laboratorio4y5;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import laboratorio4y5.Tema.Boton;

public final class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Laboratorio 4 y 5 - MUX 8 a 1");
        setSize(460, 470);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        crearGUI();
        setVisible(true);
    }

    public void crearGUI(){
        JPanel raiz = Tema.prepararVentana(this);
        raiz.add(Tema.encabezado("Centro de Monitoreo", "Laboratorio 4 y 5  ·  Multiplexor 8 a 1"), BorderLayout.NORTH);

        Boton btnInfo = new Boton("Información del sistema", Boton.Estilo.SECUNDARIO)
                .conDescripcion("Cómo funciona el MUX y su tabla de verdad");

        Boton btnSimulador = new Boton("Simulación MUX 8 a 1", Boton.Estilo.SECUNDARIO)
                .conDescripcion("Selecciona una de las 8 cámaras con C2 C1 C0");

        Boton btnCreador = new Boton("Creador", Boton.Estilo.SECUNDARIO)
                .conDescripcion("Integrantes del equipo de desarrollo");

        JPanel botones = new JPanel(new GridLayout(3, 1, 0, 14));
        botones.setOpaque(false);
        botones.setBorder(new EmptyBorder(26, 30, 10, 30));
        botones.add(btnInfo);
        botones.add(btnSimulador);
        botones.add(btnCreador);
        raiz.add(botones, BorderLayout.CENTER);

        JLabel pie = Tema.etiqueta("Universidad del Valle", Font.PLAIN, 11, Tema.TEXTO_SUAVE);
        pie.setHorizontalAlignment(SwingConstants.CENTER);
        pie.setBorder(new EmptyBorder(8, 0, 16, 0));
        raiz.add(pie, BorderLayout.SOUTH);

        btnInfo.addActionListener((e) -> {
            setVisible(false); // ocultar la ventana Principal
            new Informacion(this); // llamar a la ventana de Cliente
        });
        btnSimulador.addActionListener((e) -> {
            setVisible(false);
            new MUX(this);
        });
        btnCreador.addActionListener((e) -> {
            setVisible(false);
            new Integrantes(this);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPrincipal::new);
    }
}
