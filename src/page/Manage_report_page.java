/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package page;

import db.DatabaseConnection;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import main.LoginFrame;
import main.Main;

/**
 *
 * @author Reynald
 */
public class Manage_report_page extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Manage_report_page.class.getName());
    private static final String STOCK_STATUS_SQL = """
            SELECT
                SUM(CASE WHEN stock <= low_stock_threshold THEN 1 ELSE 0 END) AS low_stock_count,
                SUM(CASE WHEN stock > low_stock_threshold THEN 1 ELSE 0 END) AS healthy_stock_count
            FROM items
            WHERE is_active = 1
            """;
    private static final String CATEGORY_ITEMS_SQL = """
            SELECT
                category,
                COUNT(item_id) AS item_count
            FROM items
            WHERE is_active = 1
            GROUP BY category
            ORDER BY item_count DESC, category ASC
            LIMIT 6
            """;
    private static final String SUPPLIER_CONTACT_COVERAGE_SQL = """
            SELECT
                SUM(CASE WHEN TRIM(COALESCE(email, '')) <> '' THEN 1 ELSE 0 END) AS suppliers_with_email,
                SUM(CASE WHEN TRIM(COALESCE(email, '')) = '' THEN 1 ELSE 0 END) AS suppliers_without_email
            FROM suppliers
            WHERE is_active = 1
            """;
    private static final String SUPPLIER_CITY_SQL = """
            SELECT
                CASE
                    WHEN TRIM(COALESCE(address, '')) = '' THEN 'No Address'
                    WHEN LOCATE(',', address) > 0 THEN TRIM(SUBSTRING_INDEX(address, ',', 1))
                    ELSE TRIM(address)
                END AS city_name,
                COUNT(*) AS supplier_count
            FROM suppliers
            WHERE is_active = 1
            GROUP BY city_name
            ORDER BY supplier_count DESC, city_name ASC
            LIMIT 6
            """;
    private static final String REPORT_SUMMARY_SQL = """
            SELECT
                (SELECT COUNT(*) FROM items WHERE is_active = 1) AS total_items,
                (SELECT COUNT(*) FROM items WHERE is_active = 1 AND stock <= low_stock_threshold) AS low_stock_items,
                (SELECT COUNT(*) FROM suppliers WHERE is_active = 1) AS total_suppliers,
                (SELECT COUNT(*) FROM suppliers WHERE is_active = 1 AND TRIM(COALESCE(email, '')) <> '') AS suppliers_with_email,
                (SELECT COALESCE(category, 'Uncategorized')
                 FROM items
                 WHERE is_active = 1
                 GROUP BY category
                 ORDER BY COUNT(*) DESC, category ASC
                 LIMIT 1) AS top_category,
                (SELECT COUNT(*)
                 FROM items
                 WHERE is_active = 1
                 GROUP BY category
                 ORDER BY COUNT(*) DESC, category ASC
                 LIMIT 1) AS top_category_count,
                (SELECT CASE
                            WHEN TRIM(COALESCE(address, '')) = '' THEN 'No Address'
                            WHEN LOCATE(',', address) > 0 THEN TRIM(SUBSTRING_INDEX(address, ',', 1))
                            ELSE TRIM(address)
                        END
                 FROM suppliers
                 WHERE is_active = 1
                 GROUP BY CASE
                            WHEN TRIM(COALESCE(address, '')) = '' THEN 'No Address'
                            WHEN LOCATE(',', address) > 0 THEN TRIM(SUBSTRING_INDEX(address, ',', 1))
                            ELSE TRIM(address)
                          END
                 ORDER BY COUNT(*) DESC, 1 ASC
                 LIMIT 1) AS top_city,
                (SELECT COUNT(*)
                 FROM suppliers
                 WHERE is_active = 1
                 GROUP BY CASE
                            WHEN TRIM(COALESCE(address, '')) = '' THEN 'No Address'
                            WHEN LOCATE(',', address) > 0 THEN TRIM(SUBSTRING_INDEX(address, ',', 1))
                            ELSE TRIM(address)
                          END
                 ORDER BY COUNT(*) DESC, 1 ASC
                 LIMIT 1) AS top_city_count
            """;
    private final String loggedInUser;
    private final PieChartPanel stockPieChart = new PieChartPanel();
    private final BarChartPanel categoryBarChart = new BarChartPanel();
    private final PieChartPanel supplierPieChart = new PieChartPanel();
    private final BarChartPanel supplierBarChart = new BarChartPanel();

    /**
     * Creates new form Manage_report_page
     */
    public Manage_report_page() {
        this("User");
    }

    public Manage_report_page(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        initComponents();
        initializePage();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new panels.PanelBorder();
        sidePanel2 = new panels.SidePanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelBorder1.setBackground(new java.awt.Color(229, 229, 229));
        panelBorder1.setPreferredSize(new java.awt.Dimension(920, 640));

        sidePanel2.setBackground(new java.awt.Color(102, 102, 102));
        sidePanel2.setOpaque(true);
        sidePanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("User name");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        sidePanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, -1));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Dashboard");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel2)
                .addContainerGap(98, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        sidePanel2.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 66, 220, -1));

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Items");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel5)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        sidePanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 220, -1));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Logout");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        sidePanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 586, 180, -1));

        jPanel6.setBackground(new java.awt.Color(102, 102, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Suppliers");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel12)
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel12)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        sidePanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 220, -1));

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Low stocks");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel13)
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel13)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        sidePanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 220, -1));

        jPanel9.setBackground(new java.awt.Color(102, 102, 102));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Manage Report");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel14)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel14)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        sidePanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 220, -1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 192, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Manage Report");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Generate Report");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 227, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(sidePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addComponent(jButton1)))
                .addGap(51, 72, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(sidePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addGap(42, 42, 42)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initializePage() {
        jLabel1.setText(loggedInUser);
        setSize(980, 700);
        setMinimumSize(new Dimension(980, 700));
        setResizable(false);
        setLocationRelativeTo(null);

        configureAnalyticsPanels();
        configureNavigation();
        configureEvents();
        loadAnalytics();
    }

    private void configureAnalyticsPanels() {
        Dimension chartPanelSize = new Dimension(320, 220);

        jPanel4.setLayout(new BorderLayout());
        jPanel4.setBackground(Color.WHITE);
        jPanel4.setBorder(BorderFactory.createTitledBorder("Stock Status"));
        jPanel4.setPreferredSize(chartPanelSize);
        jPanel4.setMinimumSize(chartPanelSize);
        jPanel4.add(stockPieChart, BorderLayout.CENTER);

        jPanel7.setLayout(new BorderLayout());
        jPanel7.setBackground(Color.WHITE);
        jPanel7.setBorder(BorderFactory.createTitledBorder("Items Per Category"));
        jPanel7.setPreferredSize(chartPanelSize);
        jPanel7.setMinimumSize(chartPanelSize);
        jPanel7.add(categoryBarChart, BorderLayout.CENTER);

        jPanel10.setLayout(new BorderLayout());
        jPanel10.setBackground(Color.WHITE);
        jPanel10.setBorder(BorderFactory.createTitledBorder("Supplier Email Coverage"));
        jPanel10.setPreferredSize(chartPanelSize);
        jPanel10.setMinimumSize(chartPanelSize);
        jPanel10.add(supplierPieChart, BorderLayout.CENTER);

        jPanel11.setLayout(new BorderLayout());
        jPanel11.setBackground(Color.WHITE);
        jPanel11.setBorder(BorderFactory.createTitledBorder("Suppliers By City"));
        jPanel11.setPreferredSize(chartPanelSize);
        jPanel11.setMinimumSize(chartPanelSize);
        jPanel11.add(supplierBarChart, BorderLayout.CENTER);

        stockPieChart.setPreferredSize(chartPanelSize);
        stockPieChart.setMinimumSize(chartPanelSize);
        categoryBarChart.setPreferredSize(chartPanelSize);
        categoryBarChart.setMinimumSize(chartPanelSize);
        supplierPieChart.setPreferredSize(chartPanelSize);
        supplierPieChart.setMinimumSize(chartPanelSize);
        supplierBarChart.setPreferredSize(chartPanelSize);
        supplierBarChart.setMinimumSize(chartPanelSize);

        categoryBarChart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        supplierBarChart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jPanel4.revalidate();
        jPanel7.revalidate();
        jPanel10.revalidate();
        jPanel11.revalidate();
    }

    private void configureNavigation() {
        makeClickable(jPanel2, jLabel2, this::openDashboard);
        makeClickable(jPanel5, jLabel5, this::openItemsPage);
        makeClickable(jPanel6, jLabel12, this::openSuppliersPage);
        makeClickable(jPanel8, jLabel13, this::openLowStocksPage);
        makeClickable(jPanel9, jLabel14, null);
        makeClickable(jPanel3, jLabel3, this::logout);
    }

    private void configureEvents() {
        jButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton1.addActionListener(e -> generateSummaryReport());

        categoryBarChart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBarChartPopup(categoryBarChart);
            }
        });

        supplierBarChart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBarChartPopup(supplierBarChart);
            }
        });
    }

    private void makeClickable(java.awt.Component panel, java.awt.Component label, Runnable action) {
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (action == null) {
            return;
        }

        MouseAdapter handler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        };

        panel.addMouseListener(handler);
        label.addMouseListener(handler);
    }

    private void loadAnalytics() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            loadStockStatus(connection);
            loadCategoryAnalytics(connection);
            loadSupplierCoverage(connection);
            loadSupplierCities(connection);
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Unable to load analytics.", ex);
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load analytics.\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadStockStatus(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(STOCK_STATUS_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int lowStock = resultSet.getInt("low_stock_count");
                int healthyStock = resultSet.getInt("healthy_stock_count");

                List<ChartSlice> slices = new ArrayList<>();
                slices.add(new ChartSlice("Low Stock", lowStock, new Color(226, 87, 76)));
                slices.add(new ChartSlice("Healthy", healthyStock, new Color(83, 156, 78)));
                stockPieChart.setData("Stock Status Overview", slices);
            }
        }
    }

    private void loadCategoryAnalytics(Connection connection) throws SQLException {
        List<BarValue> values = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(CATEGORY_ITEMS_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                values.add(new BarValue(
                        resultSet.getString("category"),
                        resultSet.getInt("item_count")
                ));
            }
        }

        categoryBarChart.setData("Most Stocked Categories", values);
    }

    private void loadSupplierCoverage(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SUPPLIER_CONTACT_COVERAGE_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int withEmail = resultSet.getInt("suppliers_with_email");
                int withoutEmail = resultSet.getInt("suppliers_without_email");

                List<ChartSlice> slices = new ArrayList<>();
                slices.add(new ChartSlice("With Email", withEmail, new Color(94, 140, 222)));
                slices.add(new ChartSlice("No Email", withoutEmail, new Color(210, 210, 210)));
                supplierPieChart.setData("Supplier Email Coverage", slices);
            }
        }
    }

    private void loadSupplierCities(Connection connection) throws SQLException {
        List<BarValue> values = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SUPPLIER_CITY_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                values.add(new BarValue(
                        resultSet.getString("city_name"),
                        resultSet.getInt("supplier_count")
                ));
            }
        }

        supplierBarChart.setData("Suppliers By City", values);
    }

    private void openDashboard() {
        new Main(loggedInUser).setVisible(true);
        dispose();
    }

    private void openItemsPage() {
        new Item_Page(loggedInUser).setVisible(true);
        dispose();
    }

    private void openSuppliersPage() {
        new Supplier_Page(loggedInUser).setVisible(true);
        dispose();
    }

    private void openLowStocksPage() {
        new Low_stocks_page(loggedInUser).setVisible(true);
        dispose();
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    private void showBarChartPopup(BarChartPanel sourceChart) {
        JDialog dialog = new JDialog((Window) this, sourceChart.getChartTitle(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        BarChartPanel expandedChart = new BarChartPanel();
        expandedChart.setPreferredSize(new Dimension(760, 460));
        expandedChart.setMinimumSize(new Dimension(760, 460));
        expandedChart.setData(sourceChart.getChartTitle(), sourceChart.getChartValues());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        container.add(expandedChart, BorderLayout.CENTER);

        dialog.setContentPane(container);
        dialog.pack();
        dialog.setSize(820, 540);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void generateSummaryReport() {
        loadAnalytics();

        try (Connection connection = DatabaseConnection.getConnection()) {
            ReportSummary summary = fetchReportSummary(connection);
            showSummaryReportDialog(summary);
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Unable to generate summary report.", ex);
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to generate summary report.\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private ReportSummary fetchReportSummary(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(REPORT_SUMMARY_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                return new ReportSummary(0, 0, 0, 0, "N/A", 0, "N/A", 0);
            }

            return new ReportSummary(
                    resultSet.getInt("total_items"),
                    resultSet.getInt("low_stock_items"),
                    resultSet.getInt("total_suppliers"),
                    resultSet.getInt("suppliers_with_email"),
                    defaultText(resultSet.getString("top_category")),
                    resultSet.getInt("top_category_count"),
                    defaultText(resultSet.getString("top_city")),
                    resultSet.getInt("top_city_count")
            );
        }
    }

    private void showSummaryReportDialog(ReportSummary summary) {
        String generatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a"));
        int healthyItems = Math.max(0, summary.totalItems() - summary.lowStockItems());
        int suppliersWithoutEmail = Math.max(0, summary.totalSuppliers() - summary.suppliersWithEmail());

        String html = """
                <html>
                <body style='font-family:Segoe UI; background:#f6f6f6; margin:18px; color:#303030;'>
                    <div style='background:white; border:1px solid #d9d9d9; border-radius:12px; padding:20px 24px;'>
                        <div style='font-size:24px; font-weight:700; margin-bottom:4px;'>Inventory Summary Report</div>
                        <div style='font-size:12px; color:#6a6a6a; margin-bottom:18px;'>Generated %s by %s</div>

                        <div style='font-size:16px; font-weight:700; margin-bottom:8px;'>Executive Summary</div>
                        <div style='font-size:13px; line-height:1.6; margin-bottom:18px;'>
                            The inventory currently tracks <b>%d active items</b> and <b>%d active suppliers</b>.
                            Of those items, <b>%d are low stock</b> while <b>%d remain within healthy levels</b>.
                            Supplier records show <b>%d with email contact details</b> and <b>%d without email information</b>.
                        </div>

                        <div style='font-size:16px; font-weight:700; margin-bottom:8px;'>Key Highlights</div>
                        <ul style='margin-top:0; padding-left:18px; font-size:13px; line-height:1.7;'>
                            <li>Most stocked category: <b>%s</b> with <b>%d items</b>.</li>
                            <li>Largest supplier location grouping: <b>%s</b> with <b>%d suppliers</b>.</li>
                            <li>Low stock ratio: <b>%s%%</b> of active inventory.</li>
                            <li>Supplier email coverage: <b>%s%%</b> of active suppliers.</li>
                        </ul>

                        <div style='font-size:16px; font-weight:700; margin-bottom:8px;'>Recommended Follow-up</div>
                        <div style='font-size:13px; line-height:1.7;'>
                            Review low-stock items for replenishment, complete missing supplier contact details,
                            and verify supplier assignments on items to improve future supplier-based reporting.
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                generatedAt,
                loggedInUser,
                summary.totalItems(),
                summary.totalSuppliers(),
                summary.lowStockItems(),
                healthyItems,
                summary.suppliersWithEmail(),
                suppliersWithoutEmail,
                summary.topCategory(),
                summary.topCategoryCount(),
                summary.topCity(),
                summary.topCityCount(),
                formatPercentage(summary.lowStockItems(), summary.totalItems()),
                formatPercentage(summary.suppliersWithEmail(), summary.totalSuppliers())
        );

        JEditorPane reportPane = new JEditorPane("text/html", html);
        reportPane.setEditable(false);
        reportPane.setOpaque(false);
        reportPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(reportPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JDialog dialog = new JDialog((Window) this, "Summary Report", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(scrollPane);
        dialog.setSize(720, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String formatPercentage(int part, int total) {
        if (total <= 0) {
            return "0.0";
        }
        double percentage = (part * 100.0) / total;
        return String.format(java.util.Locale.US, "%.1f", percentage);
    }

    private String defaultText(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }

    private record ChartSlice(String label, int value, Color color) {
    }

    private record BarValue(String label, int value) {
    }

    private record ReportSummary(
            int totalItems,
            int lowStockItems,
            int totalSuppliers,
            int suppliersWithEmail,
            String topCategory,
            int topCategoryCount,
            String topCity,
            int topCityCount
    ) {
    }

    private static final class PieChartPanel extends JPanel {

        private String title = "";
        private List<ChartSlice> slices = List.of();
        private final Color backgroundColor = Color.WHITE;
        private final Color titleColor = new Color(50, 50, 50);
        private final Color textColor = new Color(70, 70, 70);
        private final Color ringCenterColor = new Color(250, 250, 250);

        PieChartPanel() {
            setBackground(backgroundColor);
        }

        void setData(String title, List<ChartSlice> slices) {
            this.title = title;
            this.slices = slices;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int total = slices.stream().mapToInt(ChartSlice::value).sum();
            int width = getWidth();
            int height = getHeight();

            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, width, height);

            g2.setColor(titleColor);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString(title, 14, 24);

            if (total <= 0) {
                g2.setColor(textColor);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                g2.drawString("No data available", 14, height / 2);
                g2.dispose();
                return;
            }

            int chartSize = Math.min(width - 110, height - 70);
            chartSize = Math.max(chartSize, 80);
            int chartX = 18;
            int chartY = Math.max(40, (height - chartSize) / 2);

            double startAngle = 90;
            for (ChartSlice slice : slices) {
                if (slice.value() <= 0) {
                    continue;
                }
                double angle = -360.0 * slice.value() / total;
                g2.setColor(slice.color());
                g2.fill(new Arc2D.Double(chartX, chartY, chartSize, chartSize, startAngle, angle, Arc2D.PIE));
                startAngle += angle;
            }

            g2.setColor(ringCenterColor);
            int innerSize = (int) (chartSize * 0.52);
            int innerX = chartX + (chartSize - innerSize) / 2;
            int innerY = chartY + (chartSize - innerSize) / 2;
            g2.fillOval(innerX, innerY, innerSize, innerSize);

            g2.setColor(titleColor);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            String totalText = String.valueOf(total);
            FontMetrics metrics = g2.getFontMetrics();
            g2.drawString(totalText, innerX + (innerSize - metrics.stringWidth(totalText)) / 2, innerY + innerSize / 2 - 2);

            g2.setColor(textColor);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("items", innerX + (innerSize / 2) - 14, innerY + innerSize / 2 + 18);

            int legendX = chartX + chartSize + 22;
            int legendY = chartY + 18;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            for (ChartSlice slice : slices) {
                g2.setColor(slice.color());
                g2.fillRoundRect(legendX, legendY - 10, 14, 14, 4, 4);
                g2.setColor(textColor);
                g2.drawString(slice.label() + " (" + slice.value() + ")", legendX + 22, legendY + 2);
                legendY += 24;
            }

            g2.dispose();
        }
    }

    private static final class BarChartPanel extends JPanel {

        private String title = "";
        private List<BarValue> values = List.of();
        private final Color backgroundColor = Color.WHITE;
        private final Color axisColor = new Color(170, 170, 170);
        private final Color titleColor = new Color(50, 50, 50);
        private final Color labelColor = new Color(70, 70, 70);
        private final Color[] palette = {
            new Color(83, 156, 78),
            new Color(94, 140, 222),
            new Color(232, 151, 54),
            new Color(156, 99, 181),
            new Color(237, 199, 72),
            new Color(80, 184, 145)
        };

        BarChartPanel() {
            setBackground(backgroundColor);
        }

        void setData(String title, List<BarValue> values) {
            this.title = title;
            this.values = values;
            repaint();
        }

        String getChartTitle() {
            return title;
        }

        List<BarValue> getChartValues() {
            return new ArrayList<>(values);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, width, height);

            g2.setColor(titleColor);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            FontMetrics titleMetrics = g2.getFontMetrics();
            int titleX = Math.max(12, (width - titleMetrics.stringWidth(title)) / 2);
            g2.drawString(title, titleX, 24);

            if (values.isEmpty()) {
                g2.setColor(labelColor);
                g2.setFont(getFont().deriveFont(Font.PLAIN, 14f));
                g2.drawString("No category data available", 14, height / 2);
                g2.dispose();
                return;
            }

            int left = 42;
            int top = 36;
            int right = width - 14;
            int bottom = height - 30;
            int chartHeight = bottom - top;
            int chartWidth = right - left;
            int gap = 14;
            int barWidth = Math.max(26, (chartWidth - gap * (values.size() + 1)) / Math.max(values.size(), 1));
            int maxValue = values.stream().mapToInt(BarValue::value).max().orElse(1);

            g2.setColor(axisColor);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(left, top, left, bottom);
            g2.drawLine(left, bottom, right, bottom);

            int gridLines = 5;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            for (int i = 0; i <= gridLines; i++) {
                int y = top + (chartHeight * i / gridLines);
                int gridValue = (int) Math.round(maxValue * (gridLines - i) / (double) gridLines);

                g2.setColor(axisColor);
                g2.drawLine(left - 5, y, left, y);
                g2.drawString(String.valueOf(gridValue), 10, y + 4);

                if (i < gridLines) {
                    g2.setColor(new Color(220, 220, 220));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawLine(left, y, right, y);
                    g2.setStroke(new BasicStroke(2f));
                }
            }

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            FontMetrics metrics = g2.getFontMetrics();
            int x = left + gap;
            for (int i = 0; i < values.size(); i++) {
                BarValue value = values.get(i);
                int barHeight = maxValue == 0 ? 0 : (int) ((chartHeight - 22) * (value.value() / (double) maxValue));
                int barY = bottom - barHeight;

                g2.setColor(palette[i % palette.length]);
                g2.fillRect(x, barY, barWidth, barHeight);

                g2.setColor(labelColor);
                String countText = String.valueOf(value.value());
                g2.drawString(countText, x + (barWidth - metrics.stringWidth(countText)) / 2, barY - 6);

                String label = value.label().length() > 11 ? value.label().substring(0, 11) + "…" : value.label();
                g2.drawString(label, x, top + chartHeight + 16);

                g2.setColor(backgroundColor);
                g2.fillRect(x, bottom + 4, barWidth, 18);
                g2.setColor(labelColor);
                String rawLabel = value.label();
                String cleanLabel = rawLabel.length() > 10 ? rawLabel.substring(0, 10) + "..." : rawLabel;
                int labelX = x + Math.max(0, (barWidth - metrics.stringWidth(cleanLabel)) / 2);
                g2.drawString(cleanLabel, labelX, bottom + 18);

                x += barWidth + gap;
            }

            g2.dispose();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Manage_report_page().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private panels.PanelBorder panelBorder1;
    private panels.SidePanel sidePanel2;
    // End of variables declaration//GEN-END:variables
}
