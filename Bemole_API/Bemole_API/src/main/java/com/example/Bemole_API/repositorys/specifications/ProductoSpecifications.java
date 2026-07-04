package com.example.Bemole_API.repositorys.specifications;

import com.example.Bemole_API.models.Producto;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductoSpecifications {
    private ProductoSpecifications() {
    }

    public static Specification<Producto> activo() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("activo"));
    }

    public static Specification<Producto> nombreContiene(
            String texto
    ) {
        return (root, query, criteriaBuilder) -> {
            if (texto == null || texto.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String valor = "%" + texto.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("nombre")),
                            valor
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("descripcion")),
                            valor
                    )
            );
        };
    }

    public static Specification<Producto> categoriaIgual(
            Long categoriaId
    ) {
        return (root, query, criteriaBuilder) -> {
            if (categoriaId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("categoria").get("id"),
                    categoriaId
            );
        };
    }

    public static Specification<Producto> precioMayorOIgual(BigDecimal precioMin) {
        return (root, query, criteriaBuilder) -> {
            if (precioMin == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("precio"),
                    precioMin
            );
        };
    }

    public static Specification<Producto> precioMenorOIgual(BigDecimal precioMax) {
        return (root, query, criteriaBuilder) -> {
            if (precioMax == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("precio"),
                    precioMax
            );
        };
    }

    public static Specification<Producto> conStock(Boolean soloConStock) {
        return (root, query, criteriaBuilder) -> {
            if (!Boolean.TRUE.equals(soloConStock)) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThan(
                    root.get("stock"),
                    0
            );
        };
    }
}
