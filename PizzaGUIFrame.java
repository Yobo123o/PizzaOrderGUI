import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PizzaGUIFrame is a JFrame-based GUI application that allows users to order a pizza
 * by selecting its size, crust type, and toppings.
 */
public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDish;
    private JComboBox<String> sizeCombo;
    private JCheckBox[] toppings;
    private JTextArea receiptArea;
    private JButton orderButton, clearButton, quitButton;

    private final double TAX_RATE = 0.07;
    private final double[] SIZE_PRICES = {8.00, 12.00, 16.00, 20.00};

    /**
     * Constructor for PizzaGUIFrame.
     * Initializes the GUI, locks the window size, and sets up the layout.
     */
    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(550, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel receiptPanel = createReceiptPanel();
        add(receiptPanel, BorderLayout.WEST);

        JPanel optionsPanel = createOptionsPanel();
        add(optionsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Creates the receipt panel on the left side of the GUI.
     * The receipt panel contains a scrollable text area to display the order details.
     *
     * @return JPanel containing the receipt area.
     */
    private JPanel createReceiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Receipt"));
        panel.setPreferredSize(new Dimension(300, 500));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the options panel on the right side of the GUI.
     * The panel consists of the Size, Crust Type, and Toppings selection components.
     *
     * @return JPanel containing the user input options.
     */
    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createSizePanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createCrustPanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createToppingsPanel());

        return panel;
    }

    /**
     * Creates the panel for selecting the crust type.
     *
     * @return JPanel containing the crust type options.
     */
    private JPanel createCrustPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        panel.setPreferredSize(new Dimension(250, 50));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-dish");

        ButtonGroup group = new ButtonGroup();
        group.add(thinCrust);
        group.add(regularCrust);
        group.add(deepDish);

        panel.add(thinCrust);
        panel.add(regularCrust);
        panel.add(deepDish);

        return panel;
    }

    /**
     * Creates the panel for selecting the pizza size.
     *
     * @return JPanel containing the pizza size options.
     */
    private JPanel createSizePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Pizza Size"));
        panel.setPreferredSize(new Dimension(250, 50));

        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setPreferredSize(new Dimension(150, 25));

        panel.add(sizeCombo);
        return panel;
    }

    /**
     * Creates the panel for selecting toppings.
     *
     * @return JPanel containing the toppings options.
     */
    private JPanel createToppingsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        panel.setPreferredSize(new Dimension(250, 120));

        String[] toppingOptions = {"Pepperoni", "Mushrooms", "Onions", "Bacon", "Pineapple", "Olives"};
        toppings = new JCheckBox[toppingOptions.length];

        for (int i = 0; i < toppingOptions.length; i++) {
            toppings[i] = new JCheckBox(toppingOptions[i]);
            panel.add(toppings[i]);
        }

        return panel;
    }

    /**
     * Creates the panel containing the Order, Clear, and Quit buttons.
     *
     * @return JPanel containing the buttons.
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        orderButton.addActionListener(new OrderButtonListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> quitApp());

        panel.add(orderButton);
        panel.add(clearButton);
        panel.add(quitButton);
        return panel;
    }

    /**
     * Handles the action of pressing the "Order" button.
     * Calculates the price based on selected options and displays the order details in the receipt area.
     */
    private class OrderButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String crust = getSelectedCrust();
            if (crust.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a crust type.");
                return;
            }

            int sizeIndex = sizeCombo.getSelectedIndex();
            double basePrice = SIZE_PRICES[sizeIndex];

            StringBuilder orderDetails = new StringBuilder();
            orderDetails.append("=========================================\n");
            orderDetails.append("Pizza Order Receipt\n");
            orderDetails.append("=========================================\n");
            orderDetails.append("Crust: ").append(crust).append("\n");
            orderDetails.append("Size: ").append(sizeCombo.getSelectedItem()).append("\t\t$").append(basePrice).append("\n");

            double toppingsCost = 0;
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    orderDetails.append(topping.getText()).append("\t\t$1.00\n");
                    toppingsCost += 1.00;
                }
            }

            double subTotal = basePrice + toppingsCost;
            double tax = subTotal * TAX_RATE;
            double total = subTotal + tax;

            orderDetails.append("-----------------------------------------\n");
            orderDetails.append("Subtotal:\t\t$").append(String.format("%.2f", subTotal)).append("\n");
            orderDetails.append("Tax (7%):\t\t$").append(String.format("%.2f", tax)).append("\n");
            orderDetails.append("=========================================\n");
            orderDetails.append("Total:\t\t$").append(String.format("%.2f", total)).append("\n");

            receiptArea.setText(orderDetails.toString());
        }
    }

    /**
     * Retrieves the selected crust type.
     *
     * @return The selected crust as a String.
     */
    private String getSelectedCrust() {
        if (thinCrust.isSelected()) return "Thin";
        if (regularCrust.isSelected()) return "Regular";
        if (deepDish.isSelected()) return "Deep-dish";
        return "";
    }

    /**
     * Clears all selected options and resets the receipt area.
     */
    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDish.setSelected(false);
        sizeCombo.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    /**
     * Handles the Quit button by prompting the user for confirmation before exiting.
     */
    private void quitApp() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
