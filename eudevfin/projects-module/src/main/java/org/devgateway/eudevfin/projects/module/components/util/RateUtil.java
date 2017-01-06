/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.util;

import java.math.RoundingMode;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;


public class RateUtil {
    public static String moneyToString(BigMoney money) {
        return money.getAmount().setScale(2, RoundingMode.HALF_EVEN).toString();
    }
    
    public static LocalDateTime getStartOfMonth(LocalDateTime commitmentDate) {
        return commitmentDate.minusDays(commitmentDate.getDayOfMonth()-1);
    }
}
