package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ssk.restvoting.service.VoteService;

@RestController
@RequestMapping(value = {"/voting", "/rest/voting"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class VotingRestController {
    private static final Logger log = LoggerFactory.getLogger(VotingRestController.class);

    @Autowired
    private VoteService voteService;

    @PostMapping(value = "/{restaurantId}")
    public void vote(@PathVariable("restaurantId") int restaurantId) {
        log.info("vote with restaurantId={}", restaurantId);
        voteService.vote(restaurantId);
    }
}
