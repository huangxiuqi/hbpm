package com.hbpm.uua.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

/**
 * @author huangxiuqi
 */
public class CustomUserDetails extends User {

    private static final long serialVersionUID = 1L;

    private final Long id;

    public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(id, username, password, authorities, true, true, true, true);
    }

    public CustomUserDetails(Long id,
                             String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             boolean accountNonExpired,
                             boolean accountNonLocked,
                             boolean credentialsNonExpired,
                             boolean enabled) {
        super(username, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomUserDetails)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(" [");
        sb.append("Id=").append(getId()).append(", ");
        sb.append("Username=").append(this.getUsername()).append(", ");
        sb.append("Password=[PROTECTED], ");
        sb.append("Enabled=").append(this.isEnabled()).append(", ");
        sb.append("AccountNonExpired=").append(this.isAccountNonExpired()).append(", ");
        sb.append("credentialsNonExpired=").append(this.isCredentialsNonExpired()).append(", ");
        sb.append("AccountNonLocked=").append(this.isAccountNonLocked()).append(", ");
        sb.append("Granted Authorities=").append(this.getAuthorities()).append("]");
        return sb.toString();
    }
}
