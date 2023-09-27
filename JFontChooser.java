package Memo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static java.awt.SystemColor.text;

public class JFontChooser extends JDialog {
    private Font selectedFont;
    private JComboBox<String> fontComboBox, styleComboBox;
    private JComboBox<Integer> sizeComboBox;
    private JButton okButton, cancelButton, colorButton;
    static Color selectedColor;

    private JFontChooser(Frame parent, String title, Font defaultFont) {
        super(parent, title, true);
        selectedFont = defaultFont;
        selectedColor = Color.BLACK;

        // 글꼴 선택 콤보 박스
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fontNames);

        // 크기 선택 콤보 박스
        Integer[] fontSizes = {8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 72};
        sizeComboBox = new JComboBox<>(fontSizes);

        String[] styleNames = {"보통", "굵게", "이탤릭체"};
        styleComboBox = new JComboBox<>(styleNames);

        // OK 버튼
        okButton = new JButton("확인");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fontName = (String) fontComboBox.getSelectedItem();
                int fontSize = (Integer) sizeComboBox.getSelectedItem();
                int fontStyle = Font.PLAIN;
                int styleIndex = styleComboBox.getSelectedIndex();
                if (styleIndex == 0) {
                    fontStyle = Font.PLAIN;
                } else if (styleIndex == 1) {
                    fontStyle = Font.BOLD;
                } else if (styleIndex == 2) {
                    fontStyle = Font.ITALIC;
                }
                selectedFont = new Font(fontName, fontStyle, fontSize);
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

        colorButton = new JButton("색상");
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 색상 선택 다이얼로그 열기
                selectedColor = JColorChooser.showDialog(JFontChooser.this, "색상 선택", selectedColor);
            }
        });

        // 레이아웃 설정
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 25));

        JLabel label = new JLabel("글꼴");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        panel.add(fontComboBox);
        panel.add(okButton);

        JLabel label2 = new JLabel("크기");
        label2.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label2);
        panel.add(sizeComboBox);
        panel.add(cancelButton);

        JLabel label3 = new JLabel("글속성");
        label3.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label3);
        panel.add(styleComboBox);
        panel.add(colorButton);

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
