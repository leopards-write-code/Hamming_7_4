import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Graphics2D;
// NAME: Victor Li
// DATE: 5/12->5/21
// TEACHER: HULETT
// CLASS: CS AP A/ CS II K
public class myFrame extends JFrame implements ActionListener {
    private String userName;
    private JPanel controlPanel = new JPanel();
    private JPanel drawingPanel = new JPanel();
    private boolean inputGiven;
    private ArrayList<JButton>buttons;
    private JButton encodeHamming;
    private JButton decodeHamming;

    private final int parityOneCircleX = 350;
    private final int parityOneCircleY = 150;
    private JLabel p1 =new JLabel();
    private JLabel p2= new JLabel();
    private JLabel p3 = new JLabel();
    private JLabel d1 = new JLabel();
    private JLabel d2 = new JLabel();
    private JLabel d3 = new JLabel();
    private JLabel d4 = new JLabel();

    private ArrayList<JLabel>labels = new ArrayList<>();

    private final Dimension d = new Dimension(150, 150);
    private AlphaComposite composite;
    public myFrame() {
        setTitle("Designed by \uD83C\uDD86\uD83C\uDD7E\uD83C\uDD7D\uD83C\uDD73\uD83C\uDD74\uD83C\uDD81 \uD83C\uDD70\uD83C\uDD7F\uD83C\uDD7F\uD83C\uDD7B\uD83C\uDD78\uD83C\uDD72\uD83C\uDD70\uD83C\uDD83\uD83C\uDD78\uD83C\uDD7E\uD83C\uDD7D\uD83C\uDD82"+" 2025©");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //centers the frame
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) (.5*(screensize.width-getWidth())), (int)(.5*(screensize.height-getHeight())), getWidth(), getHeight() );
        setResizable(false);
        initGUI();
        pack();
        addLabels();
    }
    private void initGUI(){
        prepareGUI();
        inputGiven = false;
        addPanels();
        addButtons();
        addButtonListeners();
        displayWelcomeMessage();
    }
    private void addPanels(){
        drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.BLACK);
        drawingPanel.setPreferredSize(new Dimension(800, 600));
        controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setPreferredSize(new Dimension(800, 75));
        getContentPane().add(drawingPanel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
    }
    private void displayWelcomeMessage(){
        userName = JOptionPane.showInputDialog("What's your name?");
        if(userName.length()==0) userName = "John Doe";
        String message = String.format("Welcome, %s, to the physical representation for Hamming 7/4 code in Java", userName);
        JOptionPane.showMessageDialog(null, message, "Greeting",JOptionPane.INFORMATION_MESSAGE);

    }
    private void addLabels(){
        labels.add(p1);
        labels.add(p2);
        labels.add(p3);
        labels.add(d1);
        labels.add(d2);
        labels.add(d3);
        labels.add(d4);
        p1.setText("p1");
        p2.setText("p2");
        p3.setText("p3");
        d1.setText("d1");
        d2.setText("d2");
        d3.setText("d3");
        d4.setText("d4");
        for(JLabel l:labels){
            l.setBackground(Color.WHITE);
            l.setSize(100, 50);
        }

    }
    private void addButtons(){
        encodeHamming = new JButton("ENCODE");
        decodeHamming = new JButton("DECODE");
        buttons = new ArrayList<>();
        buttons.add(encodeHamming);
        buttons.add(decodeHamming);
        encodeHamming.setBackground(Color.RED);
        decodeHamming.setBackground(Color.GREEN);
        for(JButton b:buttons){
            controlPanel.add(b);
        }
    }
    private void addButtonListeners(){
        for(JButton b: buttons){
            for (ActionListener listener : b.getActionListeners()) {
                b.removeActionListener(listener);
            }
        }
        for (JButton b : buttons) {
            b.addActionListener(this);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
            JFileChooser fileSelector = new JFileChooser();
            fileSelector.setDialogTitle("Select File");
            int result = fileSelector.showOpenDialog(this);
            if(result==(JFileChooser.APPROVE_OPTION)){
                File f = fileSelector.getSelectedFile();
                String fileName = f.getName();
                if(!fileName.endsWith(".txt")){
                    JOptionPane.showConfirmDialog(null, "Sorry, you must select a text file.", "ERROR", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    try {
                        if(e.getSource()==encodeHamming){
                            Hamming.encode(fileName, "encodedResults.txt");
                            JOptionPane.showConfirmDialog(null, "The encoded text has been written to encodedResults.txt", "Encoded", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        }
                        if(e.getSource()==decodeHamming){
                            Hamming.decode(fileName, "decodedResults.txt");
                            JOptionPane.showConfirmDialog(null, "The decoded output has been saved to decoded results", "Decoded", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

    }
    private void prepareGUI(){
        float alpha = 0.65f;
        int type = AlphaComposite.SRC_OVER;
         composite = AlphaComposite.getInstance(type, alpha);
    }
    public static void main(String[] args){
        new myFrame();
    }
    private class DrawingPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GREEN);
            g2d.setComposite(composite);
            g2d.fillOval(parityOneCircleX, parityOneCircleY, d.width, d.height);

            g2d.setColor(Color.BLUE);
            g2d.fillOval(parityOneCircleX-(d.width*4/10), parityOneCircleY+(d.width/2), d.width, d.height);
        //    g2d.fillOval();
            g2d.setColor(Color.RED);
            g2d.fillOval(parityOneCircleX+(d.width*4/10), parityOneCircleY+(d.width/2), d.width, d.height);
        }
    }
}
