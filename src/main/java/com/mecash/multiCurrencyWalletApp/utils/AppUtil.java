package com.mecash.multiCurrencyWalletApp.utils;

import com.mecash.multiCurrencyWalletApp.constants.APP_CONSTANT;
import com.mecash.multiCurrencyWalletApp.exceptions.UserNotFoundException;
import com.mecash.multiCurrencyWalletApp.models.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtil {
    public static AppUser getCurrentlyLoggedInUser() {
        try {
            return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            throw new UserNotFoundException(APP_CONSTANT.USER_NOT_AUTHENTICATED);
        }
    }


}
