package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ssk.restvoting.service.VoteService;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = {"/voting", "/rest/voting"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class VotingRestController {

    private static final Logger log = LoggerFactory.getLogger(VotingRestController.class);

    @Autowired
    VoteService voteService;

    @PostMapping(value = "/{restaurantId}")
    public void castVote(@PathVariable("restaurantId") int restaurantId,
                         @RequestParam("voteDateTime") LocalDateTime voteDateTime)
    {
        log.info("cast vote with restaurantId={}", restaurantId);
        voteService.castVote(restaurantId, voteDateTime);
    }

}
