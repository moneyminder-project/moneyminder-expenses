package com.moneyminder.moneyminderexpenses.feignClients;

import com.moneyminder.moneyminderexpenses.dto.CreateGroupByUsernameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name= "groupService", url = "localhost:18080/api/users/group", configuration = FeignConfig.class)
public interface GroupFeignClient {

    @GetMapping("/by-username/{username}")
    List<String> getGroupIdsByUsername(@PathVariable("username") String username);

    @PostMapping("/budget")
    String createGroupForBudget(@RequestBody final CreateGroupByUsernameDto infoGroup);
}
