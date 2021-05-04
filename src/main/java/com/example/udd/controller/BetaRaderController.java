package com.example.udd.controller;

import com.example.udd.dto.BetaReaderDTO;
import com.example.udd.dto.PointDTO;
import com.example.udd.services.BetaReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/beta-reader")
public class BetaRaderController {
    @Autowired
    BetaReaderService betaReaderService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> addBetaReader(@RequestBody BetaReaderDTO betaReaderDTO) throws IOException {
        return betaReaderService.addReader(betaReaderDTO);
    }

    @PostMapping(value = "/getBetaReadersGeo")
    public ResponseEntity<?> getBetaReaders(@RequestBody PointDTO pointDTO) throws IOException {
        return betaReaderService.getReadersQuery(pointDTO);
    }
}
