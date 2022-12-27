package com.disney.teams.utils.type;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
public class DateUtilsTest {

    @Test
    public void format(){
        Date date = DateUtils.parse("2019-12-31", "yyyy-MM-dd");
        System.out.println(DateUtils.format(date, "YYYY-MM-dd"));
        System.out.println(DateUtils.format(date, "yyyy-MM-dd"));
    }

    @Test
    public void parse(){
        String datestr = "Jun 30, 2016 07:51:24 PM";
        Date date = DateUtils.parse(datestr, "MMM dd, yyyy h:mm:ss a");
        System.out.println(date);
    }

    @Test
    public void split() {
        String text = "----卡夫卡,F.(1883-1924)-文学研究-";
        System.out.println(JSON.toJSONString(split(text)));
    }

    public List<String> split(String text) {
        if(text == null || text.length() < 1) {
            return Arrays.asList(text);
        }
        final char c = '-';
        int index = text.indexOf(c);
        if(index < 0 || index == text.length()) {
            return Arrays.asList(text);
        }

        List<String> split = new ArrayList<>();
        int start = 0, len = text.length();
        for( ; ; ) {
            while(index > 0) {
                if(index + 1 == len) {
                    split.add(text.substring(start, index));
                    return split;
                }
                char s = text.charAt(index - 1);
                char e = text.charAt(index + 1);
                if(s > '0' && s < '9' && e > '0' && e < '9') {
                    index = text.indexOf(c, index + 1);
                } else {
                    break;
                }
            }
            if(index < 0) {
                split.add(text.substring(start, len));
                return split;
            } else {
                if(start != index) {
                    split.add(text.substring(start, index));
                }
                start = index + 1;
                index = text.indexOf(c, start);
            }
        }
    }

}
