import java.awt.Color;
import javax.swing.*;

import static java.lang.System.exit;

/* 客户端登录的图形用户界面类 */
public class Client_GUI extends JFrame {

    /* 按钮定义 */
    private final JButton sendBtn = new JButton("发送");
    private final JButton clearBtn = new JButton("清屏");

    /* 文本框定义 */
    private final JTextField chattingTf = new JTextField();

    /* 文本区域定义 */
    private JTextArea chattingTa;
    private JTextArea userlistTa;

    /* 面板定义 */
    private final JPanel contentPanel = new JPanel();
    public static Client Client = new Client("", 0, "");

    /* 构造函数 */
    public Client_GUI() {
        SwingUtilities.invokeLater(this::getInput);
        this.init();
        this.addListener();
    }

    private void getInput() {
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();
        String serverip, port, username;

        Object[] message = {
                "服务器IP地址:", textField1,
                "端口号:", textField2,
                "用户名:", textField3
        };

        do {
            int option = JOptionPane.showConfirmDialog(null, message, "登录信息", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                // 用户点击了OK，处理输入
                serverip = textField1.getText();
                port = textField2.getText();
                username = textField3.getText();

                // 处理输入值
                System.out.println("服务器IP地址: " + serverip);
                System.out.println("端口号: " + port);
                System.out.println("用户名: " + username);

                if (!isInputLegal(serverip, port, username)) {
                    Client.clear();
                } else {
                    int portnum = Integer.parseInt(port);
                    // 创建一个 Client_socket 类以连接服务器
                    Client = new Client(serverip, portnum, username);
                }
            } else {
                exit(0);
            }
        } while (!Client.connectServer());

        setVisible(true);
    }

    private boolean isInputLegal(String serverip, String port, String username) {
        if (serverip.length() == 0) {
            JOptionPane.showMessageDialog(null, "服务器IP地址不能为空！", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (port.length() == 0) {
            JOptionPane.showMessageDialog(null, "端口号不能为空！", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username.length() == 0) {
            JOptionPane.showMessageDialog(null, "用户名不能为空！", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "端口号需要为1-65535的整数！", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /* 初始化函数 */
    private void init() {
        /* 窗口设置 */
        this.setTitle("聊天窗口");                // 设置窗口标题
        this.setSize(610, 570);                  // 设置窗口大小
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // 启用关闭按钮
        setLocationRelativeTo(null);               // 将窗口置于屏幕中央
        setVisible(false);                         // 设置窗口不可见

        /* 创建聊天文本区域 */
        chattingTa = new JTextArea("", 100, 80);
        Color color = new Color(245, 255, 255);
        chattingTa.setBackground(color);
        chattingTa.setEditable(false);

        /* 创建用户列表文本区域 */
        userlistTa = new JTextArea("", 10, 80);
        color = new Color(255, 245, 255);
        userlistTa.setBackground(color);
        userlistTa.setEditable(false);

        /* 创建聊天滚动窗格 */
        /* JScrollPane 定义 */
        JScrollPane chattingSp = new JScrollPane(chattingTa);
        JScrollPane userlistSp = new JScrollPane(userlistTa);

        /* 将组件添加到 contentPanel */
        contentPanel.setLayout(null);
        add(chattingSp);
        add(userlistSp);
        add(chattingTf);
        add(sendBtn);
        add(clearBtn);

        /* 位置设置 */
        chattingTf.setBounds(0, 400, 400, 100);
        sendBtn.setBounds(298, 500, 100, 30);
        clearBtn.setBounds(188, 500, 100, 30);
        chattingSp.setBounds(0, 0, 400, 400);
        userlistSp.setBounds(400, 253, 195, 280);

        // 将 contentPanel 设置为透明，以便看到背景。
        contentPanel.setOpaque(false);
        getContentPane().add(contentPanel);
    }

    /* 事件监听 */
    private void addListener() {
        /* 发送按钮的操作 */
        sendBtn.addActionListener(e -> {
            String tempmsg = chattingTf.getText().trim();
            // 检查要发送的消息的有效性
            if (tempmsg.length() == 0) {
                JOptionPane.showMessageDialog(null, "要发送的消息不能为空！", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Message msg = new Message(Message.MESSAGE, tempmsg);
            // 将消息发送到服务器
            Client_GUI.Client.sendMessage(msg);
            // 清空聊天文本框
            chattingTf.setText("");
        });

        /* 清屏按钮的操作 */
        clearBtn.addActionListener(e -> {
            // 清空聊天文本区域
            chattingTa.setText("");
        });
    }

    /* 在聊天文本区域显示消息的函数 */
    public void chattingTaDisplay(String msg) {
        chattingTa.append(msg);
        chattingTa.setCaretPosition(chattingTa.getText().length() - 1);
    }

    /* 在用户列表文本区域显示消息的函数 */
    public void userlistTaDisplay(String msg) {
        userlistTa.append(msg);
        userlistTa.setCaretPosition(userlistTa.getText().length() - 1);
    }

    /* 清空用户列表文本区域的函数 */
    public void userlistTaClear() {
        userlistTa.setText("");
    }
}
