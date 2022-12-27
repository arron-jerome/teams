package com.disney.teams.utils.type;

import com.disney.teams.utils.DecimalFormatUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/17
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/17       arron.zhou      1.0.0          create
 */
public class DecimalFormatUtilsTest {

    @Test
    public void format() throws Exception {
        double a = 91.87421;
        Assert.assertEquals("91.87", DecimalFormatUtils.formatTwoFraction(a));
    }

    @Test
    public void formatWithZero() throws Exception {
        double a = 91;
        Assert.assertEquals("91.00", DecimalFormatUtils.formatTwoFractionWithZero(a));
    }
}
