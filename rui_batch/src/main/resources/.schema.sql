DROP DATABASE IF EXISTS `rui_ivass`;
CREATE DATABASE `rui_ivass` CHARACTER SET UTF8 COLLATE utf8_general_ci;
USE `rui_ivass`;

CREATE TABLE IF NOT EXISTS `rui_cariche`
(
    `oss`                      int(11)      DEFAULT NULL,
    `numero_iscrizione_rui_pf` varchar(80)  DEFAULT NULL,
    `numero_iscrizione_rui_pg` varchar(80)  DEFAULT NULL,
    `qualifica_intermediario`  varchar(255) DEFAULT NULL,
    `data_elaborazione`        date NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `rui_collaboratori`
(
    `oss`                           int(11)      DEFAULT NULL,
    `livello`                       varchar(80)  DEFAULT NULL,
    `num_iscr_intermediario`        varchar(80)  DEFAULT NULL,
    `num_iscr_collaboratori_i_liv`  varchar(255) DEFAULT NULL,
    `num_iscr_collaboratori_ii_liv` varchar(255) DEFAULT NULL,
    `qualifica_rapporto`            varchar(255) DEFAULT NULL,
    `data_elaborazione`             date NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `rui_intermediari`
(
    `oss`                       int(11)      DEFAULT NULL,
    `inoperativo`               int(11)      DEFAULT NULL,
    `data_inizio_inoperativita` date         DEFAULT NULL,
    `numero_iscrizione_rui`     varchar(80)  DEFAULT NULL,
    `data_iscrizione`           date         DEFAULT NULL,
    `cognome_nome`              varchar(255) DEFAULT NULL,
    `stato`                     varchar(80)  DEFAULT NULL,
    `comune_nascita`            varchar(255) DEFAULT NULL,
    `data_nascita`              date         DEFAULT NULL,
    `ragione_sociale`           varchar(255) DEFAULT NULL,
    `provincia_nascita`         varchar(80)  DEFAULT NULL,
    `titolo_individuale_sez_a`  varchar(80)  DEFAULT NULL,
    `attivita_esercitata_sez_a` varchar(255) DEFAULT NULL,
    `titolo_individuale_sez_b`  varchar(80)  DEFAULT NULL,
    `attivita_esercitata_sez_b` varchar(255) DEFAULT NULL,
    `data_elaborazione`         date NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `rui_mandati`
(
    `oss`                   int(11)      DEFAULT NULL,
    `numero_iscrizione_rui` varchar(80)  DEFAULT NULL,
    `codice_compagnia`      varchar(80)  DEFAULT NULL,
    `ragione_sociale`       varchar(255) DEFAULT NULL,
    `data_elaborazione`     date NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `rui_sedi`
(
    `oss`                   int(11)      DEFAULT NULL,
    `numero_iscrizione_int` varchar(80)  DEFAULT NULL,
    `tipo_sede`             varchar(80)  DEFAULT NULL,
    `comune_sede`           varchar(255) DEFAULT NULL,
    `provincia_sede`        varchar(80)  DEFAULT NULL,
    `cap_sede`              varchar(255) DEFAULT NULL,
    `indirizzo_sede`        varchar(80)  DEFAULT NULL,
    `data_elaborazione`     date NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;
