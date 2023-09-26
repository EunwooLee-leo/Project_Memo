import _20230925.JFontChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;


public class memo extends JFrame implements ActionListener {
    JMenuBar mb;
    JMenu fileMenu, fileEdit, form, help;
    JMenuItem fnew, fopen, fsave, fexit, fwindow, fprint, fz, fy, fx, fc, fv, fd, ff, fa, ft, ffont, cut, paste, copy, delete;
    Font font = new Font("serif", Font.BOLD, 20);
    JTextPane text;
    File fileToSave;
    String filePath;
    UndoManager undoManager = new UndoManager();


    private boolean hasUnsavedChanges = true;

    private void handleWindowClosing() throws IOException {
        if (hasUnsavedChanges) {
            int userChoice = JOptionPane.showConfirmDialog(
                    memo.this,
                    "변경 내용을 저장하시겠습니까?",
                    "저장 확인",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (userChoice == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("파일 저장");
                fileChooser.setFileFilter(new FileNameExtensionFilter("텍스트 파일 (*.txt)", "txt"));

                String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
                fileChooser.setCurrentDirectory(new File(desktopPath));

                int userSelection = fileChooser.showSaveDialog(memo.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    fileToSave = fileChooser.getSelectedFile();
                    filePath = fileToSave.getAbsolutePath();
                }
                String textToSave = text.getText();

                FileWriter fileWriter = new FileWriter(filePath);
                fileWriter.write(textToSave);
                fileWriter.close();
                JOptionPane.showMessageDialog(memo.this, "파일이 성공적으로 저장되었습니다.");
                dispose();

            } else if (userChoice == JOptionPane.NO_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }


    memo() {
        setTitle("메모장");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    handleWindowClosing();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        makeMenu();
        subMenu();
        event();
        textPane();
        popUp();

        setSize(550, 600);

        setLocationRelativeTo(null);

        setVisible(true);
    }

    void makeMenu() {
        mb = new JMenuBar();
        setJMenuBar(mb);

        fileMenu = new JMenu("파일(F)");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setMnemonic('F');

        fileEdit = new JMenu("편집(E)");
        fileEdit.setMnemonic(KeyEvent.VK_F);
        fileEdit.setMnemonic('E');

        form = new JMenu("서식(Q)");
        form.setMnemonic(KeyEvent.VK_F);
        form.setMnemonic('Q');

        help = new JMenu("도움말(H)");
        help.setMnemonic(KeyEvent.VK_F);
        help.setMnemonic('H');

        mb.add(fileMenu);
        mb.add(fileEdit);
        mb.add(form);
        mb.add(help);

    }

    void subMenu() {
        fnew = new JMenuItem("새로 만들기(N)");
        fnew.setAccelerator(KeyStroke.getKeyStroke('N', Event.CTRL_MASK));
        fwindow = new JMenuItem("새창 (W)");
        fwindow.setAccelerator(KeyStroke.getKeyStroke('W', Event.CTRL_MASK));
        fopen = new JMenuItem("열기 (O)");
        fopen.setAccelerator(KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
        fsave = new JMenuItem("저장(S)");
        fsave.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
        fprint = new JMenuItem("인쇄(P)");
        fprint.setAccelerator(KeyStroke.getKeyStroke('P', Event.CTRL_MASK));
        fexit = new JMenuItem("끝내기 (X)");
        fexit.setAccelerator(KeyStroke.getKeyStroke('X', Event.CTRL_MASK));

        fileMenu.add(fnew);
        fileMenu.add(fwindow);
        fileMenu.add(fopen);
        fileMenu.add(fsave);
        fileMenu.addSeparator();
        fileMenu.add(fprint);
        fileMenu.addSeparator();
        fileMenu.add(fexit);

        fz = new JMenuItem("실행 취소(Z)");
        fz.setAccelerator(KeyStroke.getKeyStroke('Z', Event.CTRL_MASK));
        fy = new JMenuItem("다시 실행 (Y)");
        fy.setAccelerator(KeyStroke.getKeyStroke('Y', Event.CTRL_MASK));
        fx = new JMenuItem("잘라내기 (X)");
        fx.setAccelerator(KeyStroke.getKeyStroke('X', Event.CTRL_MASK));
        fc = new JMenuItem("복사 (C)");
        fc.setAccelerator(KeyStroke.getKeyStroke('C', Event.CTRL_MASK));
        fv = new JMenuItem("붙여넣기 (V)");
        fv.setAccelerator(KeyStroke.getKeyStroke('V', Event.CTRL_MASK));
        fd = new JMenuItem("삭제 (D)");
        fd.setAccelerator(KeyStroke.getKeyStroke('D', Event.CTRL_MASK));
        ff = new JMenuItem("찾기 (F)");
        ff.setAccelerator(KeyStroke.getKeyStroke('F', Event.CTRL_MASK));
        fa = new JMenuItem("모두선택 (A)");
        fa.setAccelerator(KeyStroke.getKeyStroke('A', Event.CTRL_MASK));
        ft = new JMenuItem("시간/날짜 (T)");
        ft.setAccelerator(KeyStroke.getKeyStroke('T', Event.CTRL_MASK));

        fileEdit.add(fz);
        fileEdit.add(fy);
        fileEdit.add(fx);
        fileEdit.add(fc);
        fileEdit.addSeparator();
        fileEdit.add(fv);
        fileEdit.add(fd);
        fileEdit.add(ff);
        fileEdit.addSeparator();
        fileEdit.add(fa);
        fileEdit.add(ft);

        ffont = new JMenuItem("글꼴 (F)");
        ffont.setAccelerator(KeyStroke.getKeyStroke('F', Event.CTRL_MASK));

        form.add(ffont);

    }

    void event() {

        fnew.addActionListener(this);
        fwindow.addActionListener(this);
        fopen.addActionListener(this);
        fsave.addActionListener(this);
        fprint.addActionListener(this);
        fexit.addActionListener(this);
        fc.addActionListener(this);
        fx.addActionListener(this);
        fv.addActionListener(this);
        fd.addActionListener(this);
        fy.addActionListener(this);
        fd.addActionListener(this);
        ff.addActionListener(this);
        ft.addActionListener(this);
        fa.addActionListener(this);
        ffont.addActionListener(this);
        fz.addActionListener(this);

    }

    void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("파일 저장");
        fileChooser.setFileFilter(new FileNameExtensionFilter("텍스트 파일 (*.txt)", "txt"));

        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        fileChooser.setCurrentDirectory(new File(desktopPath));

        int userSelection = fileChooser.showSaveDialog(memo.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
            filePath = fileToSave.getAbsolutePath();
        }
        String textToSave = text.getText();

        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(textToSave);
            fileWriter.close();
            JOptionPane.showMessageDialog(memo.this, "파일이 성공적으로 저장되었습니다.");
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(memo.this, "파일 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fnew) {
            int choice = JOptionPane.showConfirmDialog(memo.this, "저장하시겠습니까?", "저장 여부", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                saveFile();
            }

            new memo();

        } else if (e.getSource() == fexit) {
            System.exit(0);

        } else if (e.getSource() == fwindow) {
            int choice = JOptionPane.showConfirmDialog(memo.this, "저장하시겠습니까?", "저장 여부", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                saveFile();
            }

            new memo();

        } else if (e.getSource() == fopen) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("파일 열기");

            String desktopPath = "\"C:\\Users\\leo_m\\OneDrive\\바탕 화면\\";
            ;
            fileChooser.setCurrentDirectory(new File(desktopPath));

            int userSelection = fileChooser.showOpenDialog(memo.this);


            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();

                try {
                    FileReader fileReader = new FileReader(filePath);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    StringBuilder fileContent = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }

                    text.setText(fileContent.toString());

                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(memo.this, "파일을 열 때 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == fsave) {

            saveFile();

        } else if (e.getSource() == fprint) {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = printerJob.defaultPage();
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            PageFormat selectedPageFormat = printerJob.pageDialog(pageFormat);
            if (selectedPageFormat == null) {
                return;
            }

            if (printerJob.printDialog()) {
                printerJob.setPrintable(new Printable() {
                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        if (pageIndex > 0) {
                            return Printable.NO_SUCH_PAGE;
                        }
                        Graphics2D g2d = (Graphics2D) graphics;
                        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                        text.print(g2d);

                        return Printable.PAGE_EXISTS;
                    }
                }, pageFormat);

                try {
                    printerJob.print();
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(memo.this, "인쇄 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (e.getSource() == fexit) {
            try {
                handleWindowClosing();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } else if (e.getSource() == fc || e.getSource() == copy) {
            text.copy();

        } else if (e.getSource() == fx || e.getSource() == cut) {
            text.cut();

        } else if (e.getSource() == fv || e.getSource() == paste) {
            text.paste();

        } else if (e.getSource() == fd || e.getSource() == delete) {
            int start = text.getSelectionStart();
            int end = text.getSelectionEnd();
            if (start != end) {
                DefaultStyledDocument doc = (DefaultStyledDocument) text.getDocument();
                try {
                    doc.remove(start, end - start);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == ffont) {

            Font selectedFont = JFontChooser.showDialog(memo.this, "글꼴 선택", text.getFont());

            if (selectedFont != null) {
                text.setFont(selectedFont);
            }

        } else if (e.getSource() == fz) {
            undoManager.undo();

        } else if (e.getSource() == fy) {
            undoManager.redo();

        } else if (e.getSource() == ff) {
            String searchText = JOptionPane.showInputDialog(memo.this, "찾을 문자열을 입력하세요:");
            if (searchText != null && !searchText.isEmpty()) {
                String textContent = text.getText();
                int startIndex = textContent.indexOf(searchText);
                if (startIndex != -1) {
                    text.setSelectionStart(startIndex);
                    text.setSelectionEnd(startIndex + searchText.length());
                } else {
                    JOptionPane.showMessageDialog(memo.this, "문자열을 찾을 수 없습니다.", "찾기", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } else if (e.getSource() == fa) {
            text.selectAll();

        } else if (e.getSource() == ft) {
            java.util.Date currentDate = new java.util.Date();
            String dateTimeString = currentDate.toString();

            text.setText(text.getText() + dateTimeString);
        }
    }

    void popUp() {
        JPopupMenu popUpMenu = new JPopupMenu();
        copy = new JMenuItem("복사 (C)");
        cut = new JMenuItem("잘라내기 (X)");
        paste = new JMenuItem("붙여넣기 (V)");
        delete = new JMenuItem("지우기 (D)");
        delete.setEnabled(false);

        copy.addActionListener(this);
        cut.addActionListener(this);
        paste.addActionListener(this);
        delete.addActionListener(this);

        text.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 3) {
                    popUpMenu.show(text, e.getX(), e.getY());
                }
                if (text.getSelectedText() == null) {
                    delete.setEnabled(false);
                } else {
                    delete.setEnabled(true);
                }
            }
        });

        popUpMenu.add(copy);
        popUpMenu.add(cut);
        popUpMenu.addSeparator();
        popUpMenu.add(paste);
        popUpMenu.add(delete);

        text.add(popUpMenu);
    }

    void textPane() {
        text = new JTextPane();
        JScrollPane scroll = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scroll);

        text.setFont(font);
        text.setSelectedTextColor(Color.white);
        text.setSelectionColor(new Color(135, 206, 235));
        text.setToolTipText("이곳에 텍스트를 입력하세요");
        text.getCaret().setBlinkRate(300);
        text.getCaret().setSelectionVisible(true);

        text.getDocument().addUndoableEditListener(undoManager);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new memo();
            }
        });
    }
}
