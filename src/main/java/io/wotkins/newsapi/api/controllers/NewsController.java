package io.wotkins.newsapi.api.controllers;

import io.wotkins.newsapi.api.exceprions.InvalidParseGapException;
import io.wotkins.newsapi.api.exceprions.InvalidResourceException;
import io.wotkins.newsapi.api.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journals")
public class NewsController {

    @Autowired
    private NewsService newsService;


    @GetMapping("/{res}")
    public ResponseEntity getVcNews(@PathVariable String res,
                                    @RequestParam(name = "hours", required = false, defaultValue = "12") Integer hours){
        try
        {
            return ResponseEntity.ok(newsService.getNewsForGap(hours, res));
        } catch (InvalidParseGapException | InvalidResourceException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Ошибка 500");
        }
    }
}
