
package petrolpricecomparisonapp;

/**
 *
 * @author EMMANUEL FREDY SHANI
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class PetrolPriceComparisonApp {
    private static ArrayList<GasStation> gasStations = new ArrayList<>();
    private static final String FILE_NAME = "gas_stations.txt";

    public static void main(String[] args) {
        loadGasStations(); // Load existing gas stations from file

        JFrame frame = new JFrame("Petrol Price Comparison App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 400);
        frame.setLayout(new FlowLayout());

        JTextField stationNameField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextArea displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);

        JButton addButton = new JButton("Add Price");
        JButton viewButton = new JButton("View Prices");
        JButton searchButton = new JButton("Search");
        JButton editButton = new JButton("Edit Price");
        JButton saveButton = new JButton("Save Changes");
        JButton deleteButton = new JButton("Delete Price");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stationName = stationNameField.getText();
                double price;
                try {
                    price = Double.parseDouble(priceField.getText());
                    gasStations.add(new GasStation(stationName, price));
                    stationNameField.setText("");
                    priceField.setText("");
                    JOptionPane.showMessageDialog(frame, "Price added successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid price.");
                }
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("");
                for (GasStation station : gasStations) {
                    displayArea.append(station.toString() + "\n");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchStation = stationNameField.getText();
                displayArea.setText("");
                for (GasStation station : gasStations) {
                    if (station.getName().equalsIgnoreCase(searchStation)) {
                        displayArea.append(station.toString() + "\n");
                    }
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchStation = stationNameField.getText();
                for (GasStation station : gasStations) {
                    if (station.getName().equalsIgnoreCase(searchStation)) {
                        String newPriceStr = JOptionPane.showInputDialog(frame, "Enter new price for " + station.getName() + ":");
                        if (newPriceStr != null) {
                            try {
                                double newPrice = Double.parseDouble(newPriceStr);
                                station.setPrice(newPrice);
                                JOptionPane.showMessageDialog(frame, "Price updated successfully!");
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(frame, "Please enter a valid price.");
                            }
                        }
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Gas station not found!");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stationName = stationNameField.getText();
                boolean found = false;
                for (GasStation station : gasStations) {
                    if (station.getName().equalsIgnoreCase(stationName)) {
                        gasStations.remove(station);
                        found = true;
                        JOptionPane.showMessageDialog(frame, "Gas station deleted successfully!");
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Gas station not found!");
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGasStations();
                JOptionPane.showMessageDialog(frame, "Changes saved successfully!");
            }
        });

        frame.add(new JLabel("Gas Station Name:"));
        frame.add(stationNameField);
        frame.add(new JLabel("Petrol Price:"));
        frame.add(priceField);
        frame.add(addButton);
        frame.add(viewButton);
        frame.add(searchButton);
        frame.add(editButton);
        frame.add(deleteButton);
        frame.add(saveButton);
        frame.add(new JScrollPane(displayArea));

        frame.setVisible(true);
    }

    static class GasStation {
        private String name;
        private double price;

        public GasStation(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Gas Station: " + name + ", Petrol Price: TZS " + price;
        }
    }

    private static void loadGasStations() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    gasStations.add(new GasStation(name, price));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading gas stations: " + e.getMessage());
        }
    }

    private static void saveGasStations() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (GasStation station : gasStations) {
                bw.write(station.getName() + "," + station.price);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving gas stations: " + e.getMessage());
        }
    }
}


