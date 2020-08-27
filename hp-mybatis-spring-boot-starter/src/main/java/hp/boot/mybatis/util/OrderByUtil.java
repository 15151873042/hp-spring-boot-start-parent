package hp.boot.mybatis.util;

import java.util.List;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;

public class OrderByUtil {
    
    /**
     * convert to order by sql
     *
     * @param sql 原SQL
     * @param orderBy orderBy的SQL字符串，例：'id desc, name asc'
     * @return
     */
    public static String converToOrderBySql(String sql, String orderBy,String dbType) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support order by : " + sql);
        }
        SQLStatement stmt = stmtList.get(0);
        if (!(stmt instanceof SQLSelectStatement)) {
            throw new IllegalArgumentException("sql not support order by : " + sql);
        }
        SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;
        SQLSelect select = selectStmt.getSelect();
        if (select.getOrderBy() != null) {
            select.setOrderBy(null);
        }
        SQLSelectQuery query = select.getQuery();
        clearOrderBy(query);
        sql = SQLUtils.toSQLString(query, dbType);
        return sql + " order by " + orderBy;
    }

    public static void clearOrderBy(SQLSelectQuery query) {
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) query;
            if (queryBlock instanceof MySqlSelectQueryBlock) {
                MySqlSelectQueryBlock mysqlQueryBlock = (MySqlSelectQueryBlock) queryBlock;
                if (mysqlQueryBlock.getOrderBy() != null) {
                    mysqlQueryBlock.setOrderBy(null);
                }
            } else if (queryBlock instanceof PGSelectQueryBlock) {
                PGSelectQueryBlock pgQueryBlock = (PGSelectQueryBlock) queryBlock;
                if (pgQueryBlock.getOrderBy() != null) {
                    pgQueryBlock.setOrderBy(null);
                }
            }
            return;
        }

        if (query instanceof SQLUnionQuery) {
            SQLUnionQuery union = (SQLUnionQuery) query;
            if (union.getOrderBy() != null) {
                union.setOrderBy(null);
            }
            clearOrderBy(union.getLeft());
            clearOrderBy(union.getRight());
        }
    }

    public static void main(String[] ags){
        System.out.println(converToOrderBySql("select t.id,t.client_code,t.bill_no,t.bill_medium,t.bill_type,t.bill_draft_date,t.bill_due_date,t.holiday_count,t.bill_amount, t.bill_amount_upper,t.bill_drawer_name,t.bill_drawer_account,t.bill_payee_name,t.bill_payee_account,t.bill_payee_bank, t.bill_drawee_bank,t.bill_drawee_bank_type,t.bill_drawee_bank_no,t.bill_limit,t.bill_status,t.version,t.in_user,t.in_name, t.in_time,t.out_user,t.out_name,t.out_time,t.bill_case,t.bill_case_remarks,t.bill_accept_no,t.bill_drawee_bank_addr, t.bill_drawer_bank,t.bill_drawee_acount,t.create_id,t.create_name,t.create_time,t.update_id,t.update_name,t.update_time from v_bill t order by t.client_code,t.bill_medium"
            ,"t.id desc","mysql"));
    }
}
