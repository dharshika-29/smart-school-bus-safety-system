package com.busfleet.smartbussafety.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.busfleet.smartbussafety.dto.ParentDTO;
import com.busfleet.smartbussafety.entity.Parent;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.ParentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;

    public List<ParentDTO> getAllParents() {
        return parentRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ParentDTO createParent(ParentDTO dto) {
        Parent parent = new Parent();
        parent.setName(dto.getName());
        parent.setPhone(dto.getPhone());
        parent.setEmail(dto.getEmail());
        return toDTO(parentRepository.save(parent));
    }

    public ParentDTO updateParent(Long id, ParentDTO dto) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Parent not found"));
        parent.setName(dto.getName());
        parent.setPhone(dto.getPhone());
        parent.setEmail(dto.getEmail());
        return toDTO(parentRepository.save(parent));
    }

    public void deleteParent(Long id) {
        if (!parentRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Parent not found");
        }
        parentRepository.deleteById(id);
    }

    private ParentDTO toDTO(Parent parent) {
        ParentDTO dto = new ParentDTO();
        dto.setId(parent.getId());
        dto.setName(parent.getName());
        dto.setPhone(parent.getPhone());
        dto.setEmail(parent.getEmail());
        return dto;
    }
}