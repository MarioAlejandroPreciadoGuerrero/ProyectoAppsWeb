USE ecommerce_bemole;

START TRANSACTION;

-- =====================================================
-- 1. USUARIOS
-- =====================================================

-- Administrador
-- Correo: admin@bemole.com
-- Contraseña: Admin123*
INSERT INTO usuarios (
    email,
    fecha_registro,
    nombre,
    apellido,
    password,
    rol,
    telefono
)
SELECT
    'admin@bemole.com',
    NOW(),
    'Administrador',
    'Bemole',
    '$2y$10$G0xqQXH3cu5B6DL0vUKRieFHgTPXcONT7FHxfQw9fRUqnHfOCjILi',
    'ADMINISTRADOR',
    '6440000001'
WHERE NOT EXISTS (
    SELECT 1
    FROM usuarios
    WHERE email = 'admin@bemole.com'
);


-- Cliente
-- Correo: cliente@bemole.com
-- Contraseña: Cliente123*
INSERT INTO usuarios (
    email,
    fecha_registro,
    nombre,
    apellido,
    password,
    rol,
    telefono
)
SELECT
    'cliente@bemole.com',
    NOW(),
    'Cliente',
    'Prueba',
    '$2y$10$VGvgYCGKobk6l/IL11fyjO1h9zMresvZWTGsIH1ZcfQ5ZmuK9gamq',
    'CLIENTE',
    '6440000002'
WHERE NOT EXISTS (
    SELECT 1
    FROM usuarios
    WHERE email = 'cliente@bemole.com'
);


-- =====================================================
-- 2. CATEGORÍA
-- =====================================================

INSERT INTO categorias (
    nombre,
    descripcion
)
SELECT
    'Ropa',
    'Prendas y accesorios disponibles en la tienda'
WHERE NOT EXISTS (
    SELECT 1
    FROM categorias
    WHERE nombre = 'Ropa'
);


-- =====================================================
-- 3. PRODUCTOS
-- =====================================================

INSERT INTO productos (
    activo,
    descripcion,
    nombre,
    precio,
    stock,
    categoria_id
)
SELECT
    b'1',
    'Camiseta básica de la tienda Bemole',
    'Camiseta Bemole',
    299.00,
    20,
    c.id
FROM categorias c
WHERE c.nombre = 'Ropa'
  AND NOT EXISTS (
      SELECT 1
      FROM productos
      WHERE nombre = 'Camiseta Bemole'
  );


INSERT INTO productos (
    activo,
    descripcion,
    nombre,
    precio,
    stock,
    categoria_id
)
SELECT
    b'1',
    'Sudadera cómoda de la tienda Bemole',
    'Sudadera Bemole',
    599.00,
    15,
    c.id
FROM categorias c
WHERE c.nombre = 'Ropa'
  AND NOT EXISTS (
      SELECT 1
      FROM productos
      WHERE nombre = 'Sudadera Bemole'
  );


INSERT INTO productos (
    activo,
    descripcion,
    nombre,
    precio,
    stock,
    categoria_id
)
SELECT
    b'1',
    'Gorra ajustable con diseño Bemole',
    'Gorra Bemole',
    199.00,
    10,
    c.id
FROM categorias c
WHERE c.nombre = 'Ropa'
  AND NOT EXISTS (
      SELECT 1
      FROM productos
      WHERE nombre = 'Gorra Bemole'
  );


-- =====================================================
-- 4. CARRITO DEL CLIENTE
-- =====================================================

INSERT INTO carritos (
    fecha_creacion,
    usuario_id
)
SELECT
    NOW(),
    u.id
FROM usuarios u
WHERE u.email = 'cliente@bemole.com'
  AND NOT EXISTS (
      SELECT 1
      FROM carritos c
      WHERE c.usuario_id = u.id
  );


-- =====================================================
-- 5. PRODUCTO INICIAL EN EL CARRITO
-- =====================================================

INSERT INTO items_carrito (
    cantidad,
    carrito_id,
    producto_id
)
SELECT
    1,
    c.id,
    p.id
FROM carritos c
INNER JOIN usuarios u
    ON u.id = c.usuario_id
INNER JOIN productos p
    ON p.nombre = 'Camiseta Bemole'
WHERE u.email = 'cliente@bemole.com'
  AND NOT EXISTS (
      SELECT 1
      FROM items_carrito ic
      WHERE ic.carrito_id = c.id
        AND ic.producto_id = p.id
  );


COMMIT;