import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JFontChooser extends JDialog {
    private Font selectedFont;
    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> sizeComboBox;
    private JButton okButton;
    private JButton cancelButton;

    private JFontChooser(Frame parent, String title, Font defaultFont) {
        super(parent, title, true);
        selectedFont = defaultFont;

        // 글꼴 선택 콤보 박스
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fontNames);

        // 크기 선택 콤보 박스
        Integer[] fontSizes = { 8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 72 };
        sizeComboBox = new JComboBox<>(fontSizes);

        // OK 버튼
        okButton = new JButton("확인");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fontName = (String) fontComboBox.getSelectedItem();
                int fontSize = (Integer) sizeComboBox.getSelectedItem();
                selectedFont = new Font(fontName, Font.PLAIN, fontSize);
                dispose();
            }
        });

        // 취소 버튼
        cancelButton = new JButton("취소");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedFont = null;
                dispose();
            }
        });

        // 레이아웃 설정
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("글꼴:"));
        panel.add(fontComboBox);
        panel.add(new JLabel("크기:"));
        panel.add(sizeComboBox);
        panel.add(okButton);
        panel.add(cancelButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public static Font showDialog(Frame parent, String title, Font defaultFont) {
        JFontChooser fontChooser = new JFontChooser(parent, title, defaultFont);
        fontChooser.setVisible(true);
        return fontChooser.selectedFont;
    }
}
