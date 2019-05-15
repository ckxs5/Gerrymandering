package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.BoundaryRepository;
import com.example.gerrymanderdemo.model.Data.Boundary;
import org.springframework.stereotype.Service;

@Service
public class BoundaryService {
    private BoundaryRepository boundaryRepository;

    public BoundaryService(BoundaryRepository boundaryRepository) {
        this.boundaryRepository = boundaryRepository;
    }

    public Boundary save(Boundary boundary) {
        return boundaryRepository.save(boundary);
    }
}
