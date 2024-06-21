
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import java.awt.Component;

public class CustomScrollPane extends JScrollPane {
    
    public CustomScrollPane(Component view) {
        super(view);
    }
    
    @Override
	public JScrollBar createVerticalScrollBar() {
        return new ScrollBar(JScrollBar.VERTICAL);
    }

    @Override
	public JScrollBar createHorizontalScrollBar() {
        return new ScrollBar(JScrollBar.HORIZONTAL);
    }
}

