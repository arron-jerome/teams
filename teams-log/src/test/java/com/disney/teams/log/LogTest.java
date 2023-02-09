package com.disney.teams.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author arron.zhou
 * @version 1.0
 * @date 2022/12/22
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/22      arron.zhou    1.0          create
 */

@SpringJUnitConfig
@Slf4j
public class LogTest {

    @Test
    public void test(){
        log.info("test for warn message");

    }
}
