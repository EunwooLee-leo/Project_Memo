package Memo;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JFontChooser  extends JDialog{
    private static Font selectedFont;
    private JComboBox<String> fontComboBox, styleComboBox;
    private JComboBox<Integer> sizeComboBox;
    private JButton okButton, cancelButton, colorButton;
    static Color selectedColor;
    JTextPane textPane;
    String fontName;
    int fontSize, fontStyle, styleIndex;


    JFontChooser(Frame parent, String title, Font defaultFont) {
        super(parent, title, true);
        selectedFont = defaultFont;
        selectedColor = Color.BLACK;

        // 글꼴 선택 콤보 박스
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fontNames);
        fontComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTextPane(); // 사용자가 콤보 박스에서 폰트 설정을 변경할 때마다 호출하여 textPane 업데이트
            }
        });

        // 크기 선택 콤보 박스
        Integer[] fontSizes = {8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 72};
        sizeComboBox = new JComboBox<>(fontSizes);
        sizeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTextPane();
            }
        });

        String[] styleNames = {"보통", "굵게", "이탤릭체"};
        styleComboBox = new JComboBox<>(styleNames);
        styleComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTextPane();
            }
        });


        // OK 버튼
        okButton = new JButton("확인");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFont != null) {
                    int start = memo.text.getSelectionStart();
                    int end = memo.text.getSelectionEnd();

                    if (start != end) {
                        // 커서가 선택한 부분에만 폰트를 적용
                        MutableAttributeSet attrs = new SimpleAttributeSet();
                        StyleConstants.setFontFamily(attrs, selectedFont.getFamily());
                        StyleConstants.setFontSize(attrs, selectedFont.getSize());
                        StyleConstants.setBold(attrs, (selectedFont.getStyle() & Font.BOLD) != 0);
                        StyleConstants.setItalic(attrs, (selectedFont.getStyle() & Font.ITALIC) != 0);
                        StyleConstants.setForeground(attrs, JFontChooser.selectedColor);

                        StyledDocument doc = memo.text.getStyledDocument();
                        doc.setCharacterAttributes(start, end - start, attrs, false);

                    }
                }
               dispose(); // 다이얼로그 창 닫기
            }
        });

        // 취소 버튼
        cancelButton = new JButton("취소");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedFont = null;
                dispose(); // 다이얼로그 창 닫기
            }
        });

        colorButton = new JButton("색상");
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 색상 선택 다이얼로그 열기
                selectedColor = JColorChooser.showDialog(JFontChooser.this, "색상 선택", selectedColor);
                updateTextPane();
            }
        });

        // 레이아웃 설정
        this.setTitle("글꼴 선택");
        this.setSize(new Dimension(400, 450));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(null);

        JLabel label = new JLabel("글꼴");
        label.setBounds(50, 22, 50, 25);
        fontComboBox.setBounds(100, 22, 150, 25);
        okButton.setBounds(270, 22, 80, 25);
        this.add(label);
        this.add(fontComboBox);
        this.add(okButton);

        JLabel label2 = new JLabel("글크기");
        label2.setBounds(40, 80, 50, 25);
        sizeComboBox.setBounds(100, 80, 150, 25);
        cancelButton.setBounds(270, 80, 80, 25);
        this.add(label2);
        this.add(sizeComboBox);
        this.add(cancelButton);

        JLabel label3 = new JLabel("글속성");
        label3.setBounds(40, 138, 50, 25);
        styleComboBox.setBounds(100, 138, 150, 25);
        colorButton.setBounds(270, 138, 80, 25);
        this.add(label3);
        this.add(styleComboBox);
        this.add(colorButton);

        textPane = new JTextPane();
        textPane.setBounds(40, 200, 300, 150);
        this.add(textPane);

        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void updateTextPane() {
        String exampleText = memo.text.getText();
        fontName = (String) fontComboBox.getSelectedItem();
        fontSize = (Integer) sizeComboBox.getSelectedItem();
        fontStyle = Font.PLAIN;
        styleIndex = styleComboBox.getSelectedIndex();

        if (styleIndex == 0) {
            fontStyle = Font.PLAIN;
        } else if (styleIndex == 1) {
            fontStyle = Font.BOLD;
        } else if (styleIndex == 2) {
            fontStyle = Font.ITALIC;
        }

        Font fontEx = new Font(fontName, fontStyle, fontSize);
        selectedFont = fontEx;

        textPane.setFont(fontEx);
        textPane.setForeground(selectedColor);

        // 예제 텍스트 업데이트
        textPane.setText(exampleText);
    }

    public static Font showDialog(Frame parent, String title, Font defaultFont) {
        JFontChooser fontChooser = new JFontChooser(parent, title, defaultFont);
        return selectedFont;
    }
}
