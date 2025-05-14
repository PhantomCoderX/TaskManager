package com.taskmanager.backend.dto;

/**
 * DTO для запроса смены пароля.
 * <pre>
 * {
 *   "oldPass": "старыйПароль",
 *   "newPass": "новыйПароль"
 * }
 * </pre>
 */
public class ChangePasswordDTO {

    private String oldPass;
    private String newPass;

    /* ===== Геттеры и сеттеры ===== */

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
