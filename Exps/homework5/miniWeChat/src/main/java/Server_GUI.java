import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/* 服务器监控的图形用户界面类 */
public class Server_GUI extends JFrame {

    /* 面板定义 */
    private final JPanel contentPanel = new JPanel();

    /* 文本区域定义 */
    private JTextArea monitorTa;

    /* 构造函数 */
    public Server_GUI() {
        this.init();
    }

    /* 初始化函数 */
    private void init() {
        String port;
        int portnum = 0;
        boolean port_valid;

        // 循环直到端口号有效
        do {
            port_valid = true;
            port = JOptionPane.showInputDialog(this, "请输入准备监听的端口号", "服务器对话框", JOptionPane.PLAIN_MESSAGE);
            try {
                portnum = Integer.parseInt(port);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "端口号需要为1-65535的整数！", "Error", JOptionPane.ERROR_MESSAGE);
                port_valid = false;
            }
        } while (!port_valid);

        /* 窗口设置 */
        this.setTitle("服务器界面");                // 设置窗口标题
        this.setSize(750, 600);                  // 设置窗口大小
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // 启用关闭按钮
        setLocationRelativeTo(null);               // 将窗口置于屏幕中央
        setVisible(true);                          // 设置窗口可见

        // 监控文本区域
        monitorTa = new JTextArea("", 500, 80);
        Color color = new Color(255, 255, 245);
        monitorTa.setBackground(color);
        monitorTa.setEditable(false);

        // 监控滚动窗格
        /* JScrollPane 定义 */
        JScrollPane monitorSp = new JScrollPane(monitorTa);

        /* 将组件添加到 contentPanel */
        contentPanel.setLayout(null);
        add(monitorSp);

        /* 位置设置 */
        monitorSp.setBounds(0, 0, 597, 535);

        // 将 contentPanel 设置为透明，以便看到背景。
        contentPanel.setOpaque(false);
        getContentPane().add(contentPanel);

        // 创建 Server 的实例，并监听指定的端口
        Server sersoc = new Server(portnum, this);
        sersoc.listen();
    }

    // 在聊天窗口显示消息
    public void MonitorTaDisplay(String msg) {
        monitorTa.append(msg);
        // 将滚动条拉到 JTextArea 的底部。
        monitorTa.setCaretPosition(monitorTa.getText().length() - 1);
    }
}
