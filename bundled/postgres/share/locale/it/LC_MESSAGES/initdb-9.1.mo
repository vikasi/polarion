��    �      �  �   �      �  R   �     <  
   Z     e  -   v  �   �  �   .    �  A   �  5     J   N     �  6   �  P   �  C   =  :   �  ]   �  4     B   O  H   �  G   �  >   #  9   b  3   �  ?   �  /     -   @  y   n  (   �  #     7   5  (   m  ,   �  '   �  3   �  D     (   d  8   �  -   �  -   �  /   "  "   R  6   u  +   �     �  0   �  ;      $   \  /   �     �  $   �  ~   �  1   s     �  /   �  6   �  &   *  J   Q  �   �     d      w  C   �  -   �  8   
  !   C  ,   e  /   �  4   �  A   �  @   9  ,   z  P   �  I   �  �   B  b   �     '     8  �   W  [   �  %   7     ]     v     �  ;   �  9   �  �   "   >   �   ;   �     .!  u   ?"  q   �"  f   '#  s   �#  &   $     )$  )   1$     [$  &   j$  0   �$  .   �$  )   �$  )   %  "   E%  #   h%  "   �%  #   �%      �%  $   �%  (   &  "   B&     e&  "   �&  !   �&  ,   �&  $   �&  *   '  %   B'     h'  !   �'     �'     �'     �'      �'     (     1(  -   L(  0   z(     �(     �(     �(  *   �(  )   %)     O)     o)     s)  &   �)  %   �)     �)  +   �)  !   *  �  3*  V   �+  )   T,  
   ~,     �,  =   �,  �   �,  �   c-  y  >.  k   �/  5   $0  P   Z0     �0  8   �0  w   1  m   y1  9   �1  n   !2  >   �2  G   �2  J   3  u   b3  9   �3  9   4  ;   L4  C   �4  3   �4  9    5  �   :5  ,   �5  %   �5  ;   $6  /   `6  2   �6  ,   �6  N   �6  a   ?7  C   �7  K   �7  :   18  <   l8  5   �8  -   �8  =   9  -   K9  '   y9  9   �9  N   �9  +   *:  @   V:     �:  )   �:  �   �:  D   |;      �;  8   �;  :   <  *   V<  Z   �<  �   �<     �=  )   �=  q   �=  6   J>  K   �>  (   �>  =   �>  >   4?  >   s?  R   �?  L   @  .   R@  p   �@  ]   �@  �   PA  g   �A     LB  -   `B  �   �B  V   C  "   iC     �C  %   �C     �C  C   �C  =   5D  �   sD  >   E  G   EE  *  �E  s   �F  �   ,G  �   �G  l   ;H  -   �H  
   �H  /   �H     I  .   'I  4   VI  +   �I  '   �I  '   �I  "   J  #   *J  (   NJ  *   wJ  &   �J  :   �J  4   K  *   9K      dK  )   �K  '   �K  0   �K  ;   L  K   DL  /   �L  +   �L  )   �L      M     7M  !   PM  (   rM  #   �M  %   �M  5   �M  9   N  &   UN  "   |N     �N  4   �N  ;   �N  %   (O     NO     RO  8   dO  ,   �O     �O  ;   �O  "   !P     t   Z   6   4              x          )                   !       D   ,   ~   g         C      f   -   �   b       o       F   *      .       �   m                     /   ?       @   
   �   L   $   e       l   2       �       U       +   (   1   8       ;       �      W      M      s   5      =      z      &      T   :       "   S   u      Y   p          �                   B   I   {   A       �       O      #   n   j   r       '   H   7          a   �   ^   �   G   y   %   w      �   |   J             0       �   �   <       h      `   v   V   �   [             d   k               >               }   \   _      i              	       R   Q             E   ]   3   N           q           9   K   �   c           P   X    
If the data directory is not specified, the environment variable PGDATA
is used.
 
Less commonly used options:
 
Options:
 
Other options:
 
Report bugs to <pgsql-bugs@postgresql.org>.
 
Success. You can now start the database server using:

    %s%s%spostgres%s -D %s%s%s
or
    %s%s%spg_ctl%s -D %s%s%s -l logfile start

 
WARNING: enabling "trust" authentication for local connections
You can change this by editing pg_hba.conf or using the -A option the
next time you run initdb.
       --lc-collate=, --lc-ctype=, --lc-messages=LOCALE
      --lc-monetary=, --lc-numeric=, --lc-time=LOCALE
                            set default locale in the respective category for
                            new databases (default taken from environment)
       --locale=LOCALE       set default locale for new databases
       --no-locale           equivalent to --locale=C
       --pwfile=FILE         read password for the new superuser from file
   %s [OPTION]... [DATADIR]
   -?, --help                show this help, then exit
   -A, --auth=METHOD         default authentication method for local connections
   -E, --encoding=ENCODING   set default encoding for new databases
   -L DIRECTORY              where to find the input files
   -T, --text-search-config=CFG
                            default text search configuration
   -U, --username=NAME       database superuser name
   -V, --version             output version information, then exit
   -W, --pwprompt            prompt for a password for the new superuser
   -X, --xlogdir=XLOGDIR     location for the transaction log directory
   -d, --debug               generate lots of debugging output
   -n, --noclean             do not clean up after errors
   -s, --show                show internal settings
  [-D, --pgdata=]DATADIR     location for this database cluster
 %s initializes a PostgreSQL database cluster.

 %s: "%s" is not a valid server encoding name
 %s: cannot be run as root
Please log in (using, e.g., "su") as the (unprivileged) user that will
own the server process.
 %s: could not access directory "%s": %s
 %s: could not access file "%s": %s
 %s: could not change permissions of directory "%s": %s
 %s: could not create directory "%s": %s
 %s: could not create symbolic link "%s": %s
 %s: could not execute command "%s": %s
 %s: could not find suitable encoding for locale %s
 %s: could not find suitable text search configuration for locale %s
 %s: could not get current user name: %s
 %s: could not obtain information about current user: %s
 %s: could not open file "%s" for reading: %s
 %s: could not open file "%s" for writing: %s
 %s: could not read password from file "%s": %s
 %s: could not write file "%s": %s
 %s: data directory "%s" not removed at user's request
 %s: directory "%s" exists but is not empty
 %s: encoding mismatch
 %s: failed to remove contents of data directory
 %s: failed to remove contents of transaction log directory
 %s: failed to remove data directory
 %s: failed to remove transaction log directory
 %s: file "%s" does not exist
 %s: file "%s" is not a regular file
 %s: input file "%s" does not belong to PostgreSQL %s
Check your installation or specify the correct path using the option -L.
 %s: input file location must be an absolute path
 %s: invalid locale name "%s"
 %s: locale %s requires unsupported encoding %s
 %s: locale name has non-ASCII characters, skipped: %s
 %s: locale name too long, skipped: %s
 %s: must specify a password for the superuser to enable %s authentication
 %s: no data directory specified
You must identify the directory where the data for this database system
will reside.  Do this with either the invocation option -D or the
environment variable PGDATA.
 %s: out of memory
 %s: password file "%s" is empty
 %s: password prompt and password file cannot be specified together
 %s: removing contents of data directory "%s"
 %s: removing contents of transaction log directory "%s"
 %s: removing data directory "%s"
 %s: removing transaction log directory "%s"
 %s: symlinks are not supported on this platform %s: too many command-line arguments (first is "%s")
 %s: transaction log directory "%s" not removed at user's request
 %s: transaction log directory location must be an absolute path
 %s: unrecognized authentication method "%s"
 %s: warning: specified text search configuration "%s" might not match locale %s
 %s: warning: suitable text search configuration for locale %s is unknown
 Encoding %s implied by locale is not allowed as a server-side encoding.
The default database encoding will be set to %s instead.
 Encoding %s is not allowed as a server-side encoding.
Rerun %s with a different locale selection.
 Enter it again:  Enter new superuser password:  If you want to create a new database system, either remove or empty
the directory "%s" or run %s
with an argument other than "%s".
 If you want to store the transaction log there, either
remove or empty the directory "%s".
 No usable system locales were found.
 Passwords didn't match.
 Rerun %s with the -E option.
 Running in debug mode.
 Running in noclean mode.  Mistakes will not be cleaned up.
 The database cluster will be initialized with locale %s.
 The database cluster will be initialized with locales
  COLLATE:  %s
  CTYPE:    %s
  MESSAGES: %s
  MONETARY: %s
  NUMERIC:  %s
  TIME:     %s
 The default database encoding has accordingly been set to %s.
 The default text search configuration will be set to "%s".
 The encoding you selected (%s) and the encoding that the
selected locale uses (%s) do not match.  This would lead to
misbehavior in various character string processing functions.
Rerun %s and either do not specify an encoding explicitly,
or choose a matching combination.
 The files belonging to this database system will be owned by user "%s".
This user must also own the server process.

 The program "postgres" is needed by %s but was not found in the
same directory as "%s".
Check your installation.
 The program "postgres" was found by "%s"
but was not the same version as %s.
Check your installation.
 This might mean you have a corrupted installation or identified
the wrong directory with the invocation option -L.
 Try "%s --help" for more information.
 Usage:
 Use the option "--debug" to see details.
 caught signal
 child process exited with exit code %d child process exited with unrecognized status %d child process was terminated by exception 0x%X child process was terminated by signal %d child process was terminated by signal %s copying template1 to postgres ...  copying template1 to template0 ...  could not change directory to "%s" could not close directory "%s": %s
 could not find a "%s" to execute could not get junction for "%s": %s
 could not identify current directory: %s could not open directory "%s": %s
 could not read binary "%s" could not read directory "%s": %s
 could not read symbolic link "%s" could not remove file or directory "%s": %s
 could not set junction for "%s": %s
 could not stat file or directory "%s": %s
 could not write to child process: %s
 creating collations ...  creating configuration files ...  creating conversions ...  creating dictionaries ...  creating directory %s ...  creating information schema ...  creating subdirectories ...  creating system views ...  creating template1 database in %s/base/1 ...  fixing permissions on existing directory %s ...  initializing dependencies ...  initializing pg_authid ...  invalid binary "%s" loading PL/pgSQL server-side language ...  loading system objects' descriptions ...  not supported on this platform
 ok
 out of memory
 selecting default max_connections ...  selecting default shared_buffers ...  setting password ...  setting privileges on built-in objects ...  vacuuming database template1 ...  Project-Id-Version: initdb (PostgreSQL) 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2014-12-05 17:56+0000
PO-Revision-Date: 2014-12-06 18:31+0100
Last-Translator: Daniele Varrazzo <daniele.varrazzo@gmail.com>
Language-Team: Gruppo traduzioni ITPUG <traduzioni@itpug.org>
Language: it
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-Poedit-SourceCharset: utf-8
X-Generator: Poedit 1.5.4
 
Se la directory dati non è specificata, viene usata la variabile
d'ambiente PGDATA.
 
Opzioni utilizzate meno frequentemente:
 
Opzioni:
 
Altre opzioni:
 
Puoi segnalare eventuali bug a <pgsql-bugs@postgresql.org>.
 
Successo. Ora puoi avviare il server database con:

    %s%s%spostgres%s -D %s%s%s
oppure
    %s%s%spg_ctl%s -D %s%s%s -l logfile start

 
ATTENZIONE: abilitazione dell'autenticazione "trust" per le connessioni
locali. È possibile cambiare questa impostazione modificando il file
pg_hba.conf o utilizzando l'opzione -A alla prossima esecuzione di initdb.
       --lc-collate, --lc-ctype, --lc-messages=LOCALE
      --lc-monetary, --lc-numeric, --lc-time=LOCALE
                            inizializza il nuovo cluster di database con il
                            locale specificato nella categoria corrispondente.
                            Il valore predefinito viene preso dalle variabili
                            d'ambiente
       --locale=LOCALE       imposta il locale predefinito per i nuovi
                            database
       --no-locale           equivalente a --locale=C
       --pwfile=FILE         leggi la password per il nuovo superutente dal file
   %s [OPZIONE]... [DATADIR]
   -?, --help                mostra questo aiuto ed esci
   -A, --auth=METODO         metodo di autenticazione predefinito per le
                            connessioni locali
   -E, --encoding=ENCODING   imposta la codifica predefinita per i nuovi
                            database
   -L DIRECTORY              dove trovare i file di input
   -T, --text-search-config=CFG
                            configurazione predefinita per la ricerca di testo
   -U, --username=NOME       nome del superutente del database
   -V, --version             mostra informazioni sulla versione ed esci
   -W, --pwprompt            richiedi la password per il nuovo superutente
   -X, --xlogdir=XLOGDIR     posizione della directory contenente i log
                            delle transazioni
   -d, --debug               genera molto output di debug
   -n, --noclean             non ripulire dopo gli errori
   -s, --show                mostra le impostazioni interne
  [-D, --pgdata=]DATADIR     dove creare questo cluster di database
 %s inizializza un cluster di database PostgreSQL.

 %s: "%s" non è un nome di codifica per il server valido
 %s: non può essere eseguito da root
Effettua il login (usando per esempio "su") con l'utente
(non privilegiato) che controllerà il processo server.
 %s: accesso alla directory "%s" fallito: %s
 %s: accesso al file "%s" fallito: %s
 %s: modifica dei permessi della directory "%s" fallita: %s
 %s: creazione della directory "%s" fallita: %s
 %s: creazione del link simbolico "%s" fallita: %s
 %s: esecuzione del comando "%s" fallita: %s
 %s: non è stato possibile determinare una codifica adeguata per il locale %s
 %s: non è stato possibile trovare una configurazione per la ricerca testo adeguata al locale %s
 %s: non è stato possibile determinare il nome utente corrente: %s
 %s: non è stato possibile acquisire informazioni sull'utente corrente: %s
 %s: errore nell'apertura del file "%s" per la lettura: %s
 %s: errore nell'apertura del file "%s" per la scrittura: %s
 %s: lettura del file delle password "%s" fallita: %s
 %s: errore nella scrittura del file "%s": %s
 %s: directory dati "%s" non rimossa su richiesta dell'utente
 %s: la directory "%s" esiste ma non è vuota
 %s: mancata corrispondenza di codifica
 %s: rimozione dei contenuti dalla directory dati fallita
 %s: rimozione dei contenuti della directory dei log delle transazioni fallita
 %s: rimozione della directory dati fallita
 %s: rimozione della directory dei log delle transazioni fallita
 %s: il file "%s" non esiste
 %s: il file "%s" non è un file regolare
 %s: il file di input "%s" non appartiene a PostgreSQL %s
Controlla la correttezza dell'installazione oppure specifica
il percorso corretto con l'opzione -L.
 %s: la posizione del file di input deve essere un percorso assoluto
 %s: nome locale non valido "%s"
 %s: il locale %s richiede la codifica non supportata %s
 %s: nome locale contiene caratteri non ASCII, saltato: %s
 %s: nome locale troppo lungo, saltato: %s
 %s: occorre specificare una password per il superutente per abilitare l'autenticazione %s
 %s: nessuna directory dati specificati
È necessario indicare la directory dove risiederanno i dati di questo
database. Puoi farlo usando l'opzione -D oppure la variabile globale
PGDATA.
 %s: memoria esaurita
 %s: il file delle password "%s" è vuoto
 %s: il prompt della password ed un file contenente la password non
possono essere specificati contemporaneamente
 %s: rimozione dei contenuti della directory dati "%s"
 %s: rimozione dei contenuti della directory dei log delle transazioni "%s"
 %s: rimozione della directory dati "%s"
 %s: rimozione della directory dei log delle transazioni "%s"
 %s: i link simbolici non sono supportati su questa piattaforma %s: troppi argomenti nella riga di comando (il primo è "%s")
 %s: directory dei log delle transazioni "%s" non rimossa su richiesta dell'utente
 %s: la directory dei log delle transazioni deve essere un percorso assoluto
 %s: metodo di autenticazione sconosciuto "%s"
 %s: attenzione: la configurazione specificata per la ricerca testo "%s"
potrebbe non corrispondere al locale %s
 %s: attenzione: non si conosce una configurazione per la ricerca testo adeguata al locale %s
 La codifica %s implicata dal locale non è consentita come codifica lato server.
La codifica predefinita dei database sarà impostata invece a %s.
 La codifica %s non è disponibile come codifica lato server.
Esegui di nuovo %s con un locale diverso.
 Conferma password:  Inserisci la nuova password del superutente:  Se vuoi creare un nuovo sistema di database, elimina o svuota
la directory "%s" oppure esegui %s
con un argomento diverso da "%s".
 Se vuoi salvare lì i log delle transazioni,
elimina oppure svuota la directory "%s".
 Nessun locale di sistema trovato.
 Le password non corrispondono.
 Esegui di nuovo %s con l'opzione -E.
 Esecuzione in modalità debug
 Esecuzione in modalità noclean. Gli errori non verranno ripuliti.
 Il cluster di database sarà inizializzato con il locale %s.
 Il cluster di database sarà inizializzato con i locale
  COLLATE:  %s
  CTYPE:    %s
  MESSAGES: %s
  MONETARY: %s
  NUMERIC:  %s
  TIME:     %s
 La codifica predefinita del database è stata impostata a %s.
 La configurazione predefinita di ricerca testo sarà impostata a "%s".
 La codifica selezionata (%s) e la codifica usata dal locale
(%s) non corrispondono. Questo causerebbe un funzionamento errato
in molte funzioni di elaborazione delle stringhe di caratteri.
Esegui di nuovo %s senza specificare una codifica esplicitamente
oppure seleziona una combinazione corretta.
 I file di questo database apparterranno all'utente "%s".
Questo utente deve inoltre possedere il processo server.

 Il programma "postgres" è richiesto da %s ma non è stato trovato
nella stessa directory "%s".
Verifica la correttezza dell'installazione.
 Il programma "postgres" è stato trovato da "%s"
ma non ha la stessa versione di %s.
Verifica la correttezza dell'installazione.
 Questo potrebbe indica una installazione corrotta oppure
hai indicato la directory errata con l'opzione -L.
 Prova "%s --help" per maggiori informazioni.
 Utilizzo:
 Usa l'opzione "--debug" per vedere i dettagli.
 intercettato segnale
 processo figlio uscito con codice di uscita %d processo figlio uscito con stato non riconosciuto %d processo figlio terminato da eccezione 0x%X processo figlio terminato da segnale %d processo figlio terminato da segnale %s copia di template1 a postgres ...  copia di template1 a template0 ...  spostamento nella directory "%s" fallito chiusura della directory "%s" fallita: %s
 programma "%s" da eseguire non trovato non è stato possibile ottenere la giunzione per "%s": %s
 identificazione della directory corrente fallita: %s apertura della directory "%s" fallita: %s
 lettura del binario "%s" fallita lettura della directory "%s" fallita: %s
 lettura del link simbolico "%s" fallita rimozione del file o directory "%s" fallita: %s
 non è stato possibile impostare la giunzione per "%s": %s
 non è stato possibile ottenere informazioni sul file o directory "%s": %s
 scrittura verso il processo figlio fallita: %s
 creazione degli ordinamenti alfabetici ...  creazione dei file di configurazione ...  creazione delle conversioni ...  creazione dizionari ...  creazione della directory %s ...  creazione dello schema informazioni ...  creazione delle sottodirectory ...  creazione delle viste di sistema ...  creazione del database template1 in in %s/base/1 ...  correzione dei permessi sulla directory esistente %s ...  inizializzazione delle dipendenze ...  inizializzazione di pg_authid ...  binario non valido "%s" caricamento del linguaggio lato server PL/pgSQL ...  caricamento delle descrizioni degli oggetti di sistema ...  non supportato su questa piattaforma
 ok
 memoria esaurita
 selezione del parametro max_connections predefinito ...  selezione di shared_buffers predefinito ...  impostazione password ...  impostazione dei privilegi per gli oggetti predefiniti ...  vacuum del database template1 ...  