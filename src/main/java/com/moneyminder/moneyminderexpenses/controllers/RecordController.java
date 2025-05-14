package com.moneyminder.moneyminderexpenses.controllers;

import com.moneyminder.moneyminderexpenses.dto.RecordWithoutDetails;
import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.processors.record.DeleteRecordProcessor;
import com.moneyminder.moneyminderexpenses.processors.record.RetrieveRecordProcessor;
import com.moneyminder.moneyminderexpenses.processors.record.SaveRecordProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.RecordRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/record")
@RestController
@RequiredArgsConstructor
public class RecordController {
    private final RetrieveRecordProcessor retrieveRecordProcessor;
    private final SaveRecordProcessor saveRecordProcessor;
    private final DeleteRecordProcessor deleteRecordProcessor;

    @GetMapping()
    public ResponseEntity<List<Record>> getRecords(final RecordRequestParams params) {
        return ResponseEntity.ok(this.retrieveRecordProcessor.retrieveRecords(params, AuthUtils.getUsername()));
    }

    @GetMapping( "/{id}")
    public ResponseEntity<Record> getRecord(@PathVariable final String id) {
        return ResponseEntity.ok(this.retrieveRecordProcessor.retrieveRecordById(id));
    }

    @GetMapping("/by-budget/{id}")
    public ResponseEntity<List<RecordWithoutDetails>> getRecordsByBudget(@PathVariable final String id) {
        return ResponseEntity.ok(this.retrieveRecordProcessor.retrieveRecordByBudgetId(id));
    }

    @PostMapping()
    public ResponseEntity<Record> createRecord(@Valid @RequestBody final Record record) {
        return ResponseEntity.ok(this.saveRecordProcessor.saveRecord(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Record> updateRecord(@PathVariable final String id, @Valid @RequestBody final Record record) {
        return ResponseEntity.ok(this.saveRecordProcessor.updateRecord(id, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable final String id) {
        this.deleteRecordProcessor.deleteRecord(id);
        return ResponseEntity.ok().build();
    }
}
