package com.sistema.sistemaR1.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class HomeController {

    @GetMapping("/")
    public String rutaIndex() {
        return "index";
    }
}
