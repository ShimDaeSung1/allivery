package org.zerock.allivery.entity.user;

import lombok.Getter;
import net.bytebuddy.build.Plugin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.zerock.allivery.entity.role.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@AuthenticationPrincipal용
@Getter
public class UserAccount extends User {

    private org.zerock.allivery.entity.user.User user;

    public UserAccount(org.zerock.allivery.entity.user.User user) {
        super(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+user.getRoles())));
        this.user = user;
    }

    private static Collection<? extends GrantedAuthority> authorities(Set<UserRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }

    public org.zerock.allivery.entity.user.User getUser(){
        return user;
    }

}
