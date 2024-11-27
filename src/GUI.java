import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;
import java.util.Vector;

public class GUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable menuTable;
    private JTable ordersTable;
    private static DefaultTableModel menuModel;
    private static DefaultTableModel ordersModel;
    private final Color PRIMARY_COLOR = new Color(79, 93, 117);
    private final Color SECONDARY_COLOR = new Color(245, 245, 250);
    private final Color ACCENT_COLOR = new Color(191, 85, 105);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public GUI() {
        setTitle("Byte Me Food Ordering System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TableHeader.background", PRIMARY_COLOR);
            UIManager.put("TableHeader.foreground", Color.WHITE);
            UIManager.put("TableHeader.font", HEADER_FONT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(SECONDARY_COLOR);
        tabbedPane.setForeground(PRIMARY_COLOR);

        initializeMenuPanel();
        initializeOrdersPanel();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.setBackground(SECONDARY_COLOR);

        add(mainPanel);

        refreshData();
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Byte Me Food Ordering System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private void initializeMenuPanel() {
        JPanel menuPanel = new JPanel(new BorderLayout(10, 10));
        menuPanel.setBackground(SECONDARY_COLOR);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] menuColumns = {"ID", "Name", "Price", "Category", "Available"};
        menuModel = new DefaultTableModel(menuColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        menuTable = createStyledTable(menuModel);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);

        menuPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Menu", menuPanel);
    }

    private void initializeOrdersPanel() {
        JPanel ordersPanel = new JPanel(new BorderLayout(10, 10));
        ordersPanel.setBackground(SECONDARY_COLOR);
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] orderColumns = {"Order ID", "Items", "Total Price", "Status", "VIP", "Special Request"};
        ordersModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = createStyledTable(ordersModel);

        ordersTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ("VIP".equals(value)) {
                    setForeground(ACCENT_COLOR);
                    setFont(HEADER_FONT);
                } else {
                    setForeground(table.getForeground());
                    setFont(TABLE_FONT);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);

        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Orders", ordersPanel);
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 245));
                }
                return c;
            }
        };

        table.setFont(TABLE_FONT);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 225));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(10, 5));

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                comp.setBackground(PRIMARY_COLOR);
                comp.setForeground(Color.WHITE);
                comp.setFont(HEADER_FONT);
                setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(220, 220, 225)));
                setHorizontalAlignment(JLabel.CENTER);
                return comp;
            }
        });

        header.setOpaque(true);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 225)));

        TableColumnModel columnModel = table.getColumnModel();
        if (model == menuModel) {
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(100);
            columnModel.getColumn(3).setPreferredWidth(150);
            columnModel.getColumn(4).setPreferredWidth(100);
        } else {
            columnModel.getColumn(0).setPreferredWidth(80);
            columnModel.getColumn(1).setPreferredWidth(250);
            columnModel.getColumn(2).setPreferredWidth(100);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(4).setPreferredWidth(80);
            columnModel.getColumn(5).setPreferredWidth(150);
        }

        return table;
    }

    public static void refreshData() {
        menuModel.setRowCount(0);
        for (Item item : Item.items) {
            Vector<Object> row = new Vector<>();
            row.add(item.getId());
            row.add(item.getName());
            row.add(String.format("Rs.%.2f", item.getPrice()));
            row.add(item.getCategory());
            row.add(item.getAvailable() ? "Yes" : "No");
            menuModel.addRow(row);
        }

        ordersModel.setRowCount(0);
        for (Order order : Admin.pendingOrders) {
            Vector<Object> row = new Vector<>();
            row.add(order.getOrderId());
            row.add(formatOrderItems(order));
            row.add(String.format("Rs.%.2f", order.getTotalPrice()));
            row.add(order.getStatus());
            row.add(order.isVIP() ? "VIP" : "Regular");
            row.add(order.getSpecialRequest());
            ordersModel.addRow(row);
        }
    }

    private static String formatOrderItems(Order order) {
        StringBuilder items = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : order.orderItems.entrySet()) {
            if (items.length() > 0) {
                items.append(", ");
            }
            items.append(entry.getKey().getName())
                    .append(" x")
                    .append(entry.getValue());
        }
        return items.toString();
    }
}