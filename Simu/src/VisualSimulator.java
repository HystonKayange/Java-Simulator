import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class VisualSimulator extends JFrame {

    // For safe execution, use the Event Dispatcher Thread
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VisualSimulator frame = new VisualSimulator();


                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public VisualSimulator() throws IOException {
        this.setTitle("SIX/XE Simulator");
        JPanel pn = new JPanel();
        setUIFont(new javax.swing.plaf.FontUIResource("Dialog", Font.PLAIN, 12));

        JPanel[] pns = new JPanel[BUTTON_SIZE];
        GridBagConstraints[] gbc = new GridBagConstraints[BUTTON_SIZE];

        GridBagLayout gbl = new GridBagLayout();
        pn.setLayout(gbl);

        for (int i = 0; i < BUTTON_SIZE; i++) {
            pns[i] = new JPanel();
            gbc[i] = new GridBagConstraints();
        }

        gbc[0].gridx = 0;
        gbc[0].gridy = 0;
        gbc[0].weightx = 2;
        gbc[0].weighty = 1;
        gbc[0].fill = GridBagConstraints.BOTH;
        pns[0].setBackground(Color.white);
        pn.add(pns[0], gbc[0]);

        gbc[1].gridx = 0;
        gbc[1].gridy = 1;
        gbc[1].weightx = 2;
        gbc[1].weighty = 24;
        gbc[1].fill = GridBagConstraints.BOTH;
        pns[1] = new JPanel(new GridLayout(1, 1));
        pns[1].setBackground(Color.cyan);

        pn.add(pns[1], gbc[1]);

        gbc[2].gridx = 0;
        gbc[2].gridy = 2;
        gbc[2].weightx = 2;
        gbc[2].weighty = 16;
        gbc[2].fill = GridBagConstraints.BOTH;
        pns[2].setBackground(Color.white);
        pn.add(pns[2], gbc[2]);

        pns[3].setBackground(Color.white);
        pns[1].add(pns[3]);

        pns[4].setBackground(Color.white);
        pns[1].add(pns[4]);

        // File name: Open section
        fileNameField = new JTextField(12);
        fileNameField.setEditable(false);
        JButton openButton = new JButton("Open");
        JLabel fileName = new JLabel("File Name: ");
        openButton.setPreferredSize(new Dimension(80, 23));
        openButton.addActionListener(new fileOpenListener());

        pns[0].setPreferredSize(new Dimension(200, 20));
        pns[0].setMaximumSize(new Dimension(200, 40));
        pns[0].setBorder(new EmptyBorder(10, 0, 0, 0));
        pns[0].setBounds(0, 0, 0, 0);
        pns[0].add(fileName);
        pns[0].add(fileNameField);
        pns[0].add(openButton);

        // H (Header Record) section
        JPanel headerRecordPanel = new JPanel(new GridLayout(3, 2));
        headerRecordPanel.setBorder(BorderFactory.createTitledBorder("H (Header Record)"));
        headerRecordPanel.setBackground(Color.white);

        headerRecordPanel.add(new JLabel("Program name:"));
        programNameField = new JTextField();
        programNameField.setEditable(false);
        headerRecordPanel.add(programNameField);

        String s = "Start Address of Object Program:";
        headerRecordPanel.add(new JLabel("<html><body style='width: 70px'>" + s + "</body></html>"));
        startAddressField = new JTextField();
        startAddressField.setEditable(false);
        headerRecordPanel.add(startAddressField);

        headerRecordPanel.add(new JLabel("Length of Program: "));
        lengthOfProgramField = new JTextField();
        lengthOfProgramField.setEditable(false);
        headerRecordPanel.add(lengthOfProgramField);

        pns[3].add(headerRecordPanel);

        // Register section
        JPanel registerRecordPanel = new JPanel(new GridLayout(10, 3));
        registerRecordPanel.setBorder(BorderFactory.createTitledBorder("Register"));
        registerRecordPanel.setBackground(Color.white);

        aRegisterDec = new JTextField();
        xRegisterDec = new JTextField();
        lRegisterDec = new JTextField();
        bRegisterDec = new JTextField();
        sRegisterDec = new JTextField();
        tRegisterDec = new JTextField();
        fRegisterDec = new JTextField();
        pcRegisterDec = new JTextField();
        swRegisterDec = new JTextField();

        aRegisterHex = new JTextField();
        xRegisterHex = new JTextField();
        lRegisterHex = new JTextField();
        bRegisterHex = new JTextField();
        sRegisterHex = new JTextField();
        tRegisterHex = new JTextField();
        fRegisterHex = new JTextField();
        pcRegisterHex = new JTextField();
        swRegisterHex = new JTextField();

        aRegisterDec.setEditable(false);
        xRegisterDec.setEditable(false);
        lRegisterDec.setEditable(false);
        bRegisterDec.setEditable(false);
        sRegisterDec.setEditable(false);
        tRegisterDec.setEditable(false);
        fRegisterDec.setEditable(false);
        pcRegisterDec.setEditable(false);
        swRegisterDec.setEditable(false);

        aRegisterHex.setEditable(false);
        xRegisterHex.setEditable(false);
        lRegisterHex.setEditable(false);
        bRegisterHex.setEditable(false);
        sRegisterHex.setEditable(false);
        tRegisterHex.setEditable(false);
        fRegisterHex.setEditable(false);
        pcRegisterHex.setEditable(false);
        swRegisterHex.setEditable(false);

        aRegisterDec.setPreferredSize(new Dimension(69, 24));
        fRegisterDec.setEnabled(false);
        swRegisterDec.setEnabled(false);

        registerRecordPanel.add(new JLabel(" "));
        registerRecordPanel.add(new JLabel("Dec"));
        registerRecordPanel.add(new JLabel("Hex"));

        registerRecordPanel.add(new JLabel("A(#0)"));
        registerRecordPanel.add(aRegisterDec);
        registerRecordPanel.add(aRegisterHex);

        registerRecordPanel.add(new JLabel("X(#1)"));
        registerRecordPanel.add(xRegisterDec);
        registerRecordPanel.add(xRegisterHex);

        registerRecordPanel.add(new JLabel("L(#2)"));
        registerRecordPanel.add(lRegisterDec);
        registerRecordPanel.add(lRegisterHex);

        registerRecordPanel.add(new JLabel("B(#3)"));
        registerRecordPanel.add(bRegisterDec);
        registerRecordPanel.add(bRegisterHex);

        registerRecordPanel.add(new JLabel("S(#4)"));
        registerRecordPanel.add(sRegisterDec);
        registerRecordPanel.add(sRegisterHex);

        registerRecordPanel.add(new JLabel("T(#5)"));
        registerRecordPanel.add(tRegisterDec);
        registerRecordPanel.add(tRegisterHex);

        registerRecordPanel.add(new JLabel("F(#6)"));
        registerRecordPanel.add(fRegisterDec);
        registerRecordPanel.add(fRegisterHex);

        registerRecordPanel.add(new JLabel("PC(#8)"));
        registerRecordPanel.add(pcRegisterDec);
        registerRecordPanel.add(pcRegisterHex);

        registerRecordPanel.add(new JLabel("SW(#9)"));
        registerRecordPanel.add(swRegisterDec);
        registerRecordPanel.add(swRegisterHex);

        pns[3].add(registerRecordPanel);

        // E (End Record) section
        JPanel endRecordPanel = new JPanel(new GridLayout(2, 2));
        endRecordPanel.setBorder(BorderFactory.createTitledBorder("E (End Record)"));
        endRecordPanel.setBackground(Color.white);

        endRecordPanel.add(new JLabel(" Address  of   First"));
        endRecordPanel.add(new JLabel("instruction"));

        endRecordPanel.add(new JLabel(" in Object Program: "));
        endRecordAddress = new JTextField();
        endRecordAddress.setEditable(false);
        endRecordAddress.setPreferredSize(new Dimension(20, 20));
        endRecordPanel.add(endRecordAddress);
        pns[4].add(endRecordPanel);

        // Start Address in Memory section
        JPanel startAddressInMemory = new JPanel(new GridLayout(1, 2));
        startAddressInMemory.setBorder(BorderFactory.createTitledBorder("Start Address in Memory"));
        startAddressInMemory.setBackground(Color.white);

        startAddressInMemory.add(new JLabel(" "));
        startMemoryField = new JTextField();
        startMemoryField.setEditable(false);
        startMemoryField.setPreferredSize(new Dimension(103, 20));
        startAddressInMemory.add(startMemoryField);
        pns[4].add(startAddressInMemory);

        // Target Address section
        JPanel targetAddress = new JPanel(new GridLayout(1, 2));
        targetAddress.setBorder(BorderFactory.createTitledBorder("Target Address"));
        targetAddress.setBackground(Color.white);

        targetAddress.add(new JLabel(" "));
        targetAddressField = new JTextField();
        targetAddressField.setEditable(false);
        targetAddressField.setPreferredSize(new Dimension(103, 20));
        targetAddress.add(targetAddressField);
        pns[4].add(targetAddress);

        // Instruction Table section
        JPanel instructionTable = new JPanel(new GridLayout(1, 2));
        instructionTable.setBorder(new EmptyBorder(0, 0, 0, 0));
        instructionTable.setPreferredSize(new Dimension(400, 300));
        instructionTable.setBackground(Color.white);

        JPanel instTable = new JPanel();
        instTable.setBackground(Color.white);
        instTable.setBorder(new EmptyBorder(0, 100, 0, 10));
        instTable.setPreferredSize(new Dimension(80, 700));
        instLog = new JTextArea(16, 11);
        JScrollPane instScroll = new JScrollPane(instLog,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        instScroll.setPreferredSize(new Dimension(100, 200));
        instTable.add(new JLabel("Instructions:"));
        instTable.add(instScroll);

        instructionTable.add(instTable);

        JPanel instructionButtons = new JPanel();
        Dimension d = new Dimension(120, 100);
        instructionButtons.setMinimumSize(d);
        instructionButtons.setPreferredSize(d);
        instructionButtons.setMaximumSize(d);
        instructionButtons.setLayout(new BoxLayout(instructionButtons, BoxLayout.Y_AXIS));
        instructionButtons.setBackground(Color.white);
        instructionButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        usingDevice = new JTextField();
        usingDevice.setEditable(false);
        usingDevice.setMaximumSize(new Dimension(200, 25));
        usingDevice.setBackground(Color.white);
        usingDevice.setAlignmentX(Component.CENTER_ALIGNMENT);

        oneStep = new JButton("Execute (1 step)");
        oneStep.setPreferredSize(new Dimension(100, 25));
        oneStep.setMaximumSize(new Dimension(100, 25));
        oneStep.setMinimumSize(new Dimension(100, 25));
        allStep = new JButton("Execute (all)");
        allStep.setPreferredSize(new Dimension(100, 25));
        allStep.setMaximumSize(new Dimension(100, 25));
        allStep.setMinimumSize(new Dimension(100, 25));
        JButton exitStep = new JButton("Exit");
        exitStep.setPreferredSize(new Dimension(100, 25));
        exitStep.setMaximumSize(new Dimension(100, 25));
        exitStep.setMinimumSize(new Dimension(100, 25));

        oneStepListener = new oneStepListener();
        allStepListener = new allStepListener();
        oneStep.addMouseListener(oneStepListener);
        allStep.addMouseListener(allStepListener);
        exitStep.addMouseListener(new exitListener());
        oneStep.setEnabled(false);
        allStep.setEnabled(false);

        instructionButtons.add(new JLabel("  "));
        instructionButtons.add(new JLabel("   Using Device"));
        instructionButtons.add(Box.createVerticalStrut(5));
        instructionButtons.add(usingDevice);
        instructionButtons.add(Box.createVerticalStrut(55));
        instructionButtons.add(oneStep);
        instructionButtons.add(Box.createVerticalStrut(10));
        instructionButtons.add(allStep);
        instructionButtons.add(Box.createVerticalStrut(10));
        instructionButtons.add(exitStep);

        instructionTable.add(instructionButtons);
        pns[4].add(instructionTable);

        // Log command execution section
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
        logPanel.setBackground(Color.white);

        JPanel lgPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lgPanel1.setBackground(Color.white);
        logPanel.add(new JLabel("LOG (Command Execution):"));

        JPanel lgPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lgPanel2.setBackground(Color.white);
        logMessage = new JTextArea(10, 45);
        JScrollPane logScrollPane = new JScrollPane(logMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        logScrollPane.setPreferredSize(new Dimension(400, 120));
        lgPanel2.add(logScrollPane);

        logPanel.add(lgPanel1);
        logPanel.add(lgPanel2);
        pns[2].add(logPanel);

        this.setContentPane(pn);

        this.setSize(450, 670);
        this.setVisible(true);
    }

    // Event listener for opening a file
    class fileOpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FileDialog fd = new FileDialog(VisualSimulator.this, e.getActionCommand(), FileDialog.LOAD);
            fd.setVisible(true);
            objectFile = new File(fd.getFile());
            fileNameField.setText(objectFile.getName());

            oneStep.setEnabled(true);
            allStep.setEnabled(true);
            try {
                sicxeSimulator = new SicXeSimulator(resourceManager, objectFile.getAbsolutePath());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            update();
        }
    }

    class exitListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.exit(0);
        }
    }

    class oneStepListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                sicxeSimulator.steper.next();
                sicxeSimulator.instExecutor.execute(sicxeSimulator.steper);
                update();
            } catch (Exception ignored) {
            }
        }
    }

    class allStepListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent arg0) {
            try {
                for (int i = 0; i < 10000; i++) {
                    sicxeSimulator.steper.next();
                    sicxeSimulator.instExecutor.execute(sicxeSimulator.steper);
                    update();

                    if (sicxeSimulator.instExecutor.endOfProgram)
                        break;
                }
            } catch (Exception ignored) {
            }
        }
    }

    // Method to set the default font using UIManager
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public void update() {
        programNameField.setText(resourceManager.programName);
        startAddressField.setText(resourceManager.programStartAddress);
        lengthOfProgramField.setText(resourceManager.programLength);
        endRecordAddress.setText(String.format("%06X", 0));
        startMemoryField.setText(Integer.toHexString(resourceManager.memoryStartAddress));
        targetAddressField.setText(String.format("%06X", resourceManager.targetAddress / 2));
        usingDevice.setText(resourceManager.usingDevice);
        if (!resourceManager.instLog.isEmpty()) {
            instLog.append(resourceManager.instLog + "\n");
            resourceManager.instLog = "";
        }
        if (!resourceManager.logMessage.isEmpty()) {
            logMessage.append(resourceManager.logMessage + "\n");
            resourceManager.logMessage = "";
        }

        int a = resourceManager.A;
        int x = resourceManager.X;
        int l = resourceManager.L;
        int pc = resourceManager.PC;
        int sw = resourceManager.SW;
        int b = resourceManager.B;
        int s = resourceManager.S;
        int t = resourceManager.T;
        int f = resourceManager.F;

        aRegisterDec.setText(String.format("%d", a));
        aRegisterHex.setText(String.format("%06X", a));
        xRegisterDec.setText(String.format("%d", x));
        xRegisterHex.setText(String.format("%06X", x));
        lRegisterDec.setText(String.format("%d", l / 2));
        lRegisterHex.setText(String.format("%06X", l / 2));
        pcRegisterDec.setText(String.format("%d", pc / 2));
        pcRegisterHex.setText(String.format("%06X", pc / 2));
        bRegisterDec.setText(String.format("%d", b));
        bRegisterHex.setText(String.format("%06X", b));
        sRegisterDec.setText(String.format("%d", s));
        sRegisterHex.setText(String.format("%06X", s));
        tRegisterDec.setText(String.format("%d", t));
        tRegisterHex.setText(String.format("%06X", t));
        fRegisterHex.setText(String.format("%06X", f));
        swRegisterHex.setText(String.format("%06X", sw));

        if (resourceManager.endOfProgram) {
            oneStep.setEnabled(false);
            oneStep.removeMouseListener(oneStepListener);
            allStep.setEnabled(false);
            allStep.removeMouseListener(allStepListener);
        }

        // scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    private final static int BUTTON_SIZE = 5;
    ResourceManager resourceManager = new ResourceManager();
    SicXeSimulator sicxeSimulator;

    JTextField fileNameField;

    JTextField programNameField;
    JTextField startAddressField;
    JTextField lengthOfProgramField;

    JTextField endRecordAddress;
    JTextField startMemoryField;
    JTextField targetAddressField;

    JTextField aRegisterDec;
    JTextField xRegisterDec;
    JTextField lRegisterDec;
    JTextField bRegisterDec;
    JTextField sRegisterDec;
    JTextField tRegisterDec;
    JTextField fRegisterDec;
    JTextField pcRegisterDec;
    JTextField swRegisterDec;

    JTextField aRegisterHex;
    JTextField xRegisterHex;
    JTextField lRegisterHex;
    JTextField bRegisterHex;
    JTextField sRegisterHex;
    JTextField tRegisterHex;
    JTextField fRegisterHex;
    JTextField pcRegisterHex;
    JTextField swRegisterHex;

    JTextArea instLog;
    JTextArea logMessage;

    JTextField usingDevice;
    File objectFile;

    JButton oneStep;
    JButton allStep;

    oneStepListener oneStepListener;
    allStepListener allStepListener;
}
