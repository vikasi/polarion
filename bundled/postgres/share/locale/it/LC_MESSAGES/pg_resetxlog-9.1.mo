��    P      �  k         �  9   �  -     ,   1  8   ^  3   �  0   �  *   �  N   '  /   v  N   �     �  *   	  +   @	     l	  !   �	  +   �	  '   �	  )   �	  #   (
  &   L
  -   s
  !   �
  &   �
  !   �
  "     (   /     X  S   m  #   �  #   �  #   	  #   -  #   Q  #   u  \   �  +   �  0   "      S  @   t  D   �  &   �  -   !     O  )   _  )   �  )   �  )   �  )     )   1  )   [  )   �  )   �  )   �       V      )   w  )   �  )   �  ,   �  )   "  )   L  )   v  )   �  )   �  )   �  )     )   H  	   r  �   |       &   4  !   [  )   }  -   �     �     �     �       )     �  A  M     =   _  .   �  =   �  >   
  ;   I  8   �  j   �  :   )  v   d  *   �  8     9   ?  !   y  &   �  1   �  .   �  .   #  ,   R  .     :   �  '   �  -     &   ?  -   f  .   �     �  ^   �  &   8  &   _  &   �  &   �  &   �  &   �  n   "  7   �  =   �  6     E   >  Z   �  2   �  9        L  /   \  /   �  /   �  /   �  /      /   L   /   |   /   �   /   �   /   !     <!  a   \!  /   �!  /   �!  /   "  1   N"  /   �"  /   �"  /   �"  /   #  /   @#  /   p#  /   �#  /   �#  	    $  �   
$  %   �$  -   �$  %   !%  /   G%  <   w%     �%  
   �%     �%     �%  /   �%        6       %       ,          .      A   +      H          !       =   B   7      N   (           O   0          I   9               M   2          ;             G      	   D       '                 E   <   1               "       -          P      L      C             >      $   &   :   5          3         *   @       
       K   8              )   /               #   J       4      F   ?    
If these values seem acceptable, use -f to force reset.
 
Report bugs to <pgsql-bugs@postgresql.org>.
   --help          show this help, then exit
   --version       output version information, then exit
   -O OFFSET       set next multitransaction offset
   -e XIDEPOCH     set next transaction ID epoch
   -f              force update to be done
   -l TLI,FILE,SEG force minimum WAL starting location for new transaction log
   -m XID          set next multitransaction ID
   -n              no update, just show extracted control values (for testing)
   -o OID          set next OID
   -x XID          set next transaction ID
 %s resets the PostgreSQL transaction log.

 %s: OID (-o) must not be 0
 %s: cannot be executed by "root"
 %s: could not change directory to "%s": %s
 %s: could not close directory "%s": %s
 %s: could not create pg_control file: %s
 %s: could not delete file "%s": %s
 %s: could not open directory "%s": %s
 %s: could not open file "%s" for reading: %s
 %s: could not open file "%s": %s
 %s: could not read directory "%s": %s
 %s: could not read file "%s": %s
 %s: could not write file "%s": %s
 %s: could not write pg_control file: %s
 %s: fsync error: %s
 %s: internal error -- sizeof(ControlFileData) is too large ... fix PG_CONTROL_SIZE
 %s: invalid argument for option -O
 %s: invalid argument for option -e
 %s: invalid argument for option -l
 %s: invalid argument for option -m
 %s: invalid argument for option -o
 %s: invalid argument for option -x
 %s: lock file "%s" exists
Is a server running?  If not, delete the lock file and try again.
 %s: multitransaction ID (-m) must not be 0
 %s: multitransaction offset (-O) must not be -1
 %s: no data directory specified
 %s: pg_control exists but has invalid CRC; proceed with caution
 %s: pg_control exists but is broken or unknown version; ignoring it
 %s: transaction ID (-x) must not be 0
 %s: transaction ID epoch (-e) must not be -1
 64-bit integers Blocks per segment of large relation: %u
 Bytes per WAL segment:                %u
 Catalog version number:               %u
 Database block size:                  %u
 Database system identifier:           %s
 Date/time type storage:               %s
 First log file ID after reset:        %u
 First log file segment after reset:   %u
 Float4 argument passing:              %s
 Float8 argument passing:              %s
 Guessed pg_control values:

 If you are sure the data directory path is correct, execute
  touch %s
and try again.
 Latest checkpoint's NextMultiOffset:  %u
 Latest checkpoint's NextMultiXactId:  %u
 Latest checkpoint's NextOID:          %u
 Latest checkpoint's NextXID:          %u/%u
 Latest checkpoint's TimeLineID:       %u
 Latest checkpoint's oldestActiveXID:  %u
 Latest checkpoint's oldestXID's DB:   %u
 Latest checkpoint's oldestXID:        %u
 Maximum columns in an index:          %u
 Maximum data alignment:               %u
 Maximum length of identifiers:        %u
 Maximum size of a TOAST chunk:        %u
 Options:
 The database server was not shut down cleanly.
Resetting the transaction log might cause data to be lost.
If you want to proceed anyway, use -f to force reset.
 Transaction log reset
 Try "%s --help" for more information.
 Usage:
  %s [OPTION]... DATADIR

 WAL block size:                       %u
 You must run %s as the PostgreSQL superuser.
 by reference by value floating-point numbers pg_control values:

 pg_control version number:            %u
 Project-Id-Version: pg_resetxlog (PostgreSQL) 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2014-07-29 09:26+0000
PO-Revision-Date: 2014-07-30 22:33+0100
Last-Translator: Daniele Varrazzo <daniele.varrazzo@gmail.com>
Language-Team: Gruppo traduzioni ITPUG <traduzioni@itpug.org>
Language: it
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-Poedit-SourceCharset: utf-8
X-Generator: Poedit 1.5.4
 
Se questi parametri sembrano accettabili, utilizza -f per forzare un reset.
 
Puoi segnalare eventuali bug a <pgsql-bugs@postgresql.org>.
   --help          mostra questo aiuto ed esci
   --version       mostra informazioni sulla versione ed esci
   -O OFFSET       imposta il prossimo offset multitransazione
   -e XIDEPOCH     imposta il prossimo ID epoch transazione
   -f              forza l'esecuzione dell'aggiornamento
   -l TLI,FILE,SEG forza il minimo punto d'inizio WAL per il nuovo log
                  delle transazione
   -m XID          imposta il prossimo ID multitransazione
   -n              nessun aggiornamento, mostra solo i valori di controllo
                  estratti (solo per prova)
   -o OID          imposta il prossimo OID
   -x XID          imposta il prossimo ID di transazione
 %s riavvia il registro delle transazioni di PostgreSQL.

 %s: l'OID (-o) non deve essere 0
 %s non può essere eseguito da "root"
 %s: spostamento nella directory "%s" fallito: %s
 %s: chiusura della directory "%s" fallita: %s
 %s: creazione del file pg_control fallita: %s
 %s: cancellazione del file "%s" fallita: %s
 %s: apertura della directory "%s" fallita: %s
 %s: errore nell'apertura del file "%s" per la lettura: %s
 %s: apertura del file "%s" fallita: %s
 %s: lettura della directory "%s" fallita: %s
 %s: lettura del file "%s" fallita: %s
 %s: errore nella scrittura del file "%s": %s
 %s: scrittura del file pg_control fallita: %s
 %s: errore fsync: %s
 %s: errore interno -- sizeof(ControlFileData) è troppo grande ... correggere PG_CONTROL_SIZE
 %s: parametro errato per l'opzione -O
 %s: parametro errato per l'opzione -e
 %s: parametro errato per l'opzione -l
 %s: parametro errato per l'opzione -m
 %s: parametro errato per l'opzione -o
 %s: parametro errato per l'opzione -x
 %s: il file di lock "%s" esiste
Il server è in esecuzione? Se non lo è, cancella il file di lock e riprova.
 %s: l'ID della multitransazione (-m) non deve essere 0
 %s: l'offset di una multitransazione (-O) non può essere -1
 %s: non è stata specificata una directory per i dati
 %s: pg_control esiste ma ha un CRC non valido; procedere con cautela
 %s: pg_control esiste ma è inutilizzabile o è una versione sconosciuta; verrà ignorato
 %s: l'ID della transazione (-x) non deve essere 0
 %s: l'ID epoch della transazione (-e) non deve essere -1
 interi a 64 bit Blocchi per ogni segmento grosse tabelle:   %u
 Byte per segmento WAL:                      %u
 Numero di versione del catalogo:            %u
 Dimensione blocco database:                 %u
 Identificatore di sistema del database:     %s
 Memorizzazione per tipi data/ora:           %s
 primo ID del file di log dopo il reset:     %u
 Primo segmento file di log dopo il reset:   %u
 Passaggio di argomenti Float4:              %s
 passaggio di argomenti Float8:              %s
 Valori pg_control indovinati:

 Se sei sicuro che il percorso della directory dei dati è corretto, esegui
  touch %s
e riprova.
 NextMultiOffset dell'ultimo checkpoint:     %u
 NextMultiXactId dell'ultimo checkpoint:     %u
 NextOID dell'ultimo checkpoint:             %u
 NextXID dell'ultimo checkpoint:             %u%u
 TimeLineId dell'ultimo checkpoint:          %u
 oldestActiveXID dell'ultimo checkpoint:     %u
 DB dell'oldestXID dell'ultimo checkpoint:   %u
 oldestXID dell'ultimo checkpoint:           %u
 Massimo numero di colonne in un indice:     %u
 Massimo allineamento dei dati:              %u
 Lunghezza massima degli identificatori:     %u
 Massima dimensione di un segmento TOAST:    %u
 Opzioni:
 Il server database non è stato arrestato correttamente.
Resettare il registro delle transazioni può causare una perdita di dati.
Se vuoi continuare comunque, utilizza -f per forzare il reset.
 Registro delle transazioni riavviato
 Prova "%s --help" per maggiori informazioni.
 Utilizzo:
  %s [OPZIONI]... DATADIR

 Dimensione blocco WAL:                      %u
 È obbligatorio eseguire %s come superutente di PostgreSQL.
 per riferimento per valore numeri in virgola mobile Valori pg_control:

 Numero di versione di pg_control:           %u
 