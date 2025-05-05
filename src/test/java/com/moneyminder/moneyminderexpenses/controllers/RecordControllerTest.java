package com.moneyminder.moneyminderexpenses.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderexpenses.dto.RecordWithoutDetails;
import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.processors.record.DeleteRecordProcessor;
import com.moneyminder.moneyminderexpenses.processors.record.RetrieveRecordProcessor;
import com.moneyminder.moneyminderexpenses.processors.record.SaveRecordProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.RecordRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecordController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveRecordProcessor retrieveRecordProcessor;

    @MockBean
    private SaveRecordProcessor saveRecordProcessor;

    @MockBean
    private DeleteRecordProcessor deleteRecordProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all records test")
    void getRecords() throws Exception {
        Record record = new Record();
        record.setId("record1");
        record.setName("name");
        record.setOwner("user1");

        Mockito.mockStatic(AuthUtils.class).when(AuthUtils::getUsername).thenReturn("user1");

        Mockito.when(retrieveRecordProcessor.retrieveRecords(any(RecordRequestParams.class), eq("user1")))
                .thenReturn(List.of(record));

        mockMvc.perform(get("/record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("record1"))
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    @DisplayName("get record by id test")
    void getRecordById() throws Exception {
        Record record = new Record();
        record.setId("record1");
        record.setName("Groceries");

        Mockito.when(retrieveRecordProcessor.retrieveRecordById("record1"))
                .thenReturn(record);

        mockMvc.perform(get("/record/record1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("record1"))
                .andExpect(jsonPath("$.name").value("Groceries"));
    }

    @Test
    @DisplayName("get records by budget id test")
    void getRecordsByBudgetId() throws Exception {
        RecordWithoutDetails record = new RecordWithoutDetails();
        record.setId("record1");
        record.setName("Groceries");

        Mockito.when(retrieveRecordProcessor.retrieveRecordByBudgetId("budget1"))
                .thenReturn(List.of(record));

        mockMvc.perform(get("/record/by-budget/budget1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("record1"))
                .andExpect(jsonPath("$[0].name").value("Groceries"));
    }

    @Test
    @DisplayName("create record test")
    void createRecord() throws Exception {
        Record record = new Record();
        record.setName("record");
        record.setMoney(1200.0);
        record.setDate(LocalDate.now());
        record.setType(RecordType.EXPENSE);
        record.setOwner("user1");


        Record savedRecord = new Record();
        savedRecord.setId("record1");
        savedRecord.setName("record");

        Mockito.when(saveRecordProcessor.saveRecord(any(Record.class))).thenReturn(savedRecord);

        mockMvc.perform(post("/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("record1"))
                .andExpect(jsonPath("$.name").value("record"));
    }

    @Test
    @DisplayName("update record test")
    void updateRecord() throws Exception {
        Record record = new Record();
        record.setId("record1");
        record.setName("Utilities");
        record.setMoney(300.0);
        record.setDate(LocalDate.now());
        record.setType(RecordType.EXPENSE);
        record.setOwner("user1");

        Mockito.when(saveRecordProcessor.updateRecord(eq("record1"), any(Record.class))).thenReturn(record);

        mockMvc.perform(put("/record/record1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("record1"))
                .andExpect(jsonPath("$.name").value("Utilities"));
    }

    @Test
    @DisplayName("delete record test")
    void deleteRecord() throws Exception {
        mockMvc.perform(delete("/record/record1"))
                .andExpect(status().isOk());

        Mockito.verify(deleteRecordProcessor).deleteRecord("record1");
    }

}