��    �      $  �   ,
      �  K   �     �  f     
   r  >   }  >   �  =   �  -   9  C   g  A   �     �  #        *  (   E     n  <   �  9   �  6     H   9  E   �  B   �  9     C   E  9   �  4   �  E   �  =   >  .   |  ;   �  E   �  :   -  5   h  7   �  9   �  7     4   H  L   }  J   �  5     2   K  C   ~  7   �  2   �  2   -  J   `  :   �  5   �  G     0   d  <   �  0   �  M     J   Q  G   �  4   �  H     E   b  9   �  v   �  <   Y  I   �  @   �  5   !  4   W  1   �  ;   �  5   �  6   0  3   g  4   �  =   �  8     8   G  8   �  2   �  9   �  6   &  >   ]     �  /   �  <   �  #      #   9   ?   ]   %   �   #   �      �   3   !  &   ;!  5   b!  E   �!  I   �!  5   ("  I   ^"  5   �"  E   �"  F   $#  @   k#  >   �#  4   �#  D    $     e$  *   �$  8   �$  6   �$  %   %  (   D%  (   m%  8   �%  #   �%      �%     &  8   4&  4   m&  $   �&     �&  ,   �&  ,   '  ;   A'  9   }'     �'     �'     �'     �'  *   (  8   ;(  ,   t(  8   �(  #   �(  4   �(     3)  )   P)  7   z)     �)     �)  !   �)  +   �)     #*     4*     P*     m*     �*     �*  
   �*     �*     �*     �*  '   +  "   ++  2   N+  7   �+     �+  &   �+     �+     �+     �+     �+  :   ,     @,     B,  �  F,  b   .     v.  X   �.  
   �.  L   �.  L   A/  K   �/  =   �/  G   0  E   `0     �0  +   �0     �0  )   1     21  >   R1  ;   �1  8   �1  M   2  J   T2  D   �2  >   �2  F   #3  5   j3  7   �3  z   �3  ;   S4  C   �4  =   �4  O   5  @   a5  ;   �5  >   �5  ;   6  D   Y6  I   �6  n   �6  q   W7  B   �7  ?   8  y   L8  ;   �8  7   9  :   :9  \   u9  @   �9  5   :  G   I:  4   �:  E   �:  7   ;  E   D;  N   �;  ?   �;  >   <  K   X<  H   �<  =   �<  ~   +=  O   �=  Q   �=  K   L>  M   �>  9   �>  6    ?  ?   W?  9   �?  =   �?  :   @  @   J@  K   �@  B   �@  E   A  8   `A  6   �A  ?   �A  <   B  N   MB     �B  0   �B  K   �B  !   %C  $   GC  B   lC  )   �C  $   �C  !   �C  6    D  +   WD  K   �D  X   �D  a   (E  L   �E  i   �E  N   AF  [   �F  c   �F  N   PG  L   �G  X   �G  l   EH     �H  0   �H  C   I  D   GI  '   �I  *   �I  C   �I  K   #J  )   oJ  &   �J  %   �J  <   �J  ;   #K  ,   _K  +   �K  4   �K  7   �K  A   %L  ?   gL     �L     �L     �L  (   �L  9   M  J   LM  @   �M  L   �M  +   %N  >   QN     �N  /   �N  B   �N     "O  "   .O  1   QO  2   �O     �O  +   �O  *   �O  *   !P     LP     QP  
   pP     {P     �P     �P  /   �P  )    Q  $   *Q  '   OQ     wQ  -   �Q  
   �Q     �Q     �Q     �Q  J   �Q     R      R        W   s   a   !   �   %      X      �   j       C   ~         l   o   �   ^   :   �       e               u   �       f   V          �   v      <   c   E   
   �       R   �   �   ,       )   g       K   �               D                     S       i      	   _       9   N   �           J   2   �       \   h      >   m   y   ?              /         $                     0           -   �       1   .       �   (   �   F               `           �   8   x   �      H       @           I   �       �           �       Z       &          {   �       ]              '   A       n   G   Y   �   |   }       �   �   M   �       *      =   U   t   d       T      Q          �   #   �      P   O   r   B   k   4   +      ;   w       �   p   b               7   q       L   �   "               z               �   �   �       �   �       [   5       6   3       
By default, a database with the same name as the current user is created.
 
Connection options:
 
If one of -d, -D, -r, -R, -s, -S, and ROLENAME is not specified, you will
be prompted interactively.
 
Options:
 
Read the description of the SQL command CLUSTER for details.
 
Read the description of the SQL command REINDEX for details.
 
Read the description of the SQL command VACUUM for details.
 
Report bugs to <pgsql-bugs@postgresql.org>.
       --lc-collate=LOCALE      LC_COLLATE setting for the database
       --lc-ctype=LOCALE        LC_CTYPE setting for the database
   %s [OPTION]... DBNAME
   %s [OPTION]... LANGNAME [DBNAME]
   %s [OPTION]... [DBNAME]
   %s [OPTION]... [DBNAME] [DESCRIPTION]
   %s [OPTION]... [ROLENAME]
   --help                          show this help, then exit
   --help                       show this help, then exit
   --help                    show this help, then exit
   --version                       output version information, then exit
   --version                    output version information, then exit
   --version                 output version information, then exit
   -D, --no-createdb         role cannot create databases
   -D, --tablespace=TABLESPACE  default tablespace for the database
   -E, --encoding=ENCODING      encoding for the database
   -E, --encrypted           encrypt stored password
   -F, --freeze                    freeze row transaction information
   -I, --no-inherit          role does not inherit privileges
   -L, --no-login            role cannot login
   -N, --unencrypted         do not encrypt stored password
   -O, --owner=OWNER            database user to own the new database
   -P, --pwprompt            assign a password to new role
   -R, --no-createrole       role cannot create roles
   -S, --no-superuser        role will not be superuser
   -T, --template=TEMPLATE      template database to copy
   -U, --username=USERNAME      user name to connect as
   -U, --username=USERNAME   user name to connect as
   -U, --username=USERNAME   user name to connect as (not the one to create)
   -U, --username=USERNAME   user name to connect as (not the one to drop)
   -W, --password               force password prompt
   -W, --password            force password prompt
   -Z, --analyze-only              only update optimizer statistics
   -a, --all                       vacuum all databases
   -a, --all                 cluster all databases
   -a, --all                 reindex all databases
   -c, --connection-limit=N  connection limit for role (default: no limit)
   -d, --createdb            role can create new databases
   -d, --dbname=DBNAME             database to vacuum
   -d, --dbname=DBNAME       database from which to remove the language
   -d, --dbname=DBNAME       database to cluster
   -d, --dbname=DBNAME       database to install language in
   -d, --dbname=DBNAME       database to reindex
   -e, --echo                      show the commands being sent to the server
   -e, --echo                   show the commands being sent to the server
   -e, --echo                show the commands being sent to the server
   -f, --full                      do full vacuuming
   -h, --host=HOSTNAME          database server host or socket directory
   -h, --host=HOSTNAME       database server host or socket directory
   -i, --index=INDEX         recreate specific index only
   -i, --inherit             role inherits privileges of roles it is a
                            member of (default)
   -i, --interactive         prompt before deleting anything
   -l, --list                show a list of currently installed languages
   -l, --locale=LOCALE          locale settings for the database
   -l, --login               role can login (default)
   -p, --port=PORT              database server port
   -p, --port=PORT           database server port
   -q, --quiet                     don't write any messages
   -q, --quiet               don't write any messages
   -r, --createrole          role can create new roles
   -s, --superuser           role will be superuser
   -s, --system              reindex system catalogs
   -t, --table='TABLE[(COLUMNS)]'  vacuum specific table only
   -t, --table=TABLE         cluster specific table only
   -t, --table=TABLE         reindex specific table only
   -v, --verbose                   write a lot of output
   -v, --verbose             write a lot of output
   -w, --no-password            never prompt for password
   -w, --no-password         never prompt for password
   -z, --analyze                   update optimizer statistics
 %s (%s/%s)  %s cleans and analyzes a PostgreSQL database.

 %s clusters all previously clustered tables in a database.

 %s creates a PostgreSQL database.

 %s creates a new PostgreSQL role.

 %s installs a procedural language into a PostgreSQL database.

 %s reindexes a PostgreSQL database.

 %s removes a PostgreSQL database.

 %s removes a PostgreSQL role.

 %s removes a procedural language from a database.

 %s: "%s" is not a valid encoding name
 %s: cannot cluster a specific table in all databases
 %s: cannot cluster all databases and a specific one at the same time
 %s: cannot reindex a specific index and system catalogs at the same time
 %s: cannot reindex a specific index in all databases
 %s: cannot reindex a specific table and system catalogs at the same time
 %s: cannot reindex a specific table in all databases
 %s: cannot reindex all databases and a specific one at the same time
 %s: cannot reindex all databases and system catalogs at the same time
 %s: cannot use the "freeze" option when performing only analyze
 %s: cannot use the "full" option when performing only analyze
 %s: cannot vacuum a specific table in all databases
 %s: cannot vacuum all databases and a specific one at the same time
 %s: clustering database "%s"
 %s: clustering of database "%s" failed: %s %s: clustering of table "%s" in database "%s" failed: %s %s: comment creation failed (database was created): %s %s: could not connect to database %s
 %s: could not connect to database %s: %s %s: could not get current user name: %s
 %s: could not obtain information about current user: %s
 %s: creation of new role failed: %s %s: database creation failed: %s %s: database removal failed: %s %s: language "%s" is already installed in database "%s"
 %s: language "%s" is not installed in database "%s"
 %s: language installation failed: %s %s: language removal failed: %s %s: missing required argument database name
 %s: missing required argument language name
 %s: only one of --locale and --lc-collate can be specified
 %s: only one of --locale and --lc-ctype can be specified
 %s: out of memory
 %s: query failed: %s %s: query was: %s
 %s: reindexing database "%s"
 %s: reindexing of database "%s" failed: %s %s: reindexing of index "%s" in database "%s" failed: %s %s: reindexing of system catalogs failed: %s %s: reindexing of table "%s" in database "%s" failed: %s %s: removal of role "%s" failed: %s %s: too many command-line arguments (first is "%s")
 %s: vacuuming database "%s"
 %s: vacuuming of database "%s" failed: %s %s: vacuuming of table "%s" in database "%s" failed: %s Are you sure? Cancel request sent
 Could not send cancel request: %s Database "%s" will be permanently removed.
 Enter it again:  Enter name of role to add:  Enter name of role to drop:  Enter password for new role:  Name Password encryption failed.
 Password:  Passwords didn't match.
 Please answer "%s" or "%s".
 Procedural Languages Role "%s" will be permanently removed.
 Shall the new role be a superuser? Shall the new role be allowed to create databases? Shall the new role be allowed to create more new roles? Trusted? Try "%s --help" for more information.
 Usage:
 n no out of memory
 pg_strdup: cannot duplicate null pointer (internal error)
 y yes Project-Id-Version: pgscripts (PostgreSQL) 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2013-01-29 13:57+0000
PO-Revision-Date: 2012-12-03 18:45+0100
Last-Translator: Daniele Varrazzo <daniele.varrazzo@gmail.com>
Language-Team: Gruppo traduzioni ITPUG <traduzioni@itpug.org>
Language: it
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-Poedit-SourceCharset: utf-8
X-Generator: Poedit 1.5.4
 
Se il nome non è specificato verrà creato un database con lo stesso nome
dell'utente corrente.
 
Opzioni di connessione:
 
Se una tra -d, -D, -r, -R, -s, -S e ROLENAME manca, ti sarà chiesta
interattivamente.
 
Opzioni:
 
Consulta la descrizione del comando SQL CLUSTER per maggiori informazioni.
 
Consulta la descrizione del comando SQL REINDEX per maggiori informazioni.
 
Consulta la descrizione del comando SQL VACUUM per maggiori informazioni.
 
Puoi segnalare eventuali bug a <pgsql-bugs@postgresql.org>.
       --lc-collate=LOCALE      impostazione LC_COLLATE per il database
       --lc-ctype=LOCALE        impostazione LC_CTYPE per il database
   %s [OPZIONE]... NOMEDB
   %s [OPZIONE]... NOME_LINGUAGGIO [NOMEDB]
   %s [OPZIONE]... [NOMEDB]
   %s [OPZIONE]... [NOMEDB] [DESCRIZIONE]
   %s [OPZIONI]... [NOME_RUOLO]
   --help                          mostra questo aiuto ed esci
   --help                       mostra questo aiuto ed esci
   --help                    mostra questo aiuto ed esci
   --version                       mostra informazioni sulla versione ed esci
   --version                    mostra informazioni sulla versione ed esci
   --version                 mostra informazioni di versione ed esci
   -D, --no-createdb         il ruolo non può creare database
   -D, --tablespace=TABLESPACE  tablespace predefinito per il database
   -E, --encoding=ENCODING      codifica del database
   -E, --encrypted           cripta la password salvata
   -F, --freeze                    congela le informazioni per la transazione
                                  sulla riga
   -I, --no-inherit          il ruolo non eredita privilegi
   -L, --no-login            il ruolo non può accedere al database
   -N, --unencrypted         non criptare la password salvata
   -O, --owner=OWNER            utente database proprietario del nuovo database
   -P, --pwprompt            assegna una password al nuovo ruolo
   -R, --no-createrole       il ruolo non può creare ruoli
   -S, --no-superuser        il ruolo non sarà un superutente
   -T, --template=TEMPLATE      modello database da copiare
   -U, --username=USERNAME      nome utente da usare per connettersi
   -U, --username=UTENTE     nome utente da utilizzare per la connessione
   -U, --username=UTENTE     nome utente con cui collegarsi
                            (non quello da creare)
   -U, --username=UTENTE     nome utente con cui collegarsi
                            (non quello da eliminare)
   -W, --password               forza la richiesta di una password
   -W, --password            forza la richiesta di una password
   -Z, --analyze-only              aggiorna soltanto le statistiche per
                                  l'ottimizzatore
   -a, --all                       pulisci tutti i database
   -a, --all                 raggruppa tutti i database
   -a, --all                 re-indicizza tutti i database
   -c, --connection-limit=N  limite di connessione per un ruolo (predefinito: nessun limite)
   -d, --createdb            il ruolo può creare nuovi database
   -d, --dbname=NOMEDB             database da pulire
   -d, --dbname=NOMEDB       database dal quale eliminare il linguaggio
   -d, --dbname=NOMEDB       database da raggruppare
   -d, --dbname=NOMEDB       database in cui installare il linguaggio
   -d, --dbname=NOMEDB       database da re-indicizzare
   -e, --echo                      mostra i comandi inviati al server
   -e, --echo                   mostra i comandi che vengono inviati al server
   -e, --echo                mostra i comandi inviati al server
   -f, --full                      esegui una pulizia completa
   -h, --host=HOSTNAME          host del server database o directory socket
   -h, --host=HOSTNAME       host del server database o directory socket
   -i, --index=NOME          ricrea solo l'indice specificato
   -i, --inherit             il ruolo eredita i privilegi dei ruoli di cui
                            è membro (predefinito)
   -i, --interactive         chiedi conferma prima di cancellare qualunque cosa
   -l, --list                mostra la lista dei linguaggi attualmente installati
   -l, --locale=LOCALE          impostazioni di localizzazione del database
   -l, --login               il ruolo può accedere al database (predefinito)
   -p, --port=PORT              porta del server database
   -p, --port=PORTA          porta del server database
   -q, --quiet                     non stampare alcun messaggio
   -q, --quiet               non stampare alcun messaggio
   -r, --createrole          il ruolo può creare nuovi ruoli
   -s, --superuser           il ruolo sarà un superutente
   -s, --system              re-indicizza i cataloghi di sistema
   -t, --table='TABELLA[(COL)]'    pulisci solamente la tabella specificata
   -t, --table=TABELLA       raggruppa solo la tabella specificata
   -t, --table=TABELLA       re-indicizza solo la tabella specificata
   -v, --verbose                   mostra molti messaggi
   -v, --verbose             mostra un output completo
   -w, --no-password            non richiedere mai una password
   -w, --no-password         non richiedere mai una password
   -z, --analyze                   aggiorna le statistiche per l'ottimizzatore
 %s (%s/%s)  %s pulisce ed analizza un database PostgreSQL.

 %s raggruppa tutte le tabelle precedentemente raggruppate in un database.

 %s crea un database PostgreSQL.

 %s crea un nuovo ruolo PostgreSQL.

 %s installa un linguaggio procedurale in un database PostgreSQL.

 %s re-indicizza un database PostgreSQL.

 %s elimina un database PostgreSQL.

 %s elimina un ruolo PostgreSQL.

 %s elimina un linguaggio procedurale da un database.

 %s: "%s" non è un nome di codifica valido
 %s: non è possibile raggruppare una tabella specifica in tutti i database
 %s: non è possibile raggruppare tutti i database ed uno specifico nello stesso momento
 %s: non è possibile re-indicizzare un indice specifico ed i cataloghi di sistema stesso momento
 %s: non è possibile re-indicizzare un indice specifico in tutti i database
 %s: non è possibile re-indicizzare una tabella specifica ed i cataloghi di sistema nello stesso momento
 %s: non è possibile re-indicizzare una tabella specifica in tutti i database
 %s: non è possibile re-indicizzare tutti i database ed uno specifico nello stesso momento
 %s: non è possibile re-indicizzare tutti i database e i cataloghi di sistema nello stesso momento
 %s: non è possibile usare l'opzione "freeze" quando si effettua solo analyze
 %s: non è possibile usare l'opzione "full" quando si effettua solo analyze
 %s: non è possibile effettuare la pulizia di una tabella specifica in tutti i database
 %s: non è possibile effettuare la pulizia di tutti i database e di uno in particolare nello stesso momento
 %s: riordino del database "%s"
 %s: il riordino del database "%s" è fallito: %s %s: il riordino della tabella "%s" nel database "%s" è fallito: %s %s: creazione del commento fallita (il database è stato creato): %s %s: connessione al database %s fallita
 %s: connessione al database %s fallita: %s %s: non è stato possibile determinare il nome utente corrente: %s
 %s: non è stato possibile acquisire informazioni sull'utente corrente: %s
 %s: creazione del nuovo ruolo fallita: %s %s: creazione del database fallita: %s %s: eliminazione database fallita: %s %s: il linguaggio "%s" è già installato nel database "%s"
 %s: il linguaggio "%s" non è installato nel database "%s"
 %s: installazione del linguaggio fallita: %s %s: eliminazione del linguaggio fallita: %s %s: parametro richiesto mancante: nome del database
 %s: parametro mancante necessario: nome del linguaggio
 %s: solo uno tra --locale e --lc-collate può essere specificato
 %s: solo uno tra --locale e --lc-ctype può essere specificato
 %s: memoria esaurita
 %s: query fallita: %s %s: la query era: %s
 %s: re-indicizzazione del database "%s"
 %s: la re-indicizzazione del database "%s" è fallita: %s %s: la re-indicizzazione dell'indice "%s" nel database "%s" è fallita: %s %s: la re-indicizzazione dei cataloghi di sistema è fallita: %s %s: la re-indicizzazione della tabella "%s" nel database "%s" è fallita: %s %s: eliminazione del ruolo "%s" fallita: %s %s: troppi argomenti nella riga di comando (il primo è "%s")
 %s: pulizia del database "%s"
 %s: la pulizia del database "%s" è fallita: %s %s: la pulizia della tabella "%s" nel database "%s" è fallita: %s Sei sicuro? Richiesta di annullamento inviata
 Invio della richiesta di annullamento fallita: %s Il database "%s" sarà eliminato definitivamente.
 Conferma password:  Inserisci il nome del ruolo da aggiungere:  Inserisci il nome del ruolo da eliminare:  Inserisci la password per il nuovo ruolo:  Nome Criptazione password fallita.
 Password:  Le password non corrispondono.
 Prego rispondere "%s" o "%s".
 Linguaggi Procedurali Il ruolo "%s" sarà eliminato definitivamente.
 Il nuovo ruolo dev'essere un superutente? Il nuovo ruolo può creare database? Il nuovo ruolo può creare altri ruoli? Affidabile? Prova "%s --help" per maggiori informazioni.
 Utilizzo:
 n no memoria esaurita
 pg_strdup: non è possibile duplicare un puntatore nullo (errore interno)
 s sì 