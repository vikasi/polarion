��    :      �  O   �      �  X   �  C   R  -   �  !   �      �       ,     )   D  )   n  )   �  )   �  )   �  )     )   @  )   j  )   �  )   �  )   �  )     )   <  ,   f  )   �  )   �  )   �  ,   	  ,   >	  )   k	  )   �	  )   �	  )   �	  )   
  )   =
  )   g
  )   �
  ,   �
  ,   �
  ,     )   B  &   l  �   �  )     �   I               -     6     M     a     s  )   �  )   �  	   �     �     �               (  �  ?  h   =  D   �  3   �  (     )   H     r  :   �  5   �  5   �  4   3  4   h  4   �  4   �  4     6   <  4   s  4   �  4   �  4     4   G  7   |  4   �  4   �  4     7   S  7   �  4   �  4   �  4   -  4   b  4   �  5   �  8     4   ;  :   p  7   �  B   �  4   &  6   [  �   �  6   '  �   ^         )     ;     K      e  (   �     �  4   �  4        8  !   N     p     �     �     �        /                     8           .   4            %               :                              #      *      5              $      (   )         "   '      1       !           7   ,      0   2   
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
 shut down shut down in recovery shutting down starting up unrecognized status code unrecognized wal_level Project-Id-Version: pg_controldata (PostgreSQL 9.1)
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2011-10-08 08:40+0000
PO-Revision-Date: 2011-10-09 17:59+0200
Last-Translator: Begina Felicysym <begina.felicysym@wp.eu>
Language-Team: Begina Felicysym
Language: pl
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=3; plural=(n==1 ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2);
X-Generator: Virtaal 0.7.1-beta1
 
W przypadku gdy katalog danych nie jest podany (DATADIR), zmienna środowiskowa PGDATA
jest używana.

 %s wyświetla informacje kontrolne klastra bazy danych PostgreSQL.

 %s: nie można otworzyć pliku "%s" do odczytu: %s
 %s: nie można czytać z pliku "%s": %s
 %s: katalog danych nie został ustawiony
 64-bitowe zmienne integer Położenie początku kopii zapasowej:              %X/%X
 Bloki na segment są w relacji:                   %u
 Bajtów na segment WAL:                           %u
 Katalog w wersji numer:                          %u
 Aktualne ustawienie max_connections:             %d
 Aktualne ustawienie max_locks_per_xact:          %d
 Aktualne ustawienie max_prepared_xacts:          %d
 Aktualne ustawienie wal_level:                   %s
 Wielkość bloku bazy danych:                      %u
 Stan klastra bazy danych:                        %s
 Identyfikator systemu bazy danych:               %s
 Typ przechowywania daty/czasu:                   %s
 Przekazywanie parametru float4:                  %s
 Przekazywanie parametru float8:                  %s
 Najnowsza lokalizacja punktu kontrolnego:        %X/%X
 NextMultiOffset najnowszego punktu kontrolnego:  %u
 NextMultiXactId najnowszego punktu kontrolnego:  %u
 NextOID najnowszego punktu kontrolnego:          %u
 NextXID najnowszego punktu kontrolnego:          %u/%u
 Najnowsza lokalizacja punktu kontrolnego REDO:   %X/%X
 TimeLineID najnowszego punktu kontrolnego:       %u
 oldestActiveXID najnowszego punktu kontrolnego:  %u
 DB oldestXID'u najnowszego punktu kontrolnego:   %u
 NextXID najnowszego punktu kontrolnego:          %u
 Maksymalna liczba kolumn w indeksie:             %u
 Maksymalne wyrównanie danych:                    %u
 Maksymalna długość identyfikatorów:              %u
 Maksymalny rozmiar fragmentu TOAST:              %u
 Położenie zakończenia odzyskiwania minimalnego:  %X/%X
 Uprzednia lokalizacja punktu kontrolnego:        %X/%X
 Błędy proszę przesyłać na adres <pgsql-bugs@postgresql.org>.
 Czas najnowszego punktu kontrolnego:             %s
 Spróbuj "%s --help" aby uzyskać więcej informacji.
 Składnia:
  %s [OPCJA] [KATALOG]

Opcje:
  --help         pokaż ekran pomocy i zakończ
  --version      wyświetl informacje o wersji i zakończ
 Wielkość bloku WAL:                              %u
 UWAGA: obliczona suma kontrolna CRC pliku nie zgadza się.
Albo plik jest uszkodzony albo posiada inny układ niż program się spodziewał.
Rezultaty mogą być niepewne.

 OSTRZEŻENIE: możliwe niepoprawna kolejność bajtów
Kolejność bajtów używana do przechowywania plików pg_control może nie pasować
do używanej przez ten program.  W tym przypadku wynik poniżej jest błędny,
a instalacja PostgreSQL byłaby niezgodna z tym folderem danych.
 przez referencję przez wartość liczby zmiennoprzecinkowe w trybie odzyskiwania z archiwum w trybie odzyskiwania po awarii programu baza danych w trybie produkcji pg_control ostatnio modyfikowano:                %s
 pg_control w wersji numer:                       %u
 wyłącz bazę danych baza danych w trybie odzyskiwania wyłączanie bazy danych włączanie nieznany kod statusu nierozpoznany wal_level 