package com.moneyminder.moneyminderexpenses.controllers;

import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.processors.detail.DeleteDetailProcessor;
import com.moneyminder.moneyminderexpenses.processors.detail.RetrieveDetailProcessor;
import com.moneyminder.moneyminderexpenses.processors.detail.SaveDetailProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.DetailRequestParams;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/detail")
@RestController
@RequiredArgsConstructor
public class DetailController {
    private final RetrieveDetailProcessor retrieveDetailProcessor;
    private final SaveDetailProcessor saveDetailProcessor;
    private final DeleteDetailProcessor deleteDetailProcessor;

    @GetMapping()
    public ResponseEntity<List<Detail>> getDetails(final DetailRequestParams params) {
        return ResponseEntity.ok(this.retrieveDetailProcessor.retrieveDetails(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Detail> getDetail(@PathVariable final String id) {
        return ResponseEntity.ok(this.retrieveDetailProcessor.retrieveDetailById(id));
    }

    @PostMapping()
    public ResponseEntity<Detail> createDetail(@Valid @RequestBody final Detail detail) {
        return ResponseEntity.ok(this.saveDetailProcessor.saveDetail(detail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Detail> updateDetail(@PathVariable final String id, @Valid @RequestBody final Detail detail) {
        return ResponseEntity.ok(this.saveDetailProcessor.updateDetail(id, detail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetail(@PathVariable final String id) {
        this.deleteDetailProcessor.deleteDetail(id);
        return ResponseEntity.ok().build();
    }
}
