package com.exodus.dome.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void userRole_shouldHaveThreeValues() {
        UserRole[] values = UserRole.values();

        assertEquals(3, values.length);
    }

    @Test
    void userRole_rider_shouldExist() {
        UserRole rider = UserRole.RIDER;

        assertEquals("RIDER", rider.name());
        assertEquals(0, rider.ordinal());
    }

    @Test
    void userRole_driver_shouldExist() {
        UserRole driver = UserRole.DRIVER;

        assertEquals("DRIVER", driver.name());
        assertEquals(1, driver.ordinal());
    }

    @Test
    void userRole_admin_shouldExist() {
        UserRole admin = UserRole.ADMIN;

        assertEquals("ADMIN", admin.name());
        assertEquals(2, admin.ordinal());
    }

    @Test
    void userRole_valueOf_shouldReturnCorrectEnum() {
        assertEquals(UserRole.RIDER, UserRole.valueOf("RIDER"));
        assertEquals(UserRole.DRIVER, UserRole.valueOf("DRIVER"));
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
    }

    @Test
    void userRole_valueOf_withInvalidValue_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserRole.valueOf("INVALID");
        });
    }

    @Test
    void userRole_values_shouldReturnAllEnums() {
        UserRole[] roles = UserRole.values();

        assertTrue(containsRole(roles, UserRole.RIDER));
        assertTrue(containsRole(roles, UserRole.DRIVER));
        assertTrue(containsRole(roles, UserRole.ADMIN));
    }

    private boolean containsRole(UserRole[] roles, UserRole target) {
        for (UserRole role : roles) {
            if (role == target) {
                return true;
            }
        }
        return false;
    }
}
