-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-03-2021 a las 23:20:30
-- Versión del servidor: 10.4.16-MariaDB
-- Versión de PHP: 7.3.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `tfg_v1`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `animales`
--

CREATE TABLE `animales` (
  `NumIdentificacion` varchar(20) NOT NULL,
  `Sexo` varchar(1) NOT NULL,
  `AnoNacimiento` int(4) NOT NULL,
  `Usuario` varchar(20) NOT NULL,
  `Muerta` tinyint(1) NOT NULL,
  `FechaMuerte` date DEFAULT NULL,
  `Venta` tinyint(1) NOT NULL,
  `FechaBaja` date DEFAULT NULL,
  `FechaVenta` date DEFAULT NULL,
  `TieneBolo` tinyint(1) NOT NULL,
  `TieneCrotal` tinyint(1) NOT NULL,
  `NumExplotacion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `animales`
--

INSERT INTO `animales` (`NumIdentificacion`, `Sexo`, `AnoNacimiento`, `Usuario`, `Muerta`, `FechaMuerte`, `Venta`, `FechaBaja`, `FechaVenta`, `TieneBolo`, `TieneCrotal`, `NumExplotacion`) VALUES
('724080000531440', 'H', 2000, 'admin', 0, NULL, 1, NULL, '2021-03-01', 0, 0, 'ES491011310211'),
('724080003323723', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-18', NULL, 0, 0, 'ES491011310211'),
('724080003323744', 'H', 2021, 'admin', 1, NULL, 0, '2021-03-18', NULL, 1, 1, 'ES491011310211'),
('724080003323764', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-11', NULL, 1, 1, 'ES491011310211'),
('724080003323769', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 1, 1, 'ES491011310211'),
('724080003323771', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 1, 1, 'ES491011310211'),
('724080003323807', 'H', 2021, 'admin', 1, '2021-03-01', 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080003348191', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080003348219', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080003348236', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080003348246', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080003349635', 'H', 2021, 'admin', 1, '2021-03-04', 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080003691606', 'H', 2021, 'admin', 1, '2021-03-06', 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006159', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006161', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006162', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006163', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006164', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006166', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006167', 'H', 2021, 'admin', 1, '2021-03-15', 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006169', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006171', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006174', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006175', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006176', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006177', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006178', 'H', 0, 'admin', 1, '2021-03-15', 0, NULL, NULL, 0, 0, 'ES491011310211'),
('724080004006180', 'H', 0, 'admin', 0, NULL, 1, '2021-03-25', '2021-03-28', 0, 0, 'ES491011310211'),
('724080004006188', 'H', 0, 'admin', 0, NULL, 1, '2021-03-31', '2021-03-31', 0, 0, 'ES491011310211'),
('724080004008254', 'H', 0, 'admin', 0, NULL, 1, '2021-03-23', '2021-03-28', 0, 0, 'ES491011310211'),
('724080004008256', 'H', 0, 'admin', 0, NULL, 1, '2021-03-28', '2021-03-21', 0, 0, 'ES491011310211'),
('724080004008257', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-01', '2021-03-07', 0, 0, 'ES491011310211'),
('724080004008261', 'H', 0, 'admin', 0, NULL, 1, '2021-03-06', '2021-03-14', 0, 0, 'ES491011310211'),
('724080004008266', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-01', '2021-03-03', 0, 0, 'ES491011310211'),
('724080004008268', 'H', 0, 'admin', 0, NULL, 1, '2021-03-28', '2021-03-14', 0, 0, 'ES491011310211'),
('724080004008269', 'H', 0, 'admin', 0, NULL, 1, '2021-03-28', '2021-03-14', 0, 0, 'ES491011310211'),
('724080004008272', 'H', 0, 'admin', 0, NULL, 1, '2021-03-23', '2021-03-29', 0, 0, 'ES491011310211'),
('724080004008273', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-02', '2021-03-05', 0, 0, 'ES491011310211'),
('724080004008275', 'H', 0, 'admin', 0, NULL, 1, '2021-03-07', '2021-03-09', 0, 0, 'ES491011310211'),
('724080004008281', 'H', 2021, 'admin', 1, '2021-02-04', 0, '2021-03-05', NULL, 0, 0, 'ES491011310211'),
('724080004008282', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-14', '2021-03-07', 0, 0, 'ES491011310211'),
('724080004008285', 'H', 0, 'admin', 0, NULL, 1, '2021-03-26', '2021-03-27', 0, 0, 'ES491011310211'),
('724080004008286', 'H', 0, 'admin', 0, NULL, 1, '2021-03-27', '2021-03-21', 0, 0, 'ES491011310211'),
('724080004008291', 'H', 0, 'admin', 0, NULL, 1, '2021-03-28', '2021-03-28', 0, 0, 'ES491011310211'),
('724080004008292', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-29', '2021-03-17', 0, 0, 'ES491011310211'),
('724080004008296', 'H', 0, 'admin', 0, NULL, 1, '2021-03-30', '2021-03-29', 0, 0, 'ES491011310211'),
('724080004008297', 'H', 0, 'admin', 0, NULL, 1, '2021-03-29', '2021-03-30', 0, 0, 'ES491011310211'),
('724080004008298', 'H', 0, 'admin', 0, NULL, 1, '2021-03-20', '2021-03-31', 0, 0, 'ES491011310211'),
('724080004008300', 'M', 0, 'admin', 0, NULL, 1, '2021-03-06', '2021-03-23', 0, 0, 'ES491011310211'),
('724080004011183', 'H', 0, 'admin', 0, NULL, 1, '2021-03-31', '2021-03-22', 0, 0, 'ES491011310211'),
('724080004011186', 'H', 2021, 'admin', 0, NULL, 1, '2021-03-01', '2021-03-31', 0, 0, 'ES491011310211'),
('724080004011188', 'H', 2021, 'admin', 0, NULL, 0, NULL, NULL, 1, 1, 'ES491011310211');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `explotaciones`
--

CREATE TABLE `explotaciones` (
  `NumExplotacion` varchar(20) NOT NULL,
  `usuario` varchar(20) NOT NULL,
  `TipoAnimal` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `explotaciones`
--

INSERT INTO `explotaciones` (`NumExplotacion`, `usuario`, `TipoAnimal`) VALUES
('ES491011310211', 'admin', 'ovino');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `Usuario` varchar(20) NOT NULL,
  `Contrasena` varchar(20) NOT NULL,
  `Nombre` varchar(50) DEFAULT NULL,
  `PrimerApellido` varchar(50) DEFAULT NULL,
  `SegundoApellido` varchar(50) DEFAULT NULL,
  `DNINIF` varchar(50) DEFAULT NULL,
  `CorreoElectronico` varchar(50) DEFAULT NULL,
  `NumExplotacion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`Usuario`, `Contrasena`, `Nombre`, `PrimerApellido`, `SegundoApellido`, `DNINIF`, `CorreoElectronico`, `NumExplotacion`) VALUES
('admin', 'admin', 'Bea', 'Esteban', 'Rios', '71043105S', 'beaest@usal.es', 'ES491011310211');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `animales`
--
ALTER TABLE `animales`
  ADD PRIMARY KEY (`NumIdentificacion`),
  ADD KEY `fk_usuario` (`Usuario`);

--
-- Indices de la tabla `explotaciones`
--
ALTER TABLE `explotaciones`
  ADD PRIMARY KEY (`NumExplotacion`),
  ADD KEY `usuario` (`usuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`Usuario`),
  ADD KEY `fk_numexplotacion` (`NumExplotacion`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `animales`
--
ALTER TABLE `animales`
  ADD CONSTRAINT `fk_usuario` FOREIGN KEY (`Usuario`) REFERENCES `usuarios` (`Usuario`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `fk_numexplotacion` FOREIGN KEY (`NumExplotacion`) REFERENCES `explotaciones` (`NumExplotacion`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
