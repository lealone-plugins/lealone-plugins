import java.sql.SQLException;
import java.util.ArrayList;

public class H2TestServer {

    public static void main(String[] args) throws SQLException {
        setH2Properties();

        ArrayList<String> list = new ArrayList<String>();
        // list.add("-tcp");
        // //list.add("-tool");
        // org.h2.tools.Server.main(list.toArray(new String[list.size()]));
        //
        list.add("-tcp");
        list.add("-tcpPort");
        list.add("9092");
        list.add("-tcpAllowOthers");

        list.add("-pg");
        list.add("-pgPort");
        list.add("9192");
        list.add("-pgAllowOthers");

        // 测试org.h2.server.TcpServer.checkKeyAndGetDatabaseName(String)
        // list.add("-key");
        // list.add("mydb");
        // list.add("mydatabase");

        // list.add("-pg");
        // list.add("-tcp");
        // list.add("-web");
        // list.add("-ifExists");
        list.add("-ifNotExists");
        org.h2.tools.Server.main(list.toArray(new String[list.size()]));
    }

    public static void setH2Properties() {
        System.setProperty("h2.queryCacheSize", "0");
        // System.setProperty("DATABASE_TO_UPPER", "false");
        System.setProperty("h2.lobInDatabase", "false");
        System.setProperty("h2.lobClientMaxSizeMemory", "1024");
        System.setProperty("java.io.tmpdir", "E:/h2/h2database/target/tmp");
        System.setProperty("h2.baseDir", "E:/h2/h2database/target/data2");
        // System.setProperty("h2.check2", "true");
    }
}