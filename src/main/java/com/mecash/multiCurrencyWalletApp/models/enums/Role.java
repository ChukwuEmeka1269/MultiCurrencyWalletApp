package com.mecash.multiCurrencyWalletApp.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mecash.multiCurrencyWalletApp.models.enums.Permissions.ADMIN_CREATE;
import static com.mecash.multiCurrencyWalletApp.models.enums.Permissions.USER_CREATE;

@RequiredArgsConstructor
public enum Role {

    USER(Set.of(USER_CREATE)), ADMIN(Set.of(ADMIN_CREATE));

    @Getter
    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(this.name()));

        return authorities;
    }
}
