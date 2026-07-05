package com.example.Bemole_API.service.admin;

import com.example.Bemole_API.dto.admin.request.CategoriaAdminRequestDTO;
import com.example.Bemole_API.dto.admin.response.CategoriaAdminResponseDTO;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.models.Categoria;
import com.example.Bemole_API.repositorys.CategoriaRepository;
import com.example.Bemole_API.repositorys.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminCategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public List<CategoriaAdminResponseDTO> listarCategorias() {
        return categoriaRepository.findAll().stream().map(this::convertir).toList();
    }

    @Transactional
    public CategoriaAdminResponseDTO crearCategoria(CategoriaAdminRequestDTO request) {
        String nombre = request.getNombre().trim();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new NegocioException("Ya existe una categoría con ese nombre.");
        }

        Categoria categoria = new Categoria();

        aplicarDatos(categoria, request);

        return convertir(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaAdminResponseDTO actualizar(Long id, CategoriaAdminRequestDTO request) {
        Categoria categoria = buscar(id);

        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre().trim(), id)) {
            throw new NegocioException("Ya existe otra categoría con ese nombre.");
        }

        aplicarDatos(categoria, request);

        return convertir(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscar(id);

        if (productoRepository.existsByCategoriaId(id)) {
            throw new NegocioException("No puedes eliminar una categoría que contiene productos.");
        }

        categoriaRepository.delete(categoria);
    }

    private Categoria buscar(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Categoría con ID " + id + " no encontrada.")
        );
    }

    private void aplicarDatos(Categoria categoria, CategoriaAdminRequestDTO request) {
        categoria.setNombre(request.getNombre().trim());

        categoria.setDescripcion(request.getDescripcion() != null ? request.getDescripcion().trim() : null);
    }

    private CategoriaAdminResponseDTO convertir(Categoria categoria) {
        return new CategoriaAdminResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                productoRepository.countByCategoriaId(categoria.getId())
        );
    }
}
