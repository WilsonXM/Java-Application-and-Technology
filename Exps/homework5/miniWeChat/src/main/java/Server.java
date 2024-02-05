import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * 服务器端代码，用于处理客户端连接和消息传递
 */
public class Server {
    private final int port;                                    // 监听的端口号
    private final ArrayList<InteractWithClient> clientlist;    // 所有已连接客户端的ArrayList
    private int clientid;                                      // 每个客户端的唯一ID
    private final SimpleDateFormat curtime;                    // 当前时间格式
    public static Server_GUI server;                           // Server_GUI的实例

    public static void main(String[] args) {
        System.out.println("服务器启动中...");
        server = new Server_GUI();
    }

    /* 构造函数 */
    public Server(int port, Server_GUI server) {
        this.port = port;
        clientlist = new ArrayList<>();
        this.clientid = 0;
        this.curtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Server.server = server;
    }

    /* 监听并等待连接的函数 */
    public void listen() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            display("正在监听端口" + port + " ...\n");
            // 持续监听
            while (true) {
                Socket socket = serverSocket.accept();
                InteractWithClient tempclient = new InteractWithClient(socket);
                // 将连接的客户端添加到客户端列表
                clientlist.add(tempclient);
                tempclient.start();
                notifyClients(tempclient.userName + " 上线啦！大家快去找他/她聊天吧！");
                display(tempclient.userName + " 连接到服务器，当前在线人数为：" + clientlist.size() + "\n");
            }
        } catch (IOException e) {
            String msg = curtime.format(new Date()) + " 创建ServerSocket过程出错！: " + e + "\n";
            display(msg);
        }
    }

    /* 通知所有已连接客户端的函数 */
    private synchronized void notifyClients(String message) {
        String notice = curtime.format(new Date()) + "\n" + message + "\n\n";
        Message chatmsg = new Message(Message.MESSAGE, notice);
        /* 构造聊天消息并通知所有客户端 */
        for (InteractWithClient nowclient : clientlist) nowclient.sendMessage(chatmsg);
        /* 构造在线客户端列表消息并通知所有客户端 */
        StringBuilder tempstr = new StringBuilder("当前在线用户数: " + clientlist.size() + "\n");
        tempstr.append("在线用户列表:\n");
        tempstr.append("编号  用户名    连接时间\n");
        for (int i = 0; i < clientlist.size(); ++i) {
            InteractWithClient nowclient = clientlist.get(i);
            tempstr.append(" [").append(i + 1).append("]     ").append(nowclient.userName).append("      ").append(nowclient.linktime).append("\n");
        }
        Message listmsg = new Message(Message.USERLIST, tempstr.toString());

        for (InteractWithClient nowclient : clientlist) nowclient.sendMessage(listmsg);
    }

    /* 从客户端列表中移除客户端的函数 */
    synchronized void remove(int id) {
        // 遍历ArrayList查找要移除的客户端
        for (int i = 0; i < clientlist.size(); ++i) {
            InteractWithClient nowclient = clientlist.get(i);
            if (nowclient.id == id) {
                clientlist.remove(i);
                return;
            }
        }
    }

    /* 在Server_GUI上显示消息的函数 */
    private void display(String msg) {
        msg = curtime.format(new Date()) + " " + msg;
        server.MonitorTaDisplay(msg);
    }

    /* 内部类，用于处理与客户端的交互 */
    class InteractWithClient extends Thread {
        int id;                               // 每个线程的唯一ID
        Socket socket;                        // 服务器套接字
        ObjectInputStream socketInput;        // 套接字的输入流
        ObjectOutputStream socketOutput;      // 套接字的输出流
        String userName;                      // 连接的客户端的用户名
        String linktime;                      // 客户端的连接时间
        Message clientmessage;                // 来自客户端的消息

        /* 构造函数 */
        InteractWithClient(Socket socket) {
            id = ++clientid;
            this.socket = socket;
            String errorMsg;
            try {
                socketOutput = new ObjectOutputStream(socket.getOutputStream());
                socketInput = new ObjectInputStream(socket.getInputStream());
                // 获取用户名
                userName = (String) socketInput.readObject();

            } catch (IOException e1) {
                errorMsg = "创建套接字的I/O流时出错！ " + e1;
                JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (ClassNotFoundException ignored) {
            }
            linktime = curtime.format(new Date());
        }

        public void run() {
            // 循环直到断开连接
            while (true) {
                // 从客户端读取消息
                try {
                    clientmessage = (Message) socketInput.readObject();
                } catch (IOException | ClassNotFoundException e1) {
                    break;
                }
                notifyClients(userName + " 说: \n" + clientmessage.getContent());
            }
            // 如果客户端关闭，则将其从客户端列表中移除
            remove(id);
            notifyClients(userName + " 下线了，下次再找他/她聊天吧~\n");
            display(userName + " 断开了连接，当前在线人数为：" + clientlist.size() + "\n");
        }

        /* 向客户端发送消息的函数 */
        private void sendMessage(Message msg) {
            // 如果客户端未连接，返回false
            if (!socket.isConnected()) {
                return;
            }
            try {
                socketOutput.writeObject(msg);
            } catch (IOException e) {
                display("发送消息给" + userName + "时产生异常" + e + "\n");
            }
        }
    }
}
