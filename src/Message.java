import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import raven.glasspanepopup.GlassPanePopup;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;

/**
 *
 * @author RAVEN
 */
public class Message extends javax.swing.JPanel {

	private Button cmdOK;
    private javax.swing.JLabel title;
    private javax.swing.JTextPane message;
	
    public Message() {
        initComponents();
        setOpaque(false);
        message.setBackground(new Color(0, 0, 0, 0));
        message.setSelectionColor(new Color(48, 170, 63, 200));
        message.setOpaque(false);
        
    }
    
    public void setDimensions(int width, int height) {
    	setPreferredSize(new java.awt.Dimension(width, height));
    }
    
    public void setTitle(String title) {
    	this.title.setText(title);
    }
    
    public void setMessage(String title) {
    	this.message.setText(title);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
        g2.dispose();
        super.paintComponent(grphcs);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        message = new javax.swing.JTextPane();
        cmdOK = new Button();
        cmdOK.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GlassPanePopup.closePopupLast();
        	}
        });

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25));

        title.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        title.setForeground(new java.awt.Color(80, 80, 80));

        message.setEditable(false);
        message.setForeground(new java.awt.Color(133, 133, 133));

        cmdOK.setBackground(new Color(255, 128, 128));
        cmdOK.setForeground(new java.awt.Color(255, 255, 255));
        cmdOK.setText("OK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(title)
        					.addGap(0, 299, Short.MAX_VALUE))
        				.addComponent(message, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        				.addComponent(cmdOK, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(title)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(message, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(cmdOK, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        this.setLayout(layout);
    }// </editor-fold>//GEN-END:initComponents

    public void eventOK(ActionListener event) {
        cmdOK.addActionListener(event);
    }
    // End of variables declaration//GEN-END:variables
}
