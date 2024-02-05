import java.io.Serializable;

/* Message 类是在服务器和客户端之间传递的消息的抽象 */
public class Message implements Serializable {
    // 使用我的学号作为 serialVersionUID
    protected static final long serialVersionUID = 3210102037L;
    // 定义消息的两种类型常量
    static final int MESSAGE = 0, USERLIST = 1;
    // 消息的类型
    private final int type;
    // 消息的实际内容
    private final String content;

    /* 构造函数 */
    Message(int type, String content) {
        this.type = type;
        this.content = content;
    }

    /* 获取消息类型的方法 */
    int getType() {
        return type;
    }

    /* 获取消息内容的方法 */
    String getContent() {
        return content;
    }
}
