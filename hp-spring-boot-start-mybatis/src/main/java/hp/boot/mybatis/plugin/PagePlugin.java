package hp.boot.mybatis.plugin;

import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.util.JdbcConstants;

import hp.boot.mybatis.pojo.Page;
import hp.boot.mybatis.util.MSUtil;
import hp.boot.mybatis.util.OrderByUtil;

@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class}))
public class PagePlugin implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(PagePlugin.class);

    private String dialect = JdbcConstants.MYSQL; //数据库方言

    private String pageSqlId = ".*.ListPage"; //mapper.xml中需要拦截的ID(正则匹配)

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Object intercept(Invocation ivk) throws Throwable {
        //获取拦截方法的参数
        Object[] args = ivk.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if (ms.getId().matches(pageSqlId)) { //拦截需要分页的SQL
            Object parameterObject = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];
            if (parameterObject == null) {
                throw new NullPointerException("parameterObject尚未实例化！");
            }
            BoundSql boundSql = ms.getBoundSql(parameterObject);
            String sql = boundSql.getSql();
            Page<?> page = null;
            if (parameterObject instanceof Page) { //参数就是Page实体
                page = (Page<?>) parameterObject;
            }
            //当前的目标对象
            Executor executor = (Executor) ivk.getTarget();
            //判断是否获取记录总数
            if (page.isAutoCount()) {
                //创建 count 查询的缓存 key
                CacheKey countKey = executor.createCacheKey(ms, parameterObject, RowBounds.DEFAULT, boundSql);
                countKey.update("_Count");
                //根据当前的 ms 创建一个返回值为 Long 类型的 ms
                MappedStatement countMs = MSUtil.newCountMappedStatement(ms);
                //调用方言获取 count sql
                String countSql = PagerUtils.count(sql, dialect);
                BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), parameterObject);
                //执行 count 查询
                Object countResultList = executor.query(countMs, parameterObject, RowBounds.DEFAULT,
                    resultHandler, countKey, countBoundSql);
                Long count = (Long) ((List) countResultList).get(0);
                page.setTotalCount(count);
            }
            //生成分页的缓存 key
            CacheKey pageKey = executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);
            //调用方言获取分页 sql
            String pageSql = generatePageSql(sql, page, pageKey);
            BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql,
                boundSql.getParameterMappings(), parameterObject);
            //执行分页查询
            return executor.query(ms, parameterObject, RowBounds.DEFAULT, resultHandler, pageKey,
                pageBoundSql);
        }
        return ivk.proceed();
    }

    /**
     * 根据数据库方言，生成特定的分页sql
     * @param sql
     * @param page
     * @param pageKey
     * @return
     */
    private String generatePageSql(String sql, Page<?> page, CacheKey pageKey) {
        if (page != null && StringUtils.isNotEmpty(dialect)) {
            if (StringUtils.isNotBlank(page.getSidx())) {
                String orderBy = page.getSidx();
                pageKey.update(orderBy);
                sql = OrderByUtil.converToOrderBySql(sql, orderBy, dialect);
            }
            pageKey.update(page.getFirst());
            pageKey.update(page.getPageSize());
            sql = PagerUtils.limit(sql, dialect, page.getFirst(), page.getPageSize());
        }
        return sql;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties p) {
        dialect = p.getProperty("dialect");
        if (StringUtils.isEmpty(dialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                logger.error(e.getMessage(), e);
            }
        }
        pageSqlId = p.getProperty("pageSqlId");
        if (StringUtils.isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getPageSqlId() {
        return pageSqlId;
    }

    public void setPageSqlId(String pageSqlId) {
        this.pageSqlId = pageSqlId;
    }
}
