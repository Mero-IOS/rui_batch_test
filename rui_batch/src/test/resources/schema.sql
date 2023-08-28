CREATE TABLE IF NOT EXISTS `rui_cariche`
(
    `oss`                      INT          DEFAULT NULL,
    `numero_iscrizione_rui_pf` VARCHAR(80)  DEFAULT NULL,
    `numero_iscrizione_rui_pg` VARCHAR(80)  DEFAULT NULL,
    `qualifica_intermediario`  VARCHAR(255) DEFAULT NULL,
    `data_elaborazione`        DATE NOT NULL
);
CREATE TABLE IF NOT EXISTS `rui_collaboratori`
(
    `oss`                           INT          DEFAULT NULL,
    `livello`                       VARCHAR(80)  DEFAULT NULL,
    `num_iscr_intermediario`        VARCHAR(80)  DEFAULT NULL,
    `num_iscr_collaboratori_i_liv`  VARCHAR(255) DEFAULT NULL,
    `num_iscr_collaboratori_ii_liv` VARCHAR(255) DEFAULT NULL,
    `qualifica_rapporto`            VARCHAR(255) DEFAULT NULL,
    `data_elaborazione`             DATE NOT NULL
);
CREATE TABLE IF NOT EXISTS `rui_intermediari`
(
    `oss`                       INT          DEFAULT NULL,
    `inoperativo`               INT          DEFAULT NULL,
    `data_inizio_inoperativita` DATE         DEFAULT NULL,
    `numero_iscrizione_rui`     VARCHAR(80)  DEFAULT NULL,
    `data_iscrizione`           DATE         DEFAULT NULL,
    `cognome_nome`              VARCHAR(255) DEFAULT NULL,
    `stato`                     VARCHAR(80)  DEFAULT NULL,
    `comune_nascita`            VARCHAR(255) DEFAULT NULL,
    `data_nascita`              DATE         DEFAULT NULL,
    `ragione_sociale`           VARCHAR(255) DEFAULT NULL,
    `provincia_nascita`         VARCHAR(80)  DEFAULT NULL,
    `titolo_individuale_sez_a`  VARCHAR(80)  DEFAULT NULL,
    `attivita_esercitata_sez_a` VARCHAR(255) DEFAULT NULL,
    `titolo_individuale_sez_b`  VARCHAR(80)  DEFAULT NULL,
    `attivita_esercitata_sez_b` VARCHAR(255) DEFAULT NULL,
    `data_elaborazione`         DATE NOT NULL
);
CREATE TABLE IF NOT EXISTS `rui_mandati`
(
    `oss`                   INT          DEFAULT NULL,
    `numero_iscrizione_rui` VARCHAR(80)  DEFAULT NULL,
    `codice_compagnia`      VARCHAR(80)  DEFAULT NULL,
    `ragione_sociale`       VARCHAR(255) DEFAULT NULL,
    `data_elaborazione`     DATE NOT NULL
);
CREATE TABLE IF NOT EXISTS `rui_sedi`
(
    `oss`                   INT          DEFAULT NULL,
    `numero_iscrizione_int` VARCHAR(80)  DEFAULT NULL,
    `tipo_sede`             VARCHAR(80)  DEFAULT NULL,
    `comune_sede`           VARCHAR(255) DEFAULT NULL,
    `provincia_sede`        VARCHAR(80)  DEFAULT NULL,
    `cap_sede`              VARCHAR(255) DEFAULT NULL,
    `indirizzo_sede`        VARCHAR(80)  DEFAULT NULL,
    `data_elaborazione`     DATE NOT NULL
);

TRUNCATE TABLE `rui_cariche`;
TRUNCATE TABLE `rui_collaboratori`;
TRUNCATE TABLE `rui_intermediari`;
TRUNCATE TABLE `rui_mandati`;
TRUNCATE TABLE `rui_sedi`;