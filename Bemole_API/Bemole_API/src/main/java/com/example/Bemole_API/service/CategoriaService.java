package com.example.Bemole_API.service;

import com.example.Bemole_API.models.Categoria;
import com.example.Bemole_API.repositorys.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> listarCategorias() {
        return repository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo.");
        }
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría con ID " + id + " no encontrada."));
    }

    public Categoria crearCategoria(Categoria categoria) {
        validarCategoria(categoria);

        String nombreNormalizado = categoria.getNombre().trim();
        if (repository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nombreNormalizado);
        }

        categoria.setNombre(nombreNormalizado);
        if (categoria.getDescripcion() != null) {
            categoria.setDescripcion(categoria.getDescripcion().trim());
        }

        return repository.save(categoria);
    }

    public Categoria editarCategoria(Long id, Categoria categoriaParcial) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo.");
        }
        if (categoriaParcial == null) {
            throw new IllegalArgumentException("Los datos de la categoría no pueden ser nulos.");
        }

        return repository.findById(id).map(categoria -> {

            if (categoriaParcial.getNombre() != null) {
                String nuevoNombre = categoriaParcial.getNombre().trim();
                if (nuevoNombre.isEmpty()) {
                    throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
                }
                if (nuevoNombre.length() > 100) {
                    throw new IllegalArgumentException("El nombre de la categoría no puede superar los 100 caracteres.");
                }
                // Verificar duplicado solo si el nombre cambió
                if (!nuevoNombre.equalsIgnoreCase(categoria.getNombre())
                        && repository.existsByNombreIgnoreCase(nuevoNombre)) {
                    throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nuevoNombre);
                }
                categoria.setNombre(nuevoNombre);
            }

            if (categoriaParcial.getDescripcion() != null) {
                String nuevaDesc = categoriaParcial.getDescripcion().trim();
                if (nuevaDesc.length() > 500) {
                    throw new IllegalArgumentException("La descripción no puede superar los 500 caracteres.");
                }
                categoria.setDescripcion(nuevaDesc);
            }

            return repository.save(categoria);

        }).orElseThrow(() -> new RuntimeException("Categoría con ID " + id + " no encontrada."));
    }

    public void eliminarCategoria(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo.");
        }
        if (!repository.existsById(id)) {
            throw new RuntimeException("Categoría con ID " + id + " no encontrada.");
        }
        repository.deleteById(id);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        if (categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        if (categoria.getNombre().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre de la categoría no puede superar los 100 caracteres.");
        }
        if (categoria.getDescripcion() != null && categoria.getDescripcion().trim().length() > 500) {
            throw new IllegalArgumentException("La descripción no puede superar los 500 caracteres.");
        }
    }
}