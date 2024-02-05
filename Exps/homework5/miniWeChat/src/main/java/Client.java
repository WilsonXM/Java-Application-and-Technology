import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {
    private ObjectInputStream clientIn;        // Socket的输入流
    private ObjectOutputStream clientOut;       // Socket的输出流
    private String serverIP;                    // 服务器的IP地址
    private int port;                           // 服务器的端口
    private String username;                    // 客户端的用户名
    public static Client_GUI clientGUI;

    public static void main(String[] args) {
        System.out.println("客户端启动中...");
        clientGUI = new Client_GUI();
    }

    /* 构造函数 */
    public Client(String serverIP, int port, String username) {
        this.serverIP = serverIP;
        this.port = port;
        this.username = username;
    }

    /* 连接到服务器的方法 */
    public boolean connectServer() {
        // 客户端Socket
        Socket clientSocket;
        try {
            // 创建Socket
            clientSocket = new Socket(serverIP, port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "连接失败！服务器可能关闭，或者输入的服务器IP地址或端口有误", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(null, "连接服务器成功！");
        String errorMsg;

        // 初始化两个流
        try {
            clientIn = new ObjectInputStream(clientSocket.getInputStream());
            clientOut = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (Exception e) {
            errorMsg = "创建套接字的I/O流时出错！ " + e;
            JOptionPane.showMessageDialog(null, errorMsg, "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 尝试向服务器发送消息
        try {
            clientOut.writeObject(username);
        } catch (IOException e) {
            errorMsg = "向服务器发送消息时出错！ " + e;
            JOptionPane.showMessageDialog(null, errorMsg, "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 创建一个线程用于监听来自服务器的消息
        new InteractWithServer().start();
        return true;
    }

    /* 清除客户端界面的方法 */
    public void clear() {
        serverIP = "";
        port = 0;
    }

    // 线程：打印服务器发送的消息
    class InteractWithServer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // 接收消息
                    Message message = (Message) clientIn.readObject();
                    String content = message.getContent();
                    switch (message.getType()) {
                        // 消息类型为 MESSAGE，显示在 ChattingTa 上
                        case Message.MESSAGE -> clientGUI.chattingTaDisplay(content);
                        // 消息类型为 USERLIST，显示在 UserlistTa 上
                        case Message.USERLIST -> {
                            clientGUI.userlistTaClear();
                            clientGUI.userlistTaDisplay(content);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "与服务器的连接已断开！");
                    clientGUI.setVisible(false);
                    break;
                }
            }
        }
    }

    /* 发送消息的方法 */
    public void sendMessage(Message message) {
        try {
            clientOut.writeObject(message);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "消息发送过程出错！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
