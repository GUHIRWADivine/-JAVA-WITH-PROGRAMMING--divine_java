package com.fams;

import com.fams.panels.*;
import com.fams.model.AccountHolder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainApp extends JFrame {

    private JTabbedPane tabs;
    private AccountHolder currentUser;

    public MainApp(AccountHolder user) {
        this.currentUser = user;

        initializeLookAndFeel();
        initializeFrame();
        createDashboard();
    }

    private void initializeLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
    }

    private void initializeFrame() {
        setTitle("Finance Analytics Management System - Divine Edition");
        setSize(1420, 880);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            setIconImage(new ImageIcon(getClass().getResource("/icons/bank.png")).getImage());
        } catch (Exception e) {}
    }

    private void createDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 35, 90),
                        0, getHeight(), new Color(5, 10, 40));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        JLabel welcome = new JLabel("Welcome back, " + currentUser.getFullName() + "!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 40));
        welcome.setForeground(Color.CYAN);

        JLabel roleLabel = new JLabel("Role: " + currentUser.getRole().toUpperCase());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        roleLabel.setForeground(Color.ORANGE);

        JLabel systemTitle = new JLabel("DIVINE FAMS");
        systemTitle.setFont(new Font("Arial Black", Font.BOLD, 38));
        systemTitle.setForeground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);
        left.add(welcome);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(roleLabel);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.add(systemTitle);

        header.add(left, BorderLayout.WEST);
        header.add(center, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // Tabs
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tabs.setForeground(Color.WHITE);
        tabs.setBackground(new Color(30, 40, 80));
        tabs.setTabPlacement(JTabbedPane.TOP);

        addAllTabsCorrectly();  // FIXED: All panels get currentUser

        mainPanel.add(tabs, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("© 2025 Divine Finance Analytics Management System | User ID: " 
            + currentUser.getAccountHolderID() + " | " + currentUser.getFullName(), JLabel.CENTER);
        footer.setForeground(Color.LIGHT_GRAY);
        footer.setFont(new Font("Tahoma", Font.ITALIC, 14));
        footer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
        setJMenuBar(createMenuBar());
    }

    private void addAllTabsCorrectly() {
        ImageIcon accIcon     = loadIcon("/icons/account.png");
        ImageIcon transIcon   = loadIcon("/icons/transaction.png");
        ImageIcon loanIcon    = loadIcon("/icons/loan.png");
        ImageIcon branchIcon  = loadIcon("/icons/branch.png");
        ImageIcon cardIcon    = loadIcon("/icons/card.png");

        String role = currentUser.getRole();

        if ("Admin".equalsIgnoreCase(role)) {
            tabs.addTab(" Accounts",     accIcon,     new AccountPanel(currentUser));
            tabs.addTab(" Transactions", transIcon,   new TransactionPanel(currentUser));
            tabs.addTab(" Loans",        loanIcon,    new LoanPanel(currentUser));
            tabs.addTab(" Branches",     branchIcon,  new BranchPanel());
            tabs.addTab(" Cards",        cardIcon,    new CardPanel(currentUser));     // FIXED: Pass currentUser
        }
        else if ("Manager".equalsIgnoreCase(role)) {
            tabs.addTab(" Accounts",     accIcon,     new AccountPanel(currentUser));
            tabs.addTab(" Transactions", transIcon,   new TransactionPanel(currentUser));
            tabs.addTab(" Loans",        loanIcon,    new LoanPanel(currentUser));
        }
        else if ("User".equalsIgnoreCase(role)) {
            tabs.addTab(" My Accounts",     accIcon,   new AccountPanel(currentUser));
            tabs.addTab(" Transactions",    transIcon, new TransactionPanel(currentUser));
            tabs.addTab(" My Loans",        loanIcon,  new LoanPanel(currentUser));
            tabs.addTab(" My Cards",        cardIcon,  new CardPanel(currentUser));     // FIXED: Pass currentUser
        }
        else {
            tabs.addTab(" Dashboard", loadIcon("/icons/home.png"), new JPanel(), "Welcome");
        }
    }

    private ImageIcon loadIcon(String path) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url != null) return new ImageIcon(url);
        } catch (Exception e) {}
        return null;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(30, 40, 70));
        menuBar.setBorder(BorderFactory.createLineBorder(new Color(60, 80, 120), 4));

        JMenu menu = new JMenu("Menu");
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 16));

        for (int i = 0; i < tabs.getTabCount(); i++) {
            final int index = i;
            JMenuItem item = new JMenuItem(tabs.getTitleAt(i));
            item.setForeground(Color.WHITE);
            item.setBackground(new Color(30, 40, 70));
            item.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabs.setSelectedIndex(index);
                }
            });
            menu.add(item);
        }

        menu.addSeparator();

        JMenuItem logout = new JMenuItem("Logout");
        logout.setForeground(Color.RED);
        logout.setIcon(loadIcon("/icons/logout.png"));
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int c = JOptionPane.showConfirmDialog(MainApp.this,
                    "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginForm().setVisible(true);
                }
            }
        });
        menu.add(logout);

        JMenu help = new JMenu("Help");
        help.setForeground(Color.WHITE);
        help.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JMenuItem about = new JMenuItem("About Divine FAMS");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainApp.this,
                    "<html><center><h1>Divine Finance Analytics Management System</h1>" +
                    "<h2>Version 2.0 - Built with Love & Precision</h2>" +
                    "<p>© 2025 Divine Project</p></center></html>",
                    "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help.add(about);

        menuBar.add(menu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(help);

        return menuBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AccountHolder testUser = new AccountHolder();
                testUser.setAccountHolderID(1);
                testUser.setFullName("Divine Administrator");
                testUser.setRole("Admin");

                new MainApp(testUser).setVisible(true);
            }
        });
    }
}
