package hp.boot.web.controller;

import com.hp.common.core.vo.ExecuteResult;
import com.hp.common.core.vo.JsonResult;
import com.hp.common.core.vo.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName: BaseController
 * @Description: 基础controller
 * @author Administrator
 *
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public JsonResult copyToJsonResult(ExecuteResult<?> executeResult) {
        return new JsonResult(executeResult.getRtnEnum(), executeResult.getData(), executeResult.getMsg());
    }

    public JsonResult copyToJsonResultNoData(ExecuteResult<?> executeResult) {
        return new JsonResult(executeResult.getRtnEnum(), null, executeResult.getMsg());
    }

    public <T> RestResult<T> copyToRestResult(ExecuteResult<T> executeResult) {
        return new RestResult<>(executeResult.getRtnEnum(), executeResult.getData(), executeResult.getMsg());
    }

    public <T> RestResult<T> copyToRestResultNoData(ExecuteResult<T> executeResult) {
        return new RestResult<>(executeResult.getRtnEnum(), null, executeResult.getMsg());
    }
}
