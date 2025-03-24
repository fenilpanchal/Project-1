package com.example.Project_1.Controller;

import com.example.Project_1.Model.Employee2;
import com.example.Project_1.Model.Role;
import com.example.Project_1.Repository.Employee2Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("super")
@Slf4j
public class Super_Admin_Controller {

    @Autowired
    private Employee2Repository repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);


    @PostMapping
    public String create(@RequestBody Employee2 employee2){
        log.info("Creating Employee with username: {}",employee2.getUsername());

        employee2.setPassword(encoder.encode(employee2.getPassword()));
        employee2.setEmail(employee2.getEmail());
        employee2.setRole(employee2.getRole());
        repository.save(employee2);
        return employee2.getRole() + " Create Successfully!";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable Integer id,@RequestBody Employee2 employee2){
        Employee2 employee3 =repository.findById(id).orElseThrow(()->
                new RuntimeException(" User Not Found "));
        employee3.setUsername(employee2.getUsername());
        employee3.setPassword(encoder.encode(employee2.getPassword()));
        employee3.setEmail(employee2.getEmail());
        employee3.setRole(employee2.getRole());
        repository.save(employee3);
        return employee2.getRole() + " Update SuccessFully ";
    }

    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable Integer id){
       return repository.findById(id).map( employee2 ->{
            String deleteName = employee2.getUsername();
            Role role = employee2.getRole();
            repository.deleteById(id);
            return role + " ->> "  +  deleteName  +  " = Delete SuccessFully ";
        })
        .orElse(" User Not Found ");
    }
}
