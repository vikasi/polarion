��    A      $  Y   ,      �     �     �  !   �  -   �     
  5     A   R  >   �  6   �  5   
  >   @  O     L   �  [     D   x  ,   �  7   �  3   "	  5   V	  @   �	  /   �	  8   �	  D   6
  Z   {
  P   �
  4   '  @   \  (   �  #   �  .   �  (     #   B  9   f  &   �  /   �  #   �  &        B     a      �  *   �  +   �  4   �  /   -  0   ]  %   �  +   �     �  A   �  #   ?  9   c  &   �  -   �  !   �  (     "   =  ,   `     �  ,   �  4   �  %     
   (  &   3     Z    b     s     �  '   �  4   �     �  >     O   Q  L   �  S   �  =   B  P   �  a   �  i   3  }   �  T     A   p  H   �  -   �  +   )  Q   U  4   �  K   �  �   (  �   �  �   |  F   �  m   E  -   �  +   �  -     +   ;  #   g  <   �  %   �  A   �  R   0  0   �  %   �  +   �       ?   $  /   d  C   �  B   �  2     (   N  ,   w  "   �  J   �  $      G   7   &      <   �       �   2   !  .   7!  ;   f!     �!  7   �!  B   �!  +   8"  	   d"  6   n"     �"                /             +   )         ;          %          (                  
   2   :   @   6      "                  ,          8      <   .                       5      	               ?           #                      $                     0   *   &      1   9   =      4   !       '   3   >       A   -      7       
Connection options:
 
General options:
 
Options controlling the output:
 
Report bugs to <pgsql-bugs@postgresql.org>.
   %s [OPTION]...
   --help                   show this help, then exit
   --version                output version information, then exit
   -D, --pgdata=DIRECTORY   receive base backup into directory
   -F, --format=p|t         output format (plain, tar)
   -P, --progress           show progress information
   -U, --username=NAME      connect as specified database user
   -W, --password           force password prompt (should happen automatically)
   -Z, --compress=0-9       compress tar output with given compression level
   -c, --checkpoint=fast|spread
                           set fast or spread checkpointing
   -h, --host=HOSTNAME      database server host or socket directory
   -l, --label=LABEL        set backup label
   -p, --port=PORT          database server port number
   -v, --verbose            output verbose messages
   -w, --no-password        never prompt for password
   -x, --xlog               include required WAL files in backup
   -z, --gzip               compress tar output
 %s takes a base backup of a running PostgreSQL server.

 %s/%s kB (%d%%), %d/%d tablespace %s/%s kB (%d%%), %d/%d tablespaces %s/%s kB (%d%%), %d/%d tablespace (%-30.30s) %s/%s kB (%d%%), %d/%d tablespaces (%-30.30s) %s/%s kB (100%%), %d/%d tablespace %35s %s/%s kB (100%%), %d/%d tablespaces %35s %s: COPY stream ended before last file was finished
 %s: can only write single tablespace to stdout, database has %i
 %s: could not access directory "%s": %s
 %s: could not connect to server: %s %s: could not create compressed file "%s": %s
 %s: could not create directory "%s": %s
 %s: could not create file "%s": %s
 %s: could not create symbolic link from "%s" to "%s": %s
 %s: could not get COPY data stream: %s %s: could not get WAL end position from server
 %s: could not get backup header: %s %s: could not initiate base backup: %s %s: could not parse file mode
 %s: could not parse file size
 %s: could not read COPY data: %s %s: could not send base backup command: %s %s: could not set compression level %i: %s
 %s: could not set permissions on directory "%s": %s
 %s: could not set permissions on file "%s": %s
 %s: could not write to compressed file "%s": %s
 %s: could not write to file "%s": %s
 %s: directory "%s" exists but is not empty
 %s: final receive failed: %s %s: invalid checkpoint argument "%s", must be "fast" or "spread"
 %s: invalid compression level "%s"
 %s: invalid output format "%s", must be "plain" or "tar"
 %s: invalid tar block header size: %i
 %s: no WAL end position returned from server
 %s: no data returned from server
 %s: no start point returned from server
 %s: no target directory specified
 %s: only tar mode backups can be compressed
 %s: out of memory
 %s: this build does not support compression
 %s: too many command-line arguments (first is "%s")
 %s: unrecognized link indicator "%c"
 Password:  Try "%s --help" for more information.
 Usage:
 Project-Id-Version: PostgreSQL 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2011-12-02 04:41+0000
PO-Revision-Date: 2011-12-12 11:42-0000
Last-Translator: Gheorge Rosca Codreanu <max@oceanline.co.uk>
Language-Team: ROMANA <xsoftware.consultancy@gmail.com>
Language: ro
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=3; plural=n==1 ? 0 : (n==0 || (n%100 > 0 && n%100 < 20)) ? 1 : 2;
X-Poedit-Language: Romanian
X-Poedit-Country: ROMANIA
 
Opţiuni de conectare:
 
Opţiuni generale:
 
Opţiuni care controlează ieşirile:
 
Raportaţi erorile la <pgsql-bugs@postgresql.org>.
   %s [OPŢIUNE]...
   --help                   afişează acest ajutor, apoi iese
   --version                afişează informaţiile despre versiune, apoi iese
   -D, --pgdata=DIRECTOR   salvează copia de rezervă a bazei in directorul
   -F, --format=p|t         formatul fişierului (p - text, t - arhivă de tip tar)
   -P, --progress           arată informaţii despre progres
   -U, --username=NUME      conectare ca utilizatorul de baze de date specificat
   -W, --password           forţează solicitarea parolei (în mod normal, se face automat)
   -Z, --compress=0-9       comprimă în format tar cu specificarea nivelului de comprimare de la 0 la 9
   -c, --checkpoint=fast|spread
                           stabileşte tipul de checkpoint fast(rapid) sau spread(distribuit)
   -h, --host=NUMEGAZDĂ    gazda serverului de baze de date sau directorul soclului
   -l, --label=ETICHETĂ  stabileşte eticheta copiei de rezervă
   -p, --port=PORT          numărul portului serverului de baze de date
   -v, --verbose            afişează mesaje
   -w, --no-password        nu cere parolă
   -x, --xlog               include fişierele WAL necesare în copia de rezervă
   -z, --gzip               comprimă în format tar
 %s face o copie de rezervă a bazei unui server PostgreSQL care rulează.

 %s/%s kB (%d%%), %d/%d spaţiutabelă (tablespace) %s/%s kB (%d%%), %d/%d spaţiutabele (tablespaces) %s/%s kB (%d%%), %d/%d spaţiutabele (tablespaces) %s/%s kB (%d%%), %d/%d spaţiutabelă (tablespace)  (%-30.30s) %s/%s kB (%d%%), %d/%d spaţiutabele (tablespaces) (%-30.30s) %s/%s kB (%d%%), %d/%d spaţiutabele (tablespaces) (%-30.30s) %s/%s kB (100%%), %d/%d spaţiutabelă %35s %s/%s kB (100%%), %d/%d spaţiutabele %35s %s/%s kB (100%%), %d/%d spaţiutabele %35s %s: fluxul COPY s-a întrerupt înainte de terminarea ultimul fişier
 %s: pot scrie doar un singur spaţiu tabelă (tablespace) la ieşirea standard (stdout), baza de date are %i
 %s: imposibil de accesat directorul "%s": %s
 %s: nu pot realiza conexiunea la server: %s %s: nu pot crea fişierul comprimat "%s": %s
 %s: imposibil de creat directorul "%s": %s
 %s: nu pot crea fişierul "%s": %s
 %s: nu pot crea legătura simbolică de la "%s" la "%s": %s
 %s: nu pot obţine COPY flux date: %s %s: nu pot obţine de la server poziţia de sfârşit pentru WAL
 %s: nu pot obţine începutul de fişier al copiei de rezervă (backup header): %s %s: nu pot iniţia copia de rezervă a bazei: %s %s: nu pot analiza modul fişierului
 %s: nu pot analiza dimensiunea fişierului
 %s: nu pot citi COPY date: %s %s: nu pot trimite comanda de realizarea copiei de rezervă: %s %s: nu pot stabili nivelul de compresie %i: %s
 %s: nu pot stabili drepturile de access pentru directorul "%s": %s
 %s: nu pot stabili drepturile de access pentru fişierul "%s": %s
 %s: nu pot scrie în fişierul comprimat "%s": %s
 %s: nu pot scrie în fişierul "%s": %s
 %s: directorul "%s" există dar nu este gol
 %s: recepţia finală a eşuat: %s %s: argument checkpoint invalid "%s", trebuie să fie "fast" sau "spread"
 %s: nivel de compresie invalid "%s"
 %s: format de ieşire invalid "%s", trebuie să fiel "plain" sau "tar"
 %s: invalid tar block header size: %i
 %s: serverul nu a returnat poziţia de sfârşit pentru WAL
 %s: serverul nu a returnat date
 %s: serverul nu a returnat nici un punct de start
 %s: directorul destinaţie nu este specificat
 %s: numai copiile de rezervă de tip tar pot fi comprimate
 %s: memorie insuficientă
 %s: această versiune de program nu suportă compresie
 %s: prea multe argumente în linia de comandă (primul este "%s")
 %s: indicator al legaturii necunoscut "%c"
 Parolă:  Încercaţi "%s --help" pentru mai multe informaţii.
 Utilizare:
 