package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Categoria não existe"));
    }

    public List<Category> findAll(List<Long> categoriesId){
        return categoryRepository.findAllById(categoriesId);
    }
}
