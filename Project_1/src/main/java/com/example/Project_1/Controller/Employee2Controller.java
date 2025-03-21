package com.example.Project_1.Controller;

import com.example.Project_1.Model.Employee2;
import com.example.Project_1.Dto.EmployeeDto;
import com.example.Project_1.Services.Employee2Services;
import com.example.Project_1.Services.JWTServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/Employee")
public class Employee2Controller {

    @Autowired
    public Employee2Services employee2Services;

    @Autowired
    private JWTServices jwtServices;

///Dto
    @GetMapping("/get")
    public List<EmployeeDto> getAll(){
        return employee2Services.getAll();
    }

    @PostMapping("/add")
    public EmployeeDto create(@RequestBody EmployeeDto employeeDto){
        return employee2Services.createEmployee(employeeDto);
    }
///
    @GetMapping
    public List<Employee2> Get(){
        return employee2Services.GetAll();
    }

    @GetMapping("/{id}")
    public Employee2 GetByIdD(@PathVariable Integer id){
        return employee2Services.GetById(id).orElseThrow();
    }

    @PostMapping("/register")
    public ResponseEntity<Employee2> register(@RequestBody Employee2 employee,
                                              @RequestParam(required = false, defaultValue = "USER") String role){
        return ResponseEntity.ok(employee2Services.register(employee, role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@PathVariable Integer id,@RequestBody Employee2 employee2,@RequestHeader("Authorization") String token) {

        log.info("Update request received for Employee ID: {}", id);

        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("Unauthorized access attempt!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Please log in.");
        }
        token = token.substring(7);
        List<String> roles = jwtServices.extraRole(token);

        if (!roles.contains("ADMIN")) {
            log.warn("Access Denied: Only Admin can update users.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only Admin can update users.");
        }

        Employee2 updatedEmployee = employee2Services.UpdateE(id, employee2);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id,@RequestHeader("Authorization") String token){
        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("Unauthorized access attempt!!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Please log in.");
        }
        token = token.substring(7);

        List<String> roles = jwtServices.extraRole(token);
        log.info("Extracted roles from token: {}", roles);

        if (!roles.contains("ADMIN")) {
            log.warn("Access Denied: Only Admin can delete users..");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only Admin can delete users.");
        }
        employee2Services.Delete(id,"ADMIN");
        return ResponseEntity.noContent().build();
    }

/// Search
    @GetMapping("/search")
    public ResponseEntity<List<Employee2>>Search(@RequestParam String keyword){
        if (keyword.length()< 2){
            return ResponseEntity.badRequest().body(null);
        }
        List<Employee2> name = employee2Services.searchName(keyword);
        return ResponseEntity.ok(name);
    }
/// login , logout
    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody Employee2 employee2 ){
         return ResponseEntity.ok(employee2Services.login(employee2));
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<String> logout(@PathVariable Integer id,@RequestHeader("Authorization") String token){
       Optional<Employee2> emp = employee2Services.GetById(id);

        if (emp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Employee2 employee2 = emp.get();

        String usernameFormToken = jwtServices.First(token.substring(7));

        if (!employee2.getUsername().equals(usernameFormToken)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user. You can only logout with your own User-Id.");
        }
        return ResponseEntity.ok( "Log-out SuccessFully .. ");
    }
}