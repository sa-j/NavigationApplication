
package edu.prgpr;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Saj, Joanna
 *
 */
public class Frame extends JFrame  {

    /**
     * create a projektframe
     */
    private static final long serialVersionUID = 1L;

    public Frame(final Graph graph) {

        super("The Navigation System");
        super.setLayout(new GridBagLayout());
        /**
         * create two panels
         */
        final MPanel panel = new MPanel(graph);
        final RightPanel rPanel = new RightPanel(graph);
        
        rPanel.setLayout(new GridBagLayout());
        JScrollPane scrollPanel = new JScrollPane(panel);
        scrollPanel.setVisible(true);
        scrollPanel.createVerticalScrollBar();
        super.add(scrollPanel);
        /**
         * the objects save the streetnames and housenumbers
         */
        Object[] start = null;
        Object[] destination = null;

        /**
         * the array of streetname and housenumbers as a string
         */
        ArrayList<String> streetArray = new ArrayList<String>();
        for (Integer i : graph.hashNodes.keySet()) {
            if (graph.hashNodes.get(i).getTag().containsKey("addr:street") && graph.hashNodes.get(i).getTag().containsKey("addr:housenumber")) {
                streetArray.add(graph.hashNodes.get(i).getTag().get("addr:street") + ";" + graph.hashNodes.get(i).getTag().get("addr:housenumber"));
            }
        }
        //Brönnerstrasse funktioniert nicht!!
        streetArray.remove(4);
        destination = start = streetArray.toArray();

        /**
         * create the buttons and add in to the rPanel
         */
        JButton button1 = new JButton(" SHOW STARTING POINT ");
        JButton button2 = new JButton("   SHORTEST PATH   ");
        JButton button3 = new JButton("  FASTEST PATH  ");
        JButton button4 = new JButton("  SIMULATE  "); 
        final JLabel label = new JLabel("START:");
        final JLabel label2 = new JLabel("DESTINATION:");
        final TextArea textField = new TextArea(10,1);
        rPanel.add(button1, SetGridBagConstraints(1, 5, 1, 1));
        rPanel.add(button2, SetGridBagConstraints(1, 6, 1, 1));
        rPanel.add(button3, SetGridBagConstraints(1, 7, 1, 1));
        rPanel.add(button4, SetGridBagConstraints(1, 8, 1, 1));
        rPanel.add(label, SetGridBagConstraints(1, 0, 1, 1));
        rPanel.add(label2, SetGridBagConstraints(1, 2, 1, 1));
        rPanel.add(textField, SetGridBagConstraints(1, 9, 1, 100));
        /*
         * create two jumboboxes
         */
        final JComboBox startBox = new JComboBox(start);
        final JComboBox endBox = new JComboBox(destination);

        rPanel.add(startBox, SetGridBagConstraints(1, 1, 1, 1));
        rPanel.add(endBox, SetGridBagConstraints(1, 3, 1, 1));

       //empty panels
        rPanel.add(new JPanel(), SetGridBagConstraints(2, 0, 1, 1));
        rPanel.add(new JPanel(), SetGridBagConstraints(1, 4, 1, 10));


        /**
         * funktionality of the button1 - search the adress
         */
        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              
                String str = startBox.getSelectedItem().toString();
                FindNumber parser = new FindNumber(str);
                panel.findAdress(parser.returnStreet(), parser.returnNumber());
                 }
        });
        /**
         * funktionality of the button2 - shortest way
         */
        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<MapNode> path = new ArrayList<MapNode>();
                MapNode n1 = new MapNode();
                MapNode n2 = new MapNode();
                String str2 = startBox.getSelectedItem().toString();
                String str1 = endBox.getSelectedItem().toString();
                FindNumber parser1 = new FindNumber(str1);
                FindNumber parser2 = new FindNumber(str2);
                NearestWay near1 = new NearestWay(graph);
                NearestWay near2 = new NearestWay(graph);
                n1 = panel.findAdressNode(parser1.returnStreet(),parser1.returnNumber());
                n2 = panel.findAdressNode(parser2.returnStreet(), parser2.returnNumber());
                path = graph.Algo(near1.findNearstHighwayNode(n1), near2.findNearstHighwayNode(n2));
                panel.repaint();
                panel.simu = 0; // so that simulation can be repeated
                panel.setPath(path);
                panel.giveDistance(panel.calculatePathLenght(panel.path));
                textField.append("Distance of shortest path: "+(int)panel.pathLenght+" meters \n");
                graph.resetAllNodes();

            }
        });
        /**
         * funktionality of the button3 - shortest time
         */
       button3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<MapNode> path = new ArrayList<MapNode>();
                MapNode n1 = new MapNode();
                MapNode n2=new MapNode();
                String str1 = startBox.getSelectedItem().toString();
                String str2 = endBox.getSelectedItem().toString();
                FindNumber parser1 = new FindNumber(str1);
                FindNumber parser2 = new FindNumber(str2);
                NearestWay near1 = new NearestWay(graph);
                NearestWay near2 = new NearestWay(graph);
                n1 = panel.findAdressNode(parser1.returnStreet(),parser1.returnNumber());
                n2 = panel.findAdressNode(parser2.returnStreet(), parser2.returnNumber());
                path = graph.AlgoSpeed(near1.findNearstHighwayNode(n1), near2.findNearstHighwayNode(n2));
                panel.repaint();
                panel.simu=0; // so that simulation can be repeated
                panel.setPath(path);
                panel.giveDistance(panel.calculatePathLenght(panel.path));
                textField.append("Distance of fastest path: "+(int)panel.pathLenght+" meters \n");
                graph.resetAllNodes(); 
                
            }
        });

    button4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.simu += 1;
                if (panel.simu>=panel.path.size()) {
                    panel.simu=panel.path.size();
                }
                panel.repaint();
                panel.remainingDistance();
                double remaining = panel.remainingDistance();
                if (remaining > 0) {
                    textField.append((int)remaining+" meters remaining \n");
                } else{
                    textField.append("You reached your destination \n");
                }                        
            }
        });



        this.add(scrollPanel, SetGridBagConstraints(0, 0, 100000, 10));
        this.add(rPanel, SetGridBagConstraints(1, 0, 0, 0));
        panel.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                panel.setZoom(event.getWheelRotation());
                panel.repaint();
            }
            
        });

        panel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                //move the map
                if (panel.startX == Integer.MIN_VALUE && panel.startY == Integer.MIN_VALUE) {
                    panel.startX = e.getX();
                    panel.startY = e.getY();
                    panel.oldMoveX = panel.moveX;
                    panel.oldMoveY = panel.moveY;
                }
                if (panel.oldMoveX - panel.startX + e.getX() > panel.getWidth() - panel.maxX / panel.zoom) {
                    panel.moveX = panel.oldMoveX - panel.startX + e.getX();
                }
                if (panel.moveX > 0) {
                    panel.moveX = -1000;
                }
                if (panel.oldMoveY - panel.startY + e.getY() > panel.getHeight() - panel.maxY / panel.zoom) {
                    panel.moveY = panel.oldMoveY - panel.startY + e.getY();
                }
                if (panel.moveY > 0) {
                    panel.moveY = 2000;
                }
                panel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // noop
            }
        });
      
    }
    /**
     *
     * @param gridx order of occuring in x direction
     * @param gridy order of occuring in  direction
     * @param weightx size relation between x-values of frame components
     * @param weighty size relation between y-values of frame components
     * @return values of the GridBagLayout
     */
    private GridBagConstraints SetGridBagConstraints(int gridx, int gridy, int weightx, int weighty) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = 1;
        
        return gbc;

        
    }
}
