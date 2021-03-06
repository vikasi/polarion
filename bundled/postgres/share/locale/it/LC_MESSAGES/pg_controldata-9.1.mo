��    :      �  O   �      �  X   �  C   R  -   �  !   �      �       ,     )   D  )   n  )   �  )   �  )   �  )     )   @  )   j  )   �  )   �  )   �  )     )   <  ,   f  )   �  )   �  )   �  ,   	  ,   >	  )   k	  )   �	  )   �	  )   �	  )   
  )   =
  )   g
  )   �
  ,   �
  ,   �
  ,     )   B  &   l  �   �  )     �   I               -     6     M     a     s  )   �  )   �  	   �     �     �               (  �  ?  j     K   |  :   �  &     6   *     a  2   q  /   �  /   �  /     /   4  /   d  /   �  /   �  /   �  /   $  /   T  /   �  /   �  /   �  2     /   G  /   w  /   �  1   �  2   	  /   <  /   l  /   �  /   �  /   �  /   ,  /   \  /   �  2   �  2   �  <   "  /   _  -   �  �   �  /   T  �   �  H  P     �  
   �     �  "   �     �       /     /   N     ~     �     �     �     �     �        /                     8           .   4            %               :                              #      *      5              $      (   )         "   '      1       !           7   ,      0   2   
          9                              	   &   -       6                3   +    
If no data directory (DATADIR) is specified, the environment variable PGDATA
is used.

 %s displays control information of a PostgreSQL database cluster.

 %s: could not open file "%s" for reading: %s
 %s: could not read file "%s": %s
 %s: no data directory specified
 64-bit integers Backup start location:                %X/%X
 Blocks per segment of large relation: %u
 Bytes per WAL segment:                %u
 Catalog version number:               %u
 Current max_connections setting:      %d
 Current max_locks_per_xact setting:   %d
 Current max_prepared_xacts setting:   %d
 Current wal_level setting:            %s
 Database block size:                  %u
 Database cluster state:               %s
 Database system identifier:           %s
 Date/time type storage:               %s
 Float4 argument passing:              %s
 Float8 argument passing:              %s
 Latest checkpoint location:           %X/%X
 Latest checkpoint's NextMultiOffset:  %u
 Latest checkpoint's NextMultiXactId:  %u
 Latest checkpoint's NextOID:          %u
 Latest checkpoint's NextXID:          %u/%u
 Latest checkpoint's REDO location:    %X/%X
 Latest checkpoint's TimeLineID:       %u
 Latest checkpoint's oldestActiveXID:  %u
 Latest checkpoint's oldestXID's DB:   %u
 Latest checkpoint's oldestXID:        %u
 Maximum columns in an index:          %u
 Maximum data alignment:               %u
 Maximum length of identifiers:        %u
 Maximum size of a TOAST chunk:        %u
 Minimum recovery ending location:     %X/%X
 Prior checkpoint location:            %X/%X
 Report bugs to <pgsql-bugs@postgresql.org>.
 Time of latest checkpoint:            %s
 Try "%s --help" for more information.
 Usage:
  %s [OPTION] [DATADIR]

Options:
  --help         show this help, then exit
  --version      output version information, then exit
 WAL block size:                       %u
 WARNING: Calculated CRC checksum does not match value stored in file.
Either the file is corrupt, or it has a different layout than this program
is expecting.  The results below are untrustworthy.

 WARNING: possible byte ordering mismatch
The byte ordering used to store the pg_control file might not match the one
used by this program.  In that case the results below would be incorrect, and
the PostgreSQL installation would be incompatible with this data directory.
 by reference by value floating-point numbers in archive recovery in crash recovery in production pg_control last modified:             %s
 pg_control version number:            %u
 shut down shut down in recovery shutting down starting up unrecognized status code unrecognized wal_level Project-Id-Version: pg_controldata (PostgreSQL) 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2013-03-13 11:46+0000
PO-Revision-Date: 2012-12-03 18:44+0100
Last-Translator: Daniele Varrazzo <daniele.varrazzo@gmail.com>
Language-Team: Gruppo traduzioni ITPUG <traduzioni@itpug.org>
Language: it
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-Poedit-SourceCharset: utf-8
X-Generator: Poedit 1.5.4
 
Se non viene specificata un directory per i dati (DATADIR) verrà usata la
variabile d'ambiente PGDATA.

 %s mostra informazioni di controllo su un cluster di database PostgreSQL.

 %s: errore nell'apertura del file "%s" per la lettura: %s
 %s: lettura del file "%s" fallita: %s
 %s: non è stata specificata una directory per i dati
 interi a 64 bit Posizione dell'inizio del backup:           %X/%X
 Blocchi per ogni segmento grosse tabelle:   %u
 Byte per segmento WAL:                      %u
 Numero di versione del catalogo:            %u
 Impostazione attuale max_connections:       %d
 Impostazione attuale max_locks_per_xact:    %d
 Impostazione attuale max_prepared_xacts:    %d
 Impostazione attuale wal_level:             %s
 Dimensione blocco database:                 %u
 Stato del cluster di database:              %s
 Identificatore di sistema del database:     %s
 Memorizzazione per tipi data/ora:           %s
 Passaggio di argomenti Float4:              %s
 passaggio di argomenti Float8:              %s
 Ultima posizione del checkpoint:            %X/%X
 NextMultiOffset dell'ultimo checkpoint:     %u
 NextMultiXactId dell'ultimo checkpoint:     %u
 NextOID dell'ultimo checkpoint:             %u
 NextXID dell'ultimo checkpoint:             %u%u
 Locazione di REDO dell'ultimo checkpoint:   %X/%X
 TimeLineId dell'ultimo checkpoint:          %u
 oldestActiveXID dell'ultimo checkpoint:     %u
 DB dell'oldestXID dell'ultimo checkpoint:   %u
 oldestXID dell'ultimo checkpoint:           %u
 Massimo numero di colonne in un indice:     %u
 Massimo allineamento dei dati:              %u
 Lunghezza massima degli identificatori:     %u
 Massima dimensione di un segmento TOAST:    %u
 Posizione del minimum recovery ending:      %X/%X
 Posizione precedente del checkpoint:        %X/%X
 Puoi segnalare eventuali bug a <pgsql-bugs@postgresql.org>.
 Orario ultimo checkpoint:                   %s
 Prova "%s --help" per maggiori informazioni.
 Utilizzo:
  %s [OPZIONI] [DATADIR]

Opzioni:
  --help         mostra questo aiuto ed esci
  --version      mostra informazioni sulla versione ed esci
 Dimensione blocco WAL:                      %u
 ATTENZIONE: La somma di controllo CRC non corrisponde al valore memorizzato nel file.
O il file è corrotto oppure ha un formato differente da quello previsto.
I risultati seguenti non sono affidabili.

 ATTENZIONE: possibile errore nel byte ordering
il byte ordering usato per archiviare il file pg_control potrebbe non
corrispondere a quello usato da questo programma. In questo caso il risultato
qui sotto potrebbe essere non corretto, e l'installazione di PostgreSQL
potrebbe essere incompatibile con questa directory dei dati.
 per riferimento per valore numeri in virgola mobile in fase di recupero di un archivio in fase di recupero da un crash in produzione Ultima modifica a pg_control:               %s
 Numero di versione di pg_control:           %u
 spento arresto durante il ripristino arresto in corso avvio in corso codice di stato sconosciuto wal_level sconosciuto 