# RUI IVASS BATCH

## Progetto:
- Leggere da un file di configurazione(.ini) i dati necessari per il database ed email.
  Struttura file 'config.ini' per la configurazione
    ```ini
    [mysql]
    username
    password
    jdbcUrl
    driverclass
    [smtp]
    host
    protocolo
    porta
    username
    password
    [mail]
    from
    to
    ```
- Scaricare lo zip da: **https://www.ivass.it/operatori/intermediari/rui/Dati_RUI.zip**
- Scompattare lo zip
- Utilizzare la data presente nel nome dei file all'interno dello zip per inviare una mail nel caso la data presa in input non corrisponda con la data estrapolata dal nome
- Leggere almeno 3 file dello zip
- Aggiungere la Data presa come paramentro in input come campo DataElaborazione
- Scrivere i dati sul database Mysql(la creazione delle tabelle deve ricadere sulle classi/metodi di java spring e non a query)
- Creare un file 'Ok.txt' dopo la scrittura se tutto è andato correttamente
- Usare una Exception interna per gestire gli errori incapsulando gli errori di java ed inviando una mail in caso di problemi nel programma (Eccezzione che deve differenziare un errore che deve bloccare il progetto da una che deve skippare la riga)
- Gestire uno skip delle righe in caso di errore nel file con un limite di 10 righe skippate, le righe skippate vanno gestite inserendole in un file di scarto
---
***Strttura database***:
Il database NON deve presentare chiavi primarie, tutte le colonne devono avere come valore di default NULL eccetto __data_elaborazione__ la quale NON può essere NULL.
All'interno delle tabelle dovranno essere presenti tutti i record, non bisogna sovrascrivere quelli già presenti, eccetto nel caso in cui la __data_elaborazione__ sia la stessa.
---
***Dettagli aggiuntivi***:
- I domini(classi di model) e quello che si ritiene generico e distaccato dal progetto sono da creare in un progetto a parte che andra compilato ed usato come libreria. In sostanza bisognerà importare nel progetto principale questo primo progetto contenente le classi di dominio

- Testare ogni passaggio meglio ancora se fatti dopo ogni punto. **ATTENZIONE!** Il coverage del test deve essere pari al 90%.
---
***Parametri di input:***

| Nome Comando   | Descrizione                                        | Obbligatorio |
|:---------------|:---------------------------------------------------|:------------:|
| *.ini          | sempre in posizione 1                              |      X       |
| dataCsv        | data del elaborazione                              |      X       |
| percorsoInput  | Se popolato prende il percorso dello zip           |              |
| linkInput      | Se popolato prende il link zip                     |              |
| percorsoOutput | Cartello output Zip e File formato da data_dataCsv |      x       |

**ATTENZIONE!** I parametri url e percorsoDiLavoro **non** devono essere presenti entrambi