package dbunitSample;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;

public class Main {

    public static void main(String[] args) throws Exception {

        // ファイルオープン
        String PATH = "C:\\temp\\import_test.xlsx";
        Path path = Paths.get(PATH);
        File f = path.toFile();

        // DB接続情報
        String url = "jdbc:postgresql://localhost:154322/postgres";
        String user = "postgres";
        String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);) {

            try {
                IDatabaseConnection dbconn = new DatabaseConnection(conn);

                IDataSet dataset = new XlsDataSet(f);

                // データの全削除
                DatabaseOperation.DELETE_ALL.execute(dbconn, dataset);

                // データの挿入
                DatabaseOperation.INSERT.execute(dbconn, dataset);

                System.out.println("完了");
            } catch (Exception e) {
                conn.rollback(); // ここでは、connはクローズ済みなので例外がthrowされる！
            }
        }
    }
}
