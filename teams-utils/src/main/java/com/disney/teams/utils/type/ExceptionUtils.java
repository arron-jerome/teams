package com.disney.teams.utils.type;

import com.disney.teams.exception.BasicRuntimeException;
import com.disney.teams.model.vo.BaseStatusCode;
import com.disney.teams.utils.io.CloseUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public class ExceptionUtils {

    public static void throwUncheck(Throwable e) {
        throw uncheck(e);
    }

    public static RuntimeException uncheck(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
            return (e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e));
        } else {
            return new RuntimeException(e);
        }
    }

    public static BasicRuntimeException basic(Throwable e) {
        return basic(e, BaseStatusCode.SERVER_ERROR_MSG);
    }

    public static BasicRuntimeException basic(Throwable e, String defaultError) {
        if (e instanceof BasicRuntimeException) {
            return (BasicRuntimeException) e;
        } else if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
            return (e instanceof BasicRuntimeException ?
                    (BasicRuntimeException) e : new BasicRuntimeException(defaultError, e));
        } else {
            return new BasicRuntimeException(defaultError, e);
        }
    }

    public static String toString(Throwable e) {
        if (e == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (
                PrintStream ps = new PrintStream(bos)
        ) {
            e.printStackTrace(ps);
            return bos.toString("UTF-8");
        } catch (UnsupportedEncodingException exp) {
            return bos.toString();
        } finally {
            CloseUtils.close(bos);
        }
    }
}
