package com.moneyminder.moneyminderexpenses.feignClients;

import com.moneyminder.moneyminderexpenses.dto.CreateGroupByUsernameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name= "groupService", url = "${app.group-service-url}", configuration = FeignConfig.class)
public interface GroupFeignClient {

    @GetMapping("/by-username/{username}")
    List<String> getGroupIdsByUsername(@PathVariable("username") final String username);

    @PostMapping("/budget")
    String createGroupForBudget(@RequestBody final CreateGroupByUsernameDto infoGroup);

    @DeleteMapping("/{id}")
    Void deleteGroup(@PathVariable("id") final String id);
}
