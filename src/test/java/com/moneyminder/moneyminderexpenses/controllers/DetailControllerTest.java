package com.moneyminder.moneyminderexpenses.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderexpenses.controllers.DetailController;
import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.processors.detail.DeleteDetailProcessor;
import com.moneyminder.moneyminderexpenses.processors.detail.RetrieveDetailProcessor;
import com.moneyminder.moneyminderexpenses.processors.detail.SaveDetailProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.DetailRequestParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DetailController.class)
@AutoConfigureMockMvc(addFilters = false)
class DetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveDetailProcessor retrieveDetailProcessor;

    @MockBean
    private SaveDetailProcessor saveDetailProcessor;

    @MockBean
    private DeleteDetailProcessor deleteDetailProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all details test")
    void getDetails() throws Exception {
        Detail detail = new Detail();
        detail.setId("detail1");
        detail.setName("Detail Name");

        Mockito.when(retrieveDetailProcessor.retrieveDetails(any(DetailRequestParams.class)))
                .thenReturn(List.of(detail));

        mockMvc.perform(get("/detail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("detail1"))
                .andExpect(jsonPath("$[0].name").value("Detail Name"));
    }

    @Test
    @DisplayName("get detail by id test")
    void getDetailById() throws Exception {
        Detail detail = new Detail();
        detail.setId("detail1");
        detail.setName("Detail Name");

        Mockito.when(retrieveDetailProcessor.retrieveDetailById("detail1"))
                .thenReturn(detail);

        mockMvc.perform(get("/detail/detail1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("detail1"))
                .andExpect(jsonPath("$.name").value("Detail Name"));
    }

    @Test
    @DisplayName("create detail test")
    void createDetail() throws Exception {
        Detail detail = new Detail();
        detail.setName("New Detail");
        detail.setPricePerUnit(10.0);
        detail.setUnits(2);
        detail.setRecord("record1");

        Detail savedDetail = new Detail();
        savedDetail.setId("detail1");
        savedDetail.setName("New Detail");

        Mockito.when(saveDetailProcessor.saveDetail(any(Detail.class))).thenReturn(savedDetail);

        mockMvc.perform(post("/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detail)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("detail1"))
                .andExpect(jsonPath("$.name").value("New Detail"));
    }

    @Test
    @DisplayName("update detail test")
    void updateDetail() throws Exception {
        Detail detail = new Detail();
        detail.setId("detail1");
        detail.setName("Updated Detail");
        detail.setPricePerUnit(12.0);
        detail.setUnits(1);
        detail.setRecord("record1");

        Mockito.when(saveDetailProcessor.updateDetail(eq("detail1"), any(Detail.class)))
                .thenReturn(detail);

        mockMvc.perform(put("/detail/detail1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detail)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("detail1"))
                .andExpect(jsonPath("$.name").value("Updated Detail"));
    }

    @Test
    @DisplayName("delete detail test")
    void deleteDetail() throws Exception {
        mockMvc.perform(delete("/detail/detail1"))
                .andExpect(status().isOk());

        Mockito.verify(deleteDetailProcessor).deleteDetail("detail1");
    }
}