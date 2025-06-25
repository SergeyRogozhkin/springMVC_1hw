package org.example.springmvc_jsonview.dto;

public class Views {
    public interface UserSummary {}      // Только имя, email
    public interface UserDetails extends UserSummary {}  // Плюс заказы
}
