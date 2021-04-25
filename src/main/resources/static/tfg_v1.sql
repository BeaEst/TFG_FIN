-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-04-2021 a las 12:54:15
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
-- Estructura de tabla para la tabla `alimentos_suministrados`
--

CREATE TABLE `alimentos_suministrados` (
  `Id` int(11) NOT NULL,
  `FechaCompra` date NOT NULL,
  `NaturalezaAlimento` varchar(100) NOT NULL,
  `Cantidad` double NOT NULL,
  `NDocumento` varchar(30) NOT NULL,
  `NumExplotacion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `alimentos_suministrados`
--

INSERT INTO `alimentos_suministrados` (`Id`, `FechaCompra`, `NaturalezaAlimento`, `Cantidad`, `NDocumento`, `NumExplotacion`) VALUES
(1, '2020-04-27', 'rt6dcfhjlk', 2000, 'tfctyfygui', 'ES491011310211'),
(2, '2021-04-27', 'rt6dcfhjlk', 2000, 'tfctyfygui', 'ES491011310211'),
(3, '2021-04-09', 'Esto es una prueba', 2000, 'kugk', 'ES491011310211');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `altas_bajas_animales`
--

CREATE TABLE `altas_bajas_animales` (
  `Id` int(11) NOT NULL,
  `Fecha` date NOT NULL,
  `Motivo` varchar(50) NOT NULL,
  `Procedencia_Destino` varchar(100) NOT NULL,
  `NDocumento` varchar(100) NOT NULL,
  `NAnimales` varchar(20) NOT NULL,
  `BalanceFinal` varchar(20) NOT NULL,
  `NumExplotacion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `altas_bajas_animales`
--

INSERT INTO `altas_bajas_animales` (`Id`, `Fecha`, `Motivo`, `Procedencia_Destino`, `NDocumento`, `NAnimales`, `BalanceFinal`, `NumExplotacion`) VALUES
(1, '1099-01-01', '-', 'INICIO APLICACION', '-', '74', '74', 'ES491011310211'),
(78, '2019-04-01', 'BAJA - MUERTE', '-', '', '1', '73', 'ES491011310211'),
(79, '2021-04-02', 'BAJA - MUERTE', '-', '', '1', '72', 'ES491011310211'),
(80, '2021-04-03', 'BAJA - MUERTE', '-', '', '1', '71', 'ES491011310211'),
(81, '2021-04-05', 'BAJA - MUERTE', '-', '', '1', '70', 'ES491011310211'),
(82, '2021-04-06', 'BAJA - MUERTE', '-', '', '1', '69', 'ES491011310211'),
(83, '2021-04-07', 'BAJA - MUERTE', '-', '', '1', '68', 'ES491011310211'),
(114, '2021-04-23', 'BAJA - SALIDA', 'dsfgsdf', 'dfsfgsdfg', '1', '52', 'ES491011310211'),
(115, '2021-04-23', 'BAJA - MUERTE', '-', 'cvzbxvcbzx', '1', '51', 'ES491011310211'),
(116, '2021-04-21', 'BAJA - SALIDA', 'ljhpoi', 'ygyvkjhuigb', '12 CORDEROS', '', 'ES491011310211');

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
  `Venta` tinyint(1) NOT NULL,
  `FechaBaja` date DEFAULT NULL,
  `TieneBolo` tinyint(1) NOT NULL,
  `TieneCrotal` tinyint(1) NOT NULL,
  `NumExplotacion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `animales`
--

INSERT INTO `animales` (`NumIdentificacion`, `Sexo`, `AnoNacimiento`, `Usuario`, `Muerta`, `Venta`, `FechaBaja`, `TieneBolo`, `TieneCrotal`, `NumExplotacion`) VALUES
('724080000531440', 'H', 2000, 'admin', 1, 0, '2021-04-01', 0, 0, 'ES491011310211'),
('724080003323723', 'H', 2021, 'admin', 1, 0, '2021-04-02', 0, 0, 'ES491011310211'),
('724080003323744', 'H', 2021, 'admin', 1, 0, '2021-04-03', 1, 1, 'ES491011310211'),
('724080003323764', 'H', 2021, 'admin', 1, 0, '2021-04-05', 1, 1, 'ES491011310211'),
('724080003323769', 'H', 2021, 'admin', 1, 0, '2021-04-06', 1, 1, 'ES491011310211'),
('724080003323771', 'H', 2021, 'admin', 1, 0, '2021-04-07', 1, 1, 'ES491011310211'),
('724080003323807', 'H', 2021, 'admin', 1, 0, '2021-04-08', 0, 0, 'ES491011310211'),
('724080003348191', 'H', 2021, 'admin', 1, 0, '2021-04-09', 1, 1, 'ES491011310211'),
('724080003348219', 'H', 2021, 'admin', 1, 0, '2021-04-10', 0, 0, 'ES491011310211'),
('724080003348236', 'H', 2021, 'admin', 1, 0, '2021-04-11', 1, 1, 'ES491011310211'),
('724080003348246', 'H', 2021, 'admin', 1, 0, '2021-04-12', 0, 0, 'ES491011310211'),
('724080003349635', 'H', 2021, 'admin', 1, 0, '2021-04-13', 0, 0, 'ES491011310211'),
('724080003691606', 'H', 2021, 'admin', 1, 0, '2021-04-14', 0, 0, 'ES491011310211'),
('724080004006159', 'H', 2021, 'admin', 1, 0, '2021-04-15', 0, 0, 'ES491011310211'),
('724080004006161', 'H', 2021, 'admin', 0, 1, '2021-04-17', 0, 0, 'ES491011310211'),
('724080004006162', 'H', 2021, 'admin', 1, 0, '2021-04-18', 0, 0, 'ES491011310211'),
('724080004006163', 'H', 2021, 'admin', 1, 0, '2021-04-20', 0, 0, 'ES491011310211'),
('724080004006164', 'H', 2021, 'admin', 1, 0, '2021-04-01', 0, 0, 'ES491011310211'),
('724080004006166', 'H', 2021, 'admin', 1, 0, '2021-04-21', 0, 0, 'ES491011310211'),
('724080004006167', 'H', 2021, 'admin', 0, 1, '2021-04-30', 0, 0, 'ES491011310211'),
('724080004006169', 'H', 2021, 'admin', 1, 1, '2021-04-23', 0, 0, 'ES491011310211'),
('724080004006171', 'H', 2021, 'admin', 1, 1, '2021-05-08', 1, 1, 'ES491011310211'),
('724080004006174', 'H', 2021, 'admin', 1, 0, '2021-04-22', 0, 0, 'ES491011310211'),
('724080004006175', 'H', 2021, 'admin', 0, 1, '2021-04-23', 0, 0, 'ES491011310211'),
('724080004006176', 'H', 2021, 'admin', 1, 0, '2021-04-23', 0, 0, 'ES491011310211'),
('724080004006177', 'H', 2021, 'admin', 0, 0, '2021-04-18', 0, 0, 'ES491011310211'),
('724080004006178', 'H', 0, 'admin', 0, 0, '2021-04-20', 0, 0, 'ES491011310211'),
('724080004006180', 'H', 0, 'admin', 0, 0, '2021-04-20', 0, 0, 'ES491011310211'),
('724080004006188', 'H', 0, 'admin', 0, 0, '2021-04-09', 0, 0, 'ES491011310211'),
('724080004008254', 'H', 0, 'admin', 0, 0, '2021-04-01', 0, 0, 'ES491011310211'),
('724080004008256', 'H', 0, 'admin', 0, 0, '2021-04-17', 0, 0, 'ES491011310211'),
('724080004008257', 'H', 2021, 'admin', 0, 0, '2021-04-03', 0, 0, 'ES491011310211'),
('724080004008261', 'H', 0, 'admin', 0, 0, '2021-04-04', 0, 0, 'ES491011310211'),
('724080004008266', 'H', 2021, 'admin', 0, 0, '2022-04-01', 0, 0, 'ES491011310211'),
('724080004008268', 'H', 2022, 'admin', 0, 0, '2022-04-21', 0, 0, 'ES491011310211'),
('724080004008269', 'H', 2021, 'admin', 0, 0, '2021-03-28', 0, 0, 'ES491011310211'),
('724080004008272', 'H', 0, 'admin', 0, 0, '2021-04-05', 0, 0, 'ES491011310211'),
('724080004008273', 'H', 2021, 'admin', 0, 0, '2021-04-06', 0, 0, 'ES491011310211'),
('724080004008275', 'H', 0, 'admin', 0, 0, '2021-04-07', 0, 0, 'ES491011310211'),
('724080004008281', 'H', 2021, 'admin', 0, 0, '2021-04-08', 0, 0, 'ES491011310211'),
('724080004008282', 'H', 2021, 'admin', 0, 0, '2021-04-09', 0, 0, 'ES491011310211'),
('724080004008285', 'H', 0, 'admin', 0, 0, '2021-04-10', 0, 0, 'ES491011310211'),
('724080004008286', 'H', 0, 'admin', 0, 0, '2021-04-11', 0, 0, 'ES491011310211'),
('724080004008291', 'H', 0, 'admin', 0, 0, '2021-04-12', 0, 0, 'ES491011310211'),
('724080004008292', 'H', 2021, 'admin', 0, 0, '2021-04-13', 0, 0, 'ES491011310211'),
('724080004008296', 'H', 0, 'admin', 0, 0, '2021-04-24', 0, 0, 'ES491011310211'),
('724080004008297', 'H', 0, 'admin', 0, 0, '2021-04-14', 0, 0, 'ES491011310211'),
('724080004008298', 'H', 0, 'admin', 0, 0, '2021-04-15', 0, 0, 'ES491011310211'),
('724080004008300', 'M', 0, 'admin', 0, 0, '2021-04-16', 0, 0, 'ES491011310211'),
('724080004011183', 'H', 2022, 'admin', 0, 0, '2021-03-31', 0, 0, 'ES491011310211'),
('724080004011186', 'H', 2021, 'admin', 0, 0, '2021-04-17', 1, 1, 'ES491011310211'),
('724080004011188', 'H', 2021, 'admin', 0, 0, '2021-04-01', 1, 1, 'ES491011310211'),
('724080004011189', 'H', 2021, 'admin', 0, 0, '2021-04-01', 1, 1, 'ES491011310211'),
('724080004011200', 'H', 2021, 'admin', 0, 0, '2021-04-02', 1, 1, 'ES491011310211'),
('724080004011201', 'H', 2021, 'admin', 0, 0, '2021-04-03', 1, 1, 'ES491011310211'),
('724080004011208', 'H', 2021, 'admin', 0, 0, '2021-04-05', 1, 1, 'ES491011310211'),
('724080004011218', 'H', 2021, 'admin', 0, 0, '2021-04-24', 1, 1, 'ES491011310211'),
('724080004011221', 'H', 2021, 'admin', 0, 0, '2021-04-04', 1, 1, 'ES491011310211'),
('724080004011224', 'H', 2021, 'admin', 0, 0, '2021-04-06', 1, 1, 'ES491011310211'),
('724080004011227', 'H', 2021, 'admin', 0, 0, '2021-04-07', 0, 0, 'ES491011310211'),
('724080004011230', 'H', 2021, 'admin', 0, 0, '2021-04-08', 1, 1, 'ES491011310211'),
('724080004011235', 'H', 2021, 'admin', 0, 0, '2021-04-09', 0, 0, 'ES491011310211'),
('724080004011239', 'H', 2021, 'admin', 0, 0, '2021-04-19', 1, 1, 'ES491011310211'),
('724080004011241', 'H', 2021, 'admin', 0, 0, '2021-04-14', 0, 0, 'ES491011310211'),
('724080004011254', 'H', 2021, 'admin', 0, 0, '2021-04-19', 0, 0, 'ES491011310211'),
('724080004011255', 'H', 2021, 'admin', 0, 0, '2021-04-01', 0, 0, 'ES491011310211'),
('724080004018195', 'H', 2021, 'admin', 0, 0, '2021-04-22', 1, 1, 'ES491011310211'),
('724080004018198', 'H', 2021, 'admin', 0, 0, '2021-04-20', 1, 1, 'ES491011310211'),
('724080004018199', 'H', 2021, 'admin', 0, 0, '2021-04-28', 1, 1, 'ES491011310211'),
('724080004018200', 'H', 2021, 'admin', 0, 0, '2021-04-30', 1, 1, 'ES491011310211'),
('724080004018204', 'H', 2021, 'admin', 0, 0, '2021-04-12', 1, 1, 'ES491011310211'),
('724080004018206', 'H', 2021, 'admin', 0, 0, '2021-04-13', 1, 1, 'ES491011310211'),
('724080004018207', 'H', 2021, 'admin', 0, 0, '2021-04-13', 1, 1, 'ES491011310211'),
('724080004018208', 'H', 2021, 'admin', 0, 0, '2021-04-13', 1, 1, 'ES491011310211'),
('724080004018209', 'H', 2021, 'admin', 0, 0, NULL, 1, 1, 'ES491011310211'),
('724080004018210', 'H', 2021, 'admin', 0, 0, NULL, 1, 1, 'ES491011310211');

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
('ES491011310211', 'admin', 'ovino'),
('ES491011310212', 'admin', 'caprino');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `leche`
--

CREATE TABLE `leche` (
  `id` int(11) NOT NULL,
  `FechaEntrega` date NOT NULL,
  `CodigoCisterna` varchar(10) NOT NULL,
  `NumExplotacion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `leche`
--

INSERT INTO `leche` (`id`, `FechaEntrega`, `CodigoCisterna`, `NumExplotacion`) VALUES
(1, '2020-01-13', '7108LKX', 'ES491011310211'),
(2, '2021-04-30', '5123', 'ES491011310211'),
(3, '2021-04-22', '2645', 'ES491011310211'),
(4, '2021-04-30', '5123', 'ES491011310211'),
(5, '2021-04-22', '2645', 'ES491011310211'),
(6, '2021-04-12', '651965', 'ES491011310211'),
(7, '2021-04-20', 'SDFG', 'ES491011310211'),
(8, '2021-04-12', '651965', 'ES491011310211'),
(9, '2021-04-20', 'SDFG', 'ES491011310211'),
(10, '2021-04-22', 'ghhh', 'ES491011310211'),
(11, '2021-05-06', 'fxgsdfg', 'ES491011310211'),
(12, '2021-04-12', 'dgasdf', 'ES491011310211'),
(13, '2021-04-08', 'sdfas', 'ES491011310211'),
(14, '2021-04-12', 'dgasdf', 'ES491011310211'),
(15, '2021-04-08', 'sdfas', 'ES491011310211'),
(16, '2021-04-07', 'dsfasdf', 'ES491011310211'),
(17, '2021-04-08', 'dsfasdf', 'ES491011310211'),
(18, '2021-04-14', 'sdfasdf', 'ES491011310211'),
(19, '2021-04-22', 'dsfasdf', 'ES491011310211'),
(20, '2021-04-22', 'dsfasdf', 'ES491011310211'),
(21, '2021-04-28', 'adsfadsf', 'ES491011310211'),
(22, '2021-04-22', 'asdfasdf', 'ES491011310211'),
(23, '2021-04-07', 'dsfasdf', 'ES491011310211'),
(24, '2021-04-08', 'dsfasdf', 'ES491011310211'),
(25, '2021-04-14', 'sdfasdf', 'ES491011310211'),
(26, '2021-04-22', 'dsfasdf', 'ES491011310211'),
(27, '2021-04-22', 'dsfasdf', 'ES491011310211'),
(28, '2021-04-28', 'adsfadsf', 'ES491011310211'),
(29, '2021-04-22', 'asdfasdf', 'ES491011310211'),
(30, '2021-04-14', 'fdgsdfgfd', 'ES491011310211'),
(31, '2021-04-08', 'dfgsdfgsdf', 'ES491011310211'),
(32, '2021-04-11', 'fdsgsdfg', 'ES491011310211'),
(33, '2021-04-14', 'dfgsdfgsdf', 'ES491011310211'),
(34, '2021-04-14', 'fdgsdfgsdf', 'ES491011310211'),
(35, '2021-04-07', 'fdgsdfg', 'ES491011310211'),
(36, '2021-04-14', 'fdgsdfgfd', 'ES491011310211'),
(37, '2021-04-08', 'dfgsdfgsdf', 'ES491011310211'),
(38, '2021-04-11', 'fdsgsdfg', 'ES491011310211'),
(39, '2021-04-14', 'dfgsdfgsdf', 'ES491011310211'),
(40, '2021-04-14', 'fdgsdfgsdf', 'ES491011310211'),
(41, '2021-04-07', 'fdgsdfg', 'ES491011310211'),
(42, '2021-05-01', 'dsgvsdf', 'ES491011310211'),
(43, '2021-05-02', 'fdgsdfg', 'ES491011310211'),
(44, '2021-05-03', 'sdasdfasd', 'ES491011310211'),
(45, '2021-05-04', 'sdfasdf', 'ES491011310211'),
(46, '2021-05-05', 'fdgsfg', 'ES491011310211'),
(47, '2021-05-06', 'hdfghdfg', 'ES491011310211'),
(48, '2021-05-07', 'ghdfghdfg', 'ES491011310211'),
(49, '2021-04-08', 'fghdfgh', 'ES491011310211'),
(50, '2021-05-09', 'gdhfghd', 'ES491011310211'),
(51, '2021-05-10', 'dfghdfg', 'ES491011310211'),
(52, '2021-05-01', 'dsgvsdf', 'ES491011310211'),
(53, '2021-05-02', 'fdgsdfg', 'ES491011310211'),
(54, '2021-05-03', 'sdasdfasd', 'ES491011310211'),
(55, '2021-05-04', 'sdfasdf', 'ES491011310211'),
(56, '2021-05-05', 'fdgsfg', 'ES491011310211'),
(57, '2021-05-06', 'hdfghdfg', 'ES491011310211'),
(58, '2021-05-07', 'ghdfghdfg', 'ES491011310211'),
(59, '2021-04-08', 'fghdfgh', 'ES491011310211'),
(60, '2021-05-09', 'gdhfghd', 'ES491011310211'),
(61, '2021-05-10', 'dfghdfg', 'ES491011310211');

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
  `CorreoElectronico` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`Usuario`, `Contrasena`, `Nombre`, `PrimerApellido`, `SegundoApellido`, `DNINIF`, `CorreoElectronico`) VALUES
('admin', 'admin', 'Bea', 'Esteban', 'Rios', '71043105S', 'beaest@usal.es');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta_crias`
--

CREATE TABLE `venta_crias` (
  `ID` int(11) NOT NULL,
  `NGuia` varchar(20) NOT NULL,
  `NAnimales` varchar(20) NOT NULL,
  `FechaVenta` date NOT NULL,
  `NumExplotacion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `venta_crias`
--

INSERT INTO `venta_crias` (`ID`, `NGuia`, `NAnimales`, `FechaVenta`, `NumExplotacion`) VALUES
(1, '1234567891', '12', '2020-04-30', 'ES491011310211'),
(2, '9516358651', '25', '2022-04-20', 'ES491011310211'),
(3, '6491941981', '40', '2021-04-25', 'ES491011310211'),
(4, '9516358651', '25', '2021-04-20', 'ES491011310211'),
(5, '6491941981', '40', '2021-04-25', 'ES491011310211'),
(6, '689168961', '15', '2021-04-01', 'ES491011310211'),
(7, '86415455', '14', '2021-04-01', 'ES491011310211'),
(8, '2465151', '99', '2021-05-09', 'ES491011310211'),
(9, '6529196', '98', '2021-04-10', 'ES491011310211'),
(10, '264SGDFG', '97', '2021-05-06', 'ES491011310211'),
(11, 'VZCBZVB', '12', '2021-05-04', 'ES491011310211'),
(12, '2525', '25', '2021-04-01', 'ES491011310211'),
(13, 'sadfasd', '25', '2021-05-07', 'ES491011310211'),
(14, 'sdfasdf', '9', '2021-05-03', 'ES491011310211'),
(15, 'sdfasdfas', '1', '2021-04-03', 'ES491011310211'),
(16, 'sdfasdfa', '8', '2021-04-24', 'ES491011310211'),
(17, 'sadfasdf', '6', '2021-04-11', 'ES491011310211'),
(18, 'xfhdf', '3', '2021-05-05', 'ES491011310211'),
(19, 'fsgdfgfdg', '36', '2021-04-20', 'ES491011310211'),
(20, 'ygyvkjhuigb', '12', '2021-04-21', 'ES491011310211');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alimentos_suministrados`
--
ALTER TABLE `alimentos_suministrados`
  ADD PRIMARY KEY (`Id`);

--
-- Indices de la tabla `altas_bajas_animales`
--
ALTER TABLE `altas_bajas_animales`
  ADD PRIMARY KEY (`Id`);

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
-- Indices de la tabla `leche`
--
ALTER TABLE `leche`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`Usuario`);

--
-- Indices de la tabla `venta_crias`
--
ALTER TABLE `venta_crias`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alimentos_suministrados`
--
ALTER TABLE `alimentos_suministrados`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `altas_bajas_animales`
--
ALTER TABLE `altas_bajas_animales`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- AUTO_INCREMENT de la tabla `leche`
--
ALTER TABLE `leche`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT de la tabla `venta_crias`
--
ALTER TABLE `venta_crias`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `animales`
--
ALTER TABLE `animales`
  ADD CONSTRAINT `fk_usuario` FOREIGN KEY (`Usuario`) REFERENCES `usuarios` (`Usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
