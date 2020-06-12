package com.spring.crud.rest.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.crud.rest.api.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> 
{
	Optional<ImageModel> findByName(String name);
}
