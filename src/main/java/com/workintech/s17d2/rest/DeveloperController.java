package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.tax.DeveloperTax;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
public class DeveloperController {

    public Map<Integer, Developer> developers;
    private DeveloperTax developerTax;

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
        developers.put(1, new JuniorDeveloper(1, "Tolunay Yılmaz",50));
        developers.put(2, new JuniorDeveloper(2, "Necibe Yılmaz",60));
        developers.put(3, new JuniorDeveloper(3, "Ada Lovelace",70));
    }

    public DeveloperController(DeveloperTax developerTax) {
        this.developerTax = developerTax;
    }

    @GetMapping("/developers")
    public List<Developer> findAll(){
        return developers.values().stream().toList();
    }

    @GetMapping("/developers/{id}")
    public Developer findOne(@PathVariable int id){
        return developers.get(id);
    }

    @PostMapping("/developers")
    @ResponseStatus(HttpStatus.CREATED)
    public Developer create(@RequestBody Developer developer){
        double taxRate = 0;
        if(developer.getExperience() == Experience.JUNIOR){
            taxRate = developerTax.getSimpleTaxRate();
        } else if(developer.getExperience() == Experience.MID){
            taxRate = developerTax.getMiddleTaxRate();
        } else if(developer.getExperience() == Experience.SENIOR){
            taxRate = developerTax.getUpperTaxRate();
        }
        double netSalary = developer.getSalary() * (1 - taxRate / 100);
        developer.setSalary(netSalary);

        developers.put(developer.getId(), developer);
        return developer;
    }

    @PutMapping("/developers/{id}")
    public Developer update(@PathVariable int id,@RequestBody Developer developer){
        developers.put(id,developer);
        return  developers.get(id);
    }

    @DeleteMapping("/developers/{id}")
    public Developer delete(@PathVariable int id){
        return developers.remove(id);
    }
}
