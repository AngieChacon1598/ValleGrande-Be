package pe.edu.vallegrande.RestLosPinos.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    
    /**
     * Gets the current user ID from the security context
     * @return The user ID as Integer, or null if not authenticated
     */
    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof Integer) {
            return (Integer) authentication.getCredentials();
        }
        return null;
    }
    
    /**
     * Gets the current user's role from the security context
     * @return The role as String, or null if not authenticated
     */
    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getAuthorities().isEmpty()) {
            return authentication.getAuthorities().iterator().next().getAuthority();
        }
        return null;
    }
    
    /**
     * Checks if the current user is an administrator
     * @return true if the user has "Administrador" role, false otherwise
     */
    public static boolean isAdmin() {
        return "Administrador".equals(getCurrentUserRole());
    }
} 